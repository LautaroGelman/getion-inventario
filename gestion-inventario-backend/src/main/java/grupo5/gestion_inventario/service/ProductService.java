package grupo5.gestion_inventario.service;

import grupo5.gestion_inventario.clientpanel.dto.ProductDto;
import grupo5.gestion_inventario.clientpanel.dto.ProductRequest;
import grupo5.gestion_inventario.model.Product;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.repository.ProductRepository;
import grupo5.gestion_inventario.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
        product.setClient(client);
        product.setCode(req.getCode());
        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setCost(req.getCost());
        product.setPrice(req.getPrice());
        // CORRECCIÓN: Usar el método estandarizado setQuantity
        product.setQuantity(req.getQuantity());
        product.setLowStockThreshold(
                req.getLowStockThreshold() != null ? req.getLowStockThreshold() : 0
        );

        Product saved = productRepo.save(product);
        return toDto(saved);
    }

    @Transactional
    public Optional<ProductDto> updateProduct(Long productId, ProductRequest req, Client client) {
        return productRepo.findById(productId)
                .filter(product -> product.getClient().getId().equals(client.getId()))
                .map(product -> {
                    product.setCode(req.getCode());
                    product.setName(req.getName());
                    product.setDescription(req.getDescription());
                    product.setCost(req.getCost());
                    product.setPrice(req.getPrice());
                    // CORRECCIÓN: Usar el método estandarizado setQuantity
                    product.setQuantity(req.getQuantity());
                    product.setLowStockThreshold(
                            req.getLowStockThreshold() != null ? req.getLowStockThreshold() : 0
                    );
                    Product updated = productRepo.save(product);
                    return toDto(updated);
                });
    }

    @Transactional(readOnly = true)
    public List<ProductDto> findByClientId(Long clientId) {
        if (!clientRepo.existsById(clientId)) {
            throw new IllegalArgumentException("Cliente no encontrado: " + clientId);
        }
        return productRepo.findByClientId(clientId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ProductDto> findDtoByIdAndClient(Long productId, Client client) {
        return productRepo.findById(productId)
                .filter(product -> product.getClient().getId().equals(client.getId()))
                .map(this::toDto);
    }

    @Transactional
    public boolean deleteProduct(Long productId, Client client) {
        return productRepo.findById(productId)
                .filter(product -> product.getClient().getId().equals(client.getId()))
                .map(product -> {
                    productRepo.delete(product);
                    return true;
                })
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public long countLowStock(Long clientId) {
        if (!clientRepo.existsById(clientId)) {
            throw new IllegalArgumentException("Cliente no encontrado: " + clientId);
        }
        return productRepo.countLowStock(clientId);
    }

    private ProductDto toDto(Product p) {
        return new ProductDto(
                p.getId(),
                p.getCode(),
                p.getName(),
                p.getDescription(),
                // CORRECCIÓN: Usar el método estandarizado getQuantity
                p.getQuantity(),
                p.getCost(),
                p.getPrice()
        );
    }
}