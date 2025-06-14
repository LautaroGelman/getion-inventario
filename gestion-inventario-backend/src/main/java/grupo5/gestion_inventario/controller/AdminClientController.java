package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.dto.ClientCreateRequest;
import grupo5.gestion_inventario.clientpanel.dto.ClientListDto;
import grupo5.gestion_inventario.clientpanel.dto.ClientRowDto;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/clients")
@PreAuthorize("hasRole('ADMIN')")
public class AdminClientController {

    private final ClientService    clientService;
    private final PasswordEncoder  passwordEncoder;

    public AdminClientController(ClientService clientService,
                                 PasswordEncoder passwordEncoder) {
        this.clientService   = clientService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Crea un nuevo cliente (solo ADMIN) y devuelve un DTO sin exponer el hash.
     */
    @PostMapping
    public ResponseEntity<ClientRowDto> createClient(@RequestBody ClientCreateRequest req) {
        Client client = new Client();
        client.setName(      req.getName() );
        client.setEmail(     req.getEmail() );
        client.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        client.setTelefono(  req.getTelefono() );
        client.setPlan(      req.getPlan().toUpperCase());
        client.setEstado(    req.getEstado().toUpperCase());

        Client saved = clientService.create(client);

        ClientRowDto dto = new ClientRowDto(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getTelefono(),
                saved.getPlan(),
                saved.getEstado()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dto);
    }

    /**
     * Lista todos los clientes con contadores por plan.
     */
    @GetMapping
    public ResponseEntity<ClientListDto> listClients() {
        long total      = clientService.countAll();
        long basico     = clientService.countByPlan("BASICO");
        long intermedio = clientService.countByPlan("INTERMEDIO");
        long premium    = clientService.countByPlan("PREMIUM");

        List<ClientRowDto> rows = clientService.findAll().stream()
                .map(c -> new ClientRowDto(
                        c.getId(),
                        c.getName(),
                        c.getEmail(),
                        c.getTelefono(),
                        c.getPlan(),
                        c.getEstado()
                ))
                .toList();

        ClientListDto dto = new ClientListDto(
                total,
                basico,
                intermedio,
                premium,
                rows
        );
        return ResponseEntity.ok(dto);
    }

    /**
     * Marca un cliente como INACTIVO.
     */
    @PatchMapping("/{id}/inactive")
    public ResponseEntity<Void> inactivateClient(@PathVariable Long id) {
        clientService.inactivate(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Marca un cliente como ACTIVO.
     */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateClient(@PathVariable Long id) {
        clientService.activate(id);
        return ResponseEntity.noContent().build();
    }
}
