// src/main/java/grupo5/gestion_inventario/clientpanel/dto/SalesDailySummaryDto.java
package grupo5.gestion_inventario.clientpanel.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SalesDailySummaryDto(
        LocalDate date,
        long      salesCount,
        BigDecimal totalAmount
) {}
