package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.rest.server;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;

import java.util.UUID;

public abstract class DefaultResource<T, ID> {
    protected abstract InventarioDefaultDataAccess<T, ID> getDAO();

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
                int total = getDAO().count();
                return Response.ok(getDAO().findRange(first, max)).header("Total-records", total).build();
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
            ID id
    ) {
        if(id!=null) {
            try {
                T resp = getDAO().find(id);

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

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProducto(T entity) {
        if(entity!=null) {
            getDAO().crear(entity);
            return Response.ok(entity).status(Response.Status.CREATED).build();
        }

        return Response.status(422).header("Missing-parameter", "producto").build();

    }

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response updateProducto(
            @PathParam("id")
            ID id,
            T entity
    ) {

        if(getDAO().find(id) != null) {
            getDAO().modificar(entity);
            return Response.ok(entity).status(Response.Status.OK).build();
        }

        return Response.status(Response.Status.NOT_FOUND).header("Not-Found", "Record with id "+id+" not found").build();

    }

    @DELETE
    @Path("/{id}")
    public Response delete(
            @PathParam("id")
            ID id
    ) {
        if(id!=null) {
            try {
                T resp = getDAO().find(id);

                if(resp!=null) {
                    getDAO().eliminar(resp);
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
