package com.mobileweb.igse.service;

import com.mobileweb.igse.entity.Customer;
import com.mobileweb.igse.entity.Reading;
import com.mobileweb.igse.entity.Tariff;
import com.mobileweb.igse.repository.CustomerRepository;
import com.mobileweb.igse.repository.ReadingRepository;
import com.mobileweb.igse.repository.TariffRepository;
import com.mobileweb.igse.utility.Responses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class PropertyService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    TariffRepository tariffRepository;

    @Autowired
    ReadingRepository readingRepository;

    public ResponseEntity getPropertyCount() {
        List<String> propertyTypeList = List.of(
                "detached",
                "terrace",
                "cottage",
                "semi-detached",
                "flat",
                "bungalow",
                "mansion"
        );

        StringBuffer json = new StringBuffer();
        json.append("[");
        propertyTypeList.forEach((propertyType) -> {
            json.append("{\"")
                    .append(propertyType)
                    .append("\":\"")
                    .append(customerRepository.countByPropertyType(propertyType))
                    .append("\"},");
        });
        json.append("]");

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json.toString());
    }

    public ResponseEntity getStatistics(String propertyType, int bedroomNumber) {
        try {
            List<Customer> customerList = customerRepository.findCustomersByPropertyTypeAndBedrooms(propertyType, bedroomNumber);
            double avgCost = getAverageUsage(customerList);
            StringBuffer body = new StringBuffer();
            body.append("{")
                    .append("\"type\": \"")
                    .append(propertyType)
                    .append("\", ")
                    .append("\"bedroom\": \"")
                    .append(bedroomNumber)
                    .append("\", ")
                    .append("\"average_electricity_gas_cost_per_day\": \"")
                    .append(avgCost)
                    .append("\", ")
                    .append("\"unit\":\"pound\"")
                    .append("}");
            return ResponseEntity
                    .ok().
                    contentType(MediaType.APPLICATION_JSON)
                    .body(body.toString());
        } catch (Exception e) {
            return Responses.makeBadRequest(e.getMessage());
        }
    }

    private double getAverageUsage(List<Customer> customerList) {
        double costPerDay;
        List<Reading> readingList = new ArrayList<>();
        Map<String, Integer> usageMap = new HashMap<>();
        int days=0, dayUnits=0, nightUnits=0, gasUnits=0;
        readingList.addAll(getLatestBilling(customerList.get(0)));
        for (Customer c : customerList) {
            readingList.clear();
            readingList.addAll(getLatestBilling(c));
            for(int i=0; i<readingList.size()-1; i++) {
                dayUnits += readingList.get(i).getElec_readings_day() - readingList.get(i+1).getElec_readings_day();
                nightUnits  += readingList.get(i).getElec_readings_night() - readingList.get(i+1).getElec_readings_night();
                gasUnits += readingList.get(i).getGas_reading() - readingList.get(i+1).getGas_reading();
                LocalDate date = getLocalDateFromDate((readingList.get(i).getSubmission_date()));
                LocalDate prevDate= getLocalDateFromDate(readingList.get(i+1).getSubmission_date());
                days += ChronoUnit.DAYS.between(prevDate, date);
            }
        }

        float eDay = tariffRepository.findById("electricity_day").get().getRate();
        float eNight = tariffRepository.findById("electricity_night").get().getRate();
        float gas = tariffRepository.findById("gas").get().getRate();
        float standingCharge = tariffRepository.findById("sanding_charge").get().getRate();

        costPerDay = ((eDay * dayUnits) + (eNight * nightUnits) + (gas * gasUnits))/days;
        return costPerDay;
    }

    private LocalDate getLocalDateFromDate(Date date) {
        return new java.sql.Date(date.getTime()).toLocalDate();
    }

    private List<Reading> getLatestBilling(Customer customer) {
        List<Reading> readingList = new ArrayList<>();
        readingList.addAll(readingRepository.findAllPendingByCustomerId(customer.getCustomer_id()));
        if(readingList.size()>0) {
            Optional<Reading> lastReading = readingRepository.findLastPaidReading(customer.getCustomer_id());
            if(lastReading.isPresent()) readingList.add(lastReading.get());
        } else {
            readingList.addAll(readingRepository.findLatestPaidReadings(customer.getCustomer_id()));
        }
        return readingList;
    }
}
