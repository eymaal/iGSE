package com.mobileweb.igse.controller;

import com.mobileweb.igse.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("igse")
public class ApiController {

    @Autowired
    PropertyService propertyService;

    @GetMapping("/propertycount")
    public ResponseEntity getPropertyCount(){
        return propertyService.getPropertyCount();
    }
}
