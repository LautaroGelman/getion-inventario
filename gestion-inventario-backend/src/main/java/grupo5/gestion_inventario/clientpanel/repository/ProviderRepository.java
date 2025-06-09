// src/main/java/grupo5/gestion_inventario/clientpanel/repository/ProviderRepository.java
package grupo5.gestion_inventario.clientpanel.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import grupo5.gestion_inventario.clientpanel.model.Provider;

public interface ProviderRepository extends JpaRepository<Provider, Long> {
    /**
     * Devuelve todos los proveedores asociados a un cliente.
     */
    List<Provider> findByClientId(Long clientId);
}
