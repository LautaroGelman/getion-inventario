// src/main/java/grupo5/gestion_inventario/clientpanel/repository/CashRegisterSessionRepository.java
package grupo5.gestion_inventario.clientpanel.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import grupo5.gestion_inventario.clientpanel.model.CashRegisterSession;

public interface CashRegisterSessionRepository extends JpaRepository<CashRegisterSession, Long> {

    /**
     * Todas las sesiones de caja de un cliente.
     */
    List<CashRegisterSession> findByClientId(Long clientId);

    /**
     * La sesión de caja abierta (closedAt is null) de un cliente, si existe.
     */
    Optional<CashRegisterSession> findByClientIdAndClosedAtIsNull(Long clientId);
}
