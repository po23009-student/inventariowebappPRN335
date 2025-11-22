package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.SelectEvent;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.TipoProductoCaracteristicaDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.TipoProductoDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoProducto;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoProductoCaracteristica;

import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
public class TipoProductoFrm extends DefaultFrm<TipoProducto, Long> implements Serializable {
    @Inject
    FacesContext facesContext;

    @Inject
    TipoProductoDAO tipoProductoDAO;

    @Inject
    TipoProductoCaracteristicaDAO tpcDAO;

    private TipoProducto tipoProductoPadreSeleccionado;
    private List<TipoProducto> listaTiposProducto;
    private List<TipoProductoCaracteristica> caracteristicasPorTipoProducto;

    public TipoProductoFrm() {}

    @PostConstruct
    public void init() {
        listaTiposProducto = tipoProductoDAO.findRange(0, Integer.MAX_VALUE);
        this.nombreBean = "Tipos de Producto";
    }

    @Override
    public void selectionHandler(SelectEvent<TipoProducto> r) {
        if (r != null) {
            this.registro = r.getObject();
            this.registro.setIdTipoProductoPadre(this.getTipoProductoPadreSeleccionado());
            this.estado = ESTADO_CRUD.MODIFICAR;
            cargarCaracteristicas();
        }
    }

    public void cargarCaracteristicas() {
        if (registro != null && registro.getId() != null) {
            caracteristicasPorTipoProducto = tpcDAO.findByTipoProducto(registro.getId());
        } else {
            caracteristicasPorTipoProducto = null;
        }
    }

    @Override
    protected FacesContext getFacesContext() {
        return this.facesContext;
    }

    @Override
    protected InventarioDefaultDataAccess<TipoProducto, Long> getDAO() {
        return tipoProductoDAO;
    }

    @Override
    protected TipoProducto nuevoRegistro() {
        TipoProducto nuevoTipoProducto = new TipoProducto();
        return nuevoTipoProducto;
    }

    @Override
    protected String getIdAsText(TipoProducto r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected TipoProducto getIdByText(String id) {
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
    public void btnGuardarHandler(ActionEvent actionEvent) {
        if (this.registro != null) {
            this.registro.setIdTipoProductoPadre(this.tipoProductoPadreSeleccionado);
        }
        super.btnGuardarHandler(actionEvent);
    }

    @Override
    public void btnModificarHandler(ActionEvent actionEvent) {
        if (this.registro != null && this.tipoProductoPadreSeleccionado != null) {
            this.registro.setIdTipoProductoPadre(this.tipoProductoPadreSeleccionado);
        }
        super.btnModificarHandler(actionEvent);
    }

    protected void selectionHandler() {
        if (registro != null && registro.getIdTipoProductoPadre() != null) {
            cargarCaracteristicas();
            tipoProductoPadreSeleccionado = listaTiposProducto.stream()
                    .filter(tp -> tp.getId().equals(registro.getIdTipoProductoPadre().getId()))
                    .findFirst()
                    .orElse(null);
        } else {
            tipoProductoPadreSeleccionado = null;
        }
    }

    public List<TipoProducto> getListaTiposProducto() {
        return listaTiposProducto;
    }

    public void setListaTiposProducto(List<TipoProducto> listaTiposProducto) {
        this.listaTiposProducto = listaTiposProducto;
    }

    public TipoProductoDAO getTipoProductoDAO() {
        return tipoProductoDAO;
    }

    public void setTipoProductoDAO(TipoProductoDAO tipoProductoDAO) {
        this.tipoProductoDAO = tipoProductoDAO;
    }

    public TipoProducto getTipoProductoPadreSeleccionado() {
        return tipoProductoPadreSeleccionado;
    }

    public void setTipoProductoPadreSeleccionado(TipoProducto tipoProductoPadreSeleccionado) {
        this.tipoProductoPadreSeleccionado = tipoProductoPadreSeleccionado;
    }

    public List<TipoProductoCaracteristica> getCaracteristicasPorTipoProducto() {
        return caracteristicasPorTipoProducto;
    }

    public void setCaracteristicasPorTipoProducto(List<TipoProductoCaracteristica> caracteristicasPorTipoProducto) {
        this.caracteristicasPorTipoProducto = caracteristicasPorTipoProducto;
    }
}