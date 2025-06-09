
package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.dto.ProviderRequest;
import grupo5.gestion_inventario.clientpanel.model.Provider;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.repository.ClientRepository;
import grupo5.gestion_inventario.service.ProviderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/client/providers")
@PreAuthorize("hasAuthority('MANAGER')")
public class ClientProviderController {

    private final ProviderService providerService;
    private final ClientRepository clientRepository;

    public ClientProviderController(ProviderService providerService, ClientRepository clientRepository) {
        this.providerService = providerService;
        this.clientRepository = clientRepository;
    }

    private Client getAuthenticatedClient(Authentication auth) {
        return clientRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cliente no autenticado"));
    }

    @GetMapping
    public ResponseEntity<List<Provider>> listProviders(Authentication auth) {
        Client client = getAuthenticatedClient(auth);
        List<Provider> providers = providerService.findByClientId(client.getId());
        return ResponseEntity.ok(providers);
    }

    @PostMapping
    public ResponseEntity<Provider> createProvider(@RequestBody ProviderRequest request, Authentication auth) {
        Client client = getAuthenticatedClient(auth);
        Provider createdProvider = providerService.create(client, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProvider);
    }

    @GetMapping("/{providerId}")
    public ResponseEntity<Provider> getProviderById(@PathVariable Long providerId, Authentication auth) {
        Client client = getAuthenticatedClient(auth);
        return providerService.findByIdAndClient(providerId, client)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{providerId}")
    public ResponseEntity<Provider> updateProvider(@PathVariable Long providerId, @RequestBody ProviderRequest request, Authentication auth) {
        Client client = getAuthenticatedClient(auth);
        return providerService.update(providerId, request, client)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{providerId}")
    public ResponseEntity<Void> deleteProvider(@PathVariable Long providerId, Authentication auth) {
        Client client = getAuthenticatedClient(auth);
        if (providerService.delete(providerId, client)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}



/*package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.dto.ProviderRequest;
import grupo5.gestion_inventario.clientpanel.model.Provider;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.repository.ClientRepository;
import grupo5.gestion_inventario.service.ProviderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client/providers")
@PreAuthorize("hasRole('CLIENT')")
public class ClientProviderController {

    private final ProviderService providerService;
    private final ClientRepository clientRepository;

    public ClientProviderController(ProviderService providerService, ClientRepository clientRepository) {
        this.providerService = providerService;
        this.clientRepository = clientRepository;
    }

    /**
     * POST /client/providers
     * Crea un nuevo proveedor para el cliente autenticado.

    @PostMapping
    public ResponseEntity<Provider> createProvider(@RequestBody ProviderRequest request, Authentication auth) {
        Client client = clientRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        Provider createdProvider = providerService.create(client, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProvider);
    }

    /**
     * GET /client/providers
     * Lista todos los proveedores del cliente autenticado.

    @GetMapping
    public ResponseEntity<List<Provider>> getProviders(Authentication auth) {
        Client client = clientRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        List<Provider> providers = providerService.findByClientId(client.getId());
        return ResponseEntity.ok(providers);
    }

    /**
     * GET /client/providers/{id}
     * Obtiene un proveedor específico por ID.

    @GetMapping("/{id}")
    public ResponseEntity<Provider> getProviderById(@PathVariable Long id, Authentication auth) {
        Client client = clientRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        return providerService.findByIdAndClient(id, client)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /client/providers/{id}
     * Actualiza un proveedor existente.
    @PutMapping("/{id}")
    public ResponseEntity<Provider> updateProvider(@PathVariable Long id, @RequestBody ProviderRequest request, Authentication auth) {
        Client client = clientRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        return providerService.update(id, request, client)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /client/providers/{id}
     * Elimina un proveedor.

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvider(@PathVariable Long id, Authentication auth) {
        Client client = clientRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        boolean deleted = providerService.delete(id, client);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
*/