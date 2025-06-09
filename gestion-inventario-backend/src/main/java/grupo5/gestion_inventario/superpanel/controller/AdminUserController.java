package grupo5.gestion_inventario.superpanel.controller;

import grupo5.gestion_inventario.clientpanel.dto.AdminUserRequest;
import grupo5.gestion_inventario.superpanel.model.AdminUser;
import grupo5.gestion_inventario.superpanel.service.AdminUserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para gestión de usuarios administradores.
 */
@RestController
@RequestMapping(value = "/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUserController {

    private final AdminUserService service;

    public AdminUserController(AdminUserService service) {
        this.service = service;
    }

    /**
     * Crea un nuevo usuario administrador.
     * Este endpoint NO requiere autenticación para permitir registrar el primer admin.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminUser> create(@RequestBody AdminUserRequest req) {
        AdminUser created = service.create(req.getUsername(), req.getPassword(), req.getRoles());
        return ResponseEntity.ok(created);
    }

    /**
     * Lista todos los administradores.
     * Solo accesible por ADMIN.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminUser>> list() {
        return ResponseEntity.ok(service.findAll());
    }

    /**
     * Obtiene un administrador por ID.
     * Solo accesible por ADMIN.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminUser> get(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualiza los datos de un administrador.
     * Solo accesible por ADMIN.
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminUser> update(
            @PathVariable Long id,
            @RequestBody AdminUserRequest req) {
        return service.update(id, req.getPassword(), req.getRoles())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un administrador.
     * Solo accesible por ADMIN.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean removed = service.delete(id);
        return removed
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
