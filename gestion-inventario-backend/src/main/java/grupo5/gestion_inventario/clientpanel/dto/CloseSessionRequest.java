package grupo5.gestion_inventario.clientpanel.dto;

import java.math.BigDecimal;

public class CloseSessionRequest {

    /**
     * El monto final de efectivo contado por el empleado al cerrar la caja.
     */
    private BigDecimal countedAmount;

    // --- Constructores ---

    public CloseSessionRequest() {
    }

    public CloseSessionRequest(BigDecimal countedAmount) {
        this.countedAmount = countedAmount;
    }

    // --- Getters y Setters ---

    public BigDecimal getCountedAmount() {
        return countedAmount;
    }

    public void setCountedAmount(BigDecimal countedAmount) {
        this.countedAmount = countedAmount;
    }
}