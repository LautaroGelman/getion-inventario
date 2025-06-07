// src/main/java/grupo5/gestion_inventario/clientpanel/repository/SaleItemRepository.java
package grupo5.gestion_inventario.clientpanel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import grupo5.gestion_inventario.clientpanel.model.SaleItem;

public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
    // En este caso no necesitamos métodos adicionales: las SaleItems se persisten en cascada.
}
