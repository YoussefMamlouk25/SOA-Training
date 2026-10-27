package graphql;

import business.ModuleBusiness;
import business.UniteEnseignementBusiness;
import com.coxautodev.graphql.tools.GraphQLRootResolver;
import entities.Module;
import entities.UniteEnseignement;

/**
 * Root resolver for GraphQL mutations.
 * Handles create, update, and delete operations for UniteEnseignement and Module entities.
 */
public class MutationResolver implements GraphQLRootResolver {

    public UniteEnseignementBusiness helper;
    public UniteEnseignementBusiness ueBusiness;
    public ModuleBusiness moduleBusiness;

    public MutationResolver() {
        this.ueBusiness = new UniteEnseignementBusiness();
        this.moduleBusiness = new ModuleBusiness();
        this.helper = this.ueBusiness;
    }

    public MutationResolver(UniteEnseignementBusiness ueBusiness, ModuleBusiness moduleBusiness) {
        this.ueBusiness = ueBusiness;
        this.moduleBusiness = moduleBusiness;
        this.helper = ueBusiness;
    }

    // ==========================================
    // Unités d'Enseignement Mutations
    // ==========================================

    /**
     * 1. Create a new Teaching Unit (Unité d'Enseignement)
     */
    public boolean addUniteEnseignement(int code, String domaine, String responsable, int credits, int semestre) {
        UniteEnseignement ue = new UniteEnseignement(code, domaine, responsable, credits, semestre);
        return ueBusiness.addUniteEnseignement(ue);
    }

    /**
     * 5. Update an existing Teaching Unit by its code
     */
    public boolean updateUniteEnseignement(int code, String domaine, String responsable, int credits, int semestre) {
        UniteEnseignement updatedUE = new UniteEnseignement(code, domaine, responsable, credits, semestre);
        return ueBusiness.updateUniteEnseignement(code, updatedUE);
    }

    /**
     * 6. Delete a Teaching Unit by its code
     */
    public boolean deleteUniteEnseignement(int code) {
        return ueBusiness.deleteUniteEnseignement(code);
    }

    // ==========================================
    // Modules Mutations
    // ==========================================

    /**
     * 2. Create a new Module with Boolean return type
     */
    public boolean addModule(String matricule, String libelle, int coef, int volumeHoraire, int codeUE, Module.TypeModule type) {
        UniteEnseignement ue = ueBusiness.getUEByCode(codeUE);
        if (ue == null) {
            return false;
        }
        Module module = new Module(matricule, libelle, coef, volumeHoraire, type, ue);
        return moduleBusiness.addModule(module);
    }

    /**
     * 3. Update an existing Module by its matricule, returning the updated Module
     */
    public Module updateModule(String matricule, String libelle, int coef, int volumeHoraire, Module.TypeModule type, int codeUE) {
        UniteEnseignement ue = ueBusiness.getUEByCode(codeUE);
        Module updatedModule = new Module(matricule, libelle, coef, volumeHoraire, type, ue);
        boolean success = moduleBusiness.updateModule(matricule, updatedModule);
        if (success) {
            return updatedModule;
        }
        return null;
    }

    /**
     * 6. Delete a Module by its matricule, returning confirmation message
     */
    public String deleteModule(String matricule) {
        boolean success = moduleBusiness.deleteModule(matricule);
        if (success) {
            return "deleted successfully";
        }
        return "Module not found";
    }
}
