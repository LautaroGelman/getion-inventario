package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.dto.ProductDto;
import grupo5.gestion_inventario.clientpanel.dto.ProductRequest;
import grupo5.gestion_inventario.model.Product;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.service.ProductService;
import grupo5.gestion_inventario.repository.ClientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/client/products") // <-- ¡CAMBIO APLICADO AQUÍ!
public class ProductController {

    private final ProductService   productService;
    private final ClientRepository clientRepo;

    public ProductController(ProductService productService,
                             ClientRepository clientRepo) {
        this.productService = productService;
        this.clientRepo     = clientRepo;
    }

    /**
     * Crea un producto asociado al cliente autenticado y devuelve su DTO.
     * URL: POST /client/products
     */
    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductRequest req,
                                             Authentication auth) {
        Client client = clientRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        ProductDto dto = productService.create(client, req);
        return ResponseEntity.ok(dto);
    }


    /**
     * Lista productos del cliente autenticado como DTOs.
     * URL: GET /client/products
     */
    @GetMapping
    public ResponseEntity<List<ProductDto>> list(Authentication auth) {
        String email = auth.getName();
        Client client = clientRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + email));

        List<ProductDto> dtos = productService.findByClientId(client.getId());
        return ResponseEntity.ok(dtos);
    }
}