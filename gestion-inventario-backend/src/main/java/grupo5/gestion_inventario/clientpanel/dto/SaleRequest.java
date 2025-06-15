package grupo5.gestion_inventario.clientpanel.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SaleRequest {
    private String paymentMethod;
    private LocalDateTime saleDate;
    private List<SaleItemRequest> items;
    private Long employeeId; // CAMPO AÑADIDO

    // Getters y Setters
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public LocalDateTime getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDateTime saleDate) { this.saleDate = saleDate; }

    public List<SaleItemRequest> getItems() { return items; }
    public void setItems(List<SaleItemRequest> items) { this.items = items; }

    // --- GETTER Y SETTER AÑADIDOS ---
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
}