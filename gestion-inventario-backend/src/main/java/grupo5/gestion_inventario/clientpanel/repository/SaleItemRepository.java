package grupo5.gestion_inventario.clientpanel.repository;

import grupo5.gestion_inventario.clientpanel.dto.BestSellerDTO;
import grupo5.gestion_inventario.clientpanel.model.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {

    @Query("SELECT new grupo5.gestion_inventario.clientpanel.dto.BestSellerDTO(p.id, p.name, SUM(si.quantity)) " +
            "FROM SaleItem si JOIN si.product p JOIN si.sale s " +
            "WHERE s.client.id = :clientId AND s.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY p.id, p.name " +
            "ORDER BY SUM(si.quantity) DESC")
    List<BestSellerDTO> findBestSellers(@Param("clientId") Long clientId,
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);
}