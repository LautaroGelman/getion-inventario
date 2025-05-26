// src/main/java/grupo5/gestion_inventario/clientpanel/dto/ProductRequest.java
package grupo5.gestion_inventario.clientpanel.dto;

import java.math.BigDecimal;

public class ProductRequest {
    private String code;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private Integer lowStockThreshold;

    public ProductRequest() {}

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

    public Integer getLowStockThreshold() { return lowStockThreshold; }
    public void setLowStockThreshold(Integer lowStockThreshold) { this.lowStockThreshold = lowStockThreshold; }
}
