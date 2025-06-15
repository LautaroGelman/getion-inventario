// src/main/java/grupo5/gestion_inventario/superpanel/model/PaymentPlan.java
package grupo5.gestion_inventario.superpanel.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "payment_plan")
public class PaymentPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del plan: FREE_TRIAL, STANDARD, PREMIUM */
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    /** Límite de artículos permitidos; null = ilimitado */
    private Integer articleLimit;

    /** Límite de usuarios permitidos; null = ilimitado */
    private Integer userLimit;

    /** Precio del plan */
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    /** Periodicidad de facturación: TRIAL, MONTHLY, YEARLY */
    @Column(nullable = false, length = 50)
    private String billingPeriod;

    /** Días de prueba gratuitos; null si no aplica */
    private Integer trialPeriodDays;

    /** Descripción resumida de las funcionalidades */
    @Column(length = 500)
    private String description;

    public PaymentPlan() {}

    public PaymentPlan(String name,
                       Integer articleLimit,
                       Integer userLimit,
                       BigDecimal price,
                       String billingPeriod,
                       Integer trialPeriodDays,
                       String description) {
        this.name            = name;
        this.articleLimit    = articleLimit;
        this.userLimit       = userLimit;
        this.price           = price;
        this.billingPeriod   = billingPeriod;
        this.trialPeriodDays = trialPeriodDays;
        this.description     = description;
    }

    // —— Getters & Setters ——

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getArticleLimit() {
        return articleLimit;
    }
    public void setArticleLimit(Integer articleLimit) {
        this.articleLimit = articleLimit;
    }

    public Integer getUserLimit() {
        return userLimit;
    }
    public void setUserLimit(Integer userLimit) {
        this.userLimit = userLimit;
    }

    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getBillingPeriod() {
        return billingPeriod;
    }
    public void setBillingPeriod(String billingPeriod) {
        this.billingPeriod = billingPeriod;
    }

    public Integer getTrialPeriodDays() {
        return trialPeriodDays;
    }
    public void setTrialPeriodDays(Integer trialPeriodDays) {
        this.trialPeriodDays = trialPeriodDays;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
