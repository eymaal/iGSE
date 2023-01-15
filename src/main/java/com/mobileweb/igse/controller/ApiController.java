package com.mobileweb.igse.controller;

import com.mobileweb.igse.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("igse")
public class ApiController {

    @Autowired
    PropertyService propertyService;

//    GET /igse/propertycount
    @GetMapping("/propertycount")
    public ResponseEntity getPropertyCount(){
        return propertyService.getPropertyCount();
    }

//    GET /igse/semi-detached/3
    @GetMapping("/{propertyType}/{bedroomNumber}")
    public ResponseEntity getStatistics(@PathVariable String propertyType, @PathVariable int bedroomNumber) {
        return propertyService.getStatistics(propertyType, bedroomNumber);
    }
}
