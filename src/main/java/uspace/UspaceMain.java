package uspace;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uspace.api.ConfigurationServerRest;
import uspace.api.filters.CORSFilter;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.booking.CruiseBookings;
import uspace.domain.cruise.booking.CruiseBookingsId;
import uspace.domain.cruise.cabin.CabinAvailabilities;
import uspace.domain.cruise.cabin.CabinAvailabilitiesId;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.emergencyShuttle.EmergencyShuttleManifest;
import uspace.domain.cruise.emergencyShuttle.EmergencyShuttleManifestId;
import uspace.domain.cruise.crew.Crew;
import uspace.domain.cruise.crew.CrewId;
import uspace.domain.cruise.crew.CrewMemberCollection;
import uspace.domain.cruise.crew.crewMember.employeeId.EmployeeIdValidatorRegex;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.hyperdrive.HyperdriveInventory;
import uspace.domain.cruise.hyperdrive.HyperdriveInventoryId;
import uspace.domain.cruise.hyperdrive.module.id.HyperdriveModuleIdValidatorHYStandard;
import uspace.domain.cruise.oxygen.OxygenCalculatorFactory;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperienceId;

import java.time.LocalDateTime;
import java.util.*;

public class UspaceMain implements Runnable {
    public static final String TestEnvironmentArg = "TEST";
    public static final int PORT = 8181;
    public static final String DEFAULT_CRUISE_ID = "JUPITER_MOONS_EXPLORATION_2085";
    public static final int DEFAULT_ZERO_GRAVITY_EXPERIENCE_CAPACITY = 10;
    private static final LocalDateTime DEFAULT_CRUISE_DEPARTURE_DATE = LocalDateTime.of(2085, 1, 25, 12, 0);
    private static final LocalDateTime DEFAULT_CRUISE_END_DATE = LocalDateTime.of(2085, 2, 1, 12, 0);
    private static final int DEFAULT_STANDARD_CABINS = 100;
    private static final int DEFAULT_DELUXE_CABINS = 75;
    private static final int DEFAULT_SUITE_CABINS = 25;
    private static final int DEFAULT_RESCUE_SHIP_COUNT = 5;
    private static final int DEFAULT_RESCUE_SHIP_CAPACITY = 50;
    private static final int DEFAULT_RESCUE_SHIP_COST = 150000;
    private static final int DEFAULT_STANDARD_SHUTTLE_CAPACITY = 20;
    private static final int DEFAULT_STANDARD_SHUTTLE_COST = 50000;
    private static final double DEFAULT_OVERCONSUMPTION_OXYGEN_CALCULATION_MINIMUM_TIME_RATIO = 0.75;
    private static final double DEFAULT_OVERCONSUMPTION_OXYGEN_SECURITY_MARGIN_MULTIPLIER = 1.10;
    private static final double DEFAULT_OVERCONSUMPTION_OXYGEN_TRAVELER_MULTIPLIER = 1.05;
    private static final Logger logger = LoggerFactory.getLogger(UspaceMain.class);
    private static Server server;

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals(TestEnvironmentArg)) {
            logger.info("Running with test environment");
            ConfigurationServerRest.useInMemoryRepositories();
        }

        try {
            new UspaceMain().run();
            server.join();
        } catch (InterruptedException e) {
            logger.error("Error starting server", e);
        } finally {
            if (server.isRunning()) {
                server.destroy();
            }
        }
    }

    public static void runForTest() {
        new UspaceMain().run();
    }

    public void run() {
        server = new Server(PORT);
        ServletContextHandler contextHandler = new ServletContextHandler(server, "/");
        ResourceConfig packageConfig = new ResourceConfig()
                .packages("uspace")
                .register(JacksonFeature.withoutExceptionMappers())
                .register(ConfigurationServerRest.class)
                .register(CORSFilter.class);
        ServletContainer container = new ServletContainer(packageConfig);
        ServletHolder servletHolder = new ServletHolder(container);

        contextHandler.addServlet(servletHolder, "/*");

        try {
            server.start();
            logger.info("Server started on port " + PORT);

            CruiseRepository cruiseRepository = getCruiseRepositoryFromContainer(container);
            initializeCruiseRepositoryWithDefaultCruise(cruiseRepository);

        } catch (Exception e) {
            logger.error("Error starting server", e);
        }
    }

    public static void stop() throws Exception {
        logger.info("Shutting down the application...");
        server.stop();
    }

    private CruiseRepository getCruiseRepositoryFromContainer(ServletContainer container) {
        return container.getApplicationHandler().getInjectionManager().getInstance(CruiseRepository.class);
    }

    private void initializeCruiseRepositoryWithDefaultCruise(CruiseRepository cruiseRepository) {
        Cruise cruise = new Cruise(new CruiseId(DEFAULT_CRUISE_ID),
                                   new CruiseDateTime(DEFAULT_CRUISE_DEPARTURE_DATE),
                                   new CruiseDateTime(DEFAULT_CRUISE_END_DATE),
                                   new CabinAvailabilities(new CabinAvailabilitiesId(),
                                                           new HashMap < > (
                                                                   Map.of(CabinType.STANDARD, DEFAULT_STANDARD_CABINS,
                                                                          CabinType.DELUXE, DEFAULT_DELUXE_CABINS,
                                                                          CabinType.SUITE, DEFAULT_SUITE_CABINS))),
                                   new CruiseBookings(new CruiseBookingsId(), new HashMap<>()),
                                   new ZeroGravityExperience(new ZeroGravityExperienceId(), DEFAULT_ZERO_GRAVITY_EXPERIENCE_CAPACITY),
                                   new HyperdriveInventory(new HyperdriveInventoryId(), new ArrayList < > (), new HyperdriveModuleIdValidatorHYStandard()),
                                   new Crew(new CrewId(), new CrewMemberCollection(new HashMap<>()), EmployeeIdValidatorRegex.for3UppercaseLettersFollowedBy3Digits()),
                                   new OxygenCalculatorFactory(DEFAULT_OVERCONSUMPTION_OXYGEN_CALCULATION_MINIMUM_TIME_RATIO,
                                                               DEFAULT_OVERCONSUMPTION_OXYGEN_SECURITY_MARGIN_MULTIPLIER, DEFAULT_OVERCONSUMPTION_OXYGEN_TRAVELER_MULTIPLIER),
                                   new EmergencyShuttleManifest(new EmergencyShuttleManifestId(),
                                                                DEFAULT_RESCUE_SHIP_COUNT, DEFAULT_RESCUE_SHIP_CAPACITY, DEFAULT_RESCUE_SHIP_COST,
                                                                DEFAULT_STANDARD_SHUTTLE_CAPACITY, DEFAULT_STANDARD_SHUTTLE_COST));

        cruiseRepository.save(cruise);
    }
}