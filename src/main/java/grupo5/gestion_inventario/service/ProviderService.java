// src/main/java/grupo5/gestion_inventario/service/ProviderService.java
package grupo5.gestion_inventario.service;

import grupo5.gestion_inventario.clientpanel.dto.ProviderRequest;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.repository.ClientRepository;
import grupo5.gestion_inventario.clientpanel.model.Provider;
import grupo5.gestion_inventario.clientpanel.repository.ProviderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
     * Crea un proveedor para un cliente dado usando los datos de un DTO.
     */
    @Transactional
    public Provider create(Client client, ProviderRequest request) {
        Provider provider = new Provider();
        provider.setClient(client);
        provider.setName(request.getName());
        provider.setContactInfo(request.getContactInfo());
        provider.setPaymentTerms(request.getPaymentTerms());
        return providerRepo.save(provider);
    }

    /**
     * Actualiza un proveedor existente, verificando que pertenece al cliente.
     */
    @Transactional
    public Optional<Provider> update(Long providerId, ProviderRequest request, Client client) {
        return providerRepo.findById(providerId)
                // 1. Nos aseguramos que el proveedor pertenezca al cliente correcto.
                .filter(provider -> provider.getClient().getId().equals(client.getId()))
                // 2. Si es así, actualizamos sus datos y lo guardamos.
                .map(provider -> {
                    provider.setName(request.getName());
                    provider.setContactInfo(request.getContactInfo());
                    provider.setPaymentTerms(request.getPaymentTerms());
                    return providerRepo.save(provider);
                });
    }

    /**
     * Lista todos los proveedores de un cliente.
     */
    @Transactional(readOnly = true)
    public List<Provider> findByClientId(Long clientId) {
        // Este método se mantiene, es útil para listados.
        if (!clientRepo.existsById(clientId)) {
            throw new IllegalArgumentException("Cliente no encontrado: " + clientId);
        }
        return providerRepo.findByClientId(clientId);
    }

    /**
     * Busca un proveedor específico por su ID, verificando que pertenece al cliente.
     */
    @Transactional(readOnly = true)
    public Optional<Provider> findByIdAndClient(Long providerId, Client client) {
        return providerRepo.findById(providerId)
                .filter(p -> p.getClient().getId().equals(client.getId()));
    }


    /**
     * Elimina un proveedor por su id, verificando que pertenece al cliente.
     * @return true si existía y se borró.
     */
    @Transactional
    public boolean delete(Long providerId, Client client) {
        return providerRepo.findById(providerId)
                .filter(p -> p.getClient().getId().equals(client.getId()))
                .map(p -> {
                    providerRepo.delete(p);
                    return true;
                }).orElse(false);
    }
}