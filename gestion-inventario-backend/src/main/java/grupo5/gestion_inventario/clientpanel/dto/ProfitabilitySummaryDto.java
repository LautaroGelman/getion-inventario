package grupo5.gestion_inventario.clientpanel.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProfitabilitySummaryDto(
        LocalDate date,
        BigDecimal revenue,
        BigDecimal costOfGoods,
        BigDecimal profit
) {
    // Un "record" en Java es una forma moderna y concisa de crear una clase DTO.
    // Automáticamente nos provee constructores, getters, equals(), hashCode() y toString().
}