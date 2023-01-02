package com.mobileweb.igse.service;

import com.mobileweb.igse.entity.Customer;
import com.mobileweb.igse.entity.Tariff;
import com.mobileweb.igse.repository.TariffRepository;
import com.mobileweb.igse.utility.Responses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TariffService {

    @Autowired
    private TariffRepository tariffRepository;
    @Autowired
    private LoginService loginService;

    public ResponseEntity getUnitReadings(String customer_id) {
        try{
            Optional<Customer> customerOptional = loginService.findCustomer(customer_id);
            if(customerOptional.isEmpty()){
                throw new Exception("Customer does not exist. Register");
            } else {
                if(customerOptional.get().getType().equals("customer")){
                    throw new Exception("Endpoint is for admin access only.");
                }
            }
            Iterable<Tariff> tariffs = tariffRepository.findAll();
            return ResponseEntity.ok(tariffs);
        }catch (Exception e){
            return Responses.makeBadRequest(e.getMessage());
        }
    }

    public ResponseEntity setUnitReadings(String customer_id, List<Tariff> tariffs) {
        try{
            Optional<Customer> customerOptional = loginService.findCustomer(customer_id);
            if(customerOptional.isEmpty()){
                throw new Exception("Customer does not exist. Register");
            } else {
                if(customerOptional.get().getType().equals("customer")){
                    throw new Exception("Endpoint is for admin access only.");
                }
            }
            tariffRepository.saveAll(tariffs);
            return ResponseEntity.ok(tariffs);
        }catch (Exception e){
            return Responses.makeBadRequest(e.getMessage());
        }
    }
}
