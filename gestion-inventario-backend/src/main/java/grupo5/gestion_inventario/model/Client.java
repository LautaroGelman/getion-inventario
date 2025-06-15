package grupo5.gestion_inventario.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.math.BigDecimal; // <-- Importamos para manejar dinero con precisión
import java.util.List;
import java.util.Set; // <-- Importamos Set para la nueva relación

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

    /* -------- NUEVO CAMPO PARA IMPUESTOS -------- */
    @Column(precision = 5, scale = 2)
    private BigDecimal taxPercentage; // Porcentaje de impuestos (ej. 21.00 para 21%)

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

    /* -------- NUEVA RELACIÓN CON EMPLEADOS -------- */
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Usamos JsonIgnore para evitar problemas de bucles al convertir a JSON
    private Set<Employee> employees;

    /* -------- getters / setters -------- */
    public Long   getId()            { return id; }
    public void   setId(Long id)     { this.id = id; }

    public String getName()          { return name; }
    public void   setName(String n)  { this.name = n; }

    public String getEmail()         { return email; }
    public void   setEmail(String e) { this.email = e; }

    public String getPasswordHash()  { return passwordHash; }
    public void   setPasswordHash(String p) { this.passwordHash = p; }

    public BigDecimal getTaxPercentage() { return taxPercentage; }
    public void setTaxPercentage(BigDecimal taxPercentage) { this.taxPercentage = taxPercentage; }

    public String getTelefono()      { return telefono; }
    public void   setTelefono(String t){ this.telefono = t; }

    public String getPlan()          { return plan; }
    public void   setPlan(String p)  { this.plan = p; }

    public String getEstado()        { return estado; }
    public void   setEstado(String e){ this.estado = e; }

    public List<Product> getProducts()             { return products; }
    public void          setProducts(List<Product> list){ this.products = list; }

    public Set<Employee> getEmployees() { return employees; }
    public void setEmployees(Set<Employee> employees) { this.employees = employees; }
}