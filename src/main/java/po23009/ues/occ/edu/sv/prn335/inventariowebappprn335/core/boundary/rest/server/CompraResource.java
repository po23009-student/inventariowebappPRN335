package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.CompraDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Compra;

@Path("/compra")
public class CompraResource extends DefaultResource<Compra, Long> {
    @Inject
    CompraDAO compraDAO;

    @Override
    protected InventarioDefaultDataAccess<Compra, Long> getDAO() {
        return compraDAO;
    }
}
