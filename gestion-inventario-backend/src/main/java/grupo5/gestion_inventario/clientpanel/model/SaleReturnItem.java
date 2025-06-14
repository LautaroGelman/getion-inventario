package grupo5.gestion_inventario.clientpanel.model;

import grupo5.gestion_inventario.model.Product;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sale_return_items")
public class SaleReturnItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_return_id")
    private SaleReturn saleReturn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    @Column(precision = 10, scale = 2)
    private BigDecimal priceAtReturn; // Precio al momento de la devolución

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public SaleReturn getSaleReturn() { return saleReturn; }
    public void setSaleReturn(SaleReturn saleReturn) { this.saleReturn = saleReturn; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getPriceAtReturn() { return priceAtReturn; }
    public void setPriceAtReturn(BigDecimal priceAtReturn) { this.priceAtReturn = priceAtReturn; }
}