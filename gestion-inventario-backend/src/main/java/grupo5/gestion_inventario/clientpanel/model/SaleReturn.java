package grupo5.gestion_inventario.clientpanel.model;

import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.model.Employee;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sale_returns")
public class SaleReturn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_sale_id")
    private Sale originalSale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee processedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    private LocalDateTime returnDate;

    @OneToMany(mappedBy = "saleReturn", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleReturnItem> items;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Sale getOriginalSale() { return originalSale; }
    public void setOriginalSale(Sale originalSale) { this.originalSale = originalSale; }
    public Employee getProcessedBy() { return processedBy; }
    public void setProcessedBy(Employee processedBy) { this.processedBy = processedBy; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }
    public List<SaleReturnItem> getItems() { return items; }
    public void setItems(List<SaleReturnItem> items) { this.items = items; }
}