package grupo5.gestion_inventario.clientpanel.dto;

import java.math.BigDecimal;

public class ProductRequest {
    private String code;
    private String name;
    private String description;
    private BigDecimal cost;
    private BigDecimal price;
    private int quantity; // CAMPO Y TIPO CORREGIDOS
    private Integer lowStockThreshold;

    public ProductRequest() {}

    // Getters y Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    // --- MÉTODOS CORREGIDOS ---
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    // -------------------------

    public Integer getLowStockThreshold() { return lowStockThreshold; }
    public void setLowStockThreshold(Integer lowStockThreshold) { this.lowStockThreshold = lowStockThreshold; }
}