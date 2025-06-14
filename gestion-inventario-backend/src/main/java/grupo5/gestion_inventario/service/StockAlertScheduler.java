package grupo5.gestion_inventario.service;

import grupo5.gestion_inventario.clientpanel.model.Alert;
import grupo5.gestion_inventario.clientpanel.repository.AlertRepository;
import grupo5.gestion_inventario.model.Product;
import grupo5.gestion_inventario.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockAlertScheduler {

    private static final Logger logger = LoggerFactory.getLogger(StockAlertScheduler.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AlertRepository alertRepository;

    // Se ejecuta todos los días a las 2:00 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void checkLowStockLevels() {
        logger.info("Iniciando tarea programada: Verificación de stock bajo...");
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            // CORRECCIÓN: Usar el método estandarizado getQuantity()
            if (product.getQuantity() <= product.getLowStockThreshold()) {
                // Verificar si ya existe una alerta de stock bajo no leída para este producto
                alertRepository.findFirstByProductAndIsReadFalse(product).ifPresentOrElse(
                        (alert) -> {
                            // Ya existe una alerta, no hacemos nada
                        },
                        () -> {
                            // No existe una alerta, la creamos
                            logger.info("Generando alerta de stock bajo para el producto: " + product.getName());
                            Alert newAlert = new Alert();
                            newAlert.setClient(product.getClient());
                            newAlert.setProduct(product);
                            newAlert.setType("LOW_STOCK");
                            // CORRECCIÓN: Usar el método estandarizado getQuantity()
                            newAlert.setMessage("El producto '" + product.getName() + "' tiene bajo stock (" + product.getQuantity() + " unidades restantes).");
                            alertRepository.save(newAlert);
                        }
                );
            }
        }
        logger.info("Tarea de verificación de stock bajo finalizada.");
    }
}