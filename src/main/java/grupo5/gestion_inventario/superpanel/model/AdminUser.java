// src/main/java/grupo5/gestion_inventario/superpanel/model/AdminUser.java
package grupo5.gestion_inventario.superpanel.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "admin_user")
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "admin_user_role",
            joinColumns = @JoinColumn(name = "admin_user_id")
    )
    @Column(name = "role", length = 50)
    private Set<String> roles;

    public AdminUser() {}

    public AdminUser(String username, String passwordHash, Set<String> roles) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.roles = roles;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
}