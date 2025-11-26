package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.MessageDriven;
import jakarta.inject.Inject;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.ws.KardexEndpoint;

//Esta config es para que cuando arranque el servidor ya esté escuchando
@MessageDriven(activationConfig = {
        @jakarta.ejb.ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/JmsQueue"),
        @jakarta.ejb.ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
        @jakarta.ejb.ActivationConfigProperty(propertyName = "connectionFactoryLookup", propertyValue = "jms/JmsFactory")

})
public class ReceptorKardex implements MessageListener {
    @Inject
    KardexEndpoint kardexEndpoint;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            System.out.println("Mensaje recibido en ReceptorKardex: " + textMessage.getText());
            kardexEndpoint.enviarMensajeBroadcast(textMessage.getText());
        } catch(Exception e) {
            System.out.println("Exception: "+e);
        }
    }



}
