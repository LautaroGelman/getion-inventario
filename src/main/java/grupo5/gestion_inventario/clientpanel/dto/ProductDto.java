package grupo5.gestion_inventario.clientpanel.dto;

import java.math.BigDecimal;

/**
 * Datos para mostrar inventario en el panel del cliente,
 * incluyendo ahora el id y el costo para operaciones posteriores.
 */
public record ProductDto(
        Long id,
        String code,
        String name,
        String description,
        int stock,
        BigDecimal cost, // <-- AÑADIDO
        BigDecimal price
) {}