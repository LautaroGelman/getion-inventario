package grupo5.gestion_inventario.clientpanel.repository;

import grupo5.gestion_inventario.clientpanel.model.PurchaseOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Long> {
}