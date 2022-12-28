package com.mobileweb.igse.service;

import com.mobileweb.igse.entity.Customer;
import com.mobileweb.igse.exceptions.GeneralException;
import com.mobileweb.igse.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    @Autowired
    private CustomerRepository customerRepository;

    public ResponseEntity register(Customer customer) {
        try{
            customerRepository.save(customer);
            return new ResponseEntity(customer, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity(new GeneralException(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }
}
