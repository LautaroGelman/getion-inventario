
// src/main/java/grupo5/gestion_inventario/clientpanel/dto/AuthResponse.java
package grupo5.gestion_inventario.clientpanel.dto;

public class AuthResponse {
    private String token;
    private String role;

    public AuthResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }

    public String getToken() { return token; }
    public String getRole() { return role; }
}