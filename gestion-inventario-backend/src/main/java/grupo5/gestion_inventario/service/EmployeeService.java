package grupo5.gestion_inventario.service;

import grupo5.gestion_inventario.model.Employee;
import grupo5.gestion_inventario.model.BusinessAccount;
import grupo5.gestion_inventario.repository.EmployeeRepository;
import grupo5.gestion_inventario.repository.BusinessAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repo;
    private final BusinessAccountRepository businessAccountRepo;

    public EmployeeService(EmployeeRepository repo, BusinessAccountRepository businessAccountRepo) {
        this.repo = repo;
        this.businessAccountRepo = businessAccountRepo;
    }

    public Employee create(Long accountId, Employee e) {
        BusinessAccount account = businessAccountRepo.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("BusinessAccount no encontrado"));
        e.setBusinessAccount(account);
        return repo.save(e);
    }

    public List<Employee> listByClient(Long accountId) {
        return repo.findAll().stream()
                .filter(emp -> emp.getBusinessAccount() != null && emp.getBusinessAccount().getId().equals(accountId))
                .toList();
    }

    public void delete(Long employeeId) {
        repo.deleteById(employeeId);
    }
}
