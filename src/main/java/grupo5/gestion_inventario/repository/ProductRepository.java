/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package grupo5.gestion_inventario.repository;

/**
 *
 * @author lautaro
 */


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByClient(Client client);
}
