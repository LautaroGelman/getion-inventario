package grupo5.gestion_inventario.clientpanel.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleDto(
        String       clientName,
        String       itemName,
        int          quantity,
        BigDecimal   price,
        String       paymentMethod,
        LocalDateTime createdAt
) {}
