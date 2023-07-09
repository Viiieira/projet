package com.example.web.models;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;

public class InvoiceEntityPK implements Serializable {
    @Column(name = "idproduct")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idproduct;
    @Column(name = "idpurchase")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idpurchase;

    public int getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(int idproduct) {
        this.idproduct = idproduct;
    }

    public int getIdpurchase() {
        return idpurchase;
    }

    public void setIdpurchase(int idpurchase) {
        this.idpurchase = idpurchase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvoiceEntityPK that = (InvoiceEntityPK) o;

        if (idproduct != that.idproduct) return false;
        if (idpurchase != that.idpurchase) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idproduct;
        result = 31 * result + idpurchase;
        return result;
    }
}
