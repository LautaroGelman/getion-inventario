// src/main/java/grupo5/gestion_inventario/service/ProviderService.java
package grupo5.gestion_inventario.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.repository.ClientRepository;
import grupo5.gestion_inventario.clientpanel.model.Provider;
import grupo5.gestion_inventario.clientpanel.repository.ProviderRepository;

@Service
public class ProviderService {

    private final ProviderRepository providerRepo;
    private final ClientRepository   clientRepo;

    public ProviderService(ProviderRepository providerRepo,
                           ClientRepository clientRepo) {
        this.providerRepo = providerRepo;
        this.clientRepo   = clientRepo;
    }

    /**
     * Crea un proveedor para un cliente dado.
     */
    @Transactional
    public Provider create(Long clientId, Provider provider) {
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + clientId));
        provider.setClient(client);
        return providerRepo.save(provider);
    }

    /**
     * Lista proveedores de un cliente.
     */
    @Transactional(readOnly = true)
    public List<Provider> findByClientId(Long clientId) {
        if (!clientRepo.existsById(clientId)) {
            throw new IllegalArgumentException("Cliente no encontrado: " + clientId);
        }
        return providerRepo.findByClientId(clientId);
    }

    /**
     * Elimina un proveedor por su id.
     * @return true si existía y se borró.
     */
    @Transactional
    public boolean delete(Long clientId, Long providerId) {
        return providerRepo.findById(providerId)
                .filter(p -> p.getClient().getId().equals(clientId))
                .map(p -> {
                    providerRepo.delete(p);
                    return true;
                }).orElse(false);
    }
}
