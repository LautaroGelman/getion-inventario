package grupo5.gestion_inventario.clientpanel.dto;


public record ClientRowDto(
        Long   id,
        String name,
        String email,
        String telefono,
        String plan,
        String estado
) {}
