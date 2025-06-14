package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.dto.CloseSessionRequest;
import grupo5.gestion_inventario.clientpanel.dto.OpenSessionRequest;
import grupo5.gestion_inventario.clientpanel.model.CashRegisterSession;
import grupo5.gestion_inventario.service.CashRegisterSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cash-sessions")
@CrossOrigin(origins = "http://localhost:5173")
@PreAuthorize("hasAnyRole('CAJERO', 'MULTIFUNCION', 'ADMINISTRADOR')") // Protegemos a nivel de clase
public class CashRegisterSessionController {

    @Autowired
    private CashRegisterSessionService sessionService;

    @GetMapping("/current")
    public ResponseEntity<CashRegisterSession> getCurrentSession(Authentication authentication) {
        return sessionService.getCurrentSession(authentication.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("/open")
    public ResponseEntity<CashRegisterSession> openSession(@RequestBody OpenSessionRequest request, Authentication authentication) {
        CashRegisterSession session = sessionService.openSession(request, authentication.getName());
        return ResponseEntity.ok(session);
    }

    @PostMapping("/close")
    public ResponseEntity<CashRegisterSession> closeSession(@RequestBody CloseSessionRequest request, Authentication authentication) {
        CashRegisterSession session = sessionService.closeSession(request, authentication.getName());
        return ResponseEntity.ok(session);
    }
}