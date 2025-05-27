// src/main/java/grupo5/gestion_inventario/clientpanel/dto/ProductDto.java
package grupo5.gestion_inventario.clientpanel.dto;

import java.math.BigDecimal;

/**
 * Datos para mostrar inventario en el panel del cliente,
 * incluyendo ahora el id para operaciones posteriores.
 */
public record ProductDto(
        Long id,            // ← añadido
        String code,
        String name,
        String description,
        int    stock,
        BigDecimal price
) {}
