package com.mobileweb.igse.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Voucher {

    @Id
    @Column(columnDefinition = "VARCHAR(10)")
    private String EVC_code;

    public String getEVC_code() {
        return EVC_code;
    }

    public void setEVC_code(String EVC_code) {
        this.EVC_code = EVC_code;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public Voucher(String EVC_code, int used) {
        this.EVC_code = EVC_code;
        this.used = used;
    }

    public Voucher() {
    }

    @Column(columnDefinition = "tinyint(4)")
    private int used;
}
