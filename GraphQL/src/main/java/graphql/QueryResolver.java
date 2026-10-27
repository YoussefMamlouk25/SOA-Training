package graphql;

import business.ModuleBusiness;
import business.UniteEnseignementBusiness;
import com.coxautodev.graphql.tools.GraphQLRootResolver;
import entities.Module;
import entities.UniteEnseignement;

import java.util.List;

/**
 * Root resolver for GraphQL queries.
 * Handles data retrieval for both UniteEnseignement and Module entities.
 */
public class QueryResolver implements GraphQLRootResolver {

    public UniteEnseignementBusiness helper;
    public UniteEnseignementBusiness ueBusiness;
    public ModuleBusiness moduleBusiness;

    public QueryResolver() {
        this.ueBusiness = new UniteEnseignementBusiness();
        this.moduleBusiness = new ModuleBusiness();
        this.helper = this.ueBusiness;
    }

    public QueryResolver(UniteEnseignementBusiness ueBusiness, ModuleBusiness moduleBusiness) {
        this.ueBusiness = ueBusiness;
        this.moduleBusiness = moduleBusiness;
        this.helper = ueBusiness;
    }

    // ==========================================
    // Unités d'Enseignement Queries
    // ==========================================

    /**
     * 1. Retrieve all Teaching Units (UEs)
     */
    public List<UniteEnseignement> allUEs() {
        return ueBusiness.getListeUE();
    }

    /**
     * 2. Retrieve a Teaching Unit by its unique code
     */
    public UniteEnseignement getUEByCode(int code) {
        return ueBusiness.getUEByCode(code);
    }

    /**
     * 3. Retrieve all Teaching Units for a given domain
     */
    public List<UniteEnseignement> getUEByDomaine(String domaine) {
        return ueBusiness.getUEByDomaine(domaine);
    }

    /**
     * 4. Retrieve all Teaching Units for a given semester
     */
    public List<UniteEnseignement> getUEBySemestre(int semestre) {
        return ueBusiness.getUEBySemestre(semestre);
    }

    // ==========================================
    // Modules Queries
    // ==========================================

    /**
     * 1. Retrieve all Modules
     */
    public List<Module> allModules() {
        return moduleBusiness.getAllModules();
    }

    /**
     * 4. Retrieve all Modules for a given type
     */
    public List<Module> getModulesByType(Module.TypeModule type) {
        return moduleBusiness.getModulesByType(type);
    }

    /**
     * 5. Retrieve a Module by its unique matricule
     */
    public Module getModuleByMatricule(String matricule) {
        return moduleBusiness.getModuleByMatricule(matricule);
    }
}
