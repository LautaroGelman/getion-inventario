package grupo5.gestion_inventario.clientpanel.model;

import grupo5.gestion_inventario.model.Client;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date orderDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date receptionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchaseOrderStatus status;

    @Column(nullable = false)
    private BigDecimal totalCost;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderItem> items = new ArrayList<>(); // Inicializado para evitar NullPointerException

    public enum PurchaseOrderStatus {
        PENDING,
        RECEIVED,
        CANCELLED
    }

    @PrePersist
    protected void onCreate() {
        this.orderDate = new Date();
        this.status = PurchaseOrderStatus.PENDING;
        this.totalCost = BigDecimal.ZERO; // Inicializar el costo total
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getReceptionDate() {
        return receptionDate;
    }

    public void setReceptionDate(Date receptionDate) {
        this.receptionDate = receptionDate;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public PurchaseOrderStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseOrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public List<PurchaseOrderItem> getItems() {
        return items;
    }

    public void setItems(List<PurchaseOrderItem> items) {
        this.items = items;
    }

    // --- MÉTODO HELPER AÑADIDO ---
    public void addItem(PurchaseOrderItem item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        items.add(item);
        item.setPurchaseOrder(this); // Mantiene la consistencia de la relación
        // Recalcula el costo total
        if (this.totalCost == null) {
            this.totalCost = BigDecimal.ZERO;
        }
        BigDecimal itemCost = item.getCost().multiply(new BigDecimal(item.getQuantity()));
        this.totalCost = this.totalCost.add(itemCost);
    }
}