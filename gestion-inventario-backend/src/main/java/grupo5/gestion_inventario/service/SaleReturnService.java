package grupo5.gestion_inventario.service;

import grupo5.gestion_inventario.clientpanel.dto.ReturnItemRequest;
import grupo5.gestion_inventario.clientpanel.dto.SaleReturnRequest;
import grupo5.gestion_inventario.model.Product;
import grupo5.gestion_inventario.clientpanel.model.Sale;
import grupo5.gestion_inventario.clientpanel.model.SaleReturn;
import grupo5.gestion_inventario.clientpanel.model.SaleReturnItem;
import grupo5.gestion_inventario.repository.ProductRepository;
import grupo5.gestion_inventario.clientpanel.repository.SaleRepository;
import grupo5.gestion_inventario.clientpanel.repository.SaleReturnRepository;
import grupo5.gestion_inventario.model.Client;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class SaleReturnService {

    @Autowired
    private SaleReturnRepository saleReturnRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public SaleReturn createSaleReturn(SaleReturnRequest returnRequest) {
        Sale sale = saleRepository.findById(returnRequest.getSaleId())
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con id: " + returnRequest.getSaleId()));

        Client client = sale.getClient();
        SaleReturn saleReturn = new SaleReturn(sale, client, LocalDateTime.now(), returnRequest.getReason());

        for (ReturnItemRequest itemRequest : returnRequest.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + itemRequest.getProductId()));

            int newQuantity = product.getQuantity() + itemRequest.getQuantity();
            product.setQuantity(newQuantity);

            // CORRECCIÓN: Usar el precio del producto desde la base de datos (product.getPrice())
            // en lugar de itemRequest.getUnitPrice() que no existe.
            SaleReturnItem returnItem = new SaleReturnItem(
                    saleReturn,
                    product,
                    itemRequest.getQuantity(),
                    product.getPrice(), // <-- LÍNEA CORREGIDA
                    itemRequest.getReason()
            );
            saleReturn.addItem(returnItem);
        }

        return saleReturnRepository.save(saleReturn);
    }
}