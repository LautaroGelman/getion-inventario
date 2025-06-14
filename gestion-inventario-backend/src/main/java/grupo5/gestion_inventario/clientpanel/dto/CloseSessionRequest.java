package grupo5.gestion_inventario.clientpanel.dto;

import java.math.BigDecimal;

public class CloseSessionRequest {
    private BigDecimal countedAmount;

    // Getter y Setter
    public BigDecimal getCountedAmount() {
        return countedAmount;
    }
    public void setCountedAmount(BigDecimal countedAmount) {
        this.countedAmount = countedAmount;
    }
}