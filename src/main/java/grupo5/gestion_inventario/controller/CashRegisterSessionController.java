// src/main/java/grupo5/gestion_inventario/controller/CashRegisterSessionController.java
package grupo5.gestion_inventario.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import grupo5.gestion_inventario.clientpanel.model.CashRegisterSession;
import grupo5.gestion_inventario.service.CashRegisterSessionService;

@RestController
@RequestMapping("/clients/{clientId}/cash-sessions")
public class CashRegisterSessionController {

    private final CashRegisterSessionService service;

    public CashRegisterSessionController(CashRegisterSessionService service) {
        this.service = service;
    }

    /**
     * POST /clients/{clientId}/cash-sessions
     * Body: { "openingBalance": 100.00 }
     */
    @PostMapping
    public ResponseEntity<CashRegisterSession> open(
            @PathVariable Long clientId,
            @RequestBody CashRegisterSession dto) {
        CashRegisterSession created =
                service.openSession(clientId, dto.getOpeningBalance());
        return ResponseEntity.ok(created);
    }

    /**
     * PUT /clients/{clientId}/cash-sessions/{sessionId}/close
     * Body: { "closingBalance": 150.00 }
     */
    @PutMapping("/{sessionId}/close")
    public ResponseEntity<CashRegisterSession> close(
            @PathVariable Long clientId,
            @PathVariable Long sessionId,
            @RequestBody CashRegisterSession dto) {
        CashRegisterSession closed =
                service.closeSession(clientId, sessionId, dto.getClosingBalance());
        return ResponseEntity.ok(closed);
    }

    /**
     * GET /clients/{clientId}/cash-sessions
     */
    @GetMapping
    public ResponseEntity<List<CashRegisterSession>> list(
            @PathVariable Long clientId) {
        List<CashRegisterSession> sessions = service.listSessions(clientId);
        return ResponseEntity.ok(sessions);
    }

    /**
     * GET /clients/{clientId}/cash-sessions/open
     */
    @GetMapping("/open")
    public ResponseEntity<CashRegisterSession> getOpen(
            @PathVariable Long clientId) {
        Optional<CashRegisterSession> open = service.getOpenSession(clientId);
        return open.map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}
