// src/main/java/grupo5/gestion_inventario/superpanel/model/CustomerAccount.java
package grupo5.gestion_inventario.superpanel.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_account")
public class CustomerAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String businessName;

    @Column(length = 500)
    private String fiscalData;

    private String logoUrl;

    @Column(nullable = false)
    private String status;   // "ACTIVE", "SUSPENDED", etc.

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /** Plan contratado */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private PaymentPlan paymentPlan;

    /* —— Constructores —— */
    public CustomerAccount() {}
    public CustomerAccount(String businessName,
                           String fiscalData,
                           String logoUrl,
                           String status,
                           PaymentPlan paymentPlan) {
        this.businessName = businessName;
        this.fiscalData   = fiscalData;
        this.logoUrl      = logoUrl;
        this.status       = status;
        this.paymentPlan  = paymentPlan;
    }

    /* —— Getters & Setters —— */
    public Long getId() { return id; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getFiscalData() { return fiscalData; }
    public void setFiscalData(String fiscalData) { this.fiscalData = fiscalData; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public PaymentPlan getPaymentPlan() { return paymentPlan; }
    public void setPaymentPlan(PaymentPlan paymentPlan) { this.paymentPlan = paymentPlan; }
}
