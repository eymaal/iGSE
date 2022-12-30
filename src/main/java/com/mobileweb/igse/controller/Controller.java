package com.mobileweb.igse.controller;

import com.mobileweb.igse.entity.Customer;
import com.mobileweb.igse.entity.Reading;
import com.mobileweb.igse.service.LoginService;
import com.mobileweb.igse.service.ReadingService;
import com.mobileweb.igse.service.RegisterService;
import com.mobileweb.igse.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private VoucherService voucherService;

    @Autowired
    private LoginService loginService;

    @PostMapping("/register")
    public ResponseEntity registrationController(@RequestBody Customer customer, @RequestParam String evcCode){
        return registerService.register(customer, evcCode);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Customer customer){
        return loginService.login(customer);
    }

    @PostMapping("/addReading")
    public ResponseEntity addReading(@RequestBody Reading reading){
        return readingService.addReading(reading);
    }

    @PostMapping("/getReadings")
    public ResponseEntity getReadings(@RequestBody Customer customer){
        return readingService.getReadings(customer);
    }

    @PostMapping("/addVoucher")
    public ResponseEntity redeemVoucher(@RequestBody Customer customer, @RequestParam String evcCode){
        return voucherService.redeemCode(customer, evcCode);
    }

}
