// src/main/java/grupo5/gestion_inventario/service/CashRegisterSessionService.java
package grupo5.gestion_inventario.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import grupo5.gestion_inventario.clientpanel.model.CashRegisterSession;
import grupo5.gestion_inventario.clientpanel.repository.CashRegisterSessionRepository;
import grupo5.gestion_inventario.model.Employee;
import grupo5.gestion_inventario.repository.EmployeeRepository;

@Service
public class CashRegisterSessionService {

    private final CashRegisterSessionRepository sessionRepo;
    private final EmployeeRepository employeeRepo;

    public CashRegisterSessionService(CashRegisterSessionRepository sessionRepo,
                                      EmployeeRepository employeeRepo) {
        this.sessionRepo = sessionRepo;
        this.employeeRepo  = employeeRepo;
    }

    /**
     * Abre una nueva sesión de caja para el cliente, si no hay ninguna abierta.
     */
    @Transactional
    public CashRegisterSession openSession(Long clientId, BigDecimal openingBalance) {
        Employee client = employeeRepo.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + clientId));

        Optional<CashRegisterSession> open = sessionRepo.findByClientIdAndClosedAtIsNull(clientId);
        if (open.isPresent()) {
            throw new IllegalStateException("Ya existe una sesión abierta para el cliente: " + clientId);
        }

        CashRegisterSession session = new CashRegisterSession(openingBalance, client);
        return sessionRepo.save(session);
    }

    /**
     * Cierra la sesión de caja especificada, marcando closedAt y closingBalance.
     */
    @Transactional
    public CashRegisterSession closeSession(Long clientId,
                                            Long sessionId,
                                            BigDecimal closingBalance) {
        CashRegisterSession session = sessionRepo.findById(sessionId)
                .filter(s -> s.getClient().getId().equals(clientId))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Sesión no encontrada para cliente " + clientId + ": " + sessionId));

        if (session.getClosedAt() != null) {
            throw new IllegalStateException("La sesión ya está cerrada: " + sessionId);
        }
        session.setClosingBalance(closingBalance);
        session.setClosedAt(LocalDateTime.now());
        return sessionRepo.save(session);
    }

    /**
     * Lista todas las sesiones de caja de un cliente.
     */
    @Transactional(readOnly = true)
    public List<CashRegisterSession> listSessions(Long clientId) {
        if (!employeeRepo.existsById(clientId)) {
            throw new IllegalArgumentException("Cliente no encontrado: " + clientId);
        }
        return sessionRepo.findByClientId(clientId);
    }

    /**
     * Devuelve la sesión abierta de un cliente, si existe.
     */
    @Transactional(readOnly = true)
    public Optional<CashRegisterSession> getOpenSession(Long clientId) {
        return sessionRepo.findByClientIdAndClosedAtIsNull(clientId);
    }
}
