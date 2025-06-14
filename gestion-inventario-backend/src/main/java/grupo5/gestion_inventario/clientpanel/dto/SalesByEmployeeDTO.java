package grupo5.gestion_inventario.clientpanel.dto;

import java.math.BigDecimal;

public class SalesByEmployeeDTO {
    private Long employeeId;
    private String employeeName;
    private BigDecimal totalSalesAmount;
    private Long salesCount;

    public SalesByEmployeeDTO(Long employeeId, String employeeName, BigDecimal totalSalesAmount, Long salesCount) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.totalSalesAmount = totalSalesAmount;
        this.salesCount = salesCount;
    }

    // Getters y Setters
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public BigDecimal getTotalSalesAmount() { return totalSalesAmount; }
    public void setTotalSalesAmount(BigDecimal totalSalesAmount) { this.totalSalesAmount = totalSalesAmount; }
    public Long getSalesCount() { return salesCount; }
    public void setSalesCount(Long salesCount) { this.salesCount = salesCount; }
}