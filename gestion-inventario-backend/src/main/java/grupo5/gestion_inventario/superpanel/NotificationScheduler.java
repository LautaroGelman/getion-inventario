
// src/main/java/grupo5/gestion_inventario/superpanel/NotificationScheduler.java
package grupo5.gestion_inventario.superpanel;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import grupo5.gestion_inventario.superpanel.model.NotificationSetting;
import grupo5.gestion_inventario.superpanel.model.CustomerAccount;
import grupo5.gestion_inventario.superpanel.service.NotificationSettingService;
import grupo5.gestion_inventario.superpanel.repository.CustomerAccountRepository;

@Component
public class NotificationScheduler {

    private final NotificationSettingService notifService;
    private final CustomerAccountRepository accountRepo;

    public NotificationScheduler(NotificationSettingService notifService,
                                 CustomerAccountRepository accountRepo) {
        this.notifService = notifService;
        this.accountRepo = accountRepo;
    }

    // Cada día a la medianoche
    @Scheduled(cron = "0 0 0 * * *")
    public void checkPlanExpiry() {
        List<NotificationSetting> settings = notifService.findAll();
        for(NotificationSetting s : settings) {
            CustomerAccount acc = s.getCustomerAccount();
            // Lógica de cálculo de vencimiento basada en acc.getCreatedAt() y plan.trialPeriodDays
            // Si estamos dentro del umbral y no se ha notificado:
            //   sendEmail(acc);
            s.setLastNotifiedAt(LocalDateTime.now());
            notifService.create(s);
        }
    }
}