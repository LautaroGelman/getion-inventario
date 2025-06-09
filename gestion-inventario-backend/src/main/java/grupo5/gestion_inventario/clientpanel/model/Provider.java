// src/main/java/grupo5/gestion_inventario/clientpanel/model/Provider.java
package grupo5.gestion_inventario.clientpanel.model;

import grupo5.gestion_inventario.model.Client;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "provider")
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del proveedor */
    @Column(nullable = false)
    private String name;

    /** Información de contacto (teléfono, email, dirección) */
    @Column(length = 500)
    private String contactInfo;

    /** Condiciones de compra (plazos, descuentos, etc.) */
    @Column(length = 500)
    private String paymentTerms;

    /** Fecha de alta del proveedor */
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    /** Constructor por defecto */
    public Provider() {}

    /**
     * Constructor completo.
     * @param name         Nombre
     * @param contactInfo  Información de contacto
     * @param paymentTerms Condiciones de pago
     * @param client       Cliente asociado
     */
    public Provider(String name,
                    String contactInfo,
                    String paymentTerms,
                    Client client) {
        this.name         = name;
        this.contactInfo  = contactInfo;
        this.paymentTerms = paymentTerms;
        this.client       = client;
        this.createdAt    = LocalDateTime.now();
    }

    // —— Getters & Setters ——

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }
    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
}
