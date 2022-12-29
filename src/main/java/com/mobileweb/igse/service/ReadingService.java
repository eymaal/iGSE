package com.mobileweb.igse.service;

import com.mobileweb.igse.entity.Reading;
import com.mobileweb.igse.exceptions.GeneralException;
import com.mobileweb.igse.repository.ReadingRepository;
import com.mobileweb.igse.utility.Responses;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

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
            if(readingRepository.findLatestReadingDate(reading.getCustomer_id()).isPresent()){
                Date latestDate = readingRepository.findLatestReadingDate(reading.getCustomer_id()).get();
                if(latestDate.after(reading.getSubmission_date())){
                    throw new Exception("Entered submission date precedes latest submission");
                }
            }
            readingRepository.save(reading);
            return new ResponseEntity(HttpStatus.CREATED);
        }catch (Exception e){
            return Responses.makeBadRequest(e.getMessage());
        }
    }
}
