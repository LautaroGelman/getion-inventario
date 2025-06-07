// src/main/java/grupo5/gestion_inventario/superpanel/dto/GlobalMetricsDTO.java
package grupo5.gestion_inventario.superpanel.dto;

import java.math.BigDecimal;

public record GlobalMetricsDTO(
        long totalAccounts,
        long freeTrialAccounts,
        long standardAccounts,
        long premiumAccounts,
        BigDecimal totalRevenueLast30d,
        long totalProducts,
        long lowStockAlerts
) {}
