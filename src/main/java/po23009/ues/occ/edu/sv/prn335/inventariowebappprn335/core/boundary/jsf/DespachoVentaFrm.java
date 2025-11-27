package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.annotation.Resource;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Venta;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.VentaDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.inject.Provider;

@Named("despachoVentaFrm")
@ViewScoped
public class DespachoVentaFrm extends DefaultFrm<Venta, UUID> implements Serializable {

    @Inject
    private transient VentaDAO ventaDAO;

    @Resource(lookup = "jms/JmsQueue")
    private Queue despachoQueue;

    @Inject
    private transient JMSContext jmsContext;

    public DespachoVentaFrm() {

    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    protected InventarioDefaultDataAccess<Venta, UUID> getDAO() {
        return ventaDAO;
    }

    @Override
    protected Venta nuevoRegistro() {
        Venta venta = new Venta();
        venta.setId(UUID.randomUUID());
        return venta;
    }

    @Override
    protected String getIdAsText(Venta registro) {
        if (registro == null || registro.getId() == null) {
            return null;
        }
        return registro.getId().toString();
    }

    @Override
    protected Venta getIdByText(String id) {
        if (id == null) {
            return null;
        }
        try {
            UUID uuidId = UUID.fromString(id);
            return getDAO().find(uuidId);
        } catch (Exception e) {
            Logger.getLogger(DespachoVentaFrm.class.getName()).log(Level.SEVERE, "Error al buscar Venta por ID: " + id, e);
            return null;
        }
    }

    @Override
    public List<Venta> cargarDatos(int first, int max) {
        return ventaDAO.buscarPendientesParaDespacho(first, max);
    }

    @Override
    public int contarDatos() {
        return ventaDAO.contarPendientesParaDespacho().intValue();
    }

    public void actualizarTabla() {
        System.out.println("actualizarTabla() llamado - Recargando ventas pendientes...");

        if (getModelo() != null) {
            getModelo().setRowCount(-1);
        }
    }

    public void btnDespacharHandler() {
        FacesContext fc = getFacesContext();

        if (getRegistro() == null || getRegistro().getId() == null) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "Seleccione una venta para despachar."));
            return;
        }

        try {
            String idVenta = getIdAsText(getRegistro());

            if (!"PENDIENTE".equals(getRegistro().getEstado())) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta",
                        "Solo se pueden despachar ventas en estado PENDIENTE."));
                return;
            }

            jmsContext.createProducer().send(despachoQueue, idVenta);

            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Enviado",
                    "Venta ID " + idVenta + " encolada para despacho."));

            setRegistro(null);

            actualizarTabla();

        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Fallo al enviar el mensaje JMS: " + e.getMessage()));
            Logger.getLogger(DespachoVentaFrm.class.getName()).log(Level.SEVERE, "Error en btnDespacharHandler", e);
        }
    }

    @Override
    public void selectionHandler(SelectEvent<Venta> r) {

        super.selectionHandler(r);


    }

}