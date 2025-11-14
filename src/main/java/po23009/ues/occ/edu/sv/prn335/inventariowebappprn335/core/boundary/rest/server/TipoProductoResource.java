package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.TipoProductoDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoProducto;

@Path("/tipo_producto")
public class TipoProductoResource extends DefaultResource<TipoProducto, Long> {
    @Inject
    TipoProductoDAO tipoProductoDAO;

    @Override
    protected InventarioDefaultDataAccess<TipoProducto, Long> getDAO() {
        return tipoProductoDAO;
    }
}