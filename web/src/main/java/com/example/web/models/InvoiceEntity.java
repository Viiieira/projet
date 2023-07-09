package com.example.web.models;

import jakarta.persistence.*;

@Entity
@Table(name = "invoice", schema = "public", catalog = "proj")
@IdClass(InvoiceEntityPK.class)
public class InvoiceEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idproduct")
    private int idproduct;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idpurchase")
    private int idpurchase;
    @Basic
    @Column(name = "state", columnDefinition = "jsonb")
    private Object state;
    @Basic
    @Column(name = "quantity")
    private Integer quantity;

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

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvoiceEntity that = (InvoiceEntity) o;

        if (idproduct != that.idproduct) return false;
        if (idpurchase != that.idpurchase) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (quantity != null ? !quantity.equals(that.quantity) : that.quantity != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idproduct;
        result = 31 * result + idpurchase;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        return result;
    }
}
