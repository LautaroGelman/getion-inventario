// src/main/java/grupo5/gestion_inventario/clientpanel/model/Expense.java
package grupo5.gestion_inventario.clientpanel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import grupo5.gestion_inventario.model.Client;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Descripción del gasto */
    @Column(nullable = false, length = 500)
    private String description;

    /** Monto del gasto */
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    /** Tipo: "FIJO" o "VARIABLE" */
    @Column(nullable = false)
    private String type;

    /** Fecha del gasto */
    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @JsonIgnore
    private Client client;

    /** Constructor por defecto */
    public Expense() {}

    /**
     * Constructor completo.
     * @param description Descripción
     * @param amount      Monto
     * @param type        FIJO/VARIABLE
     * @param date        Fecha
     * @param client      Cliente asociado
     */
    public Expense(String description,
                   BigDecimal amount,
                   String type,
                   LocalDate date,
                   Client client) {
        this.description = description;
        this.amount      = amount;
        this.type        = type;
        this.date        = date;
        this.client      = client;
    }

    // —— Getters & Setters ——

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
}
