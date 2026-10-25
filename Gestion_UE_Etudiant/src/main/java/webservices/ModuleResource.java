package webservices;

import entities.Module;
import entities.UniteEnseignement;
import metiers.ModuleBusiness;
import metiers.UniteEnseignementBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/modules")
public class ModuleResource {

    private ModuleBusiness moduleBusiness = new ModuleBusiness();
    private UniteEnseignementBusiness ueBusiness = new UniteEnseignementBusiness();

    /**
     * 6) Récupérer tous les modules pédagogiques associés à une Unité d'Enseignement (UE) spécifique,
     *    identifiée par son code (codeUE).
     * Requête: GET /modules/UE?codeUE=1
     * Réponse: Status 200 OK | 404 Not Found
     */
    @GET
    @Path("/UE")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getModulesByUE(@QueryParam("codeUE") Integer codeUE) {
        if (codeUE != null) {
            UniteEnseignement ue = ueBusiness.getUEByCode(codeUE);
            if (ue == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("UE introuvable").build();
            }
            List<Module> list = moduleBusiness.getModulesByUE(ue);
            return Response.ok(list).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Le paramètre codeUE est requis").build();
    }

    /**
     * Support pour l'accès direct par PathParam: GET /modules/UE/{codeUE}
     */
    @GET
    @Path("/UE/{codeUE}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getModulesByUEPath(@PathParam("codeUE") Integer codeUE) {
        return getModulesByUE(codeUE);
    }

    /**
     * 2) Récupération de la liste de tous les modules (avec filtre optionnel par type)
     * Requête: GET /modules
     * Réponse: Status 200 OK
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAllModules(@QueryParam("type") Module.TypeModule type) {
        if (type != null) {
            List<Module> filtered = moduleBusiness.getModulesByType(type);
            return Response.ok(filtered).build();
        }
        List<Module> modules = moduleBusiness.getAllModules();
        return Response.ok(modules).build();
    }

    /**
     * 3) Récupération d'un module ayant un identifiant spécifique (matricule)
     * Requête: GET /modules/{matricule}
     * Réponse: Status 200 OK | 404 Not Found
     */
    @GET
    @Path("/{matricule}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getModuleByMatricule(@PathParam("matricule") String matricule) {
        Module module = moduleBusiness.getModuleByMatricule(matricule);
        if (module == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Module non trouvé").build();
        }
        return Response.ok(module).build();
    }

    /**
     * 1) Création d'un nouveau Module
     * Requête: POST /modules
     * Content-Type: application/json
     * Réponse: Status 201 Created | 404 Not Found
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addModule(Module module) {
        if (module == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Données du module invalides").build();
        }
        boolean created = moduleBusiness.addModule(module);
        if (created) {
            return Response.status(Response.Status.CREATED).entity(module).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("UE non trouvée pour ce module").build();
    }

    /**
     * 5) Modification d'un module ayant une matricule spécifique
     * Requête: PUT /modules/{matricule}
     * Content-Type: application/json
     * Réponse: Status 200 OK | 404 Not Found
     */
    @PUT
    @Path("/{matricule}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateModule(@PathParam("matricule") String matricule, Module updatedModule) {
        if (updatedModule == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Données de mise à jour invalides").build();
        }
        boolean updated = moduleBusiness.updateModule(matricule, updatedModule);
        if (updated) {
            return Response.ok("Module modifié avec succès").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Module non trouvé").build();
    }

    /**
     * 4) Suppression d'un module ayant une matricule spécifique
     * Requête: DELETE /modules/{matricule}
     * Réponse: Status 200 OK | 404 Not Found
     */
    @DELETE
    @Path("/{matricule}")
    public Response deleteModule(@PathParam("matricule") String matricule) {
        boolean deleted = moduleBusiness.deleteModule(matricule);
        if (deleted) {
            return Response.ok("Module supprimé avec succès").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Module non trouvé").build();
    }
}
