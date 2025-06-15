package grupo5.gestion_inventario.clientpanel.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.model.Employee; // IMPORTACIÓN AÑADIDA
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
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    // --- RELACIÓN AÑADIDA ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SaleItem> items = new ArrayList<>();

    // --- Constructores Corregidos y Actualizados ---

    public Sale() {
        this.totalAmount = BigDecimal.ZERO;
    }

    public Sale(String paymentMethod,
                BigDecimal totalAmount,
                Client client,
                Employee employee, // PARÁMETRO AÑADIDO
                List<SaleItem> items,
                LocalDateTime saleDate) {
        this.createdAt     = (saleDate != null) ? saleDate : LocalDateTime.now();
        this.paymentMethod = paymentMethod;
        this.totalAmount   = totalAmount != null ? totalAmount : BigDecimal.ZERO;
        this.client        = client;
        this.employee      = employee; // ASIGNACIÓN AÑADIDA
        if (items != null) {
            setItems(items);
        }
    }

    /**
     * Constructor para usar en el servicio. Ahora acepta el empleado y la fecha.
     * @param client El cliente que realiza la compra.
     * @param employee El empleado que registra la venta.
     * @param paymentMethod El método de pago.
     * @param saleDate La fecha de la venta (puede ser nula, en cuyo caso se usa la actual).
     */
    public Sale(Client client, Employee employee, String paymentMethod, LocalDateTime saleDate) {
        this.client = client;
        this.employee = employee; // ASIGNACIÓN AÑADIDA
        this.paymentMethod = paymentMethod;
        this.createdAt = (saleDate != null) ? saleDate : LocalDateTime.now();
        this.totalAmount = BigDecimal.ZERO;
    }

    // --- Getters y Setters ---

    public Long getId()                     { return id; }
    public LocalDateTime getCreatedAt()     { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getPaymentMethod()        { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public BigDecimal getTotalAmount()      { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public Client getClient()               { return client; }
    public void setClient(Client client)    { this.client = client; }

    public Employee getEmployee()           { return employee; } // GETTER AÑADIDO
    public void setEmployee(Employee employee) { this.employee = employee; } // SETTER AÑADIDO

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

    // --- Helpers ---

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
}