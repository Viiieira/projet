package entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "supplierorder", schema = "public", catalog = "proj")
public class SupplierOrderEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "idsupplier")
    private Integer idSupplier;

    @Basic
    @Column(name = "nameorder")
    private String nameOrder;

    @Basic
    @Column(name = "daterequested")
    private Date dateRequested;

    @Basic
    @Column(name = "dateprovided")
    private Date dateProvided;

    @Basic
    @Column(name = "state")
    private String state;

    public SupplierOrderEntity(int id, int idSupplier, String nameOrder, Date dateRequested, Date dateProvided, String state) {
        this.id = id;
        this.idSupplier = idSupplier;
        this.nameOrder = nameOrder;
        this.dateRequested = dateRequested;
        this.dateProvided = dateProvided;
        this.state = state;
    }

    public SupplierOrderEntity() {

    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNameOrder() {
        return nameOrder;
    }

    public void setNameOrder(String nameOrder) {
        this.nameOrder = nameOrder;
    }

    public Date getDateRequested() {
        return dateRequested;
    }

    public void setDateRequested(Date dateRequested) {
        this.dateRequested = dateRequested;
    }

    public Date getDateProvided() {
        return dateProvided;
    }

    public void setDateProvided(Date dateProvided) {
        this.dateProvided = dateProvided;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIdSupplier() {
        return idSupplier;
    }

    public void setIdSupplier(Integer idSupplier) {
        this.idSupplier = idSupplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SupplierOrderEntity that = (SupplierOrderEntity) o;

        if (id != that.id) return false;
        if (idSupplier != null ? !idSupplier.equals(that.idSupplier) : that.idSupplier != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (idSupplier != null ? idSupplier.hashCode() : 0);
        return result;
    }
}
