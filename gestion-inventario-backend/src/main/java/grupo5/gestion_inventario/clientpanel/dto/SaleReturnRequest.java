package grupo5.gestion_inventario.clientpanel.dto;

import java.util.List;

public class SaleReturnRequest {
    private Long saleId; // CAMPO AÑADIDO
    private String reason;
    private List<ReturnItemRequest> items;

    // GETTERS Y SETTERS

    public Long getSaleId() { // MÉTODO AÑADIDO
        return saleId;
    }

    public void setSaleId(Long saleId) { // MÉTODO AÑADIDO
        this.saleId = saleId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<ReturnItemRequest> getItems() {
        return items;
    }

    public void setItems(List<ReturnItemRequest> items) {
        this.items = items;
    }
}