// src/main/java/grupo5/gestion_inventario/controller/ProviderController.java
package grupo5.gestion_inventario.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import grupo5.gestion_inventario.clientpanel.model.Provider;
import grupo5.gestion_inventario.service.ProviderService;

@RestController
@RequestMapping("/clients/{clientId}/providers")
public class ProviderController {

    private final ProviderService service;

    public ProviderController(ProviderService service) {
        this.service = service;
    }

    /**
     * POST /clients/{clientId}/providers
     */
    @PostMapping
    public ResponseEntity<Provider> create(@PathVariable Long clientId,
                                           @RequestBody Provider provider) {
        Provider created = service.create(clientId, provider);
        return ResponseEntity.ok(created);
    }

    /**
     * GET /clients/{clientId}/providers
     */
    @GetMapping
    public ResponseEntity<List<Provider>> list(@PathVariable Long clientId) {
        List<Provider> providers = service.findByClientId(clientId);
        return ResponseEntity.ok(providers);
    }

    /**
     * DELETE /clients/{clientId}/providers/{providerId}
     */
    @DeleteMapping("/{providerId}")
    public ResponseEntity<Void> delete(@PathVariable Long clientId,
                                       @PathVariable Long providerId) {
        if (service.delete(clientId, providerId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
