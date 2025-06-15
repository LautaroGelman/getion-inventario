
// src/main/java/grupo5/gestion_inventario/clientpanel/dto/AuthResponse.java
package grupo5.gestion_inventario.clientpanel.dto;

public class AuthResponse {
    private String token;
    public AuthResponse(String token) { this.token = token; }
    public String getToken() { return token; }
}