/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package grupo5.gestion_inventario.service;

import java.util.List;
import grupo5.gestion_inventario.model.Employee;
import org.springframework.stereotype.Service;
import grupo5.gestion_inventario.repository.EmployeeRepository;

/**
 *
 * @author lautaro
 */


@Service
public class EmployeeService {

    private final EmployeeRepository repo;

    public EmployeeService(EmployeeRepository repo) {
        this.repo = repo;
    }

    public Employee create(Employee c) {
        return repo.save(c);
    }

    public List<Employee> findAll() {
        return repo.findAll();
    }

    public Employee findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employeee no encontrado"));
    }

    public Employee update(Long id, Employee data) {
        Employee c = findById(id);
        c.setName(data.getName());
        c.setEmail(data.getEmail());
        return repo.save(c);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    /** Cuenta todos los clientes */
    public long countAll() {
        return repo.count();
    }

    /** Cuenta clientes filtrados por plan */
    public long countByPlan(String plan) {
        return repo.countByPlan(plan);
    }

    /** Marca un cliente como INACTIVO */
    public void inactivate(Long id) {
        Employee c = findById(id);
        c.setEstado("INACTIVO");
        repo.save(c);
    }

    /** Marca un cliente como ACTIVO */
    public void activate(Long id) {
        Employee c = findById(id);
        c.setEstado("ACTIVO");
        repo.save(c);
    }
}