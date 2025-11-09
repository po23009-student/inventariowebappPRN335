package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.CompraDetalleDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.ProductoDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.VentaDetalleDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.CompraDetalle;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Producto;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.VentaDetalle;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Named
@ViewScoped
public class VentaDetalleFrm extends DefaultFrm<VentaDetalle> implements Serializable {
    @Inject
    FacesContext facesContext;

    @Inject
    VentaDetalleDAO ventaDetalleDAO;

    @Inject
    VentaFrm ventaFrm;

    @Inject
    ProductoDAO productoDAO;

    private List<Producto> listaProductos;
    private Producto productoSeleccionado;

    public VentaDetalleFrm() {

    }

    @PostConstruct
    public void init() {
        this.listaProductos = productoDAO.findRange(0, Integer.MAX_VALUE);
        this.nombreBean = "Detalles de Venta";
    }

    @Override
    protected FacesContext getFacesContext() {
        return this.facesContext;
    }

    @Override
    protected InventarioDefaultDataAccess<VentaDetalle> getDAO() {
        return ventaDetalleDAO;
    }

    @Override
    protected VentaDetalle nuevoRegistro() {
        VentaDetalle nuevaVentaDetalle = new VentaDetalle();
        nuevaVentaDetalle.setId(UUID.randomUUID());
        nuevaVentaDetalle.setIdVenta(ventaFrm.getRegistro());
        nuevaVentaDetalle.setIdProducto(null);
        return nuevaVentaDetalle;
    }

    @Override
    protected String getIdAsText(VentaDetalle r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected VentaDetalle getIdByText(String id) {
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
    public void btnEliminarHandler(ActionEvent event) {
        getDAO().eliminar(this.registro);
        getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Registro eliminado", "El registro fue eliminado"));
        getFacesContext().getExternalContext().getFlash().setKeepMessages(true);
        ventaFrm.cargarDetallesVenta();
        inicializarRegistros();
    }

    @Override
    public void btnGuardarHandler(ActionEvent event) {
        if (productoSeleccionado == null || productoSeleccionado.getId() == null) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe seleccionar un producto válido"));
            return;
        }

        registro.setIdProducto(productoSeleccionado);
        getDAO().crear(registro);

        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro creado", "Detalle creado correctamente"));
        facesContext.getExternalContext().getFlash().setKeepMessages(true);

        registro = null;
        productoSeleccionado = null;
        estado = ESTADO_CRUD.NADA;

        ventaFrm.cargarDetallesVenta();
        modelo = null;
    }

    @Override
    public void btnModificarHandler(ActionEvent actionEvent) {
        try {
            this.getDAO().modificar(this.registro);
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro modificado", "El registro fue modificado exitosamente"));
            getFacesContext().getExternalContext().getFlash().setKeepMessages(true);
            this.estado = ESTADO_CRUD.NADA;
            this.modelo = null;
            inicializarRegistros();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar", e.getMessage()));
        }
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public void setProductoSeleccionado(Producto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }
}
