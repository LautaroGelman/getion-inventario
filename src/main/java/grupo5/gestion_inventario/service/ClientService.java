/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package grupo5.gestion_inventario.service;

import java.util.List;
import grupo5.gestion_inventario.model.Client;
import org.springframework.stereotype.Service;
import grupo5.gestion_inventario.repository.ClientRepository;

/**
 *
 * @author lautaro
 */

@Service
public class ClientService {

    private final ClientRepository repo;

    public ClientService(ClientRepository repo) {
        this.repo = repo;
    }

    public Client create(Client c) { return repo.save(c); }
    public List<Client> findAll() { return repo.findAll(); }
    public Client findById(Long id) { return repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado")); }
    public Client update(Long id, Client data) {
        Client c = findById(id);
        c.setName(data.getName());
        c.setEmail(data.getEmail());
        return repo.save(c);
    }
    public void delete(Long id) { repo.deleteById(id); }
}