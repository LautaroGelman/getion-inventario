package grupo5.gestion_inventario.service;

import grupo5.gestion_inventario.clientpanel.dto.ProductRequest;
import grupo5.gestion_inventario.model.Product;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.repository.ProductRepository;
import grupo5.gestion_inventario.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepo;
    private final ClientRepository clientRepo;

    public ProductService(ProductRepository productRepo,
                          ClientRepository clientRepo) {
        this.productRepo = productRepo;
        this.clientRepo  = clientRepo;
    }

    /**
     * Crea un producto usando el DTO y lo asocia al cliente dado.
     */
    @Transactional
    public Product create(Long clientId, ProductRequest req) {
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + clientId));

        Product product = new Product();
        product.setCode(req.getCode());
        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setStockQuantity(req.getStockQuantity());
        product.setLowStockThreshold(req.getLowStockThreshold());
        product.setClient(client);

        return productRepo.save(product);
    }

    /**
     * Lista los productos asociados a un cliente.
     */
    @Transactional(readOnly = true)
    public List<Product> findByClientId(Long clientId) {
        if (!clientRepo.existsById(clientId)) {
            throw new IllegalArgumentException("Cliente no encontrado: " + clientId);
        }
        return productRepo.findByClientId(clientId);
    }
}
