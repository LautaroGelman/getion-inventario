package grupo5.gestion_inventario.superpanel.service;

import grupo5.gestion_inventario.superpanel.model.AdminUser;
import grupo5.gestion_inventario.superpanel.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    public List<AdminUser> findAll() {
        return adminUserRepository.findAll();
    }

    public Optional<AdminUser> findById(Long id) {
        return adminUserRepository.findById(id);
    }

    public AdminUser create(String username, String password) {
        if (adminUserRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("El nombre de usuario ya existe");
        }
        String encodedPassword = passwordEncoder.encode(password);
        AdminUser newUser = new AdminUser();
        newUser.setUsername(username);
        newUser.setPasswordHash(encodedPassword);
        newUser.setRoles(Set.of("SUPER_ADMIN"));
        return adminUserRepository.save(newUser);
    }

    public void delete(Long id) {
        adminUserRepository.deleteById(id);
    }

    public AdminUser update(Long id, String newPassword) {
        AdminUser u = adminUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        if (newPassword != null && !newPassword.isEmpty()) {
            u.setPasswordHash(passwordEncoder.encode(newPassword));
        }
        return adminUserRepository.save(u);
    }
}