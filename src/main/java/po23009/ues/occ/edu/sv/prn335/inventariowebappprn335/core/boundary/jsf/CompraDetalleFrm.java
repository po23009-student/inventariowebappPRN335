package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.enterprise.context.Dependent;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.CompraDetalleDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Compra;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.CompraDetalle;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Dependent
@Named
public class CompraDetalleFrm extends DefaultFrm<CompraDetalle> implements Serializable {
    @Inject
    FacesContext facesContext;

    @Inject
    CompraDetalleDAO compraDetalleDAO;

    private List<CompraDetalle> detalles;

    public void cargarDetalles(Long idCompra) {
        this.detalles = compraDetalleDAO.obtenerPorCompra(idCompra);
    }

    public CompraDetalleFrm() {
        this.nombreBean = "Compras";
    }

    @Override
    protected FacesContext getFacesContext() {
        return this.facesContext;
    }

    @Override
    protected InventarioDefaultDataAccess<CompraDetalle> getDAO() {
        return compraDetalleDAO;
    }

    @Override
    protected CompraDetalle nuevoRegistro() {
        CompraDetalle nuevaCompraDetalle = new CompraDetalle();
        return nuevaCompraDetalle;
    }

    @Override
    protected String getIdAsText(CompraDetalle r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected CompraDetalle getIdByText(String id) {
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

    public List<CompraDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<CompraDetalle> detalles) {
        this.detalles = detalles;
    }
}
