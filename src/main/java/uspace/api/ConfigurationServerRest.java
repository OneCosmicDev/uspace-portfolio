package uspace.api;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import standardHyperdriveStabilitySystem.StandardHyperdriveStabilitySystem;
import uspace.api.account.AccountDtoValidator;
import uspace.api.cruise.oxygen.OxygenCalculationValidator;
import uspace.api.cruise.booking.NewBookingDtoValidator;
import uspace.api.cruise.hyperdrive.HyperdriveMaintenanceValidator;
import uspace.api.cruise.hyperdrive.NewHyperdriveModuleValidator;
import uspace.application.cruise.hyperdrive.HyperdriveMaintenanceAssembler;
import uspace.application.cruise.hyperdrive.HyperdriveModuleAssembler;
import uspace.api.cruise.booking.traveler.SportBookingDtoValidator;
import uspace.api.cruise.booking.traveler.zeroGravityExperienceRequest.ZeroGravityExperienceRequestValidator;
import uspace.application.cruise.hyperdrive.HyperdriveModuleService;
import uspace.application.utils.dateTimeParser.LocalDateParser;
import uspace.api.cruise.crew.CrewMemberDtoValidator;
import uspace.application.account.AccountAssembler;
import uspace.application.account.AccountService;
import uspace.application.cruise.crew.CrewMemberAssembler;
import uspace.application.cruise.crew.CrewMemberService;
import uspace.application.cruise.oxygen.RequiredOxygenAssembler;
import uspace.application.utils.dateTimeParser.LocalDateTimeParser;
import uspace.application.cruise.booking.BookingAssembler;
import uspace.application.cruise.booking.BookingService;
import uspace.application.cruise.CruiseAssembler;
import uspace.application.cruise.CruiseService;
import uspace.application.cruise.booking.BookingTravelerAssembler;
import uspace.application.cruise.booking.traveler.TravelerService;
import uspace.configuration.ConfigurationsAllCruises;
import uspace.domain.account.AccountDeletionValidator;
import uspace.domain.account.AccountRepository;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.booking.BookingFactory;
import uspace.domain.cruise.booking.traveler.TravelerFactory;
import uspace.domain.cruise.hyperdrive.HyperdriveStabilityValidator;
import uspace.domain.cruise.hyperdrive.module.HyperdriveModuleFactory;
import uspace.infra.hyperdrive.StandardHyperdriveStabilityValidator;
import uspace.domain.cruise.oxygen.Oxygen;
import uspace.application.cruise.cabin.CabinAttributionAssembler;
import uspace.application.cruise.cabin.CabinAttributionService;
import uspace.application.cruise.emergencyShuttle.EmergencyShuttleManifestAssembler;
import uspace.application.cruise.emergencyShuttle.EmergencyShuttleService;
import uspace.domain.cruise.cabin.CabinAttributor;
import uspace.domain.cruise.cabin.CabinRegistry;
import uspace.domain.cruise.sport.SportRegistry;
import uspace.infra.cabin.CabinJsonReader;
import uspace.infra.persistence.hibernate.HibernateAccountRepository;
import uspace.infra.persistence.hibernate.HibernateCruiseRepository;
import uspace.infra.persistence.inMemory.InMemoryAccountRepository;
import uspace.infra.persistence.inMemory.InMemoryCruiseRepository;
import uspace.infra.sport.SportJsonReader;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ConfigurationServerRest extends AbstractBinder {
    private final static String LOCAL_DATE_FORMAT = "yyyy-MM-dd";
    private final static LocalTime HYPERDRIVE_ACTIVATION_TIME = LocalTime.of(1,0,0);
    private static final String SPORT_JSON_PATH = "src/main/resources/data/sports.json";
    private static final String CABIN_JSON_PATH = "src/main/resources/data/cabins.json";
    private static CruiseRepository CruiseRepositoryInstance = new HibernateCruiseRepository();
    private static AccountRepository AccountRepositoryInstance = new HibernateAccountRepository();

    public static void useInMemoryRepositories() {
        CruiseRepositoryInstance = new InMemoryCruiseRepository();
        AccountRepositoryInstance = new InMemoryAccountRepository();
    }

    @Override
    protected void configure() {
        bind(new StandardHyperdriveStabilityValidator(new StandardHyperdriveStabilitySystem())).to(HyperdriveStabilityValidator.class);

        bind(new TravelerFactory(ConfigurationsAllCruises.TRAVELERS_OXYGEN_CONSUMPTION_PER_MINUTE)).to(TravelerFactory.class);
        bindAsContract(BookingFactory.class);
        bind(new HyperdriveModuleFactory(HYPERDRIVE_ACTIVATION_TIME)).to(HyperdriveModuleFactory.class);

        bindAsContract(RequiredOxygenAssembler.class);
        bindAsContract(AccountDeletionValidator.class);

        bindAsContract(CrewMemberAssembler.class);
        bindAsContract(BookingTravelerAssembler.class);
        bindAsContract(BookingAssembler.class);
        bindAsContract(HyperdriveMaintenanceAssembler.class);
        bindAsContract(HyperdriveModuleAssembler.class);
        bindAsContract(CruiseAssembler.class);
        bindAsContract(AccountAssembler.class);

        bind(DateTimeFormatter.ISO_LOCAL_DATE_TIME).to(DateTimeFormatter.class);
        bindAsContract(LocalDateTimeParser.class);
        bind(new LocalDateParser(DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT))).to(LocalDateParser.class);

        bind(CruiseRepositoryInstance).to(CruiseRepository.class);
        bind(AccountRepositoryInstance).to(AccountRepository.class);

        bindAsContract(OxygenCalculationValidator.class);
        bind(new SportJsonReader(SPORT_JSON_PATH)).to(SportRegistry.class);
        bind(new CabinJsonReader(CABIN_JSON_PATH)).to(CabinRegistry.class);
        bindAsContract(CabinAttributor.class);
        bindAsContract(CabinAttributionAssembler.class);
        bindAsContract(CabinAttributionService.class);
        bindAsContract(EmergencyShuttleManifestAssembler.class);
        bindAsContract(EmergencyShuttleService.class);

        bindAsContract(CrewMemberDtoValidator.class);
        bindAsContract(ZeroGravityExperienceRequestValidator.class);
        bindAsContract(NewBookingDtoValidator.class);
        bindAsContract(HyperdriveMaintenanceValidator.class);
        bindAsContract(NewHyperdriveModuleValidator.class);
        bindAsContract(AccountDtoValidator.class);
        bindAsContract(SportBookingDtoValidator.class);

        bind(ConfigurationsAllCruises.CREW_MEMBER_OXYGEN_CONSUMPTION_PER_MINUTE).to(Oxygen.class).named("crew");
        bindAsContract(CrewMemberService.class);
        bindAsContract(TravelerService.class);
        bindAsContract(HyperdriveModuleService.class);
        bindAsContract(BookingService.class);
        bindAsContract(CruiseService.class);
        bindAsContract(AccountService.class);
    }
}
