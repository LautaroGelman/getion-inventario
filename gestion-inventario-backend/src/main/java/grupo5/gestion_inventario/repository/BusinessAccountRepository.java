package grupo5.gestion_inventario.repository;

import grupo5.gestion_inventario.model.BusinessAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessAccountRepository extends JpaRepository<BusinessAccount, Long> {

    /* login por e-mail (username) */
    Optional<BusinessAccount> findByEmail(String email);

    /* si algún día permites “name” como usuario: */
    Optional<BusinessAccount> findByName(String name);

    long countByPlan(String plan);


}
