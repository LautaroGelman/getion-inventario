package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.dto.ProductRequest;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.model.Product;
import grupo5.gestion_inventario.repository.ClientRepository;
import grupo5.gestion_inventario.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ClientRepository clientRepo;

    public ProductController(ProductService productService,
                             ClientRepository clientRepo) {
        this.productService = productService;
        this.clientRepo = clientRepo;
    }

    /**
     * Crea un producto asociado al cliente autenticado.
     */
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody ProductRequest req,
                                          Authentication auth) {
        String email = auth.getName();
        Client client = clientRepo.findByEmail(email);
        Product p = productService.create(client.getId(), req);
        return ResponseEntity.ok(p);
    }

    /**
     * Lista productos del cliente autenticado.
     */
    @GetMapping
    public ResponseEntity<List<Product>> list(Authentication auth) {
        String email = auth.getName();
        Client client = clientRepo.findByEmail(email);
        List<Product> products = productService.findByClientId(client.getId());
        return ResponseEntity.ok(products);
    }
}
