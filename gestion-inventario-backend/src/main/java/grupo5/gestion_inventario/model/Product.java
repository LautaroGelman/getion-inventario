package grupo5.gestion_inventario.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(
        name = "product",
        // La restricción única ahora es sobre la combinación del ID del cliente y el código del producto.
        uniqueConstraints = @UniqueConstraint(columnNames = {"client_id", "code"})
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Se elimina `unique = true` del campo porque la restricción ahora está a nivel de tabla.
    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal cost;

    @Column(nullable = false)
    private BigDecimal price;

    // --- CAMPO ESTANDARIZADO ---
    // Renombrado de 'stockQuantity' a 'quantity' y tipo cambiado a 'int' para consistencia
    @Column(nullable = false)
    private int quantity;

    // --- CAMPO ESTANDARIZADO ---
    // Tipo cambiado a 'int' para consistencia
    @Column(nullable = false)
    private int lowStockThreshold;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @JsonBackReference
    private Client client;

    // --- Constructores ---

    public Product() {}

    // Constructor actualizado con los nombres y tipos corregidos
    public Product(String code,
                   String name,
                   String description,
                   BigDecimal cost,
                   BigDecimal price,
                   int quantity, // TIPO Y NOMBRE CORREGIDO
                   int lowStockThreshold, // TIPO CORREGIDO
                   Client client) {
        this.code              = code;
        this.name              = name;
        this.description       = description;
        this.cost              = cost;
        this.price             = price;
        this.quantity          = quantity; // CAMPO CORREGIDO
        this.lowStockThreshold = lowStockThreshold;
        this.client            = client;
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCost() {
        return cost;
    }
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    // --- MÉTODOS ESTANDARIZADOS ---
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }
    public void setLowStockThreshold(int lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }

    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
}