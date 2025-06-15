package grupo5.gestion_inventario.clientpanel.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import grupo5.gestion_inventario.model.Client;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sale_return")
public class SaleReturn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false)
    private LocalDateTime returnDate;

    private String reason;

    @Column(nullable = false)
    private BigDecimal totalRefundAmount;

    @OneToMany(mappedBy = "saleReturn", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SaleReturnItem> items = new ArrayList<>();

    // CONSTRUCTORES
    public SaleReturn() {
        this.totalRefundAmount = BigDecimal.ZERO;
    }

    public SaleReturn(Sale sale, Client client, LocalDateTime returnDate, String reason) {
        this.sale = sale;
        this.client = client;
        this.returnDate = returnDate;
        this.reason = reason;
        this.totalRefundAmount = BigDecimal.ZERO;
    }

    // GETTERS Y SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getTotalRefundAmount() {
        return totalRefundAmount;
    }

    public void setTotalRefundAmount(BigDecimal totalRefundAmount) {
        this.totalRefundAmount = totalRefundAmount;
    }

    public List<SaleReturnItem> getItems() {
        return items;
    }

    public void setItems(List<SaleReturnItem> items) {
        this.items = items;
    }

    // --- MÉTODO HELPER AÑADIDO ---
    public void addItem(SaleReturnItem item) {
        items.add(item);
        item.setSaleReturn(this);
        totalRefundAmount = totalRefundAmount.add(
                item.getUnitPrice().multiply(new BigDecimal(item.getQuantity()))
        );
    }
}