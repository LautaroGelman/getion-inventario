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
    List<Sale> findByBusinessAccountId(Long businessAccountId);

    /**
     * Número de ventas de un cliente entre dos instantes
     */
    @Query("""
        SELECT COUNT(s) FROM Sale s
        WHERE s.businessAccount.id  = :accountId
          AND s.createdAt >= :from
          AND s.createdAt <  :to
    """)
    long countBetween(
            @Param("accountId") Long accountId,
            @Param("from")     LocalDateTime from,
            @Param("to")       LocalDateTime to
    );

    /**
     * Ingresos totales de un cliente desde una fecha
     */
    @Query("""
        SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s
        WHERE s.businessAccount.id  = :accountId
          AND s.createdAt >= :since
    """)
    BigDecimal totalRevenueSinceClient(
            @Param("accountId") Long accountId,
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
    @Query(value = """
    SELECT
      DATE(created_at)              AS fecha,
      COUNT(*)                      AS ventas,
      COALESCE(SUM(total_amount),0) AS importe
    FROM sale
    WHERE business_account_id = :accountId
      AND created_at >= :startDate
    GROUP BY fecha
    ORDER BY fecha
    """, nativeQuery = true)
    List<Object[]> findDailySummaryNative(
            @Param("accountId")  Long accountId,
            @Param("startDate") LocalDateTime startDate);

    // --- ¡NUEVO MÉTODO AÑADIDO AQUÍ! ---
    /**
     * Obtiene un resumen diario de rentabilidad (ingresos y costos) para un cliente.
     * Utiliza una consulta nativa para unir las tablas de ventas, ítems de venta y productos.
     * @return Una lista de Object[], donde cada array contiene:
     * [0] -> Fecha (java.sql.Date)
     * [1] -> Ingresos totales (BigDecimal)
     * [2] -> Costos totales (BigDecimal)
     */
    @Query(value = """
    SELECT
        DATE(s.created_at) AS sale_date,
        COALESCE(SUM(s.total_amount), 0) AS total_revenue,
        COALESCE(SUM(si.quantity * p.cost), 0) AS total_cost_of_goods
    FROM sale s
    JOIN sale_item si ON s.id = si.sale_id
    JOIN product p ON si.product_id = p.id
    WHERE s.business_account_id = :accountId AND s.created_at >= :startDate
    GROUP BY sale_date
    ORDER BY sale_date
    """, nativeQuery = true)
    List<Object[]> findDailyProfitabilitySummaryNative(
            @Param("accountId") Long accountId,
            @Param("startDate") LocalDateTime startDate
    );
}