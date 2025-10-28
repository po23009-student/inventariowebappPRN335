package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.Valid;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.UnidadMedidaDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.UnidadMedida;

import java.io.Serializable;
import java.util.UUID;

@Named
@ViewScoped
public class UnidadMedidaFrm extends DefaultFrm<UnidadMedida> implements Serializable {
    @Inject
    FacesContext facesContext;

    @Inject
    UnidadMedidaDAO unidadMedidaDAO;

    @Inject
    TipoUnidadMedidaFrm tipoUMFrm;

    public UnidadMedidaFrm() {
        this.nombreBean = "Clientes";
    }

    @Override
    protected FacesContext getFacesContext() {
        return this.facesContext;
    }

    @Override
    protected InventarioDefaultDataAccess<UnidadMedida> getDAO() {
        return unidadMedidaDAO;
    }

    @Override
    protected UnidadMedida nuevoRegistro() {
        @Valid
        UnidadMedida nuevo = new UnidadMedida();
        nuevo.setIdTipoUnidadMedida(tipoUMFrm.getRegistro());
        return nuevo;
    }

    @Override
    protected String getIdAsText(UnidadMedida r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected UnidadMedida getIdByText(String id) {
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
        tipoUMFrm.cargarUnidadesPorTipo();
        inicializarRegistros();
    }

    @Override
    public void btnGuardarHandler(ActionEvent event) {
        getDAO().crear(registro);
        getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro creado", "El registro se creó correctamente"));
        getFacesContext().getExternalContext().getFlash().setKeepMessages(true);
        this.modelo = null;
        tipoUMFrm.cargarUnidadesPorTipo();
        inicializarRegistros();
    }

}