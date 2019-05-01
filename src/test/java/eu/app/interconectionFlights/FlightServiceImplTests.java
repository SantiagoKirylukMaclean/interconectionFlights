package eu.app.interconectionFlights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import eu.app.interconectionFlights.model.DayFlight;
import eu.app.interconectionFlights.model.Flight;
import eu.app.interconectionFlights.model.FlightSchedule;
import eu.app.interconectionFlights.model.Route;
import eu.app.interconectionFlights.model.Schedule;
import eu.app.interconectionFlights.repository.RoutesRepository;
import eu.app.interconectionFlights.repository.ScheduleRepository;
import eu.app.interconectionFlights.service.FlightService;
import eu.app.interconectionFlights.service.impl.FlightServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest

public class FlightServiceImplTests {
	private static final String CRL = "CRL";
	private static final String LGW = "LGW";
	private static final String MAD = "MAD";
	private static final String DUB = "DUB";
	private static final String LIS = "LIS";
	private static final String CLO = "CLO";
	private static final String RYANAIR = "RYANAIR";
	private static final String ANYTHING = "ANYTHING";
	private static final String ONLY_HOUR_AND_MINUTE = "HH:mm";

	@TestConfiguration
	static class FlightServiceImplTestsContextConfiguration {
		@Bean
		public FlightService flightService() {
			return new FlightServiceImpl();
		}
	}

	@Autowired
	private FlightService flightService;

	LocalDateTime now = LocalDateTime.now();
	LocalDateTime tomorrow = now.plusDays(1);
	LocalDateTime afterTomorrow = now.plusDays(2);

	@MockBean
	private RoutesRepository routesRepository;
	@MockBean
	private ScheduleRepository scheduleRepository;

	@Before
	public void setUp() {
		List<Route> routes = new ArrayList<Route>();
		routes.add(new Route("CRL", "DUB", null, false, false, "RYANAIR", "ANYTHING", "FR"));
		routes.add(new Route("MAD", "DUB", null, false, false, "RYANAIR", "ANYTHING", "FR"));
		routes.add(new Route("DUB", "LIS", null, false, false, "RYANAIR", "ANYTHING", "FR"));
		routes.add(new Route("LGW", "DUB", null, false, false, "RYANAIR", "ANYTHING", "FR"));
		routes.add(new Route("DUB", "MAD", null, false, false, "RYANAIR", "ANYTHING", "FR"));
		Mockito.when(routesRepository.getAll()).thenReturn(routes);
	}

	@Test
	public void contextLoads() {
	}

	@Test
	@DisplayName("When looking For Flight with no Schedule")
	public void flightResultsIsEmpty() {
		int month = tomorrow.getMonthValue();
		int year = tomorrow.getYear();
		Mockito.when(scheduleRepository.get("DUB", "BAR", month, year)).thenReturn(null);
		List<FlightSchedule> flightResults = flightService.getFlights("DUB", "BAR", tomorrow, afterTomorrow);
		assertTrue(flightResults.isEmpty());
	}

	@Test
	@DisplayName("When looking For Flight with no Route")
	public void flightsIsEmpty() {
		List<FlightSchedule> flights = flightService.getFlights("MAD", "BAR", tomorrow, afterTomorrow);
		assertTrue(flights.isEmpty());
	}

	@Test
	@DisplayName("When looking For Flight with 0 stops")
	public void flightResultsGet0Stops() {
		int day = tomorrow.getDayOfMonth();
		int month = tomorrow.getMonthValue();
		int year = tomorrow.getYear();
		String departureDateTime = tomorrow.plusMinutes(30).format(DateTimeFormatter.ofPattern(ONLY_HOUR_AND_MINUTE));
		String arrivalDateTime = tomorrow.plusHours(4).format(DateTimeFormatter.ofPattern(ONLY_HOUR_AND_MINUTE));
		List<DayFlight> days = new ArrayList<DayFlight>();
		List<Flight> flights = new ArrayList<Flight>();
		flights.add(new Flight("7177", departureDateTime, arrivalDateTime));
		days.add(new DayFlight(day, flights));
		Schedule schedule = new Schedule(month, days);
		Mockito.when(scheduleRepository.get(DUB, MAD, month, year)).thenReturn(schedule);
		List<FlightSchedule> flightResults = flightService.getFlights(DUB, MAD, tomorrow, afterTomorrow);
		assertEquals(0, flightResults.get(0).getStops());
	}

	/*

	 * @Test public void
	 * whenLookForFlightNoDirectOneStopAvailableWithScheduleThenReturnFlightResultList
	 * () { int day = tomorrow.getDayOfMonth(); int month =
	 * tomorrow.getMonthValue(); int year = tomorrow.getYear(); String
	 * departureDateTime =
	 * tomorrow.plusMinutes(30).format(DateTimeFormatter.ofPattern(
	 * ONLY_HOUR_AND_MINUTE)); String arrivalDateTime =
	 * tomorrow.plusHours(4).format(DateTimeFormatter.ofPattern(ONLY_HOUR_AND_MINUTE
	 * )); List<DayFlight> days = new ArrayList<DayFlight>(); List<Flight> flights =
	 * new ArrayList<Flight>(); flights.add(new Flight("7177", departureDateTime,
	 * arrivalDateTime)); days.add(new DayFlight(day, flights)); Schedule
	 * scheduleMADDUB = new Schedule(month, days);
	 * Mockito.when(scheduleRepository.get(MAD, DUB, month,
	 * year)).thenReturn(scheduleMADDUB); days = new ArrayList<DayFlight>(); flights
	 * = new ArrayList<Flight>(); LocalDateTime connection = tomorrow.plusHours(7);
	 * day = connection.getDayOfMonth(); month = connection.getMonthValue(); year =
	 * connection.getYear(); departureDateTime =
	 * connection.format(DateTimeFormatter.ofPattern(ONLY_HOUR_AND_MINUTE));
	 * arrivalDateTime = connection.plusHours(3).format(DateTimeFormatter.ofPattern(
	 * ONLY_HOUR_AND_MINUTE)); flights.add(new Flight("7889", departureDateTime,
	 * arrivalDateTime)); days.add(new DayFlight(day, flights)); Schedule
	 * scheduleDUBLIS = new Schedule(month, days);
	 * Mockito.when(scheduleRepository.get(DUB, LIS, month,
	 * year)).thenReturn(scheduleDUBLIS); List<FlightResult> flightResults =
	 * flightService.findFlights(MAD, LIS, tomorrow, afterTomorrow); assertEquals(1,
	 * flightResults.get(0).getStops()); }
	 * 
	 * @Test public void
	 * whenLookForFlightNoDirectOneStopNoScheduleAvailableThenReturnFlightResultList
	 * () { int day = tomorrow.getDayOfMonth(); int month =
	 * tomorrow.getMonthValue(); int year = tomorrow.getYear(); String
	 * departureDateTime =
	 * tomorrow.plusMinutes(30).format(DateTimeFormatter.ofPattern(
	 * ONLY_HOUR_AND_MINUTE)); String arrivalDateTime =
	 * tomorrow.plusHours(4).format(DateTimeFormatter.ofPattern(ONLY_HOUR_AND_MINUTE
	 * )); List<DayFlight> days = new ArrayList<DayFlight>(); List<Flight> flights =
	 * new ArrayList<Flight>(); flights.add(new Flight("7177", departureDateTime,
	 * arrivalDateTime)); days.add(new DayFlight(day, flights)); Schedule
	 * scheduleMADDUB = new Schedule(month, days);
	 * Mockito.when(scheduleRepository.get(MAD, DUB, month,
	 * year)).thenReturn(scheduleMADDUB); days = new ArrayList<DayFlight>(); flights
	 * = new ArrayList<Flight>(); LocalDateTime connection = tomorrow.plusHours(7);
	 * day = connection.getDayOfMonth(); month = connection.getMonthValue(); year =
	 * connection.getYear(); departureDateTime =
	 * connection.format(DateTimeFormatter.ofPattern(ONLY_HOUR_AND_MINUTE));
	 * arrivalDateTime = connection.plusHours(3).format(DateTimeFormatter.ofPattern(
	 * ONLY_HOUR_AND_MINUTE)); flights.add(new Flight("7889", departureDateTime,
	 * arrivalDateTime)); days.add(new DayFlight(day, flights)); Schedule
	 * scheduleDUBLIS = new Schedule(month, days);
	 * Mockito.when(scheduleRepository.get(DUB, LIS, month,
	 * year)).thenReturn(scheduleDUBLIS); List<FlightResult> flightResults =
	 * flightService.findFlights(MAD, LIS, tomorrow, afterTomorrow);
	 * assertEquals(flightResults.get(0).getStops(), 1); }
	 * 
	 * @Test public void modelFlightResultLegLogTest() { List<Leg> legs = new
	 * ArrayList<Leg>(); Leg legOne = new Leg(); legOne.setDepartureAirport(MAD);
	 * legOne.setArrivalAirport(DUB); legOne.setDepartureDateTime(now.toString());
	 * legOne.setArrivalDateTime(tomorrow.toString()); legs.add(legOne); Leg legTwo
	 * = new Leg(); legTwo.setDepartureAirport(MAD); legTwo.setArrivalAirport(DUB);
	 * legTwo.setDepartureDateTime(now.toString());
	 * legTwo.setArrivalDateTime(tomorrow.toString()); legs.add(legTwo);
	 * FlightResult flightResult = new FlightResult(1, legs);
	 * assertNotNull(legOne.toString()); assertNotNull(legTwo.toString());
	 * assertNotNull(flightResult.toString()); }
	 */
}
