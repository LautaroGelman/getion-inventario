package grupo5.gestion_inventario.clientpanel.model;

import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.model.Employee; // <-- 1. Importamos Employee
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cash_register_sessions") // Renombramos la tabla para seguir convenciones
public class CashRegisterSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee; // <-- 2. Añadimos relación con el empleado

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false)
    private String status; // <-- 3. Estado de la sesión (OPEN, CLOSED)

    @Column(nullable = false)
    private LocalDateTime openingTime; // Renombrado desde openedAt

    @Column
    private LocalDateTime closingTime; // Renombrado desde closedAt

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal initialAmount; // Renombrado desde openingBalance

    @Column(precision = 19, scale = 2)
    private BigDecimal countedAmount; // Renombrado desde closingBalance (lo que el cajero cuenta)

    @Column(precision = 19, scale = 2)
    private BigDecimal expectedAmount; // <-- 4. El total que el sistema espera

    @Column(precision = 19, scale = 2)
    private BigDecimal difference; // <-- 5. La diferencia entre lo esperado y lo contado

    // Constructores
    public CashRegisterSession() {
        this.openingTime = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getOpeningTime() { return openingTime; }
    public void setOpeningTime(LocalDateTime openingTime) { this.openingTime = openingTime; }
    public LocalDateTime getClosingTime() { return closingTime; }
    public void setClosingTime(LocalDateTime closingTime) { this.closingTime = closingTime; }
    public BigDecimal getInitialAmount() { return initialAmount; }
    public void setInitialAmount(BigDecimal initialAmount) { this.initialAmount = initialAmount; }
    public BigDecimal getCountedAmount() { return countedAmount; }
    public void setCountedAmount(BigDecimal countedAmount) { this.countedAmount = countedAmount; }
    public BigDecimal getExpectedAmount() { return expectedAmount; }
    public void setExpectedAmount(BigDecimal expectedAmount) { this.expectedAmount = expectedAmount; }
    public BigDecimal getDifference() { return difference; }
    public void setDifference(BigDecimal difference) { this.difference = difference; }
}