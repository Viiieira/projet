package entities;

import jakarta.persistence.*;

@Entity
@Table(name = "supplierorder", schema = "public", catalog = "proj")
public class SupplierOrderEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "idsupplier")
    private Integer idsupplier;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIdsupplier() {
        return idsupplier;
    }

    public void setIdsupplier(Integer idsupplier) {
        this.idsupplier = idsupplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SupplierOrderEntity that = (SupplierOrderEntity) o;

        if (id != that.id) return false;
        if (idsupplier != null ? !idsupplier.equals(that.idsupplier) : that.idsupplier != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (idsupplier != null ? idsupplier.hashCode() : 0);
        return result;
    }
}
