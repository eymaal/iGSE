package com.mobileweb.igse.service;

import com.mobileweb.igse.entity.Customer;
import com.mobileweb.igse.repository.CustomerRepository;
import com.mobileweb.igse.utility.PasswordEncoder;
import com.mobileweb.igse.utility.Responses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private CustomerRepository customerRepository;


    public Optional<Customer> findCustomer(String customer_id){
        return customerRepository.findById(customer_id);
    }

    public ResponseEntity login(Customer customer) {
        try{
            Optional<Customer> customerOptional = findCustomer(customer.getCustomer_id());
            if(customerOptional.isEmpty()){
                throw new Exception("Customer does not exist. Register");
            }
            customer.setPassword_hash(PasswordEncoder.shaEncode(customer.getPassword_hash()));
            if(customerOptional.get().getPassword_hash().equals(customer.getPassword_hash())){
                return ResponseEntity.ok(customerOptional.get());
            }else {
                throw  new Exception("Passwords do not match.");
            }
        } catch (Exception e) {
            return Responses.makeBadRequest(e.getMessage());
        }
    }
}
