package entities;

import jakarta.persistence.*;

@Entity
@Table(name = "supplierorderitem", schema = "public", catalog = "proj")
@IdClass(SupplierOrderItemEntityPK.class)
public class SupplierOrderItemEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idproduct")
    private int idproduct;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idorder")
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

        SupplierOrderItemEntity that = (SupplierOrderItemEntity) o;

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
