package webservices;

import entities.UniteEnseignement;
import metiers.UniteEnseignementBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/UE")
public class UniteEnseignementResource {

    private UniteEnseignementBusiness ueBusiness = new UniteEnseignementBusiness();

    /**
     * 1) Création d'une nouvelle unité d'enseignement
     * Requête: POST /UE
     * Content-Type: application/xml or application/json
     * Réponse: Status 201 Created | 404 Not Found
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addUniteEnseignement(UniteEnseignement ue) {
        if (ue == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Données d'UE invalides").build();
        }
        boolean created = ueBusiness.addUniteEnseignement(ue);
        if (created) {
            return Response.status(Response.Status.CREATED).entity(ue).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Impossible d'ajouter l'UE").build();
    }

    /**
     * 2) Récupération de la liste de toutes les unités d'enseignements: GET /UE
     * 3) Récupération de la liste des unités d'enseignements d'un semestre spécifique: GET /UE?semestre=2
     * 6) Récupération d'une unité d'enseignement ayant un code donné: GET /UE?code=123
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getUEs(@QueryParam("semestre") Integer semestre,
                           @QueryParam("code") Integer code) {
        if (code != null) {
            UniteEnseignement ue = ueBusiness.getUEByCode(code);
            if (ue != null) {
                return Response.status(Response.Status.OK).entity(ue).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity("UE non trouvée").build();
        }

        if (semestre != null) {
            List<UniteEnseignement> list = ueBusiness.getUEBySemestre(semestre);
            return Response.status(Response.Status.OK).entity(list).build();
        }

        return Response.status(Response.Status.OK).entity(ueBusiness.getListeUE()).build();
    }

    /**
     * Support pour l'accès direct par PathParam: GET /UE/{code}
     */
    @GET
    @Path("/{code}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getUEByCodePath(@PathParam("code") int code) {
        UniteEnseignement ue = ueBusiness.getUEByCode(code);
        if (ue != null) {
            return Response.status(Response.Status.OK).entity(ue).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("UE non trouvée").build();
    }

    /**
     * 4) Suppression d'une unité d'enseignement ayant un identifiant spécifique
     * Requête: DELETE /UE/{code} (ex: DELETE /UE/102)
     * Réponse: Status 200 OK | 404 Not Found
     */
    @DELETE
    @Path("/{code}")
    public Response deleteUniteEnseignement(@PathParam("code") int code) {
        boolean deleted = ueBusiness.deleteUniteEnseignement(code);
        if (deleted) {
            return Response.status(Response.Status.OK).entity("Unité d'enseignement supprimée avec succès").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Unité d'enseignement introuvable").build();
    }

    /**
     * 5) Modification d'une unité d'enseignement ayant un identifiant spécifique
     * Requête: PUT /UE/{code} (ex: PUT /UE/101)
     * Content-Type: application/xml or application/json
     * Réponse: Status 200 OK | 404 Not Found
     */
    @PUT
    @Path("/{code}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateUniteEnseignement(@PathParam("code") int code, UniteEnseignement updatedUE) {
        if (updatedUE == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Données d'UE invalides").build();
        }
        boolean updated = ueBusiness.updateUniteEnseignement(code, updatedUE);
        if (updated) {
            return Response.status(Response.Status.OK).entity("Unité d'enseignement modifiée avec succès").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Unité d'enseignement introuvable").build();
    }
}
