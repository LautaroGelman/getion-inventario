package grupo5.gestion_inventario.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(
        name = "product",
        uniqueConstraints = @UniqueConstraint(columnNames = "code")
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false)
    private Integer lowStockThreshold;

    @ManyToOne(fetch = FetchType.LAZY)
   // @JoinColumn(name = "client_id", nullable = false)
    @JsonBackReference
    private Client client;

    // — Constructores —

    public Product() {}

    public Product(String code,
                   String name,
                   String description,
                   BigDecimal price,
                   Integer stockQuantity,
                   Integer lowStockThreshold,
                   Client client) {
        this.code              = code;
        this.name              = name;
        this.description       = description;
        this.price             = price;
        this.stockQuantity     = stockQuantity;
        this.lowStockThreshold = lowStockThreshold;
        this.client            = client;
    }

    // — Getters & Setters —

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

    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
}
