package grupo5.gestion_inventario.clientpanel.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import grupo5.gestion_inventario.model.BusinessAccount;
import grupo5.gestion_inventario.clientpanel.model.EndCustomer;
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

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_account_id", nullable = false)
    private BusinessAccount businessAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_customer_id")
    private EndCustomer endCustomer;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SaleItem> items = new ArrayList<>();

    // --- Constructores Corregidos ---

    /**
     * Constructor por defecto.
     * Se elimina la asignación de 'createdAt' para que se pueda establecer después.
     */
    public Sale() {
        this.totalAmount = BigDecimal.ZERO;
    }

    /**
     * Constructor completo. Ahora acepta la fecha de la venta como parámetro.
     */
    public Sale(String paymentMethod,
                BigDecimal totalAmount,
                BusinessAccount businessAccount,
                EndCustomer endCustomer,
                List<SaleItem> items,
                LocalDateTime saleDate) { // <-- PARÁMETRO AÑADIDO
        // Usa la fecha recibida. Si es nula, usa la actual como respaldo.
        this.createdAt     = (saleDate != null) ? saleDate : LocalDateTime.now();
        this.paymentMethod = paymentMethod;
        this.totalAmount   = totalAmount != null ? totalAmount : BigDecimal.ZERO;
        this.businessAccount        = businessAccount;
        this.endCustomer   = endCustomer;
        if (items != null) {
            setItems(items);
        }
    }

    // --- Getters y Setters (se mantienen igual) ---

    public Long getId()                     { return id; }
    public LocalDateTime getCreatedAt()     { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getPaymentMethod()        { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public BigDecimal getTotalAmount()      { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BusinessAccount getBusinessAccount()               { return businessAccount; }
    public void setBusinessAccount(BusinessAccount businessAccount)    { this.businessAccount = businessAccount; }

    public EndCustomer getEndCustomer() { return endCustomer; }
    public void setEndCustomer(EndCustomer endCustomer) { this.endCustomer = endCustomer; }

    public List<SaleItem> getItems()        { return items; }

    public void setItems(List<SaleItem> items) {
        this.items.clear();
        this.totalAmount = BigDecimal.ZERO;
        if (items != null) {
            for (SaleItem item : items) {
                addItem(item);
            }
        }
    }

    // --- Helpers (se mantienen igual) ---

    public void addItem(SaleItem item) {
        item.setSale(this);
        items.add(item);
        totalAmount = totalAmount.add(
                item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        );
    }

    public void recalcTotal() {
        totalAmount = BigDecimal.ZERO;
        for (SaleItem item : items) {
            totalAmount = totalAmount.add(
                    item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
            );
        }
    }

    // En tu archivo Sale.java

    /**
     * Constructor para usar en el servicio. Ahora acepta la fecha.
     * @param client El cliente que realiza la compra.
     * @param paymentMethod El método de pago.
     * @param saleDate La fecha de la venta (puede ser nula, en cuyo caso se usa la actual).
     */
    public Sale(BusinessAccount businessAccount, EndCustomer endCustomer, String paymentMethod, LocalDateTime saleDate) {
        this.businessAccount = businessAccount;
        this.endCustomer = endCustomer;
        this.paymentMethod = paymentMethod;
        this.createdAt = (saleDate != null) ? saleDate : LocalDateTime.now();
        this.totalAmount = BigDecimal.ZERO;
    }
}