package grupo5.gestion_inventario.clientpanel.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import grupo5.gestion_inventario.model.Product;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sale_return_item")
public class SaleReturnItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sale_return_id", nullable = false)
    @JsonBackReference
    private SaleReturn saleReturn;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    private String reason;

    // CONSTRUCTORES
    public SaleReturnItem() {
    }

    public SaleReturnItem(SaleReturn saleReturn, Product product, int quantity, BigDecimal unitPrice, String reason) {
        this.saleReturn = saleReturn;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.reason = reason;
    }

    // GETTERS Y SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SaleReturn getSaleReturn() {
        return saleReturn;
    }

    public void setSaleReturn(SaleReturn saleReturn) {
        this.saleReturn = saleReturn;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() { // <-- MÉTODO AÑADIDO
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}