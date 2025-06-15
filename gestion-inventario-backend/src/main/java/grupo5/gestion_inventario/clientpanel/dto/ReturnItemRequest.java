package grupo5.gestion_inventario.clientpanel.dto;

import java.math.BigDecimal;

public class ReturnItemRequest {
    private Long productId; // TIPO CORREGIDO de int a Long
    private int quantity;
    private String reason; // CAMPO AÑADIDO

    // GETTERS Y SETTERS

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

    public String getReason() { // MÉTODO AÑADIDO
        return reason;
    }

    public void setReason(String reason) { // MÉTODO AÑADIDO
        this.reason = reason;
    }
}