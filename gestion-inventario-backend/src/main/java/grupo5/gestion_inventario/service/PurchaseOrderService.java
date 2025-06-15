package grupo5.gestion_inventario.service;

import grupo5.gestion_inventario.clientpanel.dto.PurchaseOrderItemRequest;
import grupo5.gestion_inventario.clientpanel.dto.PurchaseOrderRequest;
import grupo5.gestion_inventario.model.Product;
import grupo5.gestion_inventario.clientpanel.model.Provider;
import grupo5.gestion_inventario.clientpanel.model.PurchaseOrderItem;
import grupo5.gestion_inventario.clientpanel.model.PurchaseOrder;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.repository.ClientRepository;
import grupo5.gestion_inventario.repository.ProductRepository;
import grupo5.gestion_inventario.clientpanel.repository.ProviderRepository;
import grupo5.gestion_inventario.clientpanel.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List; // AÑADIDO
import java.util.Optional; // AÑADIDO

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private ClientRepository clientRepository;

    // --- MÉTODO MODIFICADO ---
    // Devuelve un Optional para que el controlador maneje el caso "no encontrado".
    public Optional<PurchaseOrder> getPurchaseOrderById(Long id) {
        return purchaseOrderRepository.findById(id);
    }

    // --- NUEVO MÉTODO AÑADIDO ---
    // Para que el controlador pueda listar todas las órdenes de un cliente.
    public List<PurchaseOrder> findAllByClientId(Long clientId) {
        return purchaseOrderRepository.findByClientId(clientId);
    }

    @Transactional
    public PurchaseOrder createPurchaseOrder(Long clientId, PurchaseOrderRequest request) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Provider provider = providerRepository.findById(request.getProviderId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setClient(client);
        purchaseOrder.setProvider(provider);

        for (PurchaseOrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + itemRequest.getProductId()));
            PurchaseOrderItem item = new PurchaseOrderItem(purchaseOrder, product, itemRequest.getQuantity(), itemRequest.getCost());
            purchaseOrder.addItem(item);
        }

        return purchaseOrderRepository.save(purchaseOrder);
    }

    @Transactional
    public PurchaseOrder receivePurchaseOrder(Long orderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden de compra no encontrada"));

        if (purchaseOrder.getStatus() != PurchaseOrder.PurchaseOrderStatus.PENDING) {
            throw new IllegalStateException("Solo se pueden recibir órdenes de compra pendientes.");
        }

        for (PurchaseOrderItem item : purchaseOrder.getItems()) {
            Product product = item.getProduct();
            int newQuantity = product.getQuantity() + item.getQuantity();
            product.setQuantity(newQuantity);
        }

        purchaseOrder.setStatus(PurchaseOrder.PurchaseOrderStatus.RECEIVED);
        purchaseOrder.setReceptionDate(new Date());
        return purchaseOrderRepository.save(purchaseOrder);
    }
}