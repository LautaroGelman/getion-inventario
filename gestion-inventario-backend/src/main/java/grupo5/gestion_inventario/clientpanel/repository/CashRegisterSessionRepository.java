package grupo5.gestion_inventario.clientpanel.repository;

import grupo5.gestion_inventario.clientpanel.model.CashRegisterSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CashRegisterSessionRepository extends JpaRepository<CashRegisterSession, Long> {

    /**
     * Todas las sesiones de caja de un cliente.
     */
    List<CashRegisterSession> findByClientId(Long clientId);

    /**
     * La sesión de caja abierta (closingTime is null) de un cliente, si existe.
     * CORRECCIÓN: Renombrado de 'ClosedAt' a 'ClosingTime' para coincidir con la entidad.
     */
    Optional<CashRegisterSession> findByClientIdAndClosingTimeIsNull(Long clientId);

    /**
     * Encuentra una sesión de caja por el ID del empleado y su estado (p. ej., "OPEN").
     */
    Optional<CashRegisterSession> findByEmployeeIdAndStatus(Long employeeId, String status);
}