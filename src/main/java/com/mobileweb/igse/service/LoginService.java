package com.mobileweb.igse.service;

import com.mobileweb.igse.entity.Customer;
import com.mobileweb.igse.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private CustomerRepository customerRepository;


    public Optional<Customer> findCustomer(String customer_id){
        return customerRepository.findById(customer_id);
    }
}
