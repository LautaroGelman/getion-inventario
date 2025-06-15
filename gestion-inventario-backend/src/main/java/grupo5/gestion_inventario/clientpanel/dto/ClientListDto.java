package grupo5.gestion_inventario.clientpanel.dto;


import java.util.List;

public record ClientListDto(
        long total,
        long basico,
        long intermedio,
        long premium,
        List<ClientRowDto> clients
) {}