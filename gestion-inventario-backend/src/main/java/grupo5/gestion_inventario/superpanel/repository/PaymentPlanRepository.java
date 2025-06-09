// src/main/java/grupo5/gestion_inventario/superpanel/repository/PaymentPlanRepository.java
package grupo5.gestion_inventario.superpanel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import grupo5.gestion_inventario.superpanel.model.PaymentPlan;

public interface PaymentPlanRepository extends JpaRepository<PaymentPlan, Long> {
}
