package grupo5.gestion_inventario.superpanel.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import grupo5.gestion_inventario.superpanel.model.CustomerAccount;
import grupo5.gestion_inventario.superpanel.repository.CustomerAccountRepository;

@Service
public class CustomerAccountService {

    private final CustomerAccountRepository repo;

    public CustomerAccountService(CustomerAccountRepository repo) {
        this.repo = repo;
    }

    public CustomerAccount create(CustomerAccount acc) {
        return repo.save(acc);
    }

    public List<CustomerAccount> findAll() {
        return repo.findAll();
    }

    public Optional<CustomerAccount> findById(Long id) {
        return repo.findById(id);
    }

    public Optional<CustomerAccount> update(Long id, CustomerAccount acc) {
        return repo.findById(id).map(existing -> {
            existing.setBusinessName(acc.getBusinessName());
            existing.setFiscalData(acc.getFiscalData());
            existing.setLogoUrl(acc.getLogoUrl());
            existing.setStatus(acc.getStatus());
            return repo.save(existing);
        });
    }

    public boolean delete(Long id) {
        return repo.findById(id).map(acc -> {
            repo.delete(acc);
            return true;
        }).orElse(false);
    }
}
