package grupo5.gestion_inventario.service;

import grupo5.gestion_inventario.clientpanel.dto.PurchaseOrderItemRequest;
import grupo5.gestion_inventario.clientpanel.dto.PurchaseOrderRequest;
import grupo5.gestion_inventario.clientpanel.model.Provider;
import grupo5.gestion_inventario.clientpanel.model.PurchaseOrder;
import grupo5.gestion_inventario.clientpanel.model.PurchaseOrderItem;
import grupo5.gestion_inventario.clientpanel.model.StockMovement;
import grupo5.gestion_inventario.clientpanel.repository.ProviderRepository;
import grupo5.gestion_inventario.clientpanel.repository.PurchaseOrderRepository;
import grupo5.gestion_inventario.clientpanel.repository.StockMovementRepository;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.model.Product;
import grupo5.gestion_inventario.repository.ClientRepository;
import grupo5.gestion_inventario.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Transactional(readOnly = true)
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<PurchaseOrder> getPurchaseOrderById(Long id) {
        return purchaseOrderRepository.findById(id);
    }

    @Transactional
    public PurchaseOrder createPurchaseOrder(PurchaseOrderRequest request) {
        // 1. Validar la existencia de las entidades principales
        Provider provider = providerRepository.findById(request.getProviderId())
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con id: " + request.getProviderId()));
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + request.getClientId()));

        // 2. Crear la entidad PurchaseOrder
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setProvider(provider);
        purchaseOrder.setClient(client);

        // 3. Procesar los ítems de la orden y calcular el costo total
        List<PurchaseOrderItem> items = new ArrayList<>();
        BigDecimal totalCost = BigDecimal.ZERO;

        for (PurchaseOrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + itemRequest.getProductId()));

            PurchaseOrderItem item = new PurchaseOrderItem();
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            item.setCost(itemRequest.getCost());
            item.setPurchaseOrder(purchaseOrder); // Establecer la relación bidireccional

            items.add(item);
            totalCost = totalCost.add(item.getCost().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        // 4. Asignar los valores finales y guardar
        purchaseOrder.setItems(items);
        purchaseOrder.setTotalCost(totalCost);
        // El estado y la fecha se asignan automáticamente por la anotación @PrePersist en la entidad

        return purchaseOrderRepository.save(purchaseOrder);
    }

    @Transactional
    public PurchaseOrder receivePurchaseOrder(Long purchaseOrderId) {
        // 1. Encontrar la orden de compra
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Orden de compra no encontrada con id: " + purchaseOrderId));

        // 2. Validar que no se haya recibido previamente
        if (purchaseOrder.getStatus() == PurchaseOrder.PurchaseOrderStatus.RECEIVED) {
            throw new IllegalStateException("La orden de compra ya ha sido recibida.");
        }

        // 3. Iterar sobre los ítems, actualizar stock y crear movimientos
        for (PurchaseOrderItem item : purchaseOrder.getItems()) {
            Product product = item.getProduct();
            int quantityReceived = item.getQuantity();

            // 3.1. Actualizar el stock del producto
            int newStock = product.getStock() + quantityReceived;
            product.setStock(newStock);
            // No es necesario llamar a productRepository.save(product) explícitamente aquí
            // porque el producto está en estado "managed" por la transacción.
            // Los cambios se persistirán al final de la transacción.

            // 3.2. Crear el registro de movimiento de stock
            StockMovement stockMovement = new StockMovement();
            stockMovement.setProduct(product);
            stockMovement.setQuantityChange(quantityReceived); // Positivo para entrada
            stockMovement.setType(StockMovement.StockMovementType.PURCHASE);
            stockMovement.setRelatedPurchaseOrderId(purchaseOrder.getId());
            stockMovementRepository.save(stockMovement);
        }

        // 4. Actualizar el estado y la fecha de recepción de la orden
        purchaseOrder.setStatus(PurchaseOrder.PurchaseOrderStatus.RECEIVED);
        purchaseOrder.setReceptionDate(new java.util.Date());

        return purchaseOrderRepository.save(purchaseOrder);
    }
}