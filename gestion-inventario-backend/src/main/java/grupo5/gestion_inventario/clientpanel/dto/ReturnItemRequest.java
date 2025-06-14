package grupo5.gestion_inventario.clientpanel.dto;

public class ReturnItemRequest {
    private int productId;
    private int quantity;

    // Getters y Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}