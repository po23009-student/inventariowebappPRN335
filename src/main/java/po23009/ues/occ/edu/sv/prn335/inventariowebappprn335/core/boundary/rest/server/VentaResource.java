package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.VentaDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.VentaDetalleDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Venta;

import java.util.UUID;

@Path("/venta")
public class VentaResource extends DefaultResource<Venta, UUID> {
    @Inject
    VentaDAO ventaDAO;

    @Inject
    VentaDetalleDAO ventaDetalleDAO;

    @Override
    protected InventarioDefaultDataAccess<Venta, UUID> getDAO() {
        return ventaDAO;
    }

    @GET
    @Path("{id}/venta_detalle")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findVentaDetalleByVenta(@PathParam("id") UUID idVenta) {
        return Response.ok(ventaDetalleDAO.findByVenta(idVenta)).build();
    }
}
