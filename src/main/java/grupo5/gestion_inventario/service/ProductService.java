/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package grupo5.gestion_inventario.service;

/**
 *
 * @author lautaro
 */
import org.springframework.stereotype.Service;
import java.util.List;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.model.Product;
import grupo5.gestion_inventario.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public Product create(Product p, Client c) {
        p.setClient(c);
        return repo.save(p);
    }

    public List<Product> findByClient(Client c) { return repo.findByClient(c); }

    public Product findById(Long id) { return repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado")); }

    public Product update(Long id, Product data) {
        Product p = findById(id);
        p.setName(data.getName());
        p.setDescription(data.getDescription());
        p.setPrice(data.getPrice());
        p.setStockQuantity(data.getStockQuantity());
        return repo.save(p);
    }

    public void delete(Long id) { repo.deleteById(id); }
}