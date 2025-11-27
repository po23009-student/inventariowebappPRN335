package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.ws.VentaEndpoint;
import java.io.Serializable;

@Stateless
@LocalBean
public class NotificadorVenta implements Serializable {

    @Inject
    private VentaEndpoint ventaEndpoint;

    public void notificarCambioVenta(String mensaje) {
        System.out.println("Enviando notificación de venta por WebSocket: " + mensaje);
        ventaEndpoint.enviarMensajeBroadcast(mensaje);
    }
}
