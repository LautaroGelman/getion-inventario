package grupo5.gestion_inventario.clientpanel.dto;

import java.util.List;

public class SaleReturnRequest {
    private Long saleId;
    private List<SaleReturnItemRequest> items;

    public Long getSaleId() { return saleId; }
    public void setSaleId(Long saleId) { this.saleId = saleId; }
    public List<SaleReturnItemRequest> getItems() { return items; }
    public void setItems(List<SaleReturnItemRequest> items) { this.items = items; }
}
