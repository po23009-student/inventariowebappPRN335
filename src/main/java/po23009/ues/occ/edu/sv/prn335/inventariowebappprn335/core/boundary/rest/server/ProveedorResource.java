package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.ProveedorDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Proveedor;

@Path("/proveedor")
public class ProveedorResource extends DefaultResource<Proveedor, Integer> {
    @Inject
    ProveedorDAO proveedorDAO;

    @Override
    protected InventarioDefaultDataAccess<Proveedor, Integer> getDAO() { return proveedorDAO; }
}