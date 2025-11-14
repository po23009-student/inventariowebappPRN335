package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.Valid;
import org.primefaces.event.SelectEvent;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.TipoUnidadMedidaDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.UnidadMedidaDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoUnidadMedida;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.UnidadMedida;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class TipoUnidadMedidaFrm extends DefaultFrm<TipoUnidadMedida, Integer> implements Serializable {
    @Inject
    FacesContext facesContext;

    @Inject
    TipoUnidadMedidaDAO tipoUnidadMedidaDAO;

    @Inject
    UnidadMedidaDAO unidadMedidaDAO;

    private List<UnidadMedida> unidadesPorTipo;
    private UnidadMedida unidadSeleccionada;
    private UnidadMedida unidadActual;

    public TipoUnidadMedidaFrm() {
        this.nombreBean = "Tipos de Unidad de Medida";
    }

    @Override
    public void selectionHandler(SelectEvent<TipoUnidadMedida> r) {
        if (r != null) {
            this.registro = r.getObject();
            this.estado = ESTADO_CRUD.MODIFICAR;
            cargarUnidadesPorTipo();
        }
    }

    @Override
    protected FacesContext getFacesContext() {
        return this.facesContext;
    }

    @Override
    protected InventarioDefaultDataAccess<TipoUnidadMedida, Integer> getDAO() {
        return tipoUnidadMedidaDAO;
    }

    @Override
    protected TipoUnidadMedida nuevoRegistro() {
        @Valid
        TipoUnidadMedida nuevoTUMedida = new TipoUnidadMedida();
        return nuevoTUMedida;
    }

    @Override
    protected String getIdAsText(TipoUnidadMedida r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected TipoUnidadMedida getIdByText(String id) {
        if (id != null && this.modelo != null && !this.modelo.getWrappedData().isEmpty()) {
            try {
                Integer buscado = Integer.valueOf(id);
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

    public List<UnidadMedida> getUnidadesPorTipo() {
        return unidadesPorTipo;
    }

    public void cargarUnidadesPorTipo() {
        if (registro != null && registro.getId() != null) {
            unidadesPorTipo = unidadMedidaDAO.findByTipo(registro.getId());
        } else {
            unidadesPorTipo = null;
        }
    }

    @Override
    public void btnEliminarHandler(ActionEvent event) {
        if (unidadSeleccionada != null) {
            unidadMedidaDAO.eliminar(unidadSeleccionada);
            cargarUnidadesPorTipo();
            unidadSeleccionada = null;
        }
    }

    public void btnGuardarUnidadHandler(ActionEvent event) {
        unidadMedidaDAO.crear(unidadActual);
        cargarUnidadesPorTipo();
    }

    public UnidadMedida getUnidadSeleccionada() {
        return unidadSeleccionada;
    }

    public void setUnidadSeleccionada(UnidadMedida unidadSeleccionada) {
        this.unidadSeleccionada = unidadSeleccionada;
    }

    public UnidadMedida getUnidadActual() {
        return unidadActual;
    }

    public void setUnidadActual(UnidadMedida unidadActual) {
        this.unidadActual = unidadActual;
    }
}
