package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.ProductoDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Producto;

import java.util.UUID;

@Path("/producto")
public class ProductoResource extends DefaultResource<Producto, UUID> {
    @Inject
    ProductoDAO productoDAO;

    @Override
    protected InventarioDefaultDataAccess<Producto, UUID> getDAO() {
        return productoDAO;
    }
}
