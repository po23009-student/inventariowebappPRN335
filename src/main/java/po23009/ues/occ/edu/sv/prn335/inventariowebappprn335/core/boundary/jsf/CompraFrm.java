package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.CompraDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Compra;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;

import java.io.Serializable;

@Named
@ViewScoped
public class CompraFrm extends DefaultFrm<Compra> implements Serializable {
    @Inject
    FacesContext facesContext;

    @Inject
    CompraDAO compraDAO;

    @Inject
    CompraDetalleFrm cDetalleFrm;

    public CompraFrm() {
        this.nombreBean = "Compras";
    }

    @Override
    protected FacesContext getFacesContext() {
        return this.facesContext;
    }

    @Override
    protected InventarioDefaultDataAccess<Compra> getDAO() {
        return compraDAO;
    }

    @Override
    protected Compra nuevoRegistro() {
        Compra nuevaCompra = new Compra();
        return nuevaCompra;
    }

    @Override
    protected String getIdAsText(Compra r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected Compra getIdByText(String id) {
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
}
