package grupo5.gestion_inventario.service;

import grupo5.gestion_inventario.clientpanel.dto.SaleDto;
import grupo5.gestion_inventario.clientpanel.dto.SaleRequest;
import grupo5.gestion_inventario.clientpanel.dto.SalesDailySummaryDto;
import grupo5.gestion_inventario.clientpanel.model.Sale;
import grupo5.gestion_inventario.clientpanel.model.SaleItem;
import grupo5.gestion_inventario.clientpanel.repository.SaleRepository;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.model.Product;
import grupo5.gestion_inventario.repository.ClientRepository;
import grupo5.gestion_inventario.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class SalesService {

    private final SaleRepository saleRepo;
    private final ClientRepository  clientRepo;
    private final ProductRepository productRepo;

    public SalesService(SaleRepository saleRepo,
                        ClientRepository clientRepo,
                        ProductRepository productRepo) {
        this.saleRepo    = saleRepo;
        this.clientRepo  = clientRepo;
        this.productRepo = productRepo;
    }

    /* ─────────────────── NUEVA VENTA (CORREGIDO PARA TU CONTROLADOR) ─────────────────── */

    @Transactional
    public SaleDto createSale(Long clientId, SaleRequest req) { // <-- 1. VOLVEMOS A ACEPTAR clientId (Long)

        System.out.println("--- FECHA RECIBIDA EN EL SERVICIO: " + req.getSaleDate() + " ---");

        // 2. BUSCAMOS POR ID, COMO HACÍA TU CÓDIGO ORIGINAL
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Cliente no encontrado: " + clientId));

        // 3. USAMOS EL CONSTRUCTOR CORREGIDO DE SALE PARA USAR LA FECHA DEL FORMULARIO
        Sale sale = new Sale(client, req.getPaymentMethod(), req.getSaleDate());

        /* Mapeamos cada ítem y descontamos stock */
        req.getItems().forEach(itemRequest -> {
            Product product = productRepo.findById(itemRequest.getProductId())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Producto no encontrado (ID): " + itemRequest.getProductId()));

            int newStock = product.getStockQuantity() - itemRequest.getQuantity();
            if (newStock < 0) {
                throw new IllegalArgumentException(
                        "No hay suficiente stock de " + product.getName());
            }
            product.setStockQuantity(newStock);

            SaleItem item = new SaleItem(product, itemRequest.getQuantity(), itemRequest.getUnitPrice());
            sale.addItem(item);
        });

        Sale saved = saleRepo.save(sale);

        /* Convertimos a DTO para el front */
        String itemNames = saved.getItems().stream()
                .map(i -> i.getProduct().getName())
                .collect(Collectors.joining(", "));
        int totalQty = saved.getItems().stream()
                .mapToInt(SaleItem::getQuantity)
                .sum();

        return new SaleDto(
                client.getName(), // Usando el método correcto que ya corregimos
                itemNames,
                totalQty,
                saved.getTotalAmount(),
                saved.getPaymentMethod(),
                saved.getCreatedAt()
        );
    }

    // ... (EL RESTO DE TUS MÉTODOS SE MANTIENEN EXACTAMENTE IGUAL) ...

    @Transactional(readOnly = true)
    public long countSalesToday(Long clientId) {
        if (!clientRepo.existsById(clientId)) {
            throw new IllegalArgumentException("Cliente no encontrado: " + clientId);
        }
        LocalDate today = LocalDate.now();
        return saleRepo.countBetween(
                clientId,
                today.atStartOfDay(),
                today.plusDays(1).atStartOfDay()
        );
    }

    @Transactional(readOnly = true)
    public List<SaleDto> findByClientId(Long clientId) {
        if (!clientRepo.existsById(clientId)) {
            throw new IllegalArgumentException("Cliente no encontrado: " + clientId);
        }
        return saleRepo.findByClientId(clientId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private SaleDto toDto(Sale s) {
        return new SaleDto(
                s.getClient().getName(),
                s.getItems().isEmpty()
                        ? ""
                        : s.getItems().get(0).getProduct().getName(),
                s.getItems().isEmpty()
                        ? 0
                        : s.getItems().get(0).getQuantity(),
                s.getTotalAmount(),
                s.getPaymentMethod(),
                s.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<SalesDailySummaryDto> summaryLastDays(Long clientId, int days) {
        if (!clientRepo.existsById(clientId)) {
            throw new IllegalArgumentException("Cliente no encontrado: " + clientId);
        }

        LocalDate start = LocalDate.now().minusDays(days - 1);
        List<Object[]> raw = saleRepo.findDailySummaryNative(
                clientId, start.atStartOfDay());

        Map<LocalDate, SalesDailySummaryDto> map = raw.stream()
                .collect(Collectors.toMap(
                        r -> ((java.sql.Date) r[0]).toLocalDate(),
                        r -> new SalesDailySummaryDto(
                                ((java.sql.Date) r[0]).toLocalDate(),
                                ((Number)      r[1]).longValue(),
                                (BigDecimal)   r[2])));

        List<SalesDailySummaryDto> result = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            LocalDate d = start.plusDays(i);
            result.add(map.getOrDefault(
                    d, new SalesDailySummaryDto(d, 0, BigDecimal.ZERO)));
        }
        return result;
    }
}
