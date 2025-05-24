
// src/main/java/grupo5/gestion_inventario/superpanel/controller/NotificationSettingController.java
package grupo5.gestion_inventario.superpanel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import grupo5.gestion_inventario.superpanel.model.NotificationSetting;
import grupo5.gestion_inventario.superpanel.service.NotificationSettingService;

@RestController
@RequestMapping("/admin/notifications")
public class NotificationSettingController {

    private final NotificationSettingService service;

    public NotificationSettingController(NotificationSettingService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<NotificationSetting> create(@RequestBody NotificationSetting s) {
        return ResponseEntity.ok(service.create(s));
    }

    // GET /admin/notifications/{id}
    @GetMapping("/{id}")
    public ResponseEntity<NotificationSetting> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<NotificationSetting>> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationSetting> update(@PathVariable Long id,
                                                      @RequestBody NotificationSetting s) {
        return service.update(id,s)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if(service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
