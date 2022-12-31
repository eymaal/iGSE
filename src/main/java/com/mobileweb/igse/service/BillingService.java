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

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BillingService {

    @Autowired
    private TariffRepository tariffRepository;
    @Autowired
    private ReadingService readingService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ReadingRepository readingRepository;

    public ResponseEntity calculateBill(String customer_id) {
        try{
            Optional<Customer> optionalCustomer = customerRepository.findById(customer_id);
            if (optionalCustomer.isEmpty()) {
                throw new Exception("Customer does not exist. Register");
            }
            List<Reading> pendingReadingList = readingRepository.findAllPendingByCustomerId(customer_id);
            if(pendingReadingList.size()<2){
                return ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"amount\":\""+0+"\"}");
            }
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"amount\":\""+calculateDues(pendingReadingList)+"\"}");

        } catch (Exception e){
            return Responses.makeBadRequest(e.getMessage());
        }
    }

    private float calculateDues(List<Reading> pendingReadingList) {
        float dues;
        int numberOfDays=0, dayUnits=0, nightUnits=0, gasUnits=0;
        for (int i = 0; i < pendingReadingList.size()-1; i++) {
            dayUnits += pendingReadingList.get(i).getElec_readings_day() - pendingReadingList.get(i+1).getElec_readings_day();
            nightUnits += pendingReadingList.get(i).getElec_readings_night() - pendingReadingList.get(i+1).getElec_readings_night();
            gasUnits += pendingReadingList.get(i).getGas_reading() - pendingReadingList.get(i+1).getGas_reading();
            LocalDate date = getLocalDateFromDate((pendingReadingList.get(i).getSubmission_date()));
            LocalDate prevDate= getLocalDateFromDate(pendingReadingList.get(i+1).getSubmission_date());
            numberOfDays += ChronoUnit.DAYS.between(prevDate, date);
        }
        float eDay = tariffRepository.findById("electricity_day").get().getRate();
        float eNight = tariffRepository.findById("electricity_night").get().getRate();
        float gas = tariffRepository.findById("gas").get().getRate();
        float standingCharge = tariffRepository.findById("sanding_charge").get().getRate();
//        System.out.println(String.format("Day Units: %d",dayUnits));
//        System.out.println(String.format("Night Units: %d", nightUnits));
//        System.out.println(String.format("Gas Units: %d", gasUnits));
//        System.out.println(String.format("Number of days: %d", numberOfDays));

        dues = (eDay * dayUnits) +
                (eNight * nightUnits) +
                (gas * gasUnits) +
                (standingCharge * numberOfDays);

        return dues;
    }

    private LocalDate getLocalDateFromDate(Date date) {
        return new java.sql.Date(date.getTime()).toLocalDate();
    }
}