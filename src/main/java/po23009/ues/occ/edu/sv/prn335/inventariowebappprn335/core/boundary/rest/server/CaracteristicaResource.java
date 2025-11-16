package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.CaracteristicaDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Caracteristica;

@Path("/caracteristica")
public class CaracteristicaResource extends DefaultResource<Caracteristica, Integer> {
    @Inject
    CaracteristicaDAO caracteristicaDAO;

    @Override
    protected InventarioDefaultDataAccess<Caracteristica, Integer> getDAO() { return caracteristicaDAO; }
}

