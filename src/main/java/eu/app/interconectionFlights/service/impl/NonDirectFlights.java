package eu.app.interconectionFlights.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.app.interconectionFlights.controller.FlightController;
import eu.app.interconectionFlights.model.FlightSchedule;
import eu.app.interconectionFlights.model.Leg;
import eu.app.interconectionFlights.model.Route;
import eu.app.interconectionFlights.model.Stop;
import eu.app.interconectionFlights.repository.RoutesRepository;
import eu.app.interconectionFlights.repository.ScheduleRepository;
import eu.app.interconectionFlights.utils.Utility;

public class NonDirectFlights {
	private static Logger log = LogManager.getLogger(NonDirectFlights.class);

	
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
		
		List<Stop> stops = getNonDirectRoutes(routes, departure, arrival);
		log.info(String.format("exist %d routes with 1 stop", stops.size()));
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

		Leg candidate;
		String arrivalAt, arrivalAirport, departureAirport;
		LocalDateTime minDepartureFromStop, fromTo_departureDateTime, fromTo_arrivalDateTime;
		List<FlightSchedule> flightsFromTheStop = new ArrayList<FlightSchedule>();
		for (FlightSchedule flightTo : flightsToTheStops) {
			departureAirport = flightTo.getLegs().get(0).getDepartureAirport();
			arrivalAirport = flightTo.getLegs().get(0).getArrivalAirport();
			arrivalAt = flightTo.getLegs().get(0).getArrivalDateTime();
			minDepartureFromStop = LocalDateTime.parse(arrivalAt).plusHours(2);

			flightsFromTheStop = flightsFromTheStops.stream().filter(departFromStop(arrivalAirport))
					.collect(Collectors.toList());
			for (FlightSchedule flightFrom : flightsFromTheStop) {
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
				log.info(String.format("Add new Stop departsFrom %s arrivesTo %s", departsFrom, arrivesTo));
				stops.add(new Stop(departsFrom, arrivesTo));
			}
		}
		return stops;
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
