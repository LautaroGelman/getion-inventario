package grupo5.gestion_inventario.clientpanel.dto;

import java.math.BigDecimal;

/**
 * DTO para capturar cada ítem de venta en la petición.
 */
public class SaleItemRequest {

    /** ID del producto a vender */
    private Long productId;

    /** Cantidad a vender */
    private int quantity;

    /** Precio unitario */
    private BigDecimal unitPrice;

    public SaleItemRequest() { }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
