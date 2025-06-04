package grupo5.gestion_inventario.repository;

import grupo5.gestion_inventario.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    /* login por e-mail (username) */
    Optional<Client> findByEmail(String email);

    /* si algún día permites “name” como usuario: */
    Optional<Client> findByName(String name);

    long countByPlan(String plan);


}
