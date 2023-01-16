package com.mobileweb.igse.service;

import com.mobileweb.igse.repository.CustomerRepository;
import com.mobileweb.igse.repository.ReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ReadingRepository readingRepository;

    public ResponseEntity getBasicStats() {
        Map<String, Integer> map = new HashMap<>();
        map.put("customers", customerRepository.findCustomersByType("customer").size());
        map.put("unpaidBills", readingRepository.findReadingByStatus("pending").size());
        return ResponseEntity.ok(map);
    }
}
