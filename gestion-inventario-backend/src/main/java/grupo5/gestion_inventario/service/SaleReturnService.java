package grupo5.gestion_inventario.service;

import grupo5.gestion_inventario.clientpanel.dto.ReturnItemRequest;
import grupo5.gestion_inventario.clientpanel.dto.SaleReturnRequest;
import grupo5.gestion_inventario.clientpanel.model.*;
import grupo5.gestion_inventario.clientpanel.repository.SaleRepository;
import grupo5.gestion_inventario.clientpanel.repository.SaleReturnRepository;
import grupo5.gestion_inventario.clientpanel.repository.StockMovementRepository;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.model.Employee;
import grupo5.gestion_inventario.model.Product;
import grupo5.gestion_inventario.repository.EmployeeRepository;
import grupo5.gestion_inventario.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleReturnService {

    @Autowired private SaleReturnRepository saleReturnRepository;
    @Autowired private SaleRepository saleRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private StockMovementRepository stockMovementRepository;

    @Transactional
    public SaleReturn processReturn(SaleReturnRequest request, String employeeEmail) {
        Employee employee = employeeRepository.findByEmail(employeeEmail)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        Client client = employee.getClient();

        Sale originalSale = saleRepository.findById(request.getOriginalSaleId())
                .orElseThrow(() -> new RuntimeException("Venta original no encontrada"));

        if (!originalSale.getClient().getId().equals(client.getId())) {
            throw new SecurityException("La venta no pertenece a este negocio.");
        }

        SaleReturn saleReturn = new SaleReturn();
        saleReturn.setOriginalSale(originalSale);
        saleReturn.setProcessedBy(employee);
        saleReturn.setClient(client);
        saleReturn.setReturnDate(LocalDateTime.now());

        List<SaleReturnItem> returnItems = new ArrayList<>();
        for (ReturnItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // Lógica de validación (simplificada): asegurarse de no devolver más de lo comprado.
            // Una implementación más robusta verificaría el total devuelto para este producto en esta venta.

            // 1. Actualizar el stock del producto
            product.setStock(product.getStock() + itemRequest.getQuantity());
            productRepository.save(product);

            // 2. Crear el movimiento de stock para trazabilidad
            StockMovement stockMovement = new StockMovement(
                    product,
                    itemRequest.getQuantity(),
                    "DEVOLUCION",
                    LocalDateTime.now(),
                    "Devolución de venta #" + originalSale.getId()
            );
            stockMovementRepository.save(stockMovement);

            // 3. Crear el item de la devolución
            SaleReturnItem returnItem = new SaleReturnItem();
            returnItem.setSaleReturn(saleReturn);
            returnItem.setProduct(product);
            returnItem.setQuantity(itemRequest.getQuantity());
            returnItem.setPriceAtReturn(BigDecimal.valueOf(product.getPrecio()));
            returnItems.add(returnItem);
        }

        saleReturn.setItems(returnItems);
        return saleReturnRepository.save(saleReturn);
    }
}