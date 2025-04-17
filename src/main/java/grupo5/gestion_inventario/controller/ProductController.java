/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package grupo5.gestion_inventario.controller;

/**
 *
 * @author lautaro
 */
import org.springframework.web.bind.annotation.*;

import java.util.List;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.model.Product;
import grupo5.gestion_inventario.service.ClientService;
import grupo5.gestion_inventario.service.ProductService;

@RestController
@RequestMapping("/clients/{clientId}/products")
public class ProductController {

    private final ClientService clientService;
    private final ProductService productService;

    public ProductController(ClientService clientService, ProductService productService) {
        this.clientService = clientService;
        this.productService = productService;
    }

    @PostMapping
    public Product create(@PathVariable Long clientId, @RequestBody Product p) {
        Client c = clientService.findById(clientId);
        return productService.create(p, c);
    }

    @GetMapping
    public List<Product> list(@PathVariable Long clientId) {
        Client c = clientService.findById(clientId);
        return productService.findByClient(c);
    }

    @GetMapping("/{id}")
    public Product get(@PathVariable Long id) { return productService.findById(id); }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product p) { return productService.update(id, p); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { productService.delete(id); }
}