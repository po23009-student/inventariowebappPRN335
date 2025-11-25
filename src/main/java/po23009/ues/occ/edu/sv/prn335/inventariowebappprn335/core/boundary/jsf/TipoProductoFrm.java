package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.event.NodeSelectEvent;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.TipoProductoDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.ProductoDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.TipoProductoCaracteristicaDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.CaracteristicaDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoProducto;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Producto;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoProductoCaracteristica;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Caracteristica;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unchecked")
@Named
@ViewScoped
public class TipoProductoFrm extends DefaultFrm<TipoProducto, Long> implements Serializable {
    private boolean mostrarInactivos = false;
    private boolean mostrarFormularioCaracteristica = false;
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(TipoProductoFrm.class.getName());

    @Inject
    private TipoProductoDAO tipoProductoDAO;
    @Inject
    private ProductoDAO productoDAO;
    @Inject
    private TipoProductoCaracteristicaDAO tipoProductoCaracteristicaDAO;
    @Inject
    private CaracteristicaDAO caracteristicaDAO;

    private List<TipoProducto> tiposPadreDisponibles;
    private TreeNode root;
    private TipoProducto registroSeleccionado;
    private TreeNode nodoSeleccionado;
    private List<TipoProductoCaracteristica> listaTipoProductoCaracteristicas;
    private TipoProductoCaracteristica nuevaAsociacion;
    private Caracteristica caracteristicaSeleccionada;

    private static final java.time.format.DateTimeFormatter FORMATTER =
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public TipoProductoFrm() {
        this.nombreBean = "Tipo Producto";
    }

    @PostConstruct
    @Override
    public void inicializar() {
        super.inicializar();
        cargarListasAuxiliares();
        cargarArbol();
    }

    public void cargarArbol() {
        this.root = new DefaultTreeNode("Raíz", null);
        List<TipoProducto> tiposRaiz = tipoProductoDAO.findTiposPadre(mostrarInactivos);
        for (TipoProducto tipo : tiposRaiz) {
            TreeNode nodoTipo = new DefaultTreeNode(tipo, this.root);
            cargarHijosRecursivamente(nodoTipo, tipo);
        }
    }

    private void cargarHijosRecursivamente(TreeNode padreNode, TipoProducto tipoProductoPadre) {
        List<TipoProducto> hijos = tipoProductoDAO.findHijosByPadre(tipoProductoPadre.getId(), mostrarInactivos);
        for (TipoProducto hijo : hijos) {
            TreeNode nodoHijo = new DefaultTreeNode(hijo, padreNode);
            cargarHijosRecursivamente(nodoHijo, hijo);
        }
    }

    private void cargarListasAuxiliares() {
        try {

            this.tiposPadreDisponibles = tipoProductoDAO.findRange(0, Integer.MAX_VALUE);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al cargar tipos de producto.", e);
            this.tiposPadreDisponibles = new ArrayList<>();
        }
    }

    public String getNombreJerarquico(TipoProducto tipo) {
        if (tipo == null) return "";
        TipoProducto padre = tipo.getIdTipoProductoPadre();
        if (padre == null || padre.getId() == null) return tipo.getNombre();
        String nombrePadre = getNombreJerarquico(padre);
        return nombrePadre.equals(tipo.getNombre()) ? tipo.getNombre() : nombrePadre + " > " + tipo.getNombre();
    }

    public List<Producto> findProductosByTipo(Long idTipoProducto) {
        if (idTipoProducto == null || this.productoDAO == null) return Collections.emptyList();
        try {
            return productoDAO.findByTipoProducto(idTipoProducto);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al ejecutar consulta de findProductosByTipo con ID: " + idTipoProducto, e);
            return Collections.emptyList();
        }
    }

    public void toggleMostrarInactivos() {
        this.mostrarInactivos = !this.mostrarInactivos;
        cargarArbol();
    }

    public void cargarListaTipoProductoCaracteristicas() {
        if (this.registro != null && this.registro.getId() != null) {
            this.listaTipoProductoCaracteristicas = tipoProductoCaracteristicaDAO.findByTipoProducto(this.registro.getId());
        } else {
            this.listaTipoProductoCaracteristicas = Collections.emptyList();
        }
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    protected InventarioDefaultDataAccess<TipoProducto, Long> getDAO() {
        return tipoProductoDAO;
    }

    @Override
    protected String getIdAsText(TipoProducto registro) {
        return registro != null && registro.getId() != null ? registro.getId().toString() : null;
    }

    @Override
    protected TipoProducto getIdByText(String id) {
        if (id != null && !id.trim().isEmpty()) {
            try {
                Long idLong = Long.parseLong(id);
                return tipoProductoDAO.find(idLong);
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Error al leer registro con ID: " + id, e);
                return null;
            }
        }
        return null;
    }

    @Override
    protected TipoProducto nuevoRegistro() {
        TipoProducto nuevo = new TipoProducto();
        nuevo.setActivo(true);
        return nuevo;
    }

    @Override
    public void selectionHandler(SelectEvent<TipoProducto> event) {}

    public void treeNodeSelectionHandler(NodeSelectEvent event) {
        if (event != null) {
            this.nodoSeleccionado = event.getTreeNode();
            this.registro = (TipoProducto) this.nodoSeleccionado.getData();
            this.registroSeleccionado = this.registro;
            this.estado = ESTADO_CRUD.MODIFICAR;
            cargarListaTipoProductoCaracteristicas();
            this.nuevaAsociacion = null;
            this.caracteristicaSeleccionada = null;
            this.mostrarFormularioCaracteristica = false;
        }
    }

    public void btnNuevaCaracteristicaHandler(ActionEvent event) {
        if (this.registro == null || this.registro.getId() == null) {
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Debe seleccionar un Tipo de Producto."));
            return;
        }
        this.nuevaAsociacion = new TipoProductoCaracteristica();
        this.nuevaAsociacion.setIdTipoProducto(this.registro);
        this.nuevaAsociacion.setObligatorio(false);
        this.nuevaAsociacion.setFechaCreacion(OffsetDateTime.now());
        this.caracteristicaSeleccionada = null;
        this.mostrarFormularioCaracteristica = true;
    }

    public void btnCancelarCaracteristicaHandler(ActionEvent event) {
        this.nuevaAsociacion = null;
        this.caracteristicaSeleccionada = null;
        this.mostrarFormularioCaracteristica = false;
        getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancelado", "Asociación de característica cancelada."));
    }

    public void seleccionarCaracteristica(SelectEvent<Caracteristica> event) {
        this.caracteristicaSeleccionada = event.getObject();
        this.nuevaAsociacion.setIdCaracteristica(this.caracteristicaSeleccionada);
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Característica Seleccionada", this.caracteristicaSeleccionada.getNombre());
        getFacesContext().addMessage(null, msg);
    }

    public void btnGuardarNuevaCaracteristicaHandler(ActionEvent event) {
        if (this.nuevaAsociacion != null && this.nuevaAsociacion.getIdCaracteristica() != null) {
            try {
                TipoProductoCaracteristica existente = tipoProductoCaracteristicaDAO.findByTipoAndCaracteristica(
                        this.registro.getId(), this.nuevaAsociacion.getIdCaracteristica().getId());
                if (existente != null) {
                    getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La característica ya está asociada."));
                    return;
                }
                tipoProductoCaracteristicaDAO.crear(this.nuevaAsociacion);
                cargarListaTipoProductoCaracteristicas();
                this.nuevaAsociacion = null;
                this.caracteristicaSeleccionada = null;
                this.mostrarFormularioCaracteristica = false;
                getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Asociación guardada correctamente."));
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Error al guardar la nueva asociación", e);
                getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar la asociación."));
            }
        } else {
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Debe seleccionar una característica."));
        }
    }

    @Override
    public void btnGuardarHandler(ActionEvent event) {
        try {
            if (tipoProductoDAO.existeNombre(this.registro.getNombre(), null)) {
                getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Validación", "Ya existe un Tipo de Producto con el nombre '" + this.registro.getNombre() + "'."));
                return;
            }
            tipoProductoDAO.crear(registro);
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro creado", "El Tipo de Producto se creó correctamente."));
            this.estado = ESTADO_CRUD.NADA;
            cargarArbol();
            cargarListasAuxiliares();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al guardar el tipo de producto.", e);
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar el registro: " + e.getMessage()));
        }
    }

    public void btnEliminarCaracteristicaHandler(TipoProductoCaracteristica tpc) {
        if (tpc == null || tpc.getId() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La asociación seleccionada no es válida."));
            return;
        }
        try {
            tipoProductoCaracteristicaDAO.eliminar(tpc);
            this.listaTipoProductoCaracteristicas = tipoProductoCaracteristicaDAO.findByTipoProducto(registro.getId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Asociación eliminada correctamente."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar la asociación: " + e.getMessage()));
        }
    }

    @Override
    public void btnModificarHandler(ActionEvent event) {
        if (this.registro.getIdTipoProductoPadre() != null && this.registro.getIdTipoProductoPadre().getId().equals(this.registro.getId())) {
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Jerarquía", "Un Tipo de Producto no puede depender de sí mismo."));
            return;
        }
        try {
            tipoProductoDAO.modificar(registro);
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro modificado", "El Tipo de Producto fue modificado exitosamente."));
            this.estado = ESTADO_CRUD.NADA;
            cargarArbol();
            cargarListasAuxiliares();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al modificar el tipo de producto.", e);
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al modificar el registro."));
        }
    }

    @Override
    public void btnEliminarHandler(ActionEvent event) {
        try {
            if (tipoProductoDAO.tieneHijos(this.registro.getId())) {
                getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Jerarquía", "No se puede eliminar este Tipo de Producto porque tiene tipos de productos hijos asociados."));
                return;
            }
            tipoProductoDAO.eliminar(this.registro);
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Registro eliminado", "El Tipo de Producto fue eliminado."));
            this.estado = ESTADO_CRUD.NADA;
            this.registro = null;
            cargarArbol();
            cargarListasAuxiliares();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al eliminar el tipo de producto.", e);
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar el registro."));
        }
    }

    public List<Caracteristica> getTodasLasCaracteristicas() {
        try {
            int total = caracteristicaDAO.count();
            return caracteristicaDAO.findRange(0, total);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al cargar todas las características.", e);
            return Collections.emptyList();
        }
    }

    public CaracteristicaDAO getCaracteristicaDAO() { return caracteristicaDAO; }
    public List<TipoProductoCaracteristica> getListaTipoProductoCaracteristicas() { return listaTipoProductoCaracteristicas; }
    public TipoProductoCaracteristica getNuevaAsociacion() { return nuevaAsociacion; }
    public void setNuevaAsociacion(TipoProductoCaracteristica nuevaAsociacion) { this.nuevaAsociacion = nuevaAsociacion; }
    public Caracteristica getCaracteristicaSeleccionada() { return caracteristicaSeleccionada; }
    public void setCaracteristicaSeleccionada(Caracteristica caracteristicaSeleccionada) { this.caracteristicaSeleccionada = caracteristicaSeleccionada; }
    public boolean isMostrarFormularioCaracteristica() { return mostrarFormularioCaracteristica; }
    public void setMostrarFormularioCaracteristica(boolean mostrarFormularioCaracteristica) { this.mostrarFormularioCaracteristica = mostrarFormularioCaracteristica; }
    public TreeNode getRoot() { return root; }
    public TipoProducto getRegistroSeleccionado() { return registroSeleccionado; }
    public void setRegistroSeleccionado(TipoProducto registroSeleccionado) { this.registroSeleccionado = registroSeleccionado; }
    public TreeNode getNodoSeleccionado() { return nodoSeleccionado; }
    public void setNodoSeleccionado(TreeNode nodoSeleccionado) { this.nodoSeleccionado = nodoSeleccionado; }
    public List<TipoProducto> getTiposPadreDisponibles() { return tiposPadreDisponibles; }
    public boolean isMostrarInactivos() { return mostrarInactivos; }
    public void setMostrarInactivos(boolean mostrarInactivos) { this.mostrarInactivos = mostrarInactivos; }
    public java.time.format.DateTimeFormatter getFormatter() { return FORMATTER; }
}