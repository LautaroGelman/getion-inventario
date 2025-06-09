package grupo5.gestion_inventario.service;

import grupo5.gestion_inventario.clientpanel.model.EndCustomer;
import grupo5.gestion_inventario.clientpanel.repository.EndCustomerRepository;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EndCustomerService {
    private final EndCustomerRepository repo;
    private final ClientRepository clientRepo;

    public EndCustomerService(EndCustomerRepository repo, ClientRepository clientRepo) {
        this.repo = repo;
        this.clientRepo = clientRepo;
    }

    @Transactional
    public EndCustomer create(Client client, EndCustomer customer) {
        customer.setClient(client);
        return repo.save(customer);
    }

    @Transactional
    public Optional<EndCustomer> update(Long id, EndCustomer data, Client client) {
        return repo.findById(id)
                .filter(c -> c.getClient().getId().equals(client.getId()))
                .map(c -> {
                    c.setName(data.getName());
                    c.setContactInfo(data.getContactInfo());
                    return repo.save(c);
                });
    }

    @Transactional
    public boolean delete(Long id, Client client) {
        return repo.findById(id)
                .filter(c -> c.getClient().getId().equals(client.getId()))
                .map(c -> { repo.delete(c); return true; })
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public List<EndCustomer> listByClient(Long clientId) {
        if (!clientRepo.existsById(clientId)) {
            throw new IllegalArgumentException("Cliente no encontrado: " + clientId);
        }
        return repo.findByClientId(clientId);
    }

    @Transactional(readOnly = true)
    public Optional<EndCustomer> findByIdAndClient(Long id, Client client) {
        return repo.findById(id).filter(c -> c.getClient().getId().equals(client.getId()));
    }
}
