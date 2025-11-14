package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.TipoUnidadMedidaDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoUnidadMedida;

@Path("/tipo_unidad_medida")
public class TipoUnidadMedidaResource extends DefaultResource<TipoUnidadMedida, Integer> {
    @Inject
    TipoUnidadMedidaDAO tumDAO;

    @Override
    protected InventarioDefaultDataAccess<TipoUnidadMedida, Integer> getDAO() {
        return tumDAO;
    }
}