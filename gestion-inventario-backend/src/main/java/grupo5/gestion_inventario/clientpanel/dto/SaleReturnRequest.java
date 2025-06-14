package grupo5.gestion_inventario.clientpanel.dto;

import java.util.List;

public class SaleReturnRequest {
    private Long originalSaleId;
    private List<ReturnItemRequest> items;

    // Getters y Setters
    public Long getOriginalSaleId() { return originalSaleId; }
    public void setOriginalSaleId(Long originalSaleId) { this.originalSaleId = originalSaleId; }
    public List<ReturnItemRequest> getItems() { return items; }
    public void setItems(List<ReturnItemRequest> items) { this.items = items; }
}