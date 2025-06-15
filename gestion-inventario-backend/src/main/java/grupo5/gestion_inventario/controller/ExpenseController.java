// src/main/java/grupo5/gestion_inventario/controller/ExpenseController.java
package grupo5.gestion_inventario.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import grupo5.gestion_inventario.clientpanel.model.Expense;
import grupo5.gestion_inventario.service.ExpenseService;

@RestController
@RequestMapping("/clients/{clientId}/expenses")
public class ExpenseController {

    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    /**
     * POST /clients/{clientId}/expenses
     */
    @PostMapping
    public ResponseEntity<Expense> create(@PathVariable Long clientId,
                                          @RequestBody Expense expense) {
        Expense created = service.create(clientId, expense);
        return ResponseEntity.ok(created);
    }

    /**
     * GET /clients/{clientId}/expenses
     */
    @GetMapping
    public ResponseEntity<List<Expense>> list(@PathVariable Long clientId) {
        List<Expense> expenses = service.findByClientId(clientId);
        return ResponseEntity.ok(expenses);
    }

    /**
     * DELETE /clients/{clientId}/expenses/{expenseId}
     */
    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> delete(@PathVariable Long clientId,
                                       @PathVariable Long expenseId) {
        if (service.delete(clientId, expenseId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
