// src/main/java/grupo5/gestion_inventario/superpanel/service/PaymentPlanService.java
package grupo5.gestion_inventario.superpanel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import grupo5.gestion_inventario.superpanel.model.PaymentPlan;
import grupo5.gestion_inventario.superpanel.repository.PaymentPlanRepository;

@Service
public class PaymentPlanService {

    private final PaymentPlanRepository repo;

    public PaymentPlanService(PaymentPlanRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public PaymentPlan create(PaymentPlan plan) {
        return repo.save(plan);
    }

    @Transactional(readOnly = true)
    public List<PaymentPlan> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<PaymentPlan> findById(Long id) {
        return repo.findById(id);
    }

    @Transactional
    public Optional<PaymentPlan> update(Long id, PaymentPlan plan) {
        return repo.findById(id)
                .map(existing -> {
                    existing.setName(plan.getName());
                    existing.setArticleLimit(plan.getArticleLimit());
                    existing.setUserLimit(plan.getUserLimit());
                    existing.setPrice(plan.getPrice());
                    existing.setBillingPeriod(plan.getBillingPeriod());
                    existing.setTrialPeriodDays(plan.getTrialPeriodDays());
                    existing.setDescription(plan.getDescription());
                    return repo.save(existing);
                });
    }

    @Transactional
    public boolean delete(Long id) {
        return repo.findById(id)
                .map(plan -> {
                    repo.delete(plan);
                    return true;
                }).orElse(false);
    }
}
