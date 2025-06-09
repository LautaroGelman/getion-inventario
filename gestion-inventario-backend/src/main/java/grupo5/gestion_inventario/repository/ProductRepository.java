package grupo5.gestion_inventario.repository;

import grupo5.gestion_inventario.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /** Inventario completo de un cliente */
    List<Product> findByClientId(Long clientId);

    /** Productos cuyo stock está en o por debajo del umbral configurado */
    @Query("""
        SELECT COUNT(p) FROM Product p
        WHERE p.client.id       = :clientId
          AND p.stockQuantity   <= p.lowStockThreshold
    """)
    long countLowStock(@Param("clientId") Long clientId);

    Optional<Product> findByCode(String code);

}
