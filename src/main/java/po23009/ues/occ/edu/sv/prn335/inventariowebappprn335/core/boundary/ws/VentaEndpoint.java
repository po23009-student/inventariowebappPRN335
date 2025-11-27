package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.ws;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.Serializable;

@ApplicationScoped
@ServerEndpoint("/ventaDespachoWS")
public class VentaEndpoint implements Serializable {

    @Inject
    SessionHandler sessionHandler;

    @OnOpen
    public void abrirConexion(Session session) {
        sessionHandler.addSession(session);
    }

    @OnClose
    public void cerrarConexion(Session session) {
        sessionHandler.removeSession(session);
    }

    public void enviarMensajeBroadcast(String mensaje) {
        for (Session session : sessionHandler.getSessions()) {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(mensaje);
                } catch (Exception e) {
                    System.out.println("Error enviando WS: " + e.getMessage());
                }
            }
        }
    }
}
