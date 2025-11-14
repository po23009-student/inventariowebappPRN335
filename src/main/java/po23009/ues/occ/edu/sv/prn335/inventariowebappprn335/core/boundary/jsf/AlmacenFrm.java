package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;


import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.AlmacenDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.TipoAlmacenDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Almacen;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoAlmacen;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class AlmacenFrm extends DefaultFrm<Almacen, Integer> implements Serializable {
    @Inject
    FacesContext facesContext;

    @Inject
    AlmacenDAO almacenDAO;

    @Inject
    TipoAlmacenDAO tipoAlmacenDAO;

    private List<TipoAlmacen> listaTiposAlmacen;
    private TipoAlmacen tipoAlmacenSeleccionado;

    @PostConstruct
    public void init() {
        this.listaTiposAlmacen = tipoAlmacenDAO.findRange(0, Integer.MAX_VALUE);
    }

    public AlmacenFrm() {
        this.nombreBean = "Almacenes";
    }

    @Override
    protected FacesContext getFacesContext() {
        return this.facesContext;
    }

    @Override
    protected InventarioDefaultDataAccess<Almacen, Integer> getDAO() {
        return almacenDAO;
    }

    @Override
    protected Almacen nuevoRegistro() {
        Almacen nuevoAlmacen = new Almacen();
        return nuevoAlmacen;
    }

    @Override
    protected String getIdAsText(Almacen r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected Almacen getIdByText(String id) {
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
        if (this.registro != null && this.tipoAlmacenSeleccionado != null) {
            this.registro.setIdTipoAlmacen(this.tipoAlmacenSeleccionado);
        }
        super.btnGuardarHandler(actionEvent);
    }

    @Override
    public void btnModificarHandler(ActionEvent actionEvent) {
        if (this.registro != null && this.tipoAlmacenSeleccionado != null) {
            this.registro.setIdTipoAlmacen(this.tipoAlmacenSeleccionado);
        }
        super.btnModificarHandler(actionEvent);
    }

    protected void selectionHandler() {
        if (registro != null && registro.getIdTipoAlmacen() != null) {
            tipoAlmacenSeleccionado = listaTiposAlmacen.stream()
                    .filter(tu -> tu.getId().equals(registro.getIdTipoAlmacen().getId()))
                    .findFirst()
                    .orElse(null);
        } else {
            tipoAlmacenSeleccionado = null;
        }
    }

    public List<TipoAlmacen> getListaTiposAlmacen() {
        return listaTiposAlmacen;
    }

    public void setListaTiposAlmacen(List<TipoAlmacen> listaTiposAlmacen) {
        this.listaTiposAlmacen = listaTiposAlmacen;
    }

    public TipoAlmacen getTipoAlmacenSeleccionado() {
        return tipoAlmacenSeleccionado;
    }

    public void setTipoAlmacenSeleccionado(TipoAlmacen tipoAlmacenSeleccionado) {
        this.tipoAlmacenSeleccionado = tipoAlmacenSeleccionado;
    }

    public TipoAlmacenDAO getTipoAlmacenDAO() {
        return tipoAlmacenDAO;
    }

    public void setTipoAlmacenDAO(TipoAlmacenDAO tipoAlmacenDAO) {
        this.tipoAlmacenDAO = tipoAlmacenDAO;
    }
}