// src/main/java/grupo5/gestion_inventario/clientpanel/repository/ExpenseRepository.java
package grupo5.gestion_inventario.clientpanel.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import grupo5.gestion_inventario.clientpanel.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    /**
     * Devuelve todos los gastos de un cliente.
     */
    List<Expense> findByClientId(Long clientId);
}
