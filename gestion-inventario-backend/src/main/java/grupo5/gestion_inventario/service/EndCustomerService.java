package grupo5.gestion_inventario.service;

import grupo5.gestion_inventario.clientpanel.model.EndCustomer;
import grupo5.gestion_inventario.clientpanel.repository.EndCustomerRepository;
import grupo5.gestion_inventario.model.Employee;
import grupo5.gestion_inventario.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EndCustomerService {
    private final EndCustomerRepository repo;
    private final EmployeeRepository employeeRepo;

    public EndCustomerService(EndCustomerRepository repo, EmployeeRepository employeeRepo) {
        this.repo = repo;
        this.employeeRepo = employeeRepo;
    }

    @Transactional
    public EndCustomer create(Employee client, EndCustomer customer) {
        customer.setClient(client);
        return repo.save(customer);
    }

    @Transactional
    public Optional<EndCustomer> update(Long id, EndCustomer data, Employee client) {
        return repo.findById(id)
                .filter(c -> c.getClient().getId().equals(client.getId()))
                .map(c -> {
                    c.setName(data.getName());
                    c.setContactInfo(data.getContactInfo());
                    return repo.save(c);
                });
    }

    @Transactional
    public boolean delete(Long id, Employee client) {
        return repo.findById(id)
                .filter(c -> c.getClient().getId().equals(client.getId()))
                .map(c -> { repo.delete(c); return true; })
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public List<EndCustomer> listByClient(Long clientId) {
        if (!employeeRepo.existsById(clientId)) {
            throw new IllegalArgumentException("Cliente no encontrado: " + clientId);
        }
        return repo.findByClientId(clientId);
    }

    @Transactional(readOnly = true)
    public Optional<EndCustomer> findByIdAndClient(Long id, Employee client) {
        return repo.findById(id).filter(c -> c.getClient().getId().equals(client.getId()));
    }
}
