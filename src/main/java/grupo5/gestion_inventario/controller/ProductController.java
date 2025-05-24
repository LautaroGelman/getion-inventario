package grupo5.gestion_inventario.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import grupo5.gestion_inventario.model.Product;
import grupo5.gestion_inventario.service.ProductService;

@RestController
@RequestMapping("/clients/{clientId}/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Product> create(@PathVariable Long clientId,
                                          @RequestBody Product product) {
        Product created = service.create(clientId, product);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Product>> list(@PathVariable Long clientId) {
        List<Product> products = service.findByClientId(clientId);
        return ResponseEntity.ok(products);
    }
}
