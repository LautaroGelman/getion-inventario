// src/main/java/grupo5/gestion_inventario/service/ExpenseService.java
package grupo5.gestion_inventario.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import grupo5.gestion_inventario.model.Employee;
import grupo5.gestion_inventario.repository.EmployeeRepository;
import grupo5.gestion_inventario.clientpanel.model.Expense;
import grupo5.gestion_inventario.clientpanel.repository.ExpenseRepository;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepo;
    private final EmployeeRepository  employeeRepo;

    public ExpenseService(ExpenseRepository expenseRepo,
                          EmployeeRepository employeeRepo) {
        this.expenseRepo = expenseRepo;
        this.employeeRepo  = employeeRepo;
    }

    /**
     * Crea un gasto para un cliente.
     */
    @Transactional
    public Expense create(Long clientId, Expense expense) {
        Employee client = employeeRepo.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + clientId));
        expense.setClient(client);
        return expenseRepo.save(expense);
    }

    /**
     * Lista gastos de un cliente.
     */
    @Transactional(readOnly = true)
    public List<Expense> findByClientId(Long clientId) {
        if (!employeeRepo.existsById(clientId)) {
            throw new IllegalArgumentException("Cliente no encontrado: " + clientId);
        }
        return expenseRepo.findByClientId(clientId);
    }

    /**
     * Elimina un gasto por su id.
     * @return true si existía y se borró.
     */
    @Transactional
    public boolean delete(Long clientId, Long expenseId) {
        return expenseRepo.findById(expenseId)
                .filter(e -> e.getClient().getId().equals(clientId))
                .map(e -> {
                    expenseRepo.delete(e);
                    return true;
                }).orElse(false);
    }
}
