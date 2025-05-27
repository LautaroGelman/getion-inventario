package grupo5.gestion_inventario.service;

import grupo5.gestion_inventario.clientpanel.dto.ProductDto;
import grupo5.gestion_inventario.clientpanel.dto.ProductRequest;
import grupo5.gestion_inventario.model.Product;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.repository.ProductRepository;
import grupo5.gestion_inventario.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
    public ProductDto create(Client client, ProductRequest req) {

        Product product = new Product();
        product.setCode(req.getCode());
        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setStockQuantity(req.getStockQuantity());
        product.setLowStockThreshold(
                req.getLowStockThreshold() != null ? req.getLowStockThreshold() : 0
        );

        /* 👇 aquí es donde faltaba; garantizamos el FK */
        product.setClient(client);

        Product saved = productRepo.save(product);

        return new ProductDto(
                saved.getId(),
                saved.getCode(),
                saved.getName(),
                saved.getDescription(),
                saved.getStockQuantity(),
                saved.getPrice()
        );
    }


    @Transactional(readOnly = true)
    public List<ProductDto> findByClientId(Long clientId) {
        if (!clientRepo.existsById(clientId)) {
            throw new IllegalArgumentException("Cliente no encontrado: " + clientId);
        }
        return productRepo.findByClientId(clientId).stream()
                .map(p -> new ProductDto(
                        p.getId(),               // ← aquí pasas el id
                        p.getCode(),
                        p.getName(),
                        p.getDescription(),
                        p.getStockQuantity(),
                        p.getPrice()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Cuenta cuántos productos del cliente están por debajo o al nivel de lowStockThreshold.
     */
    @Transactional(readOnly = true)
    public long countLowStock(Long clientId) {
        if (!clientRepo.existsById(clientId)) {
            throw new IllegalArgumentException("Cliente no encontrado: " + clientId);
        }
        return productRepo.countLowStock(clientId);
    }
}

