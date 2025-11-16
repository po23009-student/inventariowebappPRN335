package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.CompraDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.CompraDetalleDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Compra;

@Path("/compra")
public class CompraResource extends DefaultResource<Compra, Long> {
    @Inject
    CompraDAO compraDAO;

    @Inject
    CompraDetalleDAO compraDetalleDAO;

    @Override
    protected InventarioDefaultDataAccess<Compra, Long> getDAO() {
        return compraDAO;
    }

    @GET
    @Path("{id}/compra_detalle")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findCompraDetalleByCompra(@PathParam("id") Long idCompra) {
        return Response.ok(compraDetalleDAO.findByCompra(idCompra)).build();
    }
}
