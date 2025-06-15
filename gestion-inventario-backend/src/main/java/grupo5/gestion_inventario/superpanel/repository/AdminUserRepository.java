// src/main/java/grupo5/gestion_inventario/superpanel/repository/AdminUserRepository.java
package grupo5.gestion_inventario.superpanel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import grupo5.gestion_inventario.superpanel.model.AdminUser;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    Optional<AdminUser> findByUsername(String username);
}
