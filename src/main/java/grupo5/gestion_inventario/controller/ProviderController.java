// src/main/java/grupo5/gestion_inventario/controller/ProviderController.java
package grupo5.gestion_inventario.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import grupo5.gestion_inventario.clientpanel.model.Provider;
import grupo5.gestion_inventario.service.ProviderService;

/**
 * ESTE CONTROLADOR HA SIDO REEMPLAZADO POR ClientProviderController PARA LA LÓGICA
 * DEL PANEL DE CLIENTE. SE COMENTA PARA EVITAR ERRORES DE COMPILACIÓN.
 */
@RestController
@RequestMapping("/clients/{clientId}/providers")
public class ProviderController {

    /*
    private final ProviderService service;

    public ProviderController(ProviderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Provider> create(@PathVariable Long clientId,
                                           @RequestBody Provider provider) {
        // La siguiente línea es la que causa el error y ha sido corregida en ClientProviderController
        // Provider created = service.create(clientId, provider);
        // return ResponseEntity.ok(created);
        return null; // Se devuelve null para que el código compile
    }

    @GetMapping
    public ResponseEntity<List<Provider>> list(@PathVariable Long clientId) {
        List<Provider> providers = service.findByClientId(clientId);
        return ResponseEntity.ok(providers);
    }

    @DeleteMapping("/{providerId}")
    public ResponseEntity<Void> delete(@PathVariable Long clientId,
                                       @PathVariable Long providerId) {
        // La lógica de borrado también fue movida a ClientProviderController
        // if (service.delete(clientId, providerId)) {
        //     return ResponseEntity.noContent().build();
        // }
        return ResponseEntity.notFound().build();
    }
    */
}