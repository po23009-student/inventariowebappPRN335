package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.Valid;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.ProveedorDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Proveedor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class ProveedorFrm extends DefaultFrm<Proveedor, Integer> implements Serializable {
    @Inject
    FacesContext facesContext;

    @Inject
    ProveedorDAO proveedorDAO;

    public ProveedorFrm() {
        this.nombreBean = "Proveedores";
    }

    @Override
    protected FacesContext getFacesContext() {
        return this.facesContext;
    }

    @Override
    protected InventarioDefaultDataAccess<Proveedor, Integer> getDAO() {
        return proveedorDAO;
    }

    @Override
    protected Proveedor nuevoRegistro() {
        @Valid
        Proveedor nuevoProveedor = new Proveedor();
        return nuevoProveedor;
    }

    @Override
    protected String getIdAsText(Proveedor r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected Proveedor getIdByText(String id) {
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

    public List<Proveedor> completeText(String query) {
        String queryLowerCase = query.toLowerCase();
        List<Proveedor> proveedores = proveedorDAO.findRange(0, Integer.MAX_VALUE);
        return proveedores.stream()
                .filter(p -> p.getActivo() && p.getNombre().toLowerCase().contains(queryLowerCase))
                .collect(Collectors.toList());
    }
}
