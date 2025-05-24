package grupo5.gestion_inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import grupo5.gestion_inventario.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Devuelve todos los productos asociados al cliente dado.
     */
    List<Product> findByClientId(Long clientId);

    /**
     * (Opcional) Devuelve los productos cuyo stock está por debajo del umbral.
     */
    List<Product> findByClientIdAndStockQuantityLessThan(Long clientId, Integer threshold);
}
