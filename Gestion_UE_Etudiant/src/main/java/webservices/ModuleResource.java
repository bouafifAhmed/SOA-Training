package webservices;

import entities.Module;
import entities.UniteEnseignement;
import metiers.ModuleBusiness;
import metiers.UniteEnseignementBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/modules")
public class ModuleResource {

    private static final ModuleBusiness moduleBusiness = new ModuleBusiness();
    private static final UniteEnseignementBusiness ueBusiness = new UniteEnseignementBusiness();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createModule(Module module, @Context UriInfo uriInfo) {
        if (module == null || module.getMatricule() == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (moduleBusiness.getModuleByMatricule(module.getMatricule()) != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }
        boolean ok = moduleBusiness.addModule(module);
        if (!ok) return Response.status(Response.Status.NOT_FOUND).build();
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(module.getMatricule());
        return Response.created(builder.build()).entity(module).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listModules() {
        List<Module> list = moduleBusiness.getAllModules();
        return Response.ok(list).build();
    }

    @GET
    @Path("/{matricule}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModule(@PathParam("matricule") String matricule) {
        Module m = moduleBusiness.getModuleByMatricule(matricule);
        if (m == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(m).build();
    }

    @PUT
    @Path("/{matricule}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateModule(@PathParam("matricule") String matricule, Module updated) {
        if (updated == null) return Response.status(Response.Status.BAD_REQUEST).build();
        updated.setMatricule(matricule);
        boolean ok = moduleBusiness.updateModule(matricule, updated);
        if (!ok) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{matricule}")
    public Response deleteModule(@PathParam("matricule") String matricule) {
        boolean ok = moduleBusiness.deleteModule(matricule);
        if (!ok) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.noContent().build();
    }

    @GET
    @Path("/UE")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listModulesByUE(@QueryParam("codeUE") int codeUE) {
        UniteEnseignement ue = ueBusiness.getUEByCode(codeUE);
        if (ue == null) return Response.status(Response.Status.NOT_FOUND).build();
        List<Module> list = moduleBusiness.getModulesByUE(ue);
        return Response.ok(list).build();
    }
}
