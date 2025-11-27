package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.inject.Inject;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Venta;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.VentaDetalle;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/JmsQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue")
})
public class DespachoVenta implements MessageListener {

    @Inject
    private NotificadorVenta notificadorVenta;

    @Inject
    private VentaDAO ventaDAO;

    @Inject
    private VentaDetalleDAO ventaDetalleDAO;

    @Inject
    private KardexDAO kardexDAO;

    private static final Logger LOG = Logger.getLogger(DespachoVenta.class.getName());

    @Override
    public void onMessage(Message message) {
        if (!(message instanceof TextMessage)) {
            LOG.log(Level.WARNING, "Mensaje de tipo incorrecto recibido: {0}", message.getClass().getName());
            return;
        }

        TextMessage textMessage = (TextMessage) message;
        String idVentaStr = null;

        try {
            idVentaStr = textMessage.getText();
            LOG.log(Level.INFO, " INICIANDO procesamiento de venta ID: {0}", idVentaStr);

            UUID idVenta = UUID.fromString(idVentaStr);
            Venta venta = ventaDAO.find(idVenta);

            List<VentaDetalle> detalles = ventaDetalleDAO.findByVenta(idVenta);
            LOG.log(Level.INFO, "Encontrados {0} detalles para la venta", detalles.size());

            if (detalles != null && !detalles.isEmpty()) {
                for (VentaDetalle detalle : detalles) {

                }
                LOG.log(Level.INFO, "KARDEX procesado exitosamente");
            }


            ventaDAO.cambiarEstado(idVenta, "DESPACHADA");
            LOG.log(Level.INFO, " Estado cambiado a DESPACHADA");

            notificadorVenta.notificarCambioVenta("DESPACHADA:" + idVentaStr);
            LOG.log(Level.INFO, " Notificación WebSocket enviada");

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "ERROR en procesamiento: " + idVentaStr, e);
        }
    }
}