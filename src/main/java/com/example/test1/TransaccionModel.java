package com.example.test1;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TransaccionModel {

    private @Id
    @GeneratedValue Long id;
    public double monto;
    private TRANSACTION_TYPE ttype;

    public TransaccionModel(double monto){
        this.monto = monto;
    }

    public TransaccionModel() {

    }

    public void setType(TRANSACTION_TYPE ttype) {
        this.ttype = ttype;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    enum TRANSACTION_TYPE{
        DEPOSITO,
        EXTRACCION,
        INTERES
    }
}

