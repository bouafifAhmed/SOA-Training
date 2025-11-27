package webservices;

import entities.UniteEnseignement;
import metiers.UniteEnseignementBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/UE")
public class UniteEnseignementResource {

    private static final UniteEnseignementBusiness ueBusiness = new UniteEnseignementBusiness();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listUE(@QueryParam("semestre") Integer semestre,
                           @QueryParam("code") Integer code) {
        if (code != null) {
            UniteEnseignement ue = ueBusiness.getUEByCode(code);
            if (ue == null) return Response.status(Response.Status.NOT_FOUND).build();
            return Response.ok(ue).build();
        }
        if (semestre != null) {
            List<UniteEnseignement> list = ueBusiness.getUEBySemestre(semestre);
            return Response.ok(list).build();
        }
        return Response.ok(ueBusiness.getListeUE()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUE(UniteEnseignement ue, @Context UriInfo uriInfo) {
        if (ue == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (ueBusiness.getUEByCode(ue.getCode()) != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }
        boolean added = ueBusiness.addUniteEnseignement(ue);
        if (!added) return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(String.valueOf(ue.getCode()));
        return Response.created(builder.build()).entity(ue).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUE(@PathParam("id") int id, UniteEnseignement updated) {
        if (updated == null) return Response.status(Response.Status.BAD_REQUEST).build();
        boolean ok = ueBusiness.updateUniteEnseignement(id, updated);
        if (!ok) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUE(@PathParam("id") int id) {
        boolean ok = ueBusiness.deleteUniteEnseignement(id);
        if (!ok) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.noContent().build();
    }
}
