// src/main/java/grupo5/gestion_inventario/clientpanel/repository/SaleRepository.java
package grupo5.gestion_inventario.clientpanel.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import grupo5.gestion_inventario.clientpanel.model.Sale;
import org.springframework.data.jpa.repository.Query;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    /**
     * Devuelve todas las ventas realizadas por un cliente.
     */
    List<Sale> findByClientId(Long clientId);

    @Query("SELECT COALESCE(SUM(s.totalAmount),0) FROM Sale s WHERE s.createdAt >= :from")
    BigDecimal totalRevenueSince(LocalDateTime from);
}
