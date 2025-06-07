// src/main/java/grupo5/gestion_inventario/superpanel/model/NotificationSetting.java
package grupo5.gestion_inventario.superpanel.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_setting")
public class NotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Cliente asociado */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_account_id", nullable = false)
    @JsonBackReference
    private CustomerAccount customerAccount;

    /** Tipo de notificación: PLAN_EXPIRY */
    @Column(nullable = false, length = 50)
    private String type;

    /** Días antes de vencimiento para avisar */
    @Column(nullable = false)
    private Integer thresholdDaysBeforeExpiry;

    /** Última vez que se notificó */
    @Column
    private LocalDateTime lastNotifiedAt;

    /** Constructor por defecto */
    public NotificationSetting() {}

    public NotificationSetting(CustomerAccount customerAccount,
                               String type,
                               Integer thresholdDaysBeforeExpiry) {
        this.customerAccount = customerAccount;
        this.type = type;
        this.thresholdDaysBeforeExpiry = thresholdDaysBeforeExpiry;
    }

    // —— Getters & Setters ——

    public Long getId() { return id; }

    public CustomerAccount getCustomerAccount() { return customerAccount; }
    public void setCustomerAccount(CustomerAccount customerAccount) { this.customerAccount = customerAccount; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Integer getThresholdDaysBeforeExpiry() { return thresholdDaysBeforeExpiry; }
    public void setThresholdDaysBeforeExpiry(Integer thresholdDaysBeforeExpiry) {
        this.thresholdDaysBeforeExpiry = thresholdDaysBeforeExpiry;
    }

    public LocalDateTime getLastNotifiedAt() { return lastNotifiedAt; }
    public void setLastNotifiedAt(LocalDateTime lastNotifiedAt) { this.lastNotifiedAt = lastNotifiedAt; }
}
