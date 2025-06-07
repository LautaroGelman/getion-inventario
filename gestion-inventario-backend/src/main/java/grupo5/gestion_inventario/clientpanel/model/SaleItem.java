// src/main/java/grupo5/gestion_inventario/clientpanel/model/SaleItem.java
package grupo5.gestion_inventario.clientpanel.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import grupo5.gestion_inventario.model.Product;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "sale_item")
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    @JsonBackReference       // <<--- evita volver a serializar la venta desde el item
    private Sale sale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /** Cantidad vendida */
    @Column(nullable = false)
    private Integer quantity;

    /** Precio unitario en el momento de la venta */
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal unitPrice;

    /** Constructor por defecto */
    public SaleItem() {}

    /**
     * Constructor básico. La asociación a Sale se hará en el Service o en Sale.setItems().
     * @param product   Producto vendido
     * @param quantity  Cantidad vendida
     * @param unitPrice Precio unitario
     */
    public SaleItem(Product product, Integer quantity, BigDecimal unitPrice) {
        this.product   = product;
        this.quantity  = quantity;
        this.unitPrice = unitPrice;
    }

    // —— Getters & Setters ——

    public Long getId() {
        return id;
    }

    public Sale getSale() {
        return sale;
    }
    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}

