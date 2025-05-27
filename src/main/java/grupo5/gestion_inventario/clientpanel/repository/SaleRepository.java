package grupo5.gestion_inventario.clientpanel.repository;

import grupo5.gestion_inventario.clientpanel.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Repositorio JPA para la entidad Sale
 */
public interface SaleRepository extends JpaRepository<Sale, Long> {

    /**
     * Ventas históricas de un cliente (para tabla)
     */
    List<Sale> findByClientId(Long clientId);

    /**
     * Número de ventas de un cliente entre dos instantes
     */
    @Query("""
        SELECT COUNT(s) FROM Sale s
        WHERE s.client.id  = :clientId
          AND s.createdAt >= :from
          AND s.createdAt <  :to
    """)
    long countBetween(
            @Param("clientId") Long clientId,
            @Param("from")     LocalDateTime from,
            @Param("to")       LocalDateTime to
    );

    /**
     * Ingresos totales de un cliente desde una fecha
     */
    @Query("""
        SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s
        WHERE s.client.id  = :clientId
          AND s.createdAt >= :since
    """)
    BigDecimal totalRevenueSinceClient(
            @Param("clientId") Long clientId,
            @Param("since")    LocalDateTime since
    );

    /**
     * Ingresos totales de todos los clientes desde una fecha
     */
    @Query("""
        SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s
        WHERE s.createdAt >= :since
    """)
    BigDecimal totalRevenueSince(
            @Param("since") LocalDateTime since
    );
}
