package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.SelectEvent;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.*;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Compra;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.CompraDetalle;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class RecepcionKardexFrm extends DefaultFrm<Compra, Long> implements Serializable {
    @Inject
    FacesContext facesContext;

    @Inject
    KardexDAO kardexDAO;

    @Inject
    CompraDAO compraDAO;

    @Inject
    CompraDetalleDAO compraDetalleDAO;

    @Inject
    CompraFrm compraFrm;

    @Inject
    CompraDetalleFrm compraDetalleFrm;

    @Inject
    NotificadorKardex notificadorKardex;

    List<Compra> comprasPagadas;
    Compra compraSeleccionada;
    List<CompraDetalle> detallesCompraSeleccionada;
    CompraDetalle compraDetalleSeleccionado;
    boolean guardarEnBodega;

    public void cargarDetallesCompra() {
        if (registro != null && registro.getId() != null) {
            this.setDetallesCompraSeleccionada(compraDetalleDAO.findByCompra(registro.getId()));
        } else {
            this.setDetallesCompraSeleccionada(null);
        }
    }

    public RecepcionKardexFrm() {}

    @PostConstruct
    public void init() {
        this.nombreBean = "Recibir Productos";
        this.setComprasPagadas(compraDAO.comprasPagadas());
        this.setGuardarEnBodega(false);
    }

    @Override
    protected FacesContext getFacesContext() {
        return this.facesContext;
    }

    @Override
    protected InventarioDefaultDataAccess<Compra, Long> getDAO() {
        return compraDAO;
    }

    @Override
    protected Compra nuevoRegistro() {
        Compra nuevaCompra = new Compra();
        return nuevaCompra;
    }

    @Override
    protected String getIdAsText(Compra r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected Compra getIdByText(String id) {
        if (id != null && this.modelo != null && !this.modelo.getWrappedData().isEmpty()) {
            try {
                Long buscado = Long.valueOf(id);
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
    public void selectionHandler(SelectEvent<Compra> r) {
        if (r != null) {
            this.registro = r.getObject();
            this.setCompraSeleccionada(r.getObject());
            cargarDetallesCompra();
            this.estado = ESTADO_CRUD.MODIFICAR;
        }
    }

    public void seleccionarCompraDetalle(SelectEvent<CompraDetalle> r) {
        if (r != null) {
            this.setCompraDetalleSeleccionado(r.getObject());
            this.setGuardarEnBodega(true);
            System.out.println(this.getCompraDetalleSeleccionado());
        }
    }

    public List<Compra> getComprasPagadas() {
        return comprasPagadas;
    }

    public void setComprasPagadas(List<Compra> comprasPagadas) {
        this.comprasPagadas = comprasPagadas;
    }

    public CompraDAO getCompraDAO() {
        return compraDAO;
    }

    public void setCompraDAO(CompraDAO compraDAO) {
        this.compraDAO = compraDAO;
    }

    public NotificadorKardex getNotificadorKardex() {
        return notificadorKardex;
    }

    public void setNotificadorKardex(NotificadorKardex notificadorKardex) {
        this.notificadorKardex = notificadorKardex;
    }

    public CompraFrm getCompraFrm() {
        return compraFrm;
    }

    public void setCompraFrm(CompraFrm compraFrm) {
        this.compraFrm = compraFrm;
    }

    public void actualizarTabla(ActionEvent actionEvent) {
        System.out.println("Actualizando tabla");
        this.setComprasPagadas(compraDAO.comprasPagadas());
    }

    public Compra getCompraSeleccionada() {
        return compraSeleccionada;
    }

    public void setCompraSeleccionada(Compra compraSeleccionada) {
        this.compraSeleccionada = compraSeleccionada;
    }

    public List<CompraDetalle> getDetallesCompraSeleccionada() {
        return detallesCompraSeleccionada;
    }

    public void setDetallesCompraSeleccionada(List<CompraDetalle> detallesCompraSeleccionada) {
        this.detallesCompraSeleccionada = detallesCompraSeleccionada;
    }

    public CompraDetalleFrm getCompraDetalleFrm() {
        return compraDetalleFrm;
    }

    public void setCompraDetalleFrm(CompraDetalleFrm compraDetalleFrm) {
        this.compraDetalleFrm = compraDetalleFrm;
    }

    public CompraDetalleDAO getCompraDetalleDAO() {
        return compraDetalleDAO;
    }

    public void setCompraDetalleDAO(CompraDetalleDAO compraDetalleDAO) {
        this.compraDetalleDAO = compraDetalleDAO;
    }

    public CompraDetalle getCompraDetalleSeleccionado() {
        return compraDetalleSeleccionado;
    }

    public void setCompraDetalleSeleccionado(CompraDetalle compraDetalleSeleccionado) {
        this.compraDetalleSeleccionado = compraDetalleSeleccionado;
    }

    public boolean isGuardarEnBodega() {
        return guardarEnBodega;
    }

    public void setGuardarEnBodega(boolean guardarEnBodega) {
        this.guardarEnBodega = guardarEnBodega;
    }
}
