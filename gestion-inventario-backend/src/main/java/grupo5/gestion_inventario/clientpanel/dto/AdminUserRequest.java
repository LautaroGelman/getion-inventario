// src/main/java/grupo5/gestion_inventario/clientpanel/dto/AdminUserRequest.java
package grupo5.gestion_inventario.clientpanel.dto;

import java.util.Set;

public class AdminUserRequest {
    private String username;
    private String password;
    private Set<String> roles;

    public AdminUserRequest() {}
    public String getUsername() { return username; }
    public void setUsername(String u) { this.username = u; }
    public String getPassword() { return password; }
    public void setPassword(String p) { this.password = p; }
    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> r) { this.roles = r; }
}
