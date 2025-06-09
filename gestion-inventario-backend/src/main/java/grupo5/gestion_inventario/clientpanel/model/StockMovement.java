package grupo5.gestion_inventario.clientpanel.model;

import grupo5.gestion_inventario.model.Product;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "stock_movements")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantityChange;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockMovementType type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date movementDate;

    @Column(nullable = true)
    private Long relatedSaleId;

    @Column(nullable = true)
    private Long relatedPurchaseOrderId;

    @Column(nullable = true)
    private String reason;

    public enum StockMovementType {
        PURCHASE,        // Entrada por compra a proveedor
        SALE,            // Salida por venta a cliente
        RETURN,          // Entrada por devolución de cliente
        MANUAL_ADJUSTMENT // Ajuste manual (ej: por rotura, pérdida)
    }

    @PrePersist
    protected void onCreate() {
        this.movementDate = new Date();
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(int quantityChange) {
        this.quantityChange = quantityChange;
    }

    public StockMovementType getType() {
        return type;
    }

    public void setType(StockMovementType type) {
        this.type = type;
    }

    public Date getMovementDate() {
        return movementDate;
    }

    public void setMovementDate(Date movementDate) {
        this.movementDate = movementDate;
    }

    public Long getRelatedSaleId() {
        return relatedSaleId;
    }

    public void setRelatedSaleId(Long relatedSaleId) {
        this.relatedSaleId = relatedSaleId;
    }

    public Long getRelatedPurchaseOrderId() {
        return relatedPurchaseOrderId;
    }

    public void setRelatedPurchaseOrderId(Long relatedPurchaseOrderId) {
        this.relatedPurchaseOrderId = relatedPurchaseOrderId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}