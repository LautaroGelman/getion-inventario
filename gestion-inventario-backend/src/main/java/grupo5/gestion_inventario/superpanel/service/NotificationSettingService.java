
// src/main/java/grupo5/gestion_inventario/superpanel/service/NotificationSettingService.java
package grupo5.gestion_inventario.superpanel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import grupo5.gestion_inventario.superpanel.model.NotificationSetting;
import grupo5.gestion_inventario.superpanel.repository.NotificationSettingRepository;

@Service
public class NotificationSettingService {

    private final NotificationSettingRepository repo;

    public NotificationSettingService(NotificationSettingRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public NotificationSetting create(NotificationSetting setting) {
        return repo.save(setting);
    }

    @Transactional(readOnly = true)
    public List<NotificationSetting> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<NotificationSetting> findById(Long id) {
        return repo.findById(id);
    }

    @Transactional
    public Optional<NotificationSetting> update(Long id, NotificationSetting setting) {
        return repo.findById(id)
                .map(existing -> {
                    existing.setThresholdDaysBeforeExpiry(setting.getThresholdDaysBeforeExpiry());
                    return repo.save(existing);
                });
    }

    @Transactional
    public boolean delete(Long id) {
        return repo.findById(id)
                .map(s -> { repo.delete(s); return true; })
                .orElse(false);
    }
}