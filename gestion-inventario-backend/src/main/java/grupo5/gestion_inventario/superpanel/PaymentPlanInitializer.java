
// src/main/java/grupo5/gestion_inventario/superpanel/PaymentPlanInitializer.java
package grupo5.gestion_inventario.superpanel;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import grupo5.gestion_inventario.superpanel.service.PaymentPlanService;
import grupo5.gestion_inventario.superpanel.model.PaymentPlan;

@Component
public class PaymentPlanInitializer implements CommandLineRunner {

    private final PaymentPlanService service;

    public PaymentPlanInitializer(PaymentPlanService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {
        if (service.findAll().isEmpty()) {
            service.create(new PaymentPlan(
                    "FREE_TRIAL", null, null,
                    BigDecimal.ZERO,
                    "TRIAL",
                    14,
                    "Prueba gratuita de 2 semanas con acceso a funciones básicas"
            ));
            service.create(new PaymentPlan(
                    "STANDARD", null, null,
                    new BigDecimal("49.99"),
                    "MONTHLY",
                    null,
                    "Funciones básicas de administración de inventario"
            ));
            service.create(new PaymentPlan(
                    "PREMIUM", null, null,
                    new BigDecimal("99.99"),
                    "MONTHLY",
                    null,
                    "Incluye notificaciones y características avanzadas"
            ));
        }
    }
}
