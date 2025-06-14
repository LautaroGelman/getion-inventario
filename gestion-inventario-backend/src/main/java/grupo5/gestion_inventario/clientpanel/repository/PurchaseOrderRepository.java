package grupo5.gestion_inventario.clientpanel.repository;

import grupo5.gestion_inventario.clientpanel.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // IMPORT AÑADIDO

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    /**
     * Encuentra todas las órdenes de compra asociadas a un ID de cliente.
     * Este es el método que faltaba y que el servicio necesita.
     */
    List<PurchaseOrder> findByClientId(Long clientId);
}