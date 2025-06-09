
// src/main/java/grupo5/gestion_inventario/superpanel/repository/NotificationSettingRepository.java
package grupo5.gestion_inventario.superpanel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import grupo5.gestion_inventario.superpanel.model.NotificationSetting;

public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {
    List<NotificationSetting> findByType(String type);
    List<NotificationSetting> findByCustomerAccountId(Long customerAccountId);
}