package grupo5.gestion_inventario.clientpanel.dto;

import grupo5.gestion_inventario.model.Role;

public class EmployeeRequest {
    private String name;
    private String email;
    private String password;
    private Role role;

    public EmployeeRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
