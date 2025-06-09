package grupo5.gestion_inventario.clientpanel.dto;

import java.util.List;

public class PurchaseOrderRequest {

    private Long providerId;
    private List<PurchaseOrderItemRequest> items;
    private Long clientId; // ID del negocio que realiza la compra

    // Getters y Setters

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public List<PurchaseOrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<PurchaseOrderItemRequest> items) {
        this.items = items;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}