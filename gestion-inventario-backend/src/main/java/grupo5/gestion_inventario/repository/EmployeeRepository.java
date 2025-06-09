package grupo5.gestion_inventario.repository;

import grupo5.gestion_inventario.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /* login por e-mail (username) */
    Optional<Employee> findByEmail(String email);

    /* si algún día permites “name” como usuario: */
    Optional<Employee> findByName(String name);

    long countByPlan(String plan);


}
