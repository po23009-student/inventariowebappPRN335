package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.VentaDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Venta;

import java.util.UUID;

@Path("/venta")
public class VentaResource extends DefaultResource<Venta, UUID> {
    @Inject
    VentaDAO ventaDAO;

    @Override
    protected InventarioDefaultDataAccess<Venta, UUID> getDAO() {
        return ventaDAO;
    }
}
