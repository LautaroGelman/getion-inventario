package grupo5.gestion_inventario.clientpanel.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import grupo5.gestion_inventario.model.Client;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sale")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Fecha y hora de la venta */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /** Medio de pago, e.g. "CASH", "CARD" */
    @Column(nullable = false)
    private String paymentMethod;

    /** Monto total de la venta */
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference      // <<--- rompe la recursión aquí
    private List<SaleItem> items = new ArrayList<>();

    /* ---------- constructores ---------- */

    /** Constructor por defecto */
    public Sale() {
        this.createdAt   = LocalDateTime.now();
        this.totalAmount = BigDecimal.ZERO;
    }

    /**
     * Constructor completo (createdAt = now)
     */
    public Sale(String paymentMethod,
                BigDecimal totalAmount,
                Client client,
                List<SaleItem> items) {
        this.createdAt     = LocalDateTime.now();
        this.paymentMethod = paymentMethod;
        this.totalAmount   = totalAmount != null ? totalAmount : BigDecimal.ZERO;
        this.client        = client;
        if (items != null) {
            setItems(items);
        }
    }

    /* ---------- getters / setters ---------- */

    public Long getId()                     { return id; }
    public LocalDateTime getCreatedAt()     { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getPaymentMethod()        { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public BigDecimal getTotalAmount()      { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public Client getClient()               { return client; }
    public void setClient(Client client)    { this.client = client; }

    public List<SaleItem> getItems()        { return items; }

    /**
     * Reemplaza items y recalcula el total.
     */
    public void setItems(List<SaleItem> items) {
        this.items.clear();
        this.totalAmount = BigDecimal.ZERO;
        if (items != null) {
            for (SaleItem item : items) {
                addItem(item);
            }
        }
    }

    /* ---------- helpers ---------- */

    /** Agrega un ítem y actualiza totalAmount */
    public void addItem(SaleItem item) {
        item.setSale(this);
        items.add(item);
        totalAmount = totalAmount.add(
                item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        );
    }

    /** Vuelve a calcular el total desde cero (por si se editaron cantidades) */
    public void recalcTotal() {
        totalAmount = BigDecimal.ZERO;
        for (SaleItem item : items) {
            totalAmount = totalAmount.add(
                    item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
            );
        }
    }
}
