package grupo5.gestion_inventario.clientpanel.repository;

import grupo5.gestion_inventario.clientpanel.model.EndCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EndCustomerRepository extends JpaRepository<EndCustomer, Long> {
    List<EndCustomer> findByClientId(Long clientId);
}
