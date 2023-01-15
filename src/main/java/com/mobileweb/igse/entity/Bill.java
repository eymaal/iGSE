package com.mobileweb.igse.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="Bill")
public class Bill {

    @Id
    @Column(name="customer_id")
    private String customerId;
    private Date startDate;
    private Date endDate;

    public Bill(String customerId, Date startDate, Date endDate) {
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Bill() {
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
