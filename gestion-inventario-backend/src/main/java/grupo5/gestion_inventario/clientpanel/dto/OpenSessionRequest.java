package grupo5.gestion_inventario.clientpanel.dto;

import java.math.BigDecimal;

public class OpenSessionRequest {
    private BigDecimal initialAmount;

    // Getter y Setter
    public BigDecimal getInitialAmount() {
        return initialAmount;
    }
    public void setInitialAmount(BigDecimal initialAmount) {
        this.initialAmount = initialAmount;
    }
}