package test;

import entities.Module;
import entities.UniteEnseignement;
import rest.utilities.RestActivator;
import webservices.ModuleResource;
import webservices.UniteEnseignementResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.List;

public class WorkshopTest {

    public static void main(String[] args) throws Exception {
        System.out.println("==================================================");
        System.out.println("Starting Atelier REST n°1 Verification Tests");
        System.out.println("==================================================");

        testRestActivator();
        testXmlUnmarshalling();
        testUniteEnseignementResource();
        testModuleResource();

        System.out.println("==================================================");
        System.out.println("ALL ATELIER REST TESTS PASSED SUCCESSFULLY! (100%)");
        System.out.println("==================================================");
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            System.err.println("TEST FAILED: " + message);
            System.exit(1);
        } else {
            System.out.println(" [PASS] " + message);
        }
    }

    private static void testRestActivator() {
        System.out.println("\n--- Testing RestActivator Configuration ---");
        ApplicationPath appPath = RestActivator.class.getAnnotation(ApplicationPath.class);
        assertTrue(appPath != null, "RestActivator must have @ApplicationPath annotation");
        assertTrue("rest".equals(appPath.value()), "@ApplicationPath must be 'rest'");
    }

    private static void testXmlUnmarshalling() throws Exception {
        System.out.println("\n--- Testing XML Unmarshalling for UniteEnseignement ---");
        String xml = "<uniteEnseignement>" +
                "<code>123</code>" +
                "<domaine>Informatique</domaine>" +
                "<responsable>Mme Maroua Douiri</responsable>" +
                "<credits>6</credits>" +
                "<semestre>2</semestre>" +
                "</uniteEnseignement>";

        JAXBContext jaxbContext = JAXBContext.newInstance(UniteEnseignement.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        UniteEnseignement ue = (UniteEnseignement) unmarshaller.unmarshal(new StringReader(xml));

        assertTrue(ue.getCode() == 123, "UE code unmarshalled properly (123)");
        assertTrue("Informatique".equals(ue.getDomaine()), "UE domaine unmarshalled properly");
        assertTrue("Mme Maroua Douiri".equals(ue.getResponsable()), "UE responsable unmarshalled properly");
        assertTrue(ue.getCredits() == 6, "UE credits unmarshalled properly");
        assertTrue(ue.getSemestre() == 2, "UE semestre unmarshalled properly");
    }

    @SuppressWarnings("unchecked")
    private static void testUniteEnseignementResource() {
        System.out.println("\n--- Testing UniteEnseignementResource Endpoints ---");
        UniteEnseignementResource resource = new UniteEnseignementResource();

        // 1) POST /UE
        UniteEnseignement newUE = new UniteEnseignement(123, "Informatique", "Mme Maroua Douiri", 6, 2);
        Response postResp = resource.addUniteEnseignement(newUE);
        assertTrue(postResp.getStatus() == 201, "POST /UE returns 201 Created");

        // 2) GET /UE (All)
        Response getAllResp = resource.getUEs(null, null);
        assertTrue(getAllResp.getStatus() == 200, "GET /UE returns 200 OK");
        List<UniteEnseignement> allList = (List<UniteEnseignement>) getAllResp.getEntity();
        assertTrue(allList.size() >= 3, "GET /UE returns list of UEs");

        // 3) GET /UE?semestre=2
        Response getSemResp = resource.getUEs(2, null);
        assertTrue(getSemResp.getStatus() == 200, "GET /UE?semestre=2 returns 200 OK");
        List<UniteEnseignement> semList = (List<UniteEnseignement>) getSemResp.getEntity();
        for (UniteEnseignement ue : semList) {
            assertTrue(ue.getSemestre() == 2, "Filtered UE has semestre == 2");
        }

        // 6) GET /UE?code=123
        Response getCodeResp = resource.getUEs(null, 123);
        assertTrue(getCodeResp.getStatus() == 200, "GET /UE?code=123 returns 200 OK");
        UniteEnseignement foundUe = (UniteEnseignement) getCodeResp.getEntity();
        assertTrue(foundUe.getCode() == 123, "UE retrieved has code 123");

        // GET /UE/123 (Path param)
        Response getPathResp = resource.getUEByCodePath(123);
        assertTrue(getPathResp.getStatus() == 200, "GET /UE/123 returns 200 OK");

        // 5) PUT /UE/101
        UniteEnseignement updatedUE = new UniteEnseignement(101, "Infographie", "Mme Oumeima Ibnelfkih", 3, 1);
        Response putResp = resource.updateUniteEnseignement(101, updatedUE);
        assertTrue(putResp.getStatus() == 200, "PUT /UE/101 returns 200 OK");

        // 4) DELETE /UE/102
        Response delResp = resource.deleteUniteEnseignement(102);
        assertTrue(delResp.getStatus() == 200, "DELETE /UE/102 returns 200 OK");

        // DELETE non-existent UE
        Response delNotFound = resource.deleteUniteEnseignement(99999);
        assertTrue(delNotFound.getStatus() == 404, "DELETE /UE/99999 returns 404 Not Found");
    }

    @SuppressWarnings("unchecked")
    private static void testModuleResource() {
        System.out.println("\n--- Testing ModuleResource Endpoints ---");
        ModuleResource resource = new ModuleResource();

        // 1) POST /modules with valid UE
        UniteEnseignement ueRef = new UniteEnseignement();
        ueRef.setCode(1);
        Module validModule = new Module("M301", "Intelligence Artificielle", 3, 30, Module.TypeModule.PROFESSIONNEL, ueRef);
        Response postResp = resource.addModule(validModule);
        assertTrue(postResp.getStatus() == 201, "POST /modules returns 201 Created for valid UE");

        // POST /modules with invalid UE
        UniteEnseignement invalidUeRef = new UniteEnseignement();
        invalidUeRef.setCode(99999);
        Module invalidModule = new Module("M302", "Invalid Module", 2, 20, Module.TypeModule.PROFESSIONNEL, invalidUeRef);
        Response postInvalidResp = resource.addModule(invalidModule);
        assertTrue(postInvalidResp.getStatus() == 404, "POST /modules returns 404 Not Found for non-existent UE");

        // 2) GET /modules
        Response getAllResp = resource.getAllModules(null);
        assertTrue(getAllResp.getStatus() == 200, "GET /modules returns 200 OK");
        List<Module> allModules = (List<Module>) getAllResp.getEntity();
        int initialModuleCount = allModules.size();
        assertTrue(initialModuleCount >= 3, "GET /modules returns all modules");

        // 3) GET /modules/M101
        Response getByMatricule = resource.getModuleByMatricule("M101");
        assertTrue(getByMatricule.getStatus() == 200, "GET /modules/M101 returns 200 OK");

        // GET /modules/UNKNOWN
        Response getNotFound = resource.getModuleByMatricule("UNKNOWN_MATRICULE");
        assertTrue(getNotFound.getStatus() == 404, "GET /modules/UNKNOWN returns 404 Not Found");

        // 5) PUT /modules/M102
        Module updatedMod = new Module("M102", "Web Services", 2, 40, Module.TypeModule.PROFESSIONNEL, ueRef);
        Response putResp = resource.updateModule("M102", updatedMod);
        assertTrue(putResp.getStatus() == 200, "PUT /modules/M102 returns 200 OK");

        // 6) GET /modules/UE?codeUE=1 (and ensure it doesn't mutate or delete other modules!)
        Response ueModulesResp = resource.getModulesByUE(1);
        assertTrue(ueModulesResp.getStatus() == 200, "GET /modules/UE?codeUE=1 returns 200 OK");
        List<Module> ue1Modules = (List<Module>) ueModulesResp.getEntity();
        assertTrue(ue1Modules.size() > 0, "GET /modules/UE?codeUE=1 found associated modules");

        // Verify no destructive mutation happened on allModules
        Response checkAllAgain = resource.getAllModules(null);
        List<Module> allModulesAfter = (List<Module>) checkAllAgain.getEntity();
        assertTrue(allModulesAfter.size() == initialModuleCount, "Non-destructive: all modules preserved after getModulesByUE");

        // GET /modules/UE for invalid UE
        Response ueModulesNotFound = resource.getModulesByUE(99999);
        assertTrue(ueModulesNotFound.getStatus() == 404, "GET /modules/UE?codeUE=99999 returns 404 Not Found");

        // 4) DELETE /modules/M101
        Response delResp = resource.deleteModule("M101");
        assertTrue(delResp.getStatus() == 200, "DELETE /modules/M101 returns 200 OK");

        // DELETE non-existent module
        Response delNotFound = resource.deleteModule("M101");
        assertTrue(delNotFound.getStatus() == 404, "DELETE /modules/M101 again returns 404 Not Found");
    }
}
