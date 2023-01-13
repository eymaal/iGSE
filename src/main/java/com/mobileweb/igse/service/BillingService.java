package com.mobileweb.igse.service;

import com.mobileweb.igse.entity.Customer;
import com.mobileweb.igse.entity.Reading;
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
    @Autowired
    private LoginService loginService;

    public ResponseEntity calculateBill(String customer_id) {
        try{
            List<Reading> pendingReadingList = getPendingReadingList(customer_id);
            Optional<Reading> lastPaidReading = getLastPaidReading(customer_id);
            if(lastPaidReading.isPresent()) {
                pendingReadingList.add(lastPaidReading.get());
            }
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"amount\":\""+calculateDues(pendingReadingList)+"\"}");

        } catch (Exception e){
            return Responses.makeBadRequest(e.getMessage());
        }
    }

    private Optional<Reading> getLastPaidReading(String customer_id) {
        return readingRepository.findLastPaidReading(customer_id);
    }

    private float calculateDues(List<Reading> pendingReadingList) {
        if(pendingReadingList.size()<2) { return 0;}
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

        dues = (eDay * dayUnits) +
                (eNight * nightUnits) +
                (gas * gasUnits) +
                (standingCharge * numberOfDays);

        return dues;
    }

    private LocalDate getLocalDateFromDate(Date date) {
        return new java.sql.Date(date.getTime()).toLocalDate();
    }

    public ResponseEntity payBill(String customer_id) {
        try{
            List<Reading> pendingReadingList = getPendingReadingList(customer_id);
            float dues = calculateDues(pendingReadingList);
            Optional<Customer> customerOptional = loginService.findCustomer(customer_id);
            if(customerOptional.isEmpty()){
                throw new Exception("Customer does not exist. Register");
            }
            Customer customer = customerOptional.get();
            if(dues>customer.getBalance()){
                throw new Exception("Balance is lesser than dues. Recharge.");
            }
            customer.setBalance(customer.getBalance()-dues);
            customerRepository.save(customer);
            readingService.settleBills(pendingReadingList);
            return ResponseEntity.ok()
                    .body(customer);

        } catch (Exception e){
            return Responses.makeBadRequest(e.getMessage());
        }
    }

    private List<Reading> getPendingReadingList(String customer_id) throws Exception{
        Optional<Customer> optionalCustomer = customerRepository.findById(customer_id);
        if (optionalCustomer.isEmpty()) {
            throw new Exception("Customer does not exist. Register");
        }
        return readingRepository.findAllPendingByCustomerId(customer_id);
    }
}
