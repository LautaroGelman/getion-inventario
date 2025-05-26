// src/main/java/grupo5/gestion_inventario/controller/AdminClientController.java
package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.dto.ClientCreateRequest;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/clients")
public class AdminClientController {

    private final ClientService clientService;
    private final PasswordEncoder passwordEncoder;

    public AdminClientController(ClientService clientService,
                                 PasswordEncoder passwordEncoder) {
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Crea un nuevo cliente (solo ADMIN).
     */
    @PostMapping
    public ResponseEntity<Client> createClient(
            @RequestBody ClientCreateRequest req) {
        Client client = new Client();
        client.setName(req.getName());
        client.setEmail(req.getEmail());
        client.setPassword(passwordEncoder.encode(req.getPassword()));
        client.setTelefono(req.getTelefono());
        client.setPlan(req.getPlan());
        client.setEstado(req.getEstado());

        Client saved = clientService.create(client);
        return ResponseEntity.ok(saved);
    }
}
