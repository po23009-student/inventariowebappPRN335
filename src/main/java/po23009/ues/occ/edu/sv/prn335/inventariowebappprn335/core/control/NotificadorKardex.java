package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.annotation.Resource;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.jms.*;

import java.io.Serializable;

@Stateless
@LocalBean
public class NotificadorKardex implements Serializable {
    @Resource(lookup = "jms/JmsFactory")
    ConnectionFactory connectionFactory; //Para conectarse

    @Resource(lookup = "jms/JmsQueue")
    Queue queue;

    public void notificarCambioKardex(String mensaje) {
        TextMessage textMessage;

        try {
            Connection cnx = connectionFactory.createConnection();
            Session session = cnx.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(queue);
            textMessage = session.createTextMessage(mensaje+System.currentTimeMillis());
            producer.send(textMessage);
            session.close();
            cnx.close();

        } catch (Exception ex) {
            System.out.println("Exception: "+ex);
        }
    }
}
