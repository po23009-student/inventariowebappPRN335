package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.SelectEvent;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.ProductoDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.ProductoTipoProductoDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.TipoProductoCaracteristicaDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.TipoProductoDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.ProductoTipoProductoCaracteristicaDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import jakarta.faces.event.ActionEvent;

@Named
@ViewScoped
public class ProductoFrm extends DefaultFrm<Producto,UUID> implements Serializable {
    private List<ProductoTipoProductoCaracteristica> caracteristicasAEliminar = new ArrayList<>();
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ProductoFrm.class.getName());

    private String textoHeader;
    private int firstRow = 0;
    private List<TipoProductoCaracteristica> caracteristicasDisponiblesParaAsociacion;
    private List<ProductoTipoProductoCaracteristica> caracteristicasAsignadasTemporalmente;
    private TipoProductoCaracteristica caracteristicaDisponibleSeleccionada;
    private ProductoTipoProductoCaracteristica caracteristicaAsignadaSeleccionada;
    private ProductoTipoProductoCaracteristica caracteristicaParaEdicionValor;
    private List<ProductoTipoProducto> listaProductoTipoProducto;
    private ProductoTipoProducto nuevaAsociacion;
    private TipoProducto tipoProductoSeleccionado;
    private String estadoAsociacion = "LISTA";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Inject
    private ProductoDAO productoDAO;
    @Inject
    private ProductoTipoProductoDAO productoTipoProductoDAO;
    @Inject
    private TipoProductoDAO tipoProductoDAO;
    @Inject
    private TipoProductoCaracteristicaDAO tipoProductoCaracteristicaDAO;
    @Inject
    private ProductoTipoProductoCaracteristicaDAO productoTipoProductoCaracteristicaDAO;

    public ProductoFrm() {}

    @Override
    public void selectionHandler(SelectEvent<Producto> r) {
        super.selectionHandler(r);
        this.nuevaAsociacion = null;
        this.caracteristicasAsignadasTemporalmente = null;
        this.caracteristicasDisponiblesParaAsociacion = null;
        this.caracteristicaParaEdicionValor = null;
        this.caracteristicaAsignadaSeleccionada = null;
        this.caracteristicaDisponibleSeleccionada = null;
        this.estadoAsociacion = "LISTA";
        if (this.estado == ESTADO_CRUD.MODIFICAR) {
            this.textoHeader = "Modificar Producto";
            cargarAsociaciones();
        }
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    protected ProductoDAO getDAO() {
        return productoDAO;
    }

    @Override
    protected String getIdAsText(Producto producto) {
        return producto != null && producto.getId() != null ? producto.getId().toString() : null;
    }

    @Override
    protected Producto getIdByText(String id) {
        try {
            if (id != null && !id.trim().isEmpty()) {
                UUID uuid = UUID.fromString(id.trim());
                return productoDAO.find(uuid);
            }
        } catch (IllegalArgumentException e) {
            LOG.warning("ID inválido: " + id);
        } catch (Exception e) {
            LOG.severe("Error al obtener producto por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    protected Producto nuevoRegistro() {
        Producto producto = new Producto();
        producto.setId(UUID.randomUUID());
        producto.setActivo(true);
        producto.setNombreProducto("");
        return producto;
    }

    @Override
    public void btnNuevoHandler(ActionEvent event) {
        this.registro = nuevoRegistro();
        this.estado = ESTADO_CRUD.CREAR;
        this.textoHeader = "Crear Nuevo Producto";
        this.caracteristicasAsignadasTemporalmente = null;
        this.caracteristicasDisponiblesParaAsociacion = null;
        this.caracteristicaParaEdicionValor = null;
        this.caracteristicaAsignadaSeleccionada = null;
        this.caracteristicaDisponibleSeleccionada = null;
        this.estadoAsociacion = "LISTA";
        this.nuevaAsociacion = null;
        LOG.info("ProductoFrm: Estado cambiado a CREAR. Nuevo registro inicializado.");
    }

    @Override
    public void btnGuardarHandler(ActionEvent event) {
        try {
            if (registro.getNombreProducto() == null || registro.getNombreProducto().trim().isEmpty()) {
                getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El nombre del producto es obligatorio"));
                return;
            }
            getDAO().crear(registro);
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Producto creado correctamente"));
            getFacesContext().getExternalContext().getFlash().setKeepMessages(true);
            this.estado = ESTADO_CRUD.NADA;
            this.nuevaAsociacion = null;
            this.caracteristicasAsignadasTemporalmente = null;
            this.caracteristicasDisponiblesParaAsociacion = null;
            this.caracteristicaParaEdicionValor = null;
            this.caracteristicaAsignadaSeleccionada = null;
            this.caracteristicaDisponibleSeleccionada = null;
            this.estadoAsociacion = "LISTA";
            this.modelo = null;
            inicializarRegistros();
        } catch (Exception e) {
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo crear el producto: " + e.getMessage()));
        }
    }

    @Override
    public void btnModificarHandler(ActionEvent event) {
        try {
            if (registro.getNombreProducto() == null || registro.getNombreProducto().trim().isEmpty()) {
                getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El nombre del producto es obligatorio"));
                return;
            }
            this.getDAO().modificar(this.registro);
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Producto modificado correctamente"));
            getFacesContext().getExternalContext().getFlash().setKeepMessages(true);
            this.estado = ESTADO_CRUD.NADA;
            this.nuevaAsociacion = null;
            this.caracteristicasAsignadasTemporalmente = null;
            this.caracteristicasDisponiblesParaAsociacion = null;
            this.caracteristicaParaEdicionValor = null;
            this.caracteristicaAsignadaSeleccionada = null;
            this.caracteristicaDisponibleSeleccionada = null;
            this.estadoAsociacion = "LISTA";
            this.modelo = null;
            inicializarRegistros();
        } catch (Exception e) {
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo modificar el producto: " + e.getMessage()));
        }
    }

    @Override
    public void btnEliminarHandler(ActionEvent event) {
        try {
            getDAO().eliminar(this.registro);
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Producto eliminado correctamente"));
            getFacesContext().getExternalContext().getFlash().setKeepMessages(true);
            this.nuevaAsociacion = null;
            this.modelo = null;
            inicializarRegistros();
        } catch (Exception e) {
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar el producto: " + e.getMessage()));
        }
    }

    public void cargarAsociaciones() {
        if (registro != null && registro.getId() != null) {
            this.listaProductoTipoProducto = productoTipoProductoDAO.findByProducto(registro.getId());
        } else {
            this.listaProductoTipoProducto = new ArrayList<>();
        }
    }

    public void btnNuevoTipoProductoHandler() {
        this.nuevaAsociacion = new ProductoTipoProducto();
        this.nuevaAsociacion.setId(UUID.randomUUID());
        this.nuevaAsociacion.setActivo(true);
        this.nuevaAsociacion.setFechaCreacion(OffsetDateTime.now());
        this.nuevaAsociacion.setIdProducto(this.registro);
        this.tipoProductoSeleccionado = null;
        this.estadoAsociacion = "CREAR";
        this.caracteristicasAsignadasTemporalmente = new ArrayList<>();
        this.caracteristicasDisponiblesParaAsociacion = new ArrayList<>();
        this.caracteristicaDisponibleSeleccionada = null;
        this.caracteristicaAsignadaSeleccionada = null;
        this.caracteristicaParaEdicionValor = null;
    }

    public void btnCancelarAsociacionHandler() {
        this.nuevaAsociacion = null;
        this.tipoProductoSeleccionado = null;
        this.estadoAsociacion = "LISTA";
        this.caracteristicasAsignadasTemporalmente = null;
        this.caracteristicasDisponiblesParaAsociacion = null;
    }

    public void seleccionarTipoProducto(TipoProducto tp) {
        this.tipoProductoSeleccionado = tp;
        this.nuevaAsociacion.setIdTipoProducto(tp);
        if(this.nuevaAsociacion != null) inicializarCaracteristicasAsignadas(tp);
        this.caracteristicaAsignadaSeleccionada = null;
        this.caracteristicaParaEdicionValor = null;
    }

    public void btnGuardarNuevaAsociacionHandler() {
        if (tipoProductoSeleccionado == null) {
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe seleccionar un Tipo de Producto."));
            return;
        }
        if (this.caracteristicasAsignadasTemporalmente != null) {
            for (ProductoTipoProductoCaracteristica ptpc : this.caracteristicasAsignadasTemporalmente) {
                boolean esObligatoria = ptpc.getIdTipoProductoCaracteristica().getObligatorio() != null && ptpc.getIdTipoProductoCaracteristica().getObligatorio();
                boolean valorVacio = ptpc.getValor() == null || ptpc.getValor().trim().isEmpty();
                if (esObligatoria && valorVacio) {
                    String nombreCaracteristica = ptpc.getIdTipoProductoCaracteristica().getIdCaracteristica().getNombre();
                    getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validación Requerida", "La característica obligatoria '" + nombreCaracteristica + "' requiere un valor."));
                    return;
                }
            }
        }
        try {
            boolean yaExiste = productoTipoProductoDAO.existeAsociacion(registro.getId(), tipoProductoSeleccionado.getId());
            if (yaExiste) {
                getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Asociación", "El Tipo de Producto '" + tipoProductoSeleccionado.getNombre() + "' ya está asociado a este producto."));
                return;
            }
            productoTipoProductoDAO.crear(nuevaAsociacion);
            if (this.caracteristicasAsignadasTemporalmente != null) {
                for (ProductoTipoProductoCaracteristica ptpc : this.caracteristicasAsignadasTemporalmente) {
                    ptpc.setIdProductoTipoProducto(this.nuevaAsociacion);
                    boolean esObligatoria = ptpc.getIdTipoProductoCaracteristica().getObligatorio() != null && ptpc.getIdTipoProductoCaracteristica().getObligatorio();
                    boolean tieneValor = ptpc.getValor() != null && !ptpc.getValor().trim().isEmpty();
                    if (esObligatoria || tieneValor) productoTipoProductoCaracteristicaDAO.crear(ptpc);
                }
            }
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Asociación y características guardadas."));
            cargarAsociaciones();
            this.nuevaAsociacion = null;
            this.tipoProductoSeleccionado = null;
            this.estadoAsociacion = "LISTA";
            this.caracteristicasAsignadasTemporalmente = null;
            this.caracteristicasDisponiblesParaAsociacion = null;
        } catch (Exception e) {
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal", "Ocurrió un error al guardar: " + e.getMessage()));
        }
    }

    public void btnEliminarAsociacionHandler(ProductoTipoProducto ptp) {
        try {
            if (ptp.getId() == null) {
                getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "La asociación no existe en la base de datos."));
                return;
            }
            List<ProductoTipoProductoCaracteristica> caracteristicasHijas = productoTipoProductoCaracteristicaDAO.findByProductoTipoProducto(ptp.getId());
            if (caracteristicasHijas != null) for (ProductoTipoProductoCaracteristica ptpc : caracteristicasHijas) productoTipoProductoCaracteristicaDAO.eliminar(ptpc);
            productoTipoProductoDAO.eliminar(ptp);
            cargarAsociaciones();
            btnCancelarAsociacionHandler();
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Asociación eliminada correctamente."));
        } catch (Exception e) {
            LOG.severe("Error al eliminar la asociación: " + e.getMessage());
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar la asociación: " + e.getMessage()));
        }
    }

    public void inicializarCaracteristicasAsignadas(TipoProducto tipoSeleccionado) {
        this.caracteristicasAsignadasTemporalmente = new ArrayList<>();
        this.caracteristicasDisponiblesParaAsociacion = new ArrayList<>();
        this.caracteristicaParaEdicionValor = null;
        this.caracteristicaAsignadaSeleccionada = null;
        this.caracteristicaDisponibleSeleccionada = null;
        if (tipoSeleccionado == null || tipoSeleccionado.getId() == null) return;
        Long idTipoProductoLong = tipoSeleccionado.getId();
        List<TipoProductoCaracteristica> definicionesTpc = tipoProductoCaracteristicaDAO.findByTipoProducto(idTipoProductoLong);
        for (TipoProductoCaracteristica tpc : definicionesTpc) {
            if (tpc.getObligatorio() != null && tpc.getObligatorio()) {
                ProductoTipoProductoCaracteristica ptpc = new ProductoTipoProductoCaracteristica();
                ptpc.setId(UUID.randomUUID());
                ptpc.setIdTipoProductoCaracteristica(tpc);
                ptpc.setIdProductoTipoProducto(this.nuevaAsociacion);
                ptpc.setValor("");
                this.caracteristicasAsignadasTemporalmente.add(ptpc);
            } else this.caracteristicasDisponiblesParaAsociacion.add(tpc);
        }
    }

    public void btnAgregarCaracteristicaOpcional() {
        if (caracteristicaDisponibleSeleccionada != null) {
            TipoProductoCaracteristica tpc = caracteristicaDisponibleSeleccionada;
            ProductoTipoProductoCaracteristica ptpc = new ProductoTipoProductoCaracteristica();
            ptpc.setId(UUID.randomUUID());
            ptpc.setIdTipoProductoCaracteristica(tpc);
            ptpc.setIdProductoTipoProducto(this.nuevaAsociacion);
            ptpc.setValor("");
            this.caracteristicasAsignadasTemporalmente.add(ptpc);
            this.caracteristicasDisponiblesParaAsociacion.remove(tpc);
            this.caracteristicaDisponibleSeleccionada = null;
            this.caracteristicaParaEdicionValor = null;
            this.caracteristicaAsignadaSeleccionada = null;
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Característica opcional agregada."));
        } else getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Seleccione una característica opcional disponible."));
    }

    public void btnEliminarCaracteristicaAsignada() {
        if (caracteristicaAsignadaSeleccionada != null) {
            if (caracteristicaAsignadaSeleccionada.getIdTipoProductoCaracteristica().getObligatorio() != null && caracteristicaAsignadaSeleccionada.getIdTipoProductoCaracteristica().getObligatorio()) {
                getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "No se puede eliminar una característica obligatoria."));
                return;
            }
            if (caracteristicaAsignadaSeleccionada.getId() != null) this.caracteristicasAEliminar.add(caracteristicaAsignadaSeleccionada);
            TipoProductoCaracteristica tpc = caracteristicaAsignadaSeleccionada.getIdTipoProductoCaracteristica();
            this.caracteristicasAsignadasTemporalmente.remove(caracteristicaAsignadaSeleccionada);
            if (!this.caracteristicasDisponiblesParaAsociacion.contains(tpc)) this.caracteristicasDisponiblesParaAsociacion.add(tpc);
            this.caracteristicaAsignadaSeleccionada = null;
            this.caracteristicaParaEdicionValor = null;
            this.caracteristicaDisponibleSeleccionada = null;
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Característica opcional eliminada de la asociación."));
        } else getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Seleccione una característica asignada."));
    }

    public void caracteristicaAsignadaSelectHandler() {
        this.caracteristicaParaEdicionValor = this.caracteristicaAsignadaSeleccionada;
        this.caracteristicaDisponibleSeleccionada = null;
    }

    public void caracteristicaDisponibleSelectHandler() {
        this.caracteristicaAsignadaSeleccionada = null;
        this.caracteristicaParaEdicionValor = null;
    }

    public void btnGuardarValorCaracteristicaHandler() {
        if (this.caracteristicaParaEdicionValor == null) {
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "No hay característica seleccionada para editar."));
            return;
        }
        getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Valor de característica actualizado en memoria."));
        this.caracteristicaParaEdicionValor = null;
    }

    public void btnLimpiarValorCaracteristicaHandler() {
        if (this.caracteristicaParaEdicionValor == null) {
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Seleccione una característica para limpiar el valor."));
            return;
        }
        this.caracteristicaParaEdicionValor.setValor(null);
        try {
            if (this.estadoAsociacion.equals("MODIFICAR") && this.caracteristicaParaEdicionValor.getId() != null) {
                productoTipoProductoCaracteristicaDAO.modificar(this.caracteristicaParaEdicionValor);
                getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Valor de característica limpiado y guardado en DB."));
            } else getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Valor de característica limpiado en memoria."));
        } catch (Exception e) {
            LOG.severe("Error al limpiar el valor: " + e.getMessage());
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al limpiar el valor: " + e.getMessage()));
        }
    }

    public void selectionAsociacionHandler(SelectEvent<ProductoTipoProducto> event) {
        this.nuevaAsociacion = event.getObject();
        this.tipoProductoSeleccionado = this.nuevaAsociacion.getIdTipoProducto();
        this.estadoAsociacion = "MODIFICAR";
        cargarCaracteristicasParaModificacion(this.nuevaAsociacion);
        this.caracteristicaParaEdicionValor = null;
        this.caracteristicaAsignadaSeleccionada = null;
        this.caracteristicaDisponibleSeleccionada = null;
        getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Asociación cargada para modificación."));
    }

    public void cargarCaracteristicasParaModificacion(ProductoTipoProducto ptpExistente) {
        this.caracteristicasAsignadasTemporalmente = new ArrayList<>();
        this.caracteristicasDisponiblesParaAsociacion = new ArrayList<>();
        if (ptpExistente == null || ptpExistente.getIdTipoProducto() == null) return;
        Long idTipoProductoLong = ptpExistente.getIdTipoProducto().getId();
        List<TipoProductoCaracteristica> definicionesTpc = tipoProductoCaracteristicaDAO.findByTipoProducto(idTipoProductoLong);
        List<ProductoTipoProductoCaracteristica> asignacionesPTPC = productoTipoProductoCaracteristicaDAO.findByProductoTipoProducto(ptpExistente.getId());
        for (TipoProductoCaracteristica tpc : definicionesTpc) {
            ProductoTipoProductoCaracteristica ptpcExistente = asignacionesPTPC.stream().filter(a -> a.getIdTipoProductoCaracteristica().getId().equals(tpc.getId())).findFirst().orElse(null);
            if (ptpcExistente != null) this.caracteristicasAsignadasTemporalmente.add(ptpcExistente);
            else if (tpc.getObligatorio() != null && tpc.getObligatorio()) {
                ProductoTipoProductoCaracteristica nuevaPtpc = new ProductoTipoProductoCaracteristica();
                nuevaPtpc.setId(UUID.randomUUID());
                nuevaPtpc.setIdTipoProductoCaracteristica(tpc);
                nuevaPtpc.setIdProductoTipoProducto(ptpExistente);
                nuevaPtpc.setValor("");
                this.caracteristicasAsignadasTemporalmente.add(nuevaPtpc);
            } else this.caracteristicasDisponiblesParaAsociacion.add(tpc);
        }
    }

    public void btnModificarAsociacionHandler() {
        if (nuevaAsociacion == null || nuevaAsociacion.getId() == null) {
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay asociación seleccionada para modificar."));
            return;
        }
        if (this.caracteristicasAsignadasTemporalmente != null) {
            for (ProductoTipoProductoCaracteristica ptpc : this.caracteristicasAsignadasTemporalmente) {
                boolean esObligatoria = ptpc.getIdTipoProductoCaracteristica().getObligatorio() != null && ptpc.getIdTipoProductoCaracteristica().getObligatorio();
                boolean valorVacio = ptpc.getValor() == null || ptpc.getValor().trim().isEmpty();
                if (esObligatoria && valorVacio) {
                    String nombreCaracteristica = ptpc.getIdTipoProductoCaracteristica().getIdCaracteristica().getNombre();
                    getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validación Requerida", "La característica obligatoria '" + nombreCaracteristica + "' requiere un valor para modificar la asociación."));
                    return;
                }
            }
        }
        try {
            for (ProductoTipoProductoCaracteristica ptpc : this.caracteristicasAEliminar) productoTipoProductoCaracteristicaDAO.eliminar(ptpc);
            this.caracteristicasAEliminar.clear();
            productoTipoProductoDAO.modificar(nuevaAsociacion);
            if (this.caracteristicasAsignadasTemporalmente != null) {
                for (ProductoTipoProductoCaracteristica ptpc : this.caracteristicasAsignadasTemporalmente) {
                    boolean esNueva = (ptpc.getId() == null || productoTipoProductoCaracteristicaDAO.find(ptpc.getId()) == null);
                    if (ptpc.getIdProductoTipoProducto() == null) ptpc.setIdProductoTipoProducto(this.nuevaAsociacion);
                    boolean esObligatoria = ptpc.getIdTipoProductoCaracteristica().getObligatorio() != null && ptpc.getIdTipoProductoCaracteristica().getObligatorio();
                    boolean tieneValor = ptpc.getValor() != null && !ptpc.getValor().trim().isEmpty();
                    if (esNueva) { if (esObligatoria || tieneValor) productoTipoProductoCaracteristicaDAO.crear(ptpc); }
                    else productoTipoProductoCaracteristicaDAO.modificar(ptpc);
                }
            }
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Asociación y características modificadas."));
            cargarAsociaciones();
            btnCancelarAsociacionHandler();
        } catch (Exception e) {
            LOG.severe("Error al modificar asociación y características: " + e.getMessage());
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal", "Ocurrió un error al modificar: " + e.getMessage()));
        }
    }

    public List<TipoProducto> getTodosLosTiposProducto() {
        try {
            int total = tipoProductoDAO.count();
            return tipoProductoDAO.findRange(0, total);
        } catch (Exception e) {
            LOG.severe("Error al cargar todos los tipos de producto: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    public Producto getProducto() { return getRegistro(); }
    public ProductoTipoProducto getNuevaAsociacion() { return nuevaAsociacion; }
    public TipoProducto getTipoProductoSeleccionado() { return tipoProductoSeleccionado; }
    public String getEstadoAsociacion() { return estadoAsociacion; }
    public DateTimeFormatter getFormatter() { return formatter; }
    public List<ProductoTipoProducto> getListaProductoTipoProducto() { return listaProductoTipoProducto; }
    public List<TipoProductoCaracteristica> getCaracteristicasDisponiblesParaAsociacion() { return caracteristicasDisponiblesParaAsociacion; }
    public List<ProductoTipoProductoCaracteristica> getCaracteristicasAsignadasTemporalmente() { return caracteristicasAsignadasTemporalmente; }
    public TipoProductoCaracteristica getCaracteristicaDisponibleSeleccionada() { return caracteristicaDisponibleSeleccionada; }
    public void setCaracteristicaDisponibleSeleccionada(TipoProductoCaracteristica caracteristicaDisponibleSeleccionada) { this.caracteristicaDisponibleSeleccionada = caracteristicaDisponibleSeleccionada; }
    public ProductoTipoProductoCaracteristica getCaracteristicaAsignadaSeleccionada() { return caracteristicaAsignadaSeleccionada; }
    public void setCaracteristicaAsignadaSeleccionada(ProductoTipoProductoCaracteristica caracteristicaAsignadaSeleccionada) { this.caracteristicaAsignadaSeleccionada = caracteristicaAsignadaSeleccionada; }
    public ProductoTipoProductoCaracteristica getCaracteristicaParaEdicionValor() { return caracteristicaParaEdicionValor; }
    public void setCaracteristicaParaEdicionValor(ProductoTipoProductoCaracteristica caracteristicaParaEdicionValor) { this.caracteristicaParaEdicionValor = caracteristicaParaEdicionValor; }
    public int getFirstRow() { return firstRow; }
    public void setFirstRow(int firstRow) { this.firstRow = firstRow; }
    public void setNuevaAsociacion(ProductoTipoProducto nuevaAsociacion) { this.nuevaAsociacion = nuevaAsociacion; }
    public String getTextoHeader() { return textoHeader; }
    public void setTextoHeader(String textoHeader) { this.textoHeader = textoHeader; }
    @Override
    public int getPageSize() { return 5; }
}
