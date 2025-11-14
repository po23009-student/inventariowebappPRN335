package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.ClienteDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Cliente;

import java.util.UUID;

@Path("/cliente")
public class ClienteResource extends DefaultResource<Cliente, UUID> {
    @Inject
    ClienteDAO clienteDAO;

    @Override
    protected InventarioDefaultDataAccess<Cliente, UUID> getDAO() { return clienteDAO; }
}