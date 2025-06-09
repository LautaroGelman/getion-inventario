// src/main/java/grupo5/gestion_inventario/clientpanel/dto/ClientCreateRequest.java
package grupo5.gestion_inventario.clientpanel.dto;

public class ClientCreateRequest {
    private String name;
    private String email;
    private String password;
    private String telefono;
    private String plan;
    private String estado;

    public ClientCreateRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getPlan() { return plan; }
    public void setPlan(String plan) { this.plan = plan; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}

