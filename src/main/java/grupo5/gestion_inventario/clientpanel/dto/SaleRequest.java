// src/main/java/grupo5/gestion_inventario/clientpanel/dto/SaleRequest.java
package grupo5.gestion_inventario.clientpanel.dto;

import java.util.List;

/**
 * DTO para capturar la petición de crear una venta desde el panel cliente.
 */
public class SaleRequest {

    private String paymentMethod;
    private List<SaleItemRequest> items;

    public SaleRequest() {
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<SaleItemRequest> getItems() {
        return items;
    }

    public void setItems(List<SaleItemRequest> items) {
        this.items = items;
    }

}
