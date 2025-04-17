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
import grupo5.gestion_inventario.service.ClientService;

@RestController
@RequestMapping("/admin/clients")
public class AdminController {

    private final ClientService service;

    public AdminController(ClientService service) {
        this.service = service;
    }

    @PostMapping
    public Client create(@RequestBody Client c) { return service.create(c); }

    @GetMapping
    public List<Client> list() { return service.findAll(); }

    @GetMapping("/{id}")
    public Client get(@PathVariable Long id) { return service.findById(id); }

    @PutMapping("/{id}")
    public Client update(@PathVariable Long id, @RequestBody Client c) { return service.update(id, c); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}