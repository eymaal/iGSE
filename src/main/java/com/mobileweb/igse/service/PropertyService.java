package com.mobileweb.igse.service;

import com.mobileweb.igse.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PropertyService {

    @Autowired
    CustomerRepository customerRepository;
    public ResponseEntity getPropertyCount() {
        List<String> propertyTypeList = List.of(
                "detached",
                "terrace",
                "cottage",
                "semi-detached",
                "flat",
                "bungalow",
                "mansion"
        );

        StringBuffer json = new StringBuffer();
        json.append("[");
        propertyTypeList.forEach((propertyType) -> {
            json.append("{\"")
                    .append(propertyType)
                    .append("\":\"")
                    .append(customerRepository.countByPropertyType(propertyType))
                    .append("\"},");
        });
        json.append("]");

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json.toString());
    }
}
