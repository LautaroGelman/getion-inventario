package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.repository.ClientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para gestionar información del cliente autenticado.
 */
@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientRepository clientRepo;

    public ClientController(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    /**
     * Devuelve los datos del cliente actualmente autenticado.
     * Endpoint: GET /clients/me
     */
    @GetMapping("/me")
    public ResponseEntity<Client> me(Authentication auth) {
        String email = auth.getName();
        Client client = clientRepo.findByEmail(email);
        return ResponseEntity.ok(client);
    }

}
