package grupo5.gestion_inventario.service;

import grupo5.gestion_inventario.clientpanel.dto.ProfitabilitySummaryDto;
import grupo5.gestion_inventario.clientpanel.dto.SaleDto;
import grupo5.gestion_inventario.clientpanel.dto.SaleRequest;
import grupo5.gestion_inventario.clientpanel.dto.SalesDailySummaryDto;
import grupo5.gestion_inventario.clientpanel.model.Sale;
import grupo5.gestion_inventario.clientpanel.model.SaleItem;
import grupo5.gestion_inventario.clientpanel.model.EndCustomer;
import grupo5.gestion_inventario.clientpanel.repository.SaleRepository;
import grupo5.gestion_inventario.clientpanel.repository.EndCustomerRepository;
import grupo5.gestion_inventario.model.Employee;
import grupo5.gestion_inventario.model.Product;
import grupo5.gestion_inventario.repository.EmployeeRepository;
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
    private final EmployeeRepository  employeeRepo;
    private final ProductRepository productRepo;
    private final EndCustomerRepository endCustomerRepo;

    public SalesService(SaleRepository saleRepo,
                        EmployeeRepository employeeRepo,
                        ProductRepository productRepo,
                        EndCustomerRepository endCustomerRepo) {
        this.saleRepo    = saleRepo;
        this.employeeRepo  = employeeRepo;
        this.productRepo = productRepo;
        this.endCustomerRepo = endCustomerRepo;
    }

    /* ─────────────────── NUEVA VENTA (CORREGIDO PARA TU CONTROLADOR) ─────────────────── */

    @Transactional
    public SaleDto createSale(Long clientId, SaleRequest req) {

        System.out.println("--- FECHA RECIBIDA EN EL SERVICIO: " + req.getSaleDate() + " ---");

        Employee client = employeeRepo.findById(clientId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Cliente no encontrado: " + clientId));

        EndCustomer endCustomer = null;
        if (req.getEndCustomerId() != null) {
            endCustomer = endCustomerRepo.findById(req.getEndCustomerId())
                    .filter(c -> c.getClient().getId().equals(clientId))
                    .orElseThrow(() -> new IllegalArgumentException("Cliente final no encontrado"));
        }

        Sale sale = new Sale(client, endCustomer, req.getPaymentMethod(), req.getSaleDate());

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

        String itemNames = saved.getItems().stream()
                .map(i -> i.getProduct().getName())
                .collect(Collectors.joining(", "));
        int totalQty = saved.getItems().stream()
                .mapToInt(SaleItem::getQuantity)
                .sum();

        return new SaleDto(
                client.getName(),
                itemNames,
                totalQty,
                saved.getTotalAmount(),
                saved.getPaymentMethod(),
                saved.getCreatedAt()
        );
    }

    // ... (EL RESTO DE TUS MÉTODOS SE MANTIENEN IGUAL) ...

    @Transactional(readOnly = true)
    public long countSalesToday(Long clientId) {
        if (!employeeRepo.existsById(clientId)) {
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
        if (!employeeRepo.existsById(clientId)) {
            throw new IllegalArgumentException("Cliente no encontrado: " + clientId);
        }
        return saleRepo.findByClientId(clientId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private SaleDto toDto(Sale s) {
        return new SaleDto(
                s.getClient().getName(),
                s.getEndCustomer() != null ? s.getEndCustomer().getName() : "",
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
        if (!employeeRepo.existsById(clientId)) {
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

    // --- ¡NUEVO MÉTODO AÑADIDO AQUÍ! ---
    @Transactional(readOnly = true)
    public List<ProfitabilitySummaryDto> getProfitabilitySummaryLastDays(Long clientId, int days) {
        if (!employeeRepo.existsById(clientId)) {
            throw new IllegalArgumentException("Cliente no encontrado: " + clientId);
        }

        LocalDate startDate = LocalDate.now().minusDays(days - 1);
        List<Object[]> rawData = saleRepo.findDailyProfitabilitySummaryNative(clientId, startDate.atStartOfDay());

        // Mapeamos los resultados por fecha para una búsqueda fácil
        Map<LocalDate, ProfitabilitySummaryDto> map = rawData.stream()
                .collect(Collectors.toMap(
                        row -> ((java.sql.Date) row[0]).toLocalDate(), // Clave: Fecha
                        row -> {
                            BigDecimal revenue = (BigDecimal) row[1];
                            BigDecimal cost = (BigDecimal) row[2];
                            BigDecimal profit = revenue.subtract(cost); // Cálculo de la ganancia
                            return new ProfitabilitySummaryDto(
                                    ((java.sql.Date) row[0]).toLocalDate(),
                                    revenue,
                                    cost,
                                    profit
                            );
                        }
                ));

        // Rellenamos los días sin ventas para asegurar un rango de fechas continuo para el gráfico
        List<ProfitabilitySummaryDto> result = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            LocalDate day = startDate.plusDays(i);
            result.add(map.getOrDefault(
                    day, new ProfitabilitySummaryDto(day, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)
            ));
        }
        return result;
    }
}