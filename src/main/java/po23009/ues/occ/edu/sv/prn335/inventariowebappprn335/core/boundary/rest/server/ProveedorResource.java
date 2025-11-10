package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.ProveedorDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Proveedor;

import java.math.BigInteger;

@Path("proveedor")
public class ProveedorResource {
    @Inject
    ProveedorDAO proveedorDAO;

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
                int total = proveedorDAO.count();
                Response.ok(proveedorDAO.findRange(first, max)).header("Total-records", total).build();
            } catch(Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception", "Cannot access db").build();
            }

        }

        return Response.status(422).header("Missing-parameter", "first, max").build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Integer id) {
        if(id!=null) {
            try {
                Proveedor resp = proveedorDAO.find(id);

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
    @Path("{id}")
    public Response delete(@PathParam("id") Integer id) {
        if(id!=null) {
            try {
                Proveedor resp = proveedorDAO.find(id);

                if(resp!=null) {
                    proveedorDAO.eliminar(resp);
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
