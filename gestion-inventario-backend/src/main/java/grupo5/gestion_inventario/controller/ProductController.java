package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.dto.ProductDto;
import grupo5.gestion_inventario.clientpanel.dto.ProductRequest;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.repository.ClientRepository;
import grupo5.gestion_inventario.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client/products")
@PreAuthorize("hasAuthority('MANAGER')")
public class ProductController {

    private final ProductService   productService;
    private final ClientRepository clientRepo;

    public ProductController(ProductService productService,
                             ClientRepository clientRepo) {
        this.productService = productService;
        this.clientRepo     = clientRepo;
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductRequest req,
                                             Authentication auth) {
        Client client = clientRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        ProductDto dto = productService.create(client, req);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> list(Authentication auth) {
        String email = auth.getName();
        Client client = clientRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + email));

        List<ProductDto> dtos = productService.findByClientId(client.getId());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id, Authentication auth) {
        Client client = clientRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        return productService.findDtoByIdAndClient(id, client)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id,
                                                    @RequestBody ProductRequest req,
                                                    Authentication auth) {
        Client client = clientRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        return productService.updateProduct(id, req, client)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- ENDPOINT PARA ELIMINAR UN PRODUCTO ---
    /**
     * Elimina un producto existente del cliente autenticado.
     * URL: DELETE /client/products/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, Authentication auth) {
        Client client = clientRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        boolean deleted = productService.deleteProduct(id, client);

        if (deleted) {
            // 204 No Content: la operación fue exitosa pero no hay contenido que devolver.
            return ResponseEntity.noContent().build();
        } else {
            // 404 Not Found: el producto no se encontró o no pertenece al cliente.
            return ResponseEntity.notFound().build();
        }
    }
}