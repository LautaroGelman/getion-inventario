package grupo5.gestion_inventario.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import grupo5.gestion_inventario.model.Role;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* -------- datos de acceso -------- */
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;                       // se usará como “username”

    @Column(nullable = false)
    private String passwordHash;                // SIEMPRE BCrypt

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /* -------- info extra -------- */
    private String telefono;
    private String plan;
    private String estado;

    /* -------- relaciones -------- */
    @OneToMany(mappedBy = "client",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference
    private List<Product> products;

    /* -------- getters / setters -------- */
    public Long   getId()            { return id; }
    public void   setId(Long id)     { this.id = id; }

    public String getName()          { return name; }
    public void   setName(String n)  { this.name = n; }

    public String getEmail()         { return email; }
    public void   setEmail(String e) { this.email = e; }

    public String getPasswordHash()  { return passwordHash; }
    public void   setPasswordHash(String p) { this.passwordHash = p; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getTelefono()      { return telefono; }
    public void   setTelefono(String t){ this.telefono = t; }

    public String getPlan()          { return plan; }
    public void   setPlan(String p)  { this.plan = p; }

    public String getEstado()        { return estado; }
    public void   setEstado(String e){ this.estado = e; }

    public List<Product> getProducts()             { return products; }
    public void          setProducts(List<Product> list){ this.products = list; }
}

