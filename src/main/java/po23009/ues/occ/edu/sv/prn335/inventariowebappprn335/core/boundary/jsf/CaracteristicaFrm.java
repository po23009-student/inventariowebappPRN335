package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.CaracteristicaDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.TipoUnidadMedidaDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Caracteristica;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoUnidadMedida;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class CaracteristicaFrm extends DefaultFrm<Caracteristica, Integer> implements Serializable {

    @Inject
    private FacesContext facesContext;

    @Inject
    private CaracteristicaDAO caracteristicaDAO;

    @Inject
    private TipoUnidadMedidaDAO tumDAO;

    private List<TipoUnidadMedida> listaTiposUnidadMedida;

    private TipoUnidadMedida tipoUnidadMedidaSeleccionada;

    public CaracteristicaFrm() {
        this.nombreBean = "Características";
    }

    @PostConstruct
    public void init() {
        this.listaTiposUnidadMedida = tumDAO.findRange(0, Integer.MAX_VALUE);
    }

    @Override
    protected FacesContext getFacesContext() {
        return this.facesContext;
    }

    @Override
    protected InventarioDefaultDataAccess<Caracteristica, Integer> getDAO() {
        return caracteristicaDAO;
    }

    @Override
    protected Caracteristica nuevoRegistro() {
        Caracteristica nueva = new Caracteristica();
        nueva.setIdTipoUnidadMedida(null);
        tipoUnidadMedidaSeleccionada = null;
        return nueva;
    }

    @Override
    protected String getIdAsText(Caracteristica r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected Caracteristica getIdByText(String id) {
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

    @Override
    public void btnGuardarHandler(ActionEvent actionEvent) {
        if (this.registro != null && this.tipoUnidadMedidaSeleccionada != null) {
            this.registro.setIdTipoUnidadMedida(this.tipoUnidadMedidaSeleccionada);
        }
        super.btnGuardarHandler(actionEvent);
    }

    @Override
    public void btnModificarHandler(ActionEvent actionEvent) {
        if (this.registro != null && this.tipoUnidadMedidaSeleccionada != null) {
            this.registro.setIdTipoUnidadMedida(this.tipoUnidadMedidaSeleccionada);
        }
        super.btnModificarHandler(actionEvent);
    }

    protected void selectionHandler() {
        if (registro != null && registro.getIdTipoUnidadMedida() != null) {
            tipoUnidadMedidaSeleccionada = listaTiposUnidadMedida.stream()
                    .filter(tu -> tu.getId().equals(registro.getIdTipoUnidadMedida().getId()))
                    .findFirst()
                    .orElse(null);
        } else {
            tipoUnidadMedidaSeleccionada = null;
        }
    }

    public TipoUnidadMedidaDAO getTumDAO() {
        return tumDAO;
    }

    public void setTumDAO(TipoUnidadMedidaDAO tumDAO) {
        this.tumDAO = tumDAO;
    }

    public List<TipoUnidadMedida> getListaTiposUnidadMedida() {
        return listaTiposUnidadMedida;
    }

    public void setListaTiposUnidadMedida(List<TipoUnidadMedida> listaTiposUnidadMedida) {
        this.listaTiposUnidadMedida = listaTiposUnidadMedida;
    }

    public TipoUnidadMedida getTipoUnidadMedidaSeleccionada() {
        return tipoUnidadMedidaSeleccionada;
    }

    public void setTipoUnidadMedidaSeleccionada(TipoUnidadMedida tipoUnidadMedidaSeleccionada) {
        this.tipoUnidadMedidaSeleccionada = tipoUnidadMedidaSeleccionada;
    }
}