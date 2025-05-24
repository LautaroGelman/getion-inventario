// src/main/java/grupo5/gestion_inventario/superpanel/controller/AdminUserController.java
package grupo5.gestion_inventario.superpanel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import grupo5.gestion_inventario.superpanel.model.AdminUser;
import grupo5.gestion_inventario.superpanel.service.AdminUserService;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    private final AdminUserService service;

    public AdminUserController(AdminUserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AdminUser> create(@RequestBody Map<String,Object> body) {
        String username = (String) body.get("username");
        String password = (String) body.get("password");
        @SuppressWarnings("unchecked")
        List<String> rolesList = (List<String>) body.get("roles");
        // Convertir la lista JSON a Set para el servicio
        Set<String> roles = rolesList != null
                ? new HashSet<>(rolesList)
                : Set.of();
        AdminUser created = service.create(username, password, roles);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<AdminUser>> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUser> get(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminUser> update(
            @PathVariable Long id,
            @RequestBody Map<String,Object> body) {
        String password = (String) body.get("password");
        @SuppressWarnings("unchecked")
        List<String> rolesList = (List<String>) body.get("roles");
        Set<String> roles = rolesList != null
                ? new HashSet<>(rolesList)
                : null;  // null aquí para indicar “no cambiar”
        return service.update(id, password, roles)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
