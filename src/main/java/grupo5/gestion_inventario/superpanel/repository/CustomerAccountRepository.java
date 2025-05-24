// src/main/java/grupo5/gestion_inventario/superpanel/repository/CustomerAccountRepository.java
package grupo5.gestion_inventario.superpanel.repository;

import grupo5.gestion_inventario.superpanel.model.CustomerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, Long> {


    @Query("SELECT COUNT(c) FROM CustomerAccount c WHERE c.paymentPlan.name = :planName")
    long countByPlanName(String planName);

    @Query("SELECT COUNT(p) FROM Product p")
    long countAllProducts();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.stockQuantity <= p.lowStockThreshold")
    long countLowStock();
}
