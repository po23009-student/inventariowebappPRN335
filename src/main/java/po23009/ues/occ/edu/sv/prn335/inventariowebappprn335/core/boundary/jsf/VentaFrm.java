package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.SelectEvent;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.VentaDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.VentaDetalleDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.*;
import jakarta.faces.application.FacesMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Named
@ViewScoped
public class VentaFrm extends DefaultFrm<Venta, UUID> implements Serializable {
    @Inject
    FacesContext facesContext;

    @Inject
    private VentaDAO ventaDAO;

    @Inject
    private VentaDetalleDAO ventaDetalleDAO;

    private List<VentaDetalle> detallesPorVenta;
    private Cliente clienteSeleccionado;

    public VentaFrm() {
        this.nombreBean = "Ventas";
    }

    public void cargarDetallesVenta() {
        if (registro != null && registro.getId() != null) {
            detallesPorVenta = ventaDetalleDAO.findByVenta(registro.getId());
        } else {
            detallesPorVenta = null;
        }
    }

    @Override
    public void selectionHandler(SelectEvent<Venta> r) {
        if (r != null) {
            this.registro = r.getObject();
            clienteSeleccionado = this.registro.getIdCliente();
            this.estado = ESTADO_CRUD.MODIFICAR;
            cargarDetallesVenta();
        }
    }

    @Override
    protected FacesContext getFacesContext() {
        return this.facesContext;
    }

    @Override
    protected InventarioDefaultDataAccess<Venta, UUID> getDAO() {
        return ventaDAO;
    }

    @Override
    protected Venta nuevoRegistro() {
        Venta nuevaVenta = new Venta();
        nuevaVenta.setId(UUID.randomUUID());
        return nuevaVenta;
    }

    @Override
    protected String getIdAsText(Venta r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected Venta getIdByText(String id) {
        if (id != null && this.modelo != null && !this.modelo.getWrappedData().isEmpty()) {
            try {
                UUID buscado = UUID.fromString(id);
                return this.modelo.getWrappedData().stream()
                        .filter(r -> r.getId() != null && r.getId().equals(buscado))
                        .findFirst()
                        .orElse(null);
            } catch (NumberFormatException e) {
                System.err.println("ID no es un número válido: " + id);
                return null;
            }
        }
        return null;
    }

    @Override
    public void btnCancelarHandler(ActionEvent event) {
        this.registro = null;
        this.clienteSeleccionado = null;
        this.estado = ESTADO_CRUD.NADA;
    }

    public void btnSeleccionarCliente(ActionEvent actionEvent) {
        if (clienteSeleccionado != null) {
            this.registro.setIdCliente(clienteSeleccionado);
        } else {
            System.out.println("No se seleccionó ningún cliente");
        }
    }

    public void btnGuardarHandler(ActionEvent event) {
        FacesContext fc = getFacesContext();

        if (registro == null) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay registro para guardar."));
            return;
        }

        try {
            if (ESTADO_CRUD.CREAR.equals(estado)) {
                getDAO().crear(registro);
            } else if (ESTADO_CRUD.MODIFICAR.equals(estado)) {
                getDAO().modificar(registro);
            } else {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Estado no válido para guardar."));
                return;
            }

            if (detallesPorVenta != null && !detallesPorVenta.isEmpty()) {
                for (VentaDetalle detalle : detallesPorVenta) {

                    if (detalle.getIdVenta() == null) {
                        detalle.setIdVenta(registro);
                    }

                    ventaDetalleDAO.crear(detalle);
                }
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Venta y Detalles guardados."));
            } else {

                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Venta guardada sin detalles de producto."));
            }

            this.registro = null;
            this.clienteSeleccionado = null;
            this.estado = ESTADO_CRUD.NADA;

        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Fallo al guardar la Venta: " + e.getMessage()));
            Logger.getLogger(VentaFrm.class.getName()).log(Level.SEVERE, "Error en btnGuardarHandler", e);
        }
    }

    @Override
    public void btnEliminarHandler(ActionEvent event) {
        FacesContext fc = getFacesContext();

        if (getRegistro() == null) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "Seleccione una venta para eliminar."));
            return;
        }

        try {
            if (!ventaDAO.puedeEliminar(getRegistro().getId())) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                        "No se puede eliminar la venta porque ya ha sido DESPACHADA."));
                return;
            }

            getDAO().eliminar(getRegistro());

            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Venta eliminada correctamente."));

            this.registro = null;
            this.estado = ESTADO_CRUD.NADA;

        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Error al eliminar la venta: " + e.getMessage()));
            Logger.getLogger(VentaFrm.class.getName()).log(Level.SEVERE, "Error en btnEliminarHandler", e);
        }
    }


    public VentaDAO getVentaDAO() {
        return ventaDAO;
    }

    public void setVentaDAO(VentaDAO ventaDAO) {
        this.ventaDAO = ventaDAO;
    }

    public VentaDetalleDAO getVentaDetalleDAO() {
        return ventaDetalleDAO;
    }

    public void setVentaDetalleDAO(VentaDetalleDAO ventaDetalleDAO) {
        this.ventaDetalleDAO = ventaDetalleDAO;
    }

    public List<VentaDetalle> getDetallesPorVenta() {
        return detallesPorVenta;
    }

    public void setDetallesPorVenta(List<VentaDetalle> detallesPorVenta) {
        this.detallesPorVenta = detallesPorVenta;
    }

    public Cliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public void setClienteSeleccionado(Cliente clienteSeleccionado) {
        this.clienteSeleccionado = clienteSeleccionado;
    }
}