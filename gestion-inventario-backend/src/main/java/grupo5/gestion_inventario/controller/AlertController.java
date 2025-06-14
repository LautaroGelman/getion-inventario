package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.model.Alert;
import grupo5.gestion_inventario.clientpanel.repository.AlertRepository;
import grupo5.gestion_inventario.model.Employee;
import grupo5.gestion_inventario.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin(origins = "http://localhost:5173")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MULTIFUNCION')") // Solo los administradores ven alertas
public class AlertController {

    @Autowired
    private AlertRepository alertRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/unread")
    public ResponseEntity<List<Alert>> getUnreadAlerts(Authentication authentication) {
        Employee employee = employeeRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        List<Alert> alerts = alertRepository.findByClientIdAndIsReadFalseOrderByCreatedAtDesc(employee.getClient().getId());
        return ResponseEntity.ok(alerts);
    }

    @PostMapping("/{alertId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long alertId, Authentication authentication) {
        Employee employee = employeeRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada"));

        // Asegurarse de que la alerta pertenezca al negocio del empleado
        if (!alert.getClient().getId().equals(employee.getClient().getId())) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        alert.setRead(true);
        alertRepository.save(alert);
        return ResponseEntity.ok().build();
    }
}