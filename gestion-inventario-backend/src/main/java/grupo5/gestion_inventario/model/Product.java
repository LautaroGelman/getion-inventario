package grupo5.gestion_inventario.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(
        name = "product",
        // --- ¡CAMBIO REALIZADO AQUÍ! ---
        // Ahora la restricción única es sobre la combinación del ID del cliente y el código del producto.
        uniqueConstraints = @UniqueConstraint(columnNames = {"client_id", "code"})
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- ¡CAMBIO REALIZADO AQUÍ! ---
    // Se elimina `unique = true` porque la restricción ahora está a nivel de tabla.
    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal cost;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false)
    private Integer lowStockThreshold;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @JsonBackReference
    private Employee client;

    // — Constructores —

    public Product() {}

    public Product(String code,
                   String name,
                   String description,
                   BigDecimal cost,
                   BigDecimal price,
                   Integer stockQuantity,
                   Integer lowStockThreshold,
                   Employee client) {
        this.code              = code;
        this.name              = name;
        this.description       = description;
        this.cost              = cost;
        this.price             = price;
        this.stockQuantity     = stockQuantity;
        this.lowStockThreshold = lowStockThreshold;
        this.client            = client;
    }

    // — Getters & Setters (sin cambios) —

    public Long getId() {
        return id;
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

    public Integer getStockQuantity() {
        return stockQuantity;
    }
    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer getLowStockThreshold() {
        return lowStockThreshold;
    }
    public void setLowStockThreshold(Integer lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }

    public Employee getClient() {
        return client;
    }
    public void setClient(Employee client) {
        this.client = client;
    }
}