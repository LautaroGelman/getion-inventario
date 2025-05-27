// src/main/java/grupo5/gestion_inventario/superpanel/service/AdminUserService.java
package grupo5.gestion_inventario.superpanel.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import grupo5.gestion_inventario.superpanel.model.AdminUser;
import grupo5.gestion_inventario.superpanel.repository.AdminUserRepository;

@Service
public class AdminUserService {

    private final AdminUserRepository repo;
    private final PasswordEncoder encoder;

    public AdminUserService(AdminUserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Transactional
    public AdminUser create(String username, String rawPassword, Set<String> roles) {
        String hash = encoder.encode(rawPassword);
        return repo.save(new AdminUser(username, hash, roles));
    }

    @Transactional(readOnly = true)
    public List<AdminUser> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<AdminUser> findById(Long id) {
        return repo.findById(id);
    }

    @Transactional
    public Optional<AdminUser> update(Long id, String rawPassword, Set<String> roles) {
        return repo.findById(id).map(u -> {
            if (rawPassword != null) {
                u.setPasswordHash(encoder.encode(rawPassword));
            }
            if (roles != null) {
                u.setRoles(roles);
            }
            return repo.save(u);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        return repo.findById(id).map(u -> { repo.delete(u); return true; }).orElse(false);
    }
}