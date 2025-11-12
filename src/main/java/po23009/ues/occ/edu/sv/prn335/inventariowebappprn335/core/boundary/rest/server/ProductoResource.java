package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.ProductoDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Producto;

import java.util.UUID;

@Path("/producto")
public class ProductoResource {
    @Inject
    ProductoDAO productoDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findRange(
            @Min(0)
            @DefaultValue("0")
            @QueryParam("first")
            int first,

            @Max(100)
            @DefaultValue("50")
            @QueryParam("max")
            int max
    ) {
        if(first >= 0 && max <= 100) {
            try {
                int total = productoDAO.count();
                return Response.ok(productoDAO.findRange(first, max)).header("Total-records", total).build();
            } catch(Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception", "Cannot access db").build();
            }
        }

        return Response.status(422).header("Missing-parameter", "first, max").build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(
            @PathParam("id")
            UUID id
    ) {
        if(id!=null) {
            try {
                Producto resp = productoDAO.find(id);

                if(resp!=null) {
                    return Response.ok(resp).build();
                }

                return Response.status(Response.Status.NOT_FOUND).header("Not-Found", "Record with id "+id+" not found").build();


            } catch(Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception", "Cannot access db").build();
            }
        }

        return Response.status(422).header("Missing-parameter", "id").build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(
            @PathParam("id")
            UUID id
    ) {
        if(id!=null) {
            try {
                Producto resp = productoDAO.find(id);

                if(resp!=null) {
                    productoDAO.eliminar(resp);
                    return Response.noContent().build();
                }

                return Response.status(Response.Status.NOT_FOUND).header("Not-Found", "Record with id "+id+" not found").build();


            } catch(Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception", "Cannot access db").build();
            }
        }

        return Response.status(422).header("Missing-parameter", "id").build();
    }
}
