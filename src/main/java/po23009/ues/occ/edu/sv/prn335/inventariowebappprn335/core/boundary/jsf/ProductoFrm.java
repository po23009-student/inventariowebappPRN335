package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.Valid;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.ProductoDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Producto;

import java.io.Serializable;
import java.util.UUID;

@ViewScoped
@Named
public class ProductoFrm extends DefaultFrm<Producto> implements Serializable {
    @Inject
    FacesContext facesContext;

    @Inject
    ProductoDAO productoDAO;

    public ProductoFrm() {
        this.nombreBean = "Productos";
    }

    @Override
    protected FacesContext getFacesContext() {
        return this.facesContext;
    }

    @Override
    protected InventarioDefaultDataAccess<Producto> getDAO() {
        return productoDAO;
    }

    @Override
    protected Producto nuevoRegistro() {
        @Valid
        Producto nuevoProducto = new Producto();
        nuevoProducto.setId(UUID.randomUUID());
        return nuevoProducto;
    }

    @Override
    protected String getIdAsText(Producto r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected Producto getIdByText(String id) {
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
}