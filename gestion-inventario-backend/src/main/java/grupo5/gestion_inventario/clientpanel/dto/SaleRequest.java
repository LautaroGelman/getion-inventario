package grupo5.gestion_inventario.clientpanel.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SaleRequest {
    private String paymentMethod;
    private Long endCustomerId;
    private LocalDateTime saleDate;
    private List<SaleItemRequest> items;

    // Getters y Setters
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Long getEndCustomerId() { return endCustomerId; }
    public void setEndCustomerId(Long endCustomerId) { this.endCustomerId = endCustomerId; }

    public LocalDateTime getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDateTime saleDate) { this.saleDate = saleDate; }

    public List<SaleItemRequest> getItems() { return items; }
    public void setItems(List<SaleItemRequest> items) { this.items = items; }
}