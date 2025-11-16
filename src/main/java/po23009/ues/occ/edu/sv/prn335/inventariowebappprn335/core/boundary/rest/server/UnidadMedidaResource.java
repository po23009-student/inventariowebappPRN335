package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.UnidadMedidaDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.UnidadMedida;

@Path("/unidad_medida")
public class UnidadMedidaResource extends DefaultResource<UnidadMedida, Integer> {
    @Inject
    UnidadMedidaDAO unidadMedidaDAO;

    @Override
    protected InventarioDefaultDataAccess<UnidadMedida, Integer> getDAO() { return unidadMedidaDAO; }
}
