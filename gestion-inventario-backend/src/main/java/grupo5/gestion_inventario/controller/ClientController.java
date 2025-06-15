/*package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.dto.ClientCreateRequest;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.repository.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para que el ADMIN registre nuevos clientes.
@RestController
@RequestMapping("/admin/clients")
public class ClientController {

    private final ClientRepository   clientRepo;
    private final PasswordEncoder    passwordEncoder;

    public ClientController(ClientRepository clientRepo,
                            PasswordEncoder passwordEncoder) {
        this.clientRepo      = clientRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Client> create(@RequestBody ClientCreateRequest req) {
        // 1) Construimos la entidad usando los campos correctos
        Client c = new Client();
        c.setName(req.getName());
        c.setEmail(req.getEmail());
        // ── Hasheamos en BCrypt y guardamos en passwordHash ──
        c.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        // ───────────────────────────────────────────────────────
        c.setTelefono(req.getTelefono());
        c.setPlan(req.getPlan().toUpperCase());
        c.setEstado(req.getEstado().toUpperCase());

        // 2) Persistimos
        Client saved = clientRepo.save(c);

        // 3) Respondemos con CREATED
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }
}
 */
