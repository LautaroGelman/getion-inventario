package grupo5.gestion_inventario.service;

import grupo5.gestion_inventario.clientpanel.dto.SaleReturnItemRequest;
import grupo5.gestion_inventario.clientpanel.dto.SaleReturnRequest;
import grupo5.gestion_inventario.clientpanel.model.Sale;
import grupo5.gestion_inventario.clientpanel.model.SaleReturn;
import grupo5.gestion_inventario.clientpanel.model.SaleReturnItem;
import grupo5.gestion_inventario.clientpanel.model.StockMovement;
import grupo5.gestion_inventario.clientpanel.repository.SaleRepository;
import grupo5.gestion_inventario.clientpanel.repository.SaleReturnRepository;
import grupo5.gestion_inventario.clientpanel.repository.StockMovementRepository;
import grupo5.gestion_inventario.model.BusinessAccount;
import grupo5.gestion_inventario.model.Product;
import grupo5.gestion_inventario.repository.BusinessAccountRepository;
import grupo5.gestion_inventario.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SaleReturnService {
    private final SaleReturnRepository saleReturnRepo;
    private final BusinessAccountRepository businessAccountRepo;
    private final SaleRepository saleRepo;
    private final ProductRepository productRepo;
    private final StockMovementRepository stockMovementRepo;

    public SaleReturnService(SaleReturnRepository saleReturnRepo,
                             BusinessAccountRepository businessAccountRepo,
                             SaleRepository saleRepo,
                             ProductRepository productRepo,
                             StockMovementRepository stockMovementRepo) {
        this.saleReturnRepo = saleReturnRepo;
        this.businessAccountRepo = businessAccountRepo;
        this.saleRepo = saleRepo;
        this.productRepo = productRepo;
        this.stockMovementRepo = stockMovementRepo;
    }

    @Transactional
    public SaleReturn createReturn(Long clientId, SaleReturnRequest req) {
        BusinessAccount account = businessAccountRepo.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("BusinessAccount no encontrado: " + clientId));

        Sale sale = null;
        if (req.getSaleId() != null) {
            sale = saleRepo.findById(req.getSaleId())
                    .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada: " + req.getSaleId()));
        }

        SaleReturn saleReturn = new SaleReturn();
        saleReturn.setBusinessAccount(account);
        saleReturn.setSale(sale);

        for (SaleReturnItemRequest itemReq : req.getItems()) {
            Product product = productRepo.findById(itemReq.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado (ID): " + itemReq.getProductId()));

            product.setStockQuantity(product.getStockQuantity() + itemReq.getQuantity());

            SaleReturnItem item = new SaleReturnItem();
            item.setSaleReturn(saleReturn);
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(itemReq.getUnitPrice() != null ? itemReq.getUnitPrice() : BigDecimal.ZERO);
            saleReturn.getItems().add(item);

            StockMovement m = new StockMovement();
            m.setProduct(product);
            m.setQuantityChange(itemReq.getQuantity());
            m.setType(StockMovement.StockMovementType.RETURN);
            m.setRelatedSaleId(req.getSaleId());
            stockMovementRepo.save(m);
        }

        return saleReturnRepo.save(saleReturn);
    }

    @Transactional(readOnly = true)
    public List<SaleReturn> findByClientId(Long clientId) {
        return saleReturnRepo.findByBusinessAccountId(clientId);
    }
}
