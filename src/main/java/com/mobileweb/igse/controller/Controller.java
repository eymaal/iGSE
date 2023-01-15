package com.mobileweb.igse.controller;

import com.mobileweb.igse.entity.Customer;
import com.mobileweb.igse.entity.Reading;
import com.mobileweb.igse.entity.Tariff;
import com.mobileweb.igse.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Autowired
    private BillingService billingService;
    @Autowired
    private TariffService tariffService;

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

    @GetMapping("/calculateBill")
    public ResponseEntity calculateBill(@RequestParam String customer_id){
        return billingService.calculateBill(customer_id);
    }

    @PatchMapping("/payBill")
    public ResponseEntity payBill(@RequestParam String customer_id){
        return billingService.payBill(customer_id);
    }

    @GetMapping("/admin/readings")
    public ResponseEntity getAllReadings(@RequestParam String customer_id){
        return readingService.getAllReadings(customer_id);
    }

    @GetMapping("/admin/rates")
    public ResponseEntity getUnitRates(@RequestParam String customer_id){
        return tariffService.getUnitReadings(customer_id);
    }

    @PutMapping("/admin/setrates")
    public ResponseEntity setUnitRates(@RequestParam String customer_id, @RequestBody List<Tariff> tariffList){
        return tariffService.setUnitReadings(customer_id, tariffList);
    }

    @GetMapping("/admin/stats")
    public ResponseEntity getStatMap(@RequestParam String customer_id) {
        return billingService.getStats(customer_id);
    }
}
