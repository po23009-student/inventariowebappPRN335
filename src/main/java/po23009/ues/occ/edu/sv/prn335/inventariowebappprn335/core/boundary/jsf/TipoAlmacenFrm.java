package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.Valid;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.TipoAlmacenDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoAlmacen;

import java.io.Serializable;

@Named
@ViewScoped
public class TipoAlmacenFrm extends DefaultFrm<TipoAlmacen> implements Serializable {
    @Inject
    FacesContext facesContext;

    @Inject
    TipoAlmacenDAO tipoAlmacenDAO;

    public TipoAlmacenFrm() {
        this.nombreBean = "Tipos de Almacen";
    }

    @Override
    protected FacesContext getFacesContext() {
        return this.facesContext;
    }

    @Override
    protected InventarioDefaultDataAccess<TipoAlmacen> getDAO() {
        return tipoAlmacenDAO;
    }

    @Override
    protected TipoAlmacen nuevoRegistro() {
        @Valid
        TipoAlmacen nuevoTipoAlmacen = new TipoAlmacen();
        return nuevoTipoAlmacen;
    }

    @Override
    protected String getIdAsText(TipoAlmacen r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected TipoAlmacen getIdByText(String id) {
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
}