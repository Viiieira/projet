package com.example.web.models;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;

public class SupplierOrderItemEntityPK implements Serializable {
    @Column(name = "idproduct")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idproduct;
    @Column(name = "idorder")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idorder;

    public int getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(int idproduct) {
        this.idproduct = idproduct;
    }

    public int getIdorder() {
        return idorder;
    }

    public void setIdorder(int idorder) {
        this.idorder = idorder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SupplierOrderItemEntityPK that = (SupplierOrderItemEntityPK) o;

        if (idproduct != that.idproduct) return false;
        if (idorder != that.idorder) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idproduct;
        result = 31 * result + idorder;
        return result;
    }
}
