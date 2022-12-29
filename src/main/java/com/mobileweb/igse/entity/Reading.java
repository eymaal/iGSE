package com.mobileweb.igse.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Reading {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int reading_id;
    @Column(nullable = false)
    private String customer_id;
    @Column(nullable = false)
    private Date submission_date;
    @Column(nullable = false)
    private float elec_readings_day;
    @Column(nullable = false)
    private float elec_readings_night;
    @Column(nullable = false)
    private float gas_reading;
    @Column(columnDefinition = "VARCHAR(25)")
    private String status = "pending";

    public Reading(String customer_id, Date submission_date, float elec_readings_day, float elec_readings_night, float gas_reading, String status) {
        this.customer_id = customer_id;
        this.submission_date = submission_date;
        this.elec_readings_day = elec_readings_day;
        this.elec_readings_night = elec_readings_night;
        this.gas_reading = gas_reading;
        this.status = status;
    }

    public Reading() {
    }

    public int getReading_id() {
        return reading_id;
    }

    public void setReading_id(int reading_id) {
        this.reading_id = reading_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public Date getSubmission_date() {
        return submission_date;
    }

    public void setSubmission_date(Date submission_date) {
        this.submission_date = submission_date;
    }

    public float getElec_readings_day() {
        return elec_readings_day;
    }

    public void setElec_readings_day(float elec_readings_day) {
        this.elec_readings_day = elec_readings_day;
    }

    public float getElec_readings_night() {
        return elec_readings_night;
    }

    public void setElec_readings_night(float elec_readings_night) {
        this.elec_readings_night = elec_readings_night;
    }

    public float getGas_reading() {
        return gas_reading;
    }

    public void setGas_reading(float gas_reading) {
        this.gas_reading = gas_reading;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
