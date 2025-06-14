package grupo5.gestion_inventario.clientpanel.model;

import grupo5.gestion_inventario.model.Client;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "end_customer")
public class EndCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String contactInfo;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public EndCustomer() {}

    public EndCustomer(String name, String contactInfo, Client client) {
        this.name = name;
        this.contactInfo = contactInfo;
        this.client = client;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
}
