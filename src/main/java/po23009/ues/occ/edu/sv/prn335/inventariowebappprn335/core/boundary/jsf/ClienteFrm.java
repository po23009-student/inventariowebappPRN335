package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.Valid;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.ClienteDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.ProveedorDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Cliente;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Proveedor;

import java.io.Serializable;
import java.util.UUID;

@Named
@ViewScoped
public class ClienteFrm extends DefaultFrm<Cliente> implements Serializable {
    @Inject
    FacesContext facesContext;

    @Inject
    ClienteDAO clienteDAO;

    public ClienteFrm() {
        this.nombreBean = "Clientes";
    }

    @Override
    protected FacesContext getFacesContext() {
        return this.facesContext;
    }

    @Override
    protected InventarioDefaultDataAccess<Cliente> getDAO() {
        return clienteDAO;
    }

    @Override
    protected Cliente nuevoRegistro() {
        @Valid
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setId(UUID.randomUUID());
        return nuevoCliente;
    }

    @Override
    protected String getIdAsText(Cliente r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected Cliente getIdByText(String id) {
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
