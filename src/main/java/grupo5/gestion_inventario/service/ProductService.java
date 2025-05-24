package grupo5.gestion_inventario.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.model.Product;
import grupo5.gestion_inventario.repository.ClientRepository;
import grupo5.gestion_inventario.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepo;
    private final ClientRepository  clientRepo;

    public ProductService(ProductRepository productRepo,
                          ClientRepository clientRepo) {
        this.productRepo = productRepo;
        this.clientRepo  = clientRepo;
    }

    @Transactional
    public Product create(Long clientId, Product product) {
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + clientId));
        product.setClient(client);
        return productRepo.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> findByClientId(Long clientId) {
        // opcional: lanzar excepción si el cliente no existe
        if (!clientRepo.existsById(clientId)) {
            throw new IllegalArgumentException("Cliente no encontrado: " + clientId);
        }
        return productRepo.findByClientId(clientId);
    }
}
