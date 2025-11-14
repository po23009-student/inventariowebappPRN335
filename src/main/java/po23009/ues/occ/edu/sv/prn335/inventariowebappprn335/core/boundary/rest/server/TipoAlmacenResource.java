package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.TipoAlmacenDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoAlmacen;

@Path("/tipo_almacen")
public class TipoAlmacenResource extends DefaultResource<TipoAlmacen, Integer> {
    @Inject
    TipoAlmacenDAO tipoAlmacenDAO;

    @Override
    protected InventarioDefaultDataAccess<TipoAlmacen, Integer> getDAO() {
        return tipoAlmacenDAO;
    }
}