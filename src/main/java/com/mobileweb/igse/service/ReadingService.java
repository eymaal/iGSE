package com.mobileweb.igse.service;

import com.mobileweb.igse.entity.Customer;
import com.mobileweb.igse.entity.Reading;
import com.mobileweb.igse.repository.ReadingRepository;
import com.mobileweb.igse.utility.Responses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReadingService {

    @Autowired
    ReadingRepository readingRepository;
    @Autowired
    LoginService loginService;

    public ResponseEntity addReading(Reading reading){
        try{
            if(loginService.findCustomer(reading.getCustomer_id()).isEmpty()){
                throw new Exception("Customer does not exist. Register");
            }
            Optional<Reading> latestReadingOptional = readingRepository.findLatestReading(reading.getCustomer_id());
            if(latestReadingOptional.isPresent()){
                Reading latestReading = latestReadingOptional.get();
                if(!latestReading.getSubmission_date().before(reading.getSubmission_date())){
                    throw new Exception("Entered submission date precedes latest submission");
                }
                if(latestReading.getElec_readings_day()>reading.getElec_readings_day()){
                    throw new Exception("Entered Electricity reading (day) is less than previous reading");
                }
                if(latestReading.getElec_readings_night()>reading.getElec_readings_night()){
                    throw new Exception("Entered Electricity reading (night) is less than previous reading");
                }
                if(latestReading.getGas_reading()>reading.getGas_reading()){
                    throw new Exception("Entered Gas reading is less than previous reading");
                }
            }
            readingRepository.save(reading);
            return new ResponseEntity(HttpStatus.CREATED);
        }catch (Exception e){
            return Responses.makeBadRequest(e.getMessage());
        }
    }

    public ResponseEntity getReadings(Customer customer) {
        try{
            if(loginService.findCustomer(customer.getCustomer_id()).isEmpty()){
                throw new Exception("Customer does not exist. Register");
            }
            List<Reading> readingList = readingRepository.findAllByCustomerId(customer.getCustomer_id());
            return ResponseEntity.ok(readingList);
        }catch (Exception e){
            return Responses.makeBadRequest(e.getMessage());
        }
    }

    public void settleBills(List<Reading> pendingReadingList) {
        pendingReadingList.stream().forEach(reading -> reading.setStatus("paid"));
        readingRepository.saveAll(pendingReadingList);
    }
}
