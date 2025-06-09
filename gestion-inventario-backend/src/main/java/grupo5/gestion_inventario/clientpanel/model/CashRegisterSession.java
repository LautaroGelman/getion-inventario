// src/main/java/grupo5/gestion_inventario/clientpanel/model/CashRegisterSession.java
package grupo5.gestion_inventario.clientpanel.model;

import grupo5.gestion_inventario.model.Employee;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cash_register_session")
public class CashRegisterSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Saldo inicial al abrir caja */
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal openingBalance;

    /** Saldo al cerrar caja */
    @Column(precision = 19, scale = 2)
    private BigDecimal closingBalance;

    /** Fecha y hora de apertura */
    @Column(nullable = false)
    private LocalDateTime openedAt;

    /** Fecha y hora de cierre; puede ser null si aún está abierta */
    @Column
    private LocalDateTime closedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Employee client;

    /** Constructor por defecto: establece openedAt = ahora */
    public CashRegisterSession() {
        this.openedAt = LocalDateTime.now();
    }

    /**
     * Constructor completo (cierre se asigna luego).
     */
    public CashRegisterSession(BigDecimal openingBalance, Employee client) {
        this.openingBalance = openingBalance;
        this.client = client;
        this.openedAt = LocalDateTime.now();
    }

    // —— Getters y Setters ——

    public Long getId() {
        return id;
    }

    public BigDecimal getOpeningBalance() {
        return openingBalance;
    }
    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }

    public BigDecimal getClosingBalance() {
        return closingBalance;
    }
    public void setClosingBalance(BigDecimal closingBalance) {
        this.closingBalance = closingBalance;
    }

    public LocalDateTime getOpenedAt() {
        return openedAt;
    }
    public void setOpenedAt(LocalDateTime openedAt) {
        this.openedAt = openedAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }
    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }

    public Employee getClient() {
        return client;
    }
    public void setClient(Employee client) {
        this.client = client;
    }
}
