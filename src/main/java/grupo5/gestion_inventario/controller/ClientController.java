package grupo5.gestion_inventario.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.service.ClientService;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping
    public Client create(@RequestBody Client c) {
        return service.create(c);
    }

    @GetMapping
    public List<Client> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Client get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public Client update(@PathVariable Long id, @RequestBody Client c) {
        return service.update(id, c);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
