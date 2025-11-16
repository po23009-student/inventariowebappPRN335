package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.AlmacenDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Almacen;

@Path("/almacen")
public class AlmacenResource extends DefaultResource<Almacen, Integer> {
    @Inject
    AlmacenDAO almacenDAO;

    @Override
    protected InventarioDefaultDataAccess<Almacen, Integer> getDAO() { return almacenDAO; }
}
