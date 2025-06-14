package grupo5.gestion_inventario.service;

import grupo5.gestion_inventario.clientpanel.dto.CloseSessionRequest;
import grupo5.gestion_inventario.clientpanel.dto.OpenSessionRequest;
import grupo5.gestion_inventario.clientpanel.model.CashRegisterSession;
import grupo5.gestion_inventario.clientpanel.repository.CashRegisterSessionRepository;
import grupo5.gestion_inventario.clientpanel.repository.SaleRepository;
import grupo5.gestion_inventario.model.Employee;
import grupo5.gestion_inventario.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CashRegisterSessionService {

    @Autowired
    private CashRegisterSessionRepository sessionRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private SaleRepository saleRepository;

    public Optional<CashRegisterSession> getCurrentSession(String employeeEmail) {
        Employee employee = employeeRepository.findByEmail(employeeEmail)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        return sessionRepository.findByEmployeeIdAndStatus(employee.getId(), "OPEN");
    }

    @Transactional
    public CashRegisterSession openSession(OpenSessionRequest request, String employeeEmail) {
        Employee employee = employeeRepository.findByEmail(employeeEmail)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        if (getCurrentSession(employeeEmail).isPresent()) {
            throw new IllegalStateException("El empleado ya tiene una sesión de caja abierta.");
        }

        CashRegisterSession session = new CashRegisterSession();
        session.setEmployee(employee);
        session.setClient(employee.getClient());
        session.setOpeningTime(LocalDateTime.now());
        session.setInitialAmount(request.getInitialAmount());
        session.setStatus("OPEN");

        return sessionRepository.save(session);
    }

    @Transactional
    public CashRegisterSession closeSession(CloseSessionRequest request, String employeeEmail) {
        CashRegisterSession session = getCurrentSession(employeeEmail)
                .orElseThrow(() -> new IllegalStateException("No hay una sesión de caja abierta para este empleado."));

        LocalDateTime closingTime = LocalDateTime.now();

        BigDecimal salesTotal = saleRepository.sumTotalAmountByEmployeeAndDateBetween(
                session.getEmployee().getId(),
                session.getOpeningTime(),
                closingTime
        );

        BigDecimal expectedAmount = session.getInitialAmount().add(salesTotal);
        BigDecimal countedAmount = request.getCountedAmount();
        BigDecimal difference = countedAmount.subtract(expectedAmount);

        session.setClosingTime(closingTime);
        session.setExpectedAmount(expectedAmount);
        session.setCountedAmount(countedAmount);
        session.setDifference(difference);
        session.setStatus("CLOSED");

        return sessionRepository.save(session);
    }
}
