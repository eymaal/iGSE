package com.mobileweb.igse.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Taiff")
@Table(name="Taiff")
public class Tariff {

    @Id
    @Column(name = "taiff_type", columnDefinition = "VARCHAR(20)", nullable = false)
    private String tariff_type;
    private float rate;

    public Tariff(String tariff_type, float rate) {
        this.tariff_type = tariff_type;
        this.rate = rate;
    }

    public String getTariff_type() {
        return tariff_type;
    }

    public void setTariff_type(String tariff_type) {
        this.tariff_type = tariff_type;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public Tariff() {
    }
}
