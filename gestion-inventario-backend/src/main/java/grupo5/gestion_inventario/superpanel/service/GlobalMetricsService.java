// src/main/java/grupo5/gestion_inventario/superpanel/service/GlobalMetricsService.java
package grupo5.gestion_inventario.superpanel.service;

import grupo5.gestion_inventario.clientpanel.repository.SaleRepository;
import grupo5.gestion_inventario.superpanel.dto.GlobalMetricsDTO;
import grupo5.gestion_inventario.superpanel.repository.CustomerAccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class GlobalMetricsService {

    private final CustomerAccountRepository accountRepo;
    private final SaleRepository saleRepo;

    public GlobalMetricsService(CustomerAccountRepository accountRepo,
                                SaleRepository saleRepo) {
        this.accountRepo = accountRepo;
        this.saleRepo    = saleRepo;
    }

    public GlobalMetricsDTO summary() {
        long total    = accountRepo.count();
        long trial    = accountRepo.countByPlanName("FREE_TRIAL");
        long standard = accountRepo.countByPlanName("STANDARD");
        long premium  = accountRepo.countByPlanName("PREMIUM");

        BigDecimal revenue30d = saleRepo.totalRevenueSince(LocalDateTime.now().minusDays(30));

        long products   = accountRepo.countAllProducts();
        long lowStock   = accountRepo.countLowStock();

        return new GlobalMetricsDTO(
                total,
                trial,
                standard,
                premium,
                revenue30d,
                products,
                lowStock
        );
    }
}
