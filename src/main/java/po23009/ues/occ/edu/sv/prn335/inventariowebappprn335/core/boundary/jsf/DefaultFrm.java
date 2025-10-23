package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class DefaultFrm<T> {
    protected T registro;
    protected LazyDataModel<T> modelo;
    protected String nombreBean;
    protected ESTADO_CRUD estado;

    protected abstract FacesContext getFacesContext();
    protected abstract InventarioDefaultDataAccess<T> getDAO();
    protected abstract String getIdAsText(T r);
    protected abstract T getIdByText(String id);
    protected abstract T nuevoRegistro();

    protected int pageSize=7;

    @PostConstruct
    public void inicializar() {
        inicializarRegistros();
    }

    public List<T> cargarDatos(int first, int max) {
        try {
            return getDAO().findRange(first, max);
        } catch (Exception e) {
            Logger.getLogger(DefaultFrm.class.getName()).log(Level.SEVERE, null, e);
        }
        return Collections.emptyList();
    }

    public int contarDatos() {
        try {
            return getDAO().count();
        } catch (Exception e) {
            Logger.getLogger(DefaultFrm.class.getName()).log(Level.SEVERE, null, e);
        }
        return 0;
    }

    public void inicializarRegistros() {
        this.estado = ESTADO_CRUD.NADA;
        this.modelo = new LazyDataModel<T>() {

            @Override
            public String getRowKey(T object) {
                if (object != null) {
                    try {
                        return getIdAsText(object);
                    } catch (Exception e) {
                        Logger.getLogger(DefaultFrm.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
                return null;
            }

            @Override
            public T getRowData(String rowKey) {
                if (rowKey != null) {
                    try {
                        return getIdByText(rowKey);
                    } catch (Exception e) {
                        Logger.getLogger(DefaultFrm.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
                return null;
            }

            @Override
            public int count(Map<String, FilterMeta> map) {
                try {
                    return contarDatos();
                } catch (Exception e) {
                    Logger.getLogger(DefaultFrm.class.getName()).log(Level.SEVERE, null, e);
                }
                return 0;
            }

            @Override
            public List<T> load(int first, int max, Map<String, SortMeta> map, Map<String, FilterMeta> map1) {
                try {
                    return cargarDatos(first, max);
                } catch (Exception e) {
                    Logger.getLogger(DefaultFrm.class.getName()).log(Level.SEVERE, null, e);
                }
                return Collections.emptyList();
            }
        };
    }

    public void selectionHandler(SelectEvent<T> r) {
        if (r != null) {
            this.registro = r.getObject();
            this.estado = ESTADO_CRUD.MODIFICAR;
        }
    }

    public void btnGuardarHandler(ActionEvent event) {
        getDAO().crear(registro);
        this.modelo = null;
        inicializarRegistros();
    }

    public void btnCancelarHandler(ActionEvent event) {
        this.registro = null;
        this.estado = ESTADO_CRUD.NADA;
    }

    public void btnEliminarHandler(ActionEvent event) {
        getDAO().eliminar(this.registro);
        inicializarRegistros();
    }

    public void btnNuevoHandler(ActionEvent actionEvent) {
        this.registro = nuevoRegistro();
        this.estado = ESTADO_CRUD.CREAR;
    }

    public void btnModificarHandler(ActionEvent actionEvent) {
        try {
            this.getDAO().modificar(this.registro);
            this.estado = ESTADO_CRUD.NADA;
            this.modelo = null;
            inicializarRegistros();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar", e.getMessage()));
        }
    }

    public T getRegistro() {
        return registro;
    }

    public void setRegistro(T registro) {
        this.registro = registro;
    }

    public LazyDataModel<T> getModelo() {
        return modelo;
    }

    public void setModelo(LazyDataModel<T> modelo) {
        this.modelo = modelo;
    }

    public String getNombreBean() {
        return nombreBean;
    }

    public void setNombreBean(String nombreBean) {
        this.nombreBean = nombreBean;
    }

    public ESTADO_CRUD getEstado() {
        return estado;
    }

    public void setEstado(ESTADO_CRUD estado) {
        this.estado = estado;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
