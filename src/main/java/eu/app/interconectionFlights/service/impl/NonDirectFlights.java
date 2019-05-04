package eu.app.interconectionFlights.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.app.interconectionFlights.model.FlightSchedule;
import eu.app.interconectionFlights.model.Leg;
import eu.app.interconectionFlights.model.Route;
import eu.app.interconectionFlights.model.Stop;
import eu.app.interconectionFlights.repository.RoutesRepository;
import eu.app.interconectionFlights.repository.ScheduleRepository;
import eu.app.interconectionFlights.utils.Utility;

public class NonDirectFlights {
	private static Logger log = LogManager.getLogger(NonDirectFlights.class);
	private static final HashMap<String, HashSet<String>> routesInHash = new HashMap<>();
	private static ArrayList<Route> serviceResult = new ArrayList<>();

	/**
	 * Return a list of availables Flights with 1 stop
	 * 
	 * @param scheduleRepository
	 * @param routesRepository
	 * @param departure
	 * @param arrival
	 * @param departureDateTime
	 * @param arrivalDateTime
	 * @return List<FlightSchedule>
	 */
	public List<FlightSchedule> getNonDirectConnections(ScheduleRepository scheduleRepository,
			RoutesRepository routesRepository, String departure, String arrival, LocalDateTime departureDateTime,
			LocalDateTime arrivalDateTime) {

		Utility utility = Utility.getInstance();

		List<Route> routes = routesRepository.getAll();

		List<FlightSchedule> flightsAvailables = new ArrayList<FlightSchedule>();
		List<FlightSchedule> flightsToTheStops = new ArrayList<FlightSchedule>();
		List<FlightSchedule> flightsFromTheStops = new ArrayList<FlightSchedule>();
		List<FlightSchedule> flightsFromTheStopsHash = new ArrayList<FlightSchedule>();
		List<FlightSchedule> flightsToTheStopsHash = new ArrayList<FlightSchedule>();

		/*
		List<Stop> stops = getNonDirectRoutes(routes, departure, arrival);
		log.info(String.format("exist %d routes with 1 stop from For", stops.size()));
		*/
		List<String> legs = getNonDirectRoutesHash(routes, departure, arrival);
		log.info(String.format("exist %d routes with 1 stop From hash", legs.size()));

		/*
		String from = "", to = "";
		for (Stop stop : stops) {
			from = stop.getTo().getAirportFrom();
			to = stop.getTo().getAirportTo();
			flightsFromTheStops.addAll(
					utility.getFlightsAvailables(scheduleRepository, from, to, departureDateTime, arrivalDateTime));
			from = stop.getFrom().getAirportFrom();
			to = stop.getFrom().getAirportTo();
			flightsToTheStops.addAll(
					utility.getFlightsAvailables(scheduleRepository, from, to, departureDateTime, arrivalDateTime));
		}
		 */
		for (String leg : legs) {
			flightsToTheStopsHash.addAll(utility.getFlightsAvailables(scheduleRepository, departure, leg,
					departureDateTime, arrivalDateTime));
			flightsFromTheStopsHash.addAll(
					utility.getFlightsAvailables(scheduleRepository, leg, arrival, departureDateTime, arrivalDateTime));
		}

		Leg candidate;
		String arrivalAt, arrivalAirport, departureAirport;

		LocalDateTime minDepartureFromStop, fromTo_departureDateTime, fromTo_arrivalDateTime;
		List<FlightSchedule> flightsFromTheStop = new ArrayList<FlightSchedule>();
		for (FlightSchedule flightTo : flightsToTheStopsHash) {
			departureAirport = flightTo.getLegs().get(0).getDepartureAirport();
			arrivalAirport = flightTo.getLegs().get(0).getArrivalAirport();
			final String arrivalAirportHash = flightTo.getLegs().get(0).getArrivalAirport();
			arrivalAt = flightTo.getLegs().get(0).getArrivalDateTime();
			minDepartureFromStop = LocalDateTime.parse(arrivalAt).plusHours(2);

			/*
			 * flightsFromTheStop = flightsFromTheStops.stream().filter(departFromStop(arrivalAirport))
					.collect(Collectors.toList());
			*/
			
			List<FlightSchedule> flightsFromTheStopHash = flightsFromTheStopsHash.stream()
					.filter(p -> p.getLegs().get(0).getDepartureAirport().equalsIgnoreCase(arrivalAirportHash))
					.collect(Collectors.toList());

			for (FlightSchedule flightFrom : flightsFromTheStopHash) {
				candidate = flightFrom.getLegs().get(0);
				fromTo_departureDateTime = LocalDateTime.parse(candidate.getDepartureDateTime());
				fromTo_arrivalDateTime = LocalDateTime.parse(candidate.getArrivalDateTime());
				if (fromTo_departureDateTime.isAfter(minDepartureFromStop)) {

					if (utility.validFlight(departureDateTime, arrivalDateTime, fromTo_departureDateTime,
							fromTo_arrivalDateTime)) {
						flightsAvailables.add(utility.createFlightResult(flightTo.getLegs().get(0), candidate));
					}
				}
			}
		}

		return flightsAvailables;
	}

	/**
	 * Get all routes with 1 stop in the midle
	 * 
	 * @param routes
	 * @param departure
	 * @param arrival
	 * @return List<Stop>
	 */
	public List<Stop> getNonDirectRoutes(List<Route> routes, String departure, String arrival) {
		List<Route> arriveTo = getArriveTo(routes, arrival);
		List<Route> departFrom = getDepartFrom(routes, departure);
		List<Route> routesAlternative = new ArrayList<Route>();
		List<Stop> stops = new ArrayList<Stop>();
		String airportFrom;
		for (Route arrivesTo : arriveTo) {
			airportFrom = arrivesTo.getAirportFrom();
			routesAlternative = departFrom.stream().filter(arriveTo(airportFrom)).collect(Collectors.toList());
			for (Route departsFrom : routesAlternative) {
				// log.info(String.format("Add new Stop departsFrom %s arrivesTo %s",
				// departsFrom, arrivesTo));
				stops.add(new Stop(departsFrom, arrivesTo));
			}
		}
		return stops;
	}

	private static void populateRoutes(List<Route> routes) {
		for (Route route : routes) {

			if (!routesInHash.containsKey(route.getAirportFrom())) {
				routesInHash.put(route.getAirportFrom(), new HashSet<>());
			}

			HashSet<String> from = routesInHash.get(route.getAirportFrom());

			if (!from.contains(route.getAirportTo())) {
				from.add(route.getAirportTo());
			}
		}
	}

	/**
	 * Get all routes with 1 stop in the midle
	 * 
	 * @param routes
	 * @param departure
	 * @param arrival
	 * @return List<Stop>
	 */
	public List<String> getNonDirectRoutesHash(List<Route> routes, String from, String to) {
		List<String> result = new ArrayList<>();
		populateRoutes(routes);
		HashSet<String> firstLegFlights = routesInHash.get(from);

		for (String firstLegDestination : firstLegFlights) {
			HashSet<String> secondLegFlights = routesInHash.get(firstLegDestination);

			if (secondLegFlights != null && secondLegFlights.contains(to)) {
				result.add(firstLegDestination);
			}
		}

		return result;
	}

	/**
	 * get arrival Airport
	 * 
	 * @param routes
	 * @param airport
	 * @return List<Route>
	 */
	private List<Route> getArriveTo(List<Route> routes, String airport) {
		return routes.stream().filter(arriveTo(airport)).collect(Collectors.toList());
	}

	/**
	 * Check arrival airport of a Route
	 * 
	 * @param airport
	 * @return Predicate<Route>
	 */
	private Predicate<Route> arriveTo(String airport) {
		return p -> p.getAirportTo().equalsIgnoreCase(airport);
	}

	/**
	 * Get departure airport
	 * 
	 * @param airport
	 * @return List<Route>
	 */
	private List<Route> getDepartFrom(List<Route> routes, String airport) {
		return routes.stream().filter(departFrom(airport)).collect(Collectors.toList());
	}

	/**
	 * Check departure airport of a Route
	 * 
	 * @param airport
	 * @return Predicate<Route>
	 */
	private Predicate<Route> departFrom(String airport) {
		return p -> p.getAirportFrom().equalsIgnoreCase(airport);
	}

	/**
	 * Check if I can go to airport from another Leg
	 * 
	 * @param airport
	 * @return Predicate<FlightSchedule>
	 */
	private Predicate<FlightSchedule> departFromStop(String airport) {
		return p -> p.getLegs().get(0).getDepartureAirport().equalsIgnoreCase(airport);
	}

}
