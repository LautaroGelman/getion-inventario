package grupo5.gestion_inventario.clientpanel.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import grupo5.gestion_inventario.model.Client;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime fechaCompra;

    private double total;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Provider provider;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CompraItem> items = new ArrayList<>();

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public List<CompraItem> getItems() {
        return items;
    }

    public void setItems(List<CompraItem> items) {
        this.items = items;
        for (CompraItem item : items) {
            item.setCompra(this);
        }
    }
}