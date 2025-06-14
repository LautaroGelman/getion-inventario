package grupo5.gestion_inventario.model;

import org.springframework.security.core.GrantedAuthority;

public enum EmployeeRole implements GrantedAuthority {
    ADMINISTRADOR,
    CAJERO,
    MULTIFUNCION;

    @Override
    public String getAuthority() {
        // Devuelve el nombre del enum como la representación en String de la autoridad.
        // Spring Security usará esto para las comprobaciones de roles.
        return this.name();
    }
}