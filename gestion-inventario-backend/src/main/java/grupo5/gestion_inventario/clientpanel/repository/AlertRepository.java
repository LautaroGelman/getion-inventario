package grupo5.gestion_inventario.clientpanel.repository;

import grupo5.gestion_inventario.clientpanel.model.Alert;
import grupo5.gestion_inventario.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByClientIdAndIsReadFalseOrderByCreatedAtDesc(Long clientId);
    Optional<Alert> findFirstByProductAndIsReadFalse(Product product);
}