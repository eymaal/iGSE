package com.mobileweb.igse.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="Customer")
public class Customer {

    @Id
    private String customer_id;
    @Column(nullable = false)
    private String password_hash;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String property_type;
    @Column(nullable = false)
    private int bedroom_num;
    @Column(nullable = false)
    private float balance = 200;
    @Column(nullable = false, columnDefinition = "VARCHAR(45)")
    private String type = "customer";

    public Customer(String customer_id, String password_hash, String address, String property_type, int bedroom_num) {
        this.customer_id = customer_id;
        this.password_hash = password_hash;
        this.address = address;
        this.property_type = property_type;
        this.bedroom_num = bedroom_num;
    }

    public Customer() {}

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public int getBedroom_num() {
        return bedroom_num;
    }

    public void setBedroom_num(int bedroom_num) {
        this.bedroom_num = bedroom_num;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
