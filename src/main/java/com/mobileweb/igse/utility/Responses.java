package com.mobileweb.igse.utility;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class Responses {

    public static ResponseEntity makeBadRequest(String message){
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"message\":\""+message+"\"}");
    }
}
