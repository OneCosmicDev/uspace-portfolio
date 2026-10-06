package uspace.domain.cruise;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import uspace.domain.cruise.booking.*;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.booking.traveler.exceptions.TravelerNotFoundException;
import uspace.domain.cruise.cabin.CabinAttribution;
import uspace.domain.cruise.cabin.CabinAttributionCriteria;
import uspace.domain.cruise.cabin.CabinAttributor;
import uspace.domain.cruise.cabin.CabinAvailabilities;
import uspace.domain.cruise.cabin.CabinId;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.crew.Crew;
import uspace.domain.cruise.crew.crewMember.CrewMember;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.emergencyShuttle.EmergencyShuttleManifest;
import uspace.domain.cruise.hyperdrive.HyperdriveInventory;
import uspace.domain.cruise.hyperdrive.module.maintenance.HyperdriveMaintenance;
import uspace.domain.cruise.hyperdrive.module.maintenance.exceptions.EmployeeNotFoundException;
import uspace.domain.cruise.hyperdrive.module.HyperdriveModule;
import uspace.domain.cruise.hyperdrive.module.exceptions.InvalidHyperdriveModuleActivationDateException;
import uspace.domain.cruise.hyperdrive.module.id.HyperdriveModuleId;
import uspace.domain.cruise.oxygen.Oxygen;
import uspace.domain.cruise.oxygen.OxygenCalculator;
import uspace.domain.cruise.oxygen.OxygenCalculatorFactory;
import uspace.domain.cruise.oxygen.exceptions.OxygenCalculationDateOutsideCruiseException;
import uspace.domain.cruise.sport.Sport;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;
import uspace.domain.cruise.zeroGravityExperience.exceptions.ZeroGravityExperienceBookingTimeException;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "Cruise")
public class Cruise {
    @Id
    private CruiseId id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(
                name = "localDateTime",
                column = @Column(name = "departureDateTime")
        ),
    })
    private CruiseDateTime departureDateTime;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "localDateTime",
                    column = @Column(name = "endDateTime")
            ),
    })
    private CruiseDateTime endDateTime;

    @OneToOne(cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private CabinAvailabilities cabinAvailabilities;

    @OneToOne(cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private CruiseBookings cruiseBookings;

    @OneToOne(cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private ZeroGravityExperience zeroGravityExperience;

    @OneToOne(cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private HyperdriveInventory hyperdriveInventory;

    @OneToOne(cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private Crew crew;

    @OneToOne(cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private EmergencyShuttleManifest emergencyShuttleManifest;

    private OxygenCalculatorFactory oxygenCalculatorFactory;

    public Cruise() {

    }

    public Cruise(CruiseId id, CruiseDateTime departureDateTime, CruiseDateTime endDateTime, CabinAvailabilities cabinAvailabilities,
                  CruiseBookings cruiseBookings, ZeroGravityExperience zeroGravityExperience, HyperdriveInventory hyperdriveInventory, Crew crew,
                  OxygenCalculatorFactory oxygenCalculatorFactory, EmergencyShuttleManifest emergencyShuttleManifest) {
        this.id = id;
        this.departureDateTime = departureDateTime;
        this.endDateTime = endDateTime;
        this.cabinAvailabilities = cabinAvailabilities;
        this.cruiseBookings = cruiseBookings;
        this.zeroGravityExperience = zeroGravityExperience;
        this.hyperdriveInventory = hyperdriveInventory;
        this.crew = crew;
        this.oxygenCalculatorFactory = oxygenCalculatorFactory;
        this.emergencyShuttleManifest = emergencyShuttleManifest;
    }

    public CruiseId getId() {
        return id;
    }

    public CruiseDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public CruiseDateTime getEndDateTime() {
        return endDateTime;
    }

    public List<HyperdriveModule> getHyperdriveModules() {
        return hyperdriveInventory.getAllHyperdriveModules();
    }

    public void processBooking(Booking booking) {
        booking.validate(departureDateTime);
        cabinAvailabilities.bookCabin(booking.getCabinType());
        cruiseBookings.add(booking);
        booking.getTravelers().forEach(emergencyShuttleManifest::addTraveler);
    }

    public Booking findBookingById(BookingId bookingId) {
        return cruiseBookings.findById(bookingId);
    }

    public void bookZeroGravityExperience(BookingId bookingId, TravelerId travelerId, CruiseDateTime experienceBookingDateTime) {
        if (experienceBookingDateTime.isAfter(departureDateTime)) {
            throw new ZeroGravityExperienceBookingTimeException();
        }

        Booking booking = cruiseBookings.findById(bookingId);
        if (booking == null) {
            throw new TravelerNotFoundException();
        }

        booking.bookZeroGravityExperience(travelerId, zeroGravityExperience);
    }

    public void addHyperdriveModule(HyperdriveModule hyperdriveModule) {
        if (!hyperdriveModule.isActivationBetween(departureDateTime, endDateTime)) {
            throw new InvalidHyperdriveModuleActivationDateException();
        }

        hyperdriveInventory.addHyperdriveModule(hyperdriveModule);
    }

    public void addCrewMember(CrewMember newCrewMember) {
        crew.addCrewMember(newCrewMember, cabinAvailabilities);
        emergencyShuttleManifest.addCrewMember(newCrewMember);
    }

    public EmergencyShuttleManifest getEmergencyShuttleManifest() {
        return emergencyShuttleManifest;
    }

    public List<CabinAttribution> generateAttributions(CabinAttributor cabinAttributor,
                                                        Map<CabinType, List<CabinId>> cabinIdsByCategory,
                                                        CabinAttributionCriteria criteria) {
        return cabinAttributor.attribute(cruiseBookings.getAll(), cabinIdsByCategory, criteria);
    }

    public List<CrewMember> getCrewMembers() {
        return crew.getAllCrewMembers();
    }

    public void addHyperdriveMaintenance(HyperdriveModuleId moduleId, HyperdriveMaintenance maintenance) {
        if (!maintenance.hasValidEmployees(crew)) {
            throw new EmployeeNotFoundException();
        }
        hyperdriveInventory.addMaintenance(moduleId, maintenance);
    }

    public Oxygen calculateRequiredOxygen(CruiseDateTime calculationCruiseDateTime) {
        if (!calculationCruiseDateTime.isBetween(departureDateTime, endDateTime)) {
            throw new OxygenCalculationDateOutsideCruiseException();
        }

        OxygenCalculator oxygenCalculator = oxygenCalculatorFactory.create(calculationCruiseDateTime, departureDateTime,
                                                                           endDateTime);
        return oxygenCalculator.calculateRequiredOxygen(cruiseBookings, crew);
    }

    public void bookSport(BookingId bookingId, TravelerId travelerId, Sport sport) {
        Booking booking = cruiseBookings.findById(bookingId);
        if (booking == null) {
            throw new TravelerNotFoundException();
        }

        booking.bookSport(travelerId, sport);
    }
}
