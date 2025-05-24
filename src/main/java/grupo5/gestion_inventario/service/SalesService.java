package grupo5.gestion_inventario.service;

import grupo5.gestion_inventario.clientpanel.dto.SaleRequest;
import grupo5.gestion_inventario.clientpanel.model.Sale;
import grupo5.gestion_inventario.clientpanel.model.SaleItem;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.model.Product;
import grupo5.gestion_inventario.clientpanel.repository.SaleRepository;
import grupo5.gestion_inventario.repository.ClientRepository;
import grupo5.gestion_inventario.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class SalesService {

    private final SaleRepository saleRepo;
    private final ProductRepository prodRepo;
    private final ClientRepository clientRepo;

    public SalesService(SaleRepository saleRepo,
                        ProductRepository prodRepo,
                        ClientRepository clientRepo) {
        this.saleRepo   = saleRepo;
        this.prodRepo   = prodRepo;
        this.clientRepo = clientRepo;
    }

    @Transactional
    public Sale create(Long clientId, SaleRequest request) {
        // 1) Recuperar cliente
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + clientId));

        // 2) Inicializar entidad Sale
        Sale sale = new Sale();
        sale.setClient(client);
        sale.setPaymentMethod(request.getPaymentMethod());
        // createdAt y totalAmount inicializados en el constructor

        // 3) Agregar items usando helper para mantener total y bidireccionalidad
        for (var dtoItem : request.getItems()) {
            Product product = prodRepo.findById(dtoItem.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + dtoItem.getProductId()));

            SaleItem item = new SaleItem();
            item.setProduct(product);
            item.setQuantity(dtoItem.getQuantity());
            item.setUnitPrice(dtoItem.getUnitPrice());

            sale.addItem(item);
        }

        // 4) (Opcional) Recalcular total si se modificaron manualmente los items después
        sale.recalcTotal();

        // 5) Persistir venta y sus items
        return saleRepo.save(sale);
    }
}
