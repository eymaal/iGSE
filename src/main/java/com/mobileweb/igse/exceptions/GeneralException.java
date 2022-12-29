package com.mobileweb.igse.exceptions;

import java.io.Serializable;

public class GeneralException implements Serializable {
    private String message;

    public GeneralException(String message) {
        this.message = message;
    }
}
