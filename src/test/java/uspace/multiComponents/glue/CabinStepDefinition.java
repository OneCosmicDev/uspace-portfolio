package uspace.multiComponents.glue;

import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import uspace.UspaceMain;
import uspace.multiComponents.util.APIHelper;
import uspace.multiComponents.util.ResponseMemory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CabinStepDefinition {
    private static final String CRUISE_ID = UspaceMain.DEFAULT_CRUISE_ID;
    private final List<String> bookingIds = new ArrayList<>();
    private int bookingCounter = 0;

    @Etantdonné("une réservation {string} à {string} avec {int} adulte, {int} enfant et {int} senior")
    public void uneReservationAvec(String cabinType, String dateTime, int adults, int children, int seniors) {
        bookingCounter++;
        String accountUsername = "cabinTestUser" + bookingCounter;
        APIHelper.createAccount(accountUsername, "Terre");
        List<String> adultNames = Collections.nCopies(adults, "Adult " + bookingCounter);
        List<String> childNames = Collections.nCopies(children, "Child " + bookingCounter);
        List<String> seniorNames = Collections.nCopies(seniors, "Senior " + bookingCounter);
        String bookingId = APIHelper.createBooking(CRUISE_ID, accountUsername, cabinType, dateTime, adultNames, childNames, seniorNames)
                .getHeader("Location").split("bookings/")[1];
        bookingIds.add(bookingId);
    }

    @Quand("on récupère les attributions de cabine avec le critère {string}")
    public void onRecupereLesAttributionsDeCabineAvecLeCritere(String criteria) {
        ResponseMemory.setResponse(APIHelper.getCabinAttributions(CRUISE_ID, criteria));
    }

    @Alors("au moins une attribution de cabine est retournée")
    public void auMoinsUneAttributionDeCabineEstRetournee() {
        ResponseMemory.getResponse().then().body("cabins.size()", org.hamcrest.Matchers.greaterThan(0));
    }

    @Alors("aucune attribution de cabine n'est retournée")
    public void aucuneAttributionDeCabineNestRetournee() {
        ResponseMemory.getResponse().then().body("cabins.size()", org.hamcrest.Matchers.equalTo(0));
    }

    @Alors("la première attribution de cabine correspond à la réservation numéro {int}")
    public void laPremièreAttributionDeCabineCorrespondÀLaReservationNumero(int num) {
        String expectedBookingId = bookingIds.get(num - 1);
        ResponseMemory.getResponse().then().body("cabins[0].bookingId", org.hamcrest.Matchers.equalTo(expectedBookingId));
    }
}