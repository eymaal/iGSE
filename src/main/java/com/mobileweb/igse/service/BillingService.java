package com.mobileweb.igse.service;

import com.mobileweb.igse.entity.Bill;
import com.mobileweb.igse.entity.Customer;
import com.mobileweb.igse.entity.Reading;
import com.mobileweb.igse.repository.BillRepository;
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

    @Autowired
    private BillRepository billRepository;

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
            createBill(customer.getCustomer_id());
            readingService.settleBills(pendingReadingList);
            return ResponseEntity.ok()
                    .body(customer);

        } catch (Exception e){
            return Responses.makeBadRequest(e.getMessage());
        }
    }

    private void createBill(String customerId) {
        Date startDate = new Date();
        Date endDate = new Date();
        Optional<Reading> lastPaid = readingRepository.findLastPaidReading(customerId);
        if(lastPaid.isPresent()) {
            startDate = lastPaid.get().getSubmission_date();
        } else {
            List<Reading> pendingList = readingRepository.findAllPendingByCustomerId(customerId);
            startDate = pendingList.get(pendingList.size()-1).getSubmission_date();
        }
        Optional<Reading> latestReading = readingRepository.findLatestReading(customerId);
        if(latestReading.isPresent()) {
            endDate = latestReading.get().getSubmission_date();
        }
        Bill bill = new Bill(customerId, startDate, endDate);
        billRepository.save(bill);
    }

    private List<Reading> getPendingReadingList(String customer_id) throws Exception{
        Optional<Customer> optionalCustomer = customerRepository.findById(customer_id);
        if (optionalCustomer.isEmpty()) {
            throw new Exception("Customer does not exist. Register");
        }
        return readingRepository.findAllPendingByCustomerId(customer_id);
    }

    public ResponseEntity getStats(String customer_id) {
        try{
            Optional<Customer> customerOptional = loginService.findCustomer(customer_id);
            if(customerOptional.isEmpty()){
                throw new Exception("Customer does not exist. Register");
            } else {
                if(customerOptional.get().getType().equals("customer")){
                    throw new Exception("Endpoint is for admin access only.");
                }
            }

            Map<String, Map<String, Double>> statMap = new HashMap<>();
            List<Customer> customerList = customerRepository.findCustomersByType("customer");

            for(Customer c : customerList) {
                Bill bill = new Bill();
                bill.setCustomerId(c.getCustomer_id());
                Map<String, Double> usageMap = new HashMap<>();
                //set customerId, startDate, endDate into bill.
                if(readingRepository.findAllByCustomerId(c.getCustomer_id()).size()<2) {
                    usageMap.put("dayUnits", 0.0);
                    usageMap.put("nightUnits", 0.0);
                    usageMap.put("gasUnits", 0.0);
                    usageMap.put("days", 0.0);
                    usageMap.put("totalUnits", 0.0);
                } else {
                    Reading endReading;
                    Reading startReading;
                    if (readingRepository.findAllPendingByCustomerId(c.getCustomer_id()).size()==0) {
                        bill = billRepository.findById(c.getCustomer_id()).get();
                        endReading = readingRepository.findReadingByCustomerIdAndSubmissionDate(c.getCustomer_id(), bill.getEndDate());
                        startReading = readingRepository.findReadingByCustomerIdAndSubmissionDate(c.getCustomer_id(), bill.getStartDate());

                    } else if (readingRepository.findAllPendingByCustomerId(c.getCustomer_id()).size()==1) {
                        bill = billRepository.findById(c.getCustomer_id()).get();
                        endReading = readingRepository.findAllPendingByCustomerId(c.getCustomer_id()).get(0);
                        startReading = readingRepository.findReadingByCustomerIdAndSubmissionDate(c.getCustomer_id(), bill.getEndDate());

                    } else {
                        List<Reading> readingList = readingRepository.findAllPendingByCustomerId(c.getCustomer_id());
                        endReading = readingList.get(0);
                        startReading = readingList.get(readingList.size()-1);
                    }
                    usageMap.put("dayUnits", (double) (endReading.getElec_readings_day() - startReading.getElec_readings_day()));
                    usageMap.put("nightUnits", (double) (endReading.getElec_readings_night() - startReading.getElec_readings_night()));
                    usageMap.put("gasUnits", (double) (endReading.getGas_reading() - startReading.getGas_reading()));
                    LocalDate endDate = getLocalDateFromDate(endReading.getSubmission_date());
                    LocalDate startDate = getLocalDateFromDate(startReading.getSubmission_date());
                    usageMap.put("days", (double) ChronoUnit.DAYS.between(startDate, endDate));
                    double totalUnits = usageMap.get("dayUnits") + usageMap.get("nightUnits") + usageMap.get("gasUnits");
                    usageMap.put("totalUnits", totalUnits);
                    usageMap.put("averageUnits", totalUnits/usageMap.get("days"));
                }

                statMap.put(c.getCustomer_id(), usageMap);
            }
            return ResponseEntity.ok(statMap);

        }catch (Exception e){
            return Responses.makeBadRequest(e.getMessage());
        }
    }
}
