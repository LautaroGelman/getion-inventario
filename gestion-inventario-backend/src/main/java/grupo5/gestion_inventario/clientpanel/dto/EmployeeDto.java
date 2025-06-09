package grupo5.gestion_inventario.clientpanel.dto;

import grupo5.gestion_inventario.model.EmployeeRole;

/**
 * Datos básicos de un empleado para exponer a la UI sin la contraseña.
 */
public record EmployeeDto(
        Long id,
        String name,
        String email,
        EmployeeRole role
) {}
