package com.mobileweb.igse.controller;

import com.mobileweb.igse.entity.Customer;
import com.mobileweb.igse.entity.Reading;
import com.mobileweb.igse.service.ReadingService;
import com.mobileweb.igse.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("iGSE")
public class Controller {

    @Autowired
    private RegisterService registerService;
    @Autowired
    private ReadingService readingService;

    @PostMapping("/register")
    public ResponseEntity registrationController(@RequestBody Customer customer, @RequestParam String evcCode){
        return registerService.register(customer, evcCode);
    }

    @PostMapping("/addReading")
    public ResponseEntity addReading(@RequestBody Reading reading){
        return readingService.addReading(reading);
    }

}
