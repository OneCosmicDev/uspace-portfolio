package uspace.multiComponents.glue;

import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import org.hamcrest.Matchers;
import uspace.UspaceMain;
import uspace.multiComponents.util.APIHelper;
import uspace.multiComponents.util.ResponseMemory;

import java.util.List;

public class EmergencyShuttleStepDefinition {
    private static final String CRUISE_ID = UspaceMain.DEFAULT_CRUISE_ID;
    private static final String VALID_ACCOUNT_USERNAME = "emergencyShuttleTestUser";
    private static final String VALID_HOME_PLANET_NAME = "Terre";
    private static final String VALID_EMPLOYEE_ID = "EMP001";
    private static final String VALID_CREW_MEMBER_NAME = "Jane Smith";

    private String bookingId;
    private String travelerId;

    @Etantdonné("une réservation avec 1 adulte est créée")
    public void uneReservationAvec1AdulteEstCreee() {
        APIHelper.createAccount(VALID_ACCOUNT_USERNAME, VALID_HOME_PLANET_NAME);
        bookingId = APIHelper.createBooking(CRUISE_ID, VALID_ACCOUNT_USERNAME, "DELUXE", "2084-01-25T12:00",
                List.of("John Doe"), List.of(), List.of())
                .getHeader("Location").split("bookings/")[1];
        travelerId = APIHelper.getBooking(CRUISE_ID, bookingId).jsonPath().getString("travelers[0].id");
    }

    @Etantdonné("un membre d'équipage est ajouté à la croisière")
    public void unMembreDEquipageEstAjouteALaCroisiere() {
        APIHelper.addCrewMember(CRUISE_ID, VALID_EMPLOYEE_ID, VALID_CREW_MEMBER_NAME);
    }

    @Quand("on récupère le manifeste des navettes d'urgence")
    public void onRecupereLeManifesteDesNavettesDurgence() {
        ResponseMemory.setResponse(APIHelper.getEmergencyShuttleManifest(CRUISE_ID));
    }

    @Quand("on récupère le manifeste des navettes d'urgence de la croisière {string}")
    public void onRecupereLeManifesteDesNavettesDurgenceDeLaCroisiere(String cruiseId) {
        ResponseMemory.setResponse(APIHelper.getEmergencyShuttleManifest(cruiseId));
    }

    @Alors("aucune navette d'urgence n'est dans le manifeste")
    public void aucuneNavetteDurgenceNestDansLeManifeste() {
        ResponseMemory.getResponse().then().body("emergencyShuttles.size()", Matchers.equalTo(0));
    }

    @Alors("le coût total du manifeste est {int}")
    public void leCoutTotalDuManifesteEst(int cost) {
        ResponseMemory.getResponse().then().body("cost", Matchers.equalTo(cost));
    }

    @Alors("le manifeste contient une navette de type {string}")
    public void leManifesteContientUneNavetteDetype(String type) {
        ResponseMemory.getResponse().then().body("emergencyShuttles[0].type", Matchers.equalTo(type));
    }

    @Alors("la navette contient le voyageur de la réservation")
    public void laNavetteContientLeVoyageurDeLaReservation() {
        ResponseMemory.getResponse().then()
                .body("emergencyShuttles[0].travelers[0].travelerId", Matchers.equalTo(travelerId));
    }

    @Alors("la navette contient le membre d'équipage")
    public void laNavetteContientLeMembreDEquipage() {
        ResponseMemory.getResponse().then()
                .body("emergencyShuttles[0].crewMembers[0].employeeId", Matchers.equalTo(VALID_EMPLOYEE_ID));
    }
}
