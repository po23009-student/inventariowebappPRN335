package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.*;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Almacen;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Kardex;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.KardexDetalle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Named
@ViewScoped
public class KardexDetalleFrm extends DefaultFrm<Kardex, UUID> implements Serializable {
    @Inject
    FacesContext facesContext;

    @Inject
    KardexDetalleDAO kardexDetalleDAO;

    @Inject
    AlmacenDAO almacenDAO;

    @Inject
    KardexDAO kardexDAO;

    @Inject
    CompraDetalleDAO compraDetalleDAO;

    @Inject
    RecepcionKardexFrm recepcionKardexFrm;

    Almacen almacenSeleccionado;
    List<Almacen> listaAlmacenes;
    KardexDetalle kardexDetalleRegistro;

    public KardexDetalleFrm() {}

    @PostConstruct
    public void init() {
        this.registro = this.nuevoRegistro();
        this.setListaAlmacenes(almacenDAO.findRange(0, Integer.MAX_VALUE));
        this.nombreBean = "Detalles de Kardex";
    }

    @Override
    protected FacesContext getFacesContext() {
        return this.facesContext;
    }

    @Override
    protected InventarioDefaultDataAccess<Kardex, UUID> getDAO() {
        return kardexDAO;
    }

    @Override
    protected Kardex nuevoRegistro() {
        Kardex nuevoKardex = new Kardex();
        return nuevoKardex;
    }

    @Override
    protected String getIdAsText(Kardex r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected Kardex getIdByText(String id) {
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
        super.btnCancelarHandler(event);
        this.getRecepcionKardexFrm().setGuardarEnBodega(false);
    }

    @Override
    public void btnGuardarHandler(ActionEvent event) {
        if (almacenSeleccionado == null || almacenSeleccionado.getId() == null) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe seleccionar un almacen válido"));
            return;
        }

        registro.setId(UUID.randomUUID());
        registro.setIdProducto(recepcionKardexFrm.getCompraDetalleSeleccionado().getIdProducto());
        registro.setCantidad(recepcionKardexFrm.getCompraDetalleSeleccionado().getCantidad());
        registro.setPrecio(recepcionKardexFrm.getCompraDetalleSeleccionado().getPrecio());
        registro.setTipoMovimiento("COMPRA");
        registro.setIdCompraDetalle(recepcionKardexFrm.getCompraDetalleSeleccionado());
        registro.setIdAlmacen(almacenSeleccionado);
        registro.setFecha(OffsetDateTime.now());
        registro.setCantidadActual(BigDecimal.valueOf(kardexDAO.contarKardexPorProducto(this.getRecepcionKardexFrm().getCompraDetalleSeleccionado().getIdProducto().getId())));
        registro.setPrecioActual(kardexDAO.calcularPrecioActual(this.getRecepcionKardexFrm().getCompraDetalleSeleccionado().getIdProducto().getId()));
        registro.setIdVentaDetalle(null);

        getDAO().crear(registro);

        this.kardexDetalleRegistro = new KardexDetalle();
        this.getKardexDetalleRegistro().setIdKardex(registro);
        this.getKardexDetalleRegistro().setId(UUID.randomUUID());
        this.getKardexDetalleRegistro().setActivo(true);
        this.getKardexDetalleRegistro().setLote("Lote del día "+registro.getFecha().getDayOfMonth()+"/"+registro.getFecha().getMonthValue()+"/"+registro.getFecha().getYear());
        kardexDetalleDAO.crear(this.getKardexDetalleRegistro());

        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro creado", "Guardado correctamente"));
        facesContext.getExternalContext().getFlash().setKeepMessages(true);

        registro = null;
        almacenSeleccionado = null;
        kardexDetalleRegistro = null;
        estado = ESTADO_CRUD.NADA;

        recepcionKardexFrm.cargarDetallesCompra();
        modelo = null;
    }

    public RecepcionKardexFrm getRecepcionKardexFrm() {
        return recepcionKardexFrm;
    }

    public void setRecepcionKardexFrm(RecepcionKardexFrm recepcionKardexFrm) {
        this.recepcionKardexFrm = recepcionKardexFrm;
    }

    public Almacen getAlmacenSeleccionado() {
        return almacenSeleccionado;
    }

    public void setAlmacenSeleccionado(Almacen almacenSeleccionado) {
        this.almacenSeleccionado = almacenSeleccionado;
    }

    public List<Almacen> getListaAlmacenes() {
        return listaAlmacenes;
    }

    public void setListaAlmacenes(List<Almacen> listaAlmacenes) {
        this.listaAlmacenes = listaAlmacenes;
    }

    public KardexDetalle getKardexDetalleRegistro() {
        return kardexDetalleRegistro;
    }

    public void setKardexDetalleRegistro(KardexDetalle kardexDetalleRegistro) {
        this.kardexDetalleRegistro = kardexDetalleRegistro;
    }
}