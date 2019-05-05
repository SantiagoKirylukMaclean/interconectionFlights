package eu.app.interconectionFlights.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import eu.app.interconectionFlights.controller.FlightController;
import eu.app.interconectionFlights.model.DayFlight;
import eu.app.interconectionFlights.model.Flight;
import eu.app.interconectionFlights.model.FlightSchedule;
import eu.app.interconectionFlights.model.Leg;
import eu.app.interconectionFlights.model.Schedule;
import eu.app.interconectionFlights.repository.ScheduleRepository;
import eu.app.interconectionFlights.utils.Utility;

public class DirectFlights {
	private static Logger log = LogManager.getLogger(DirectFlights.class);

	/**
	 * Return a list of availables Flights
	 * 
	 * @param schedule
	 * @param departure
	 * @param arrival
	 * @param departureDateTime
	 * @param arrivalDateTime
	 * @return List<FlightSchedule>
	 */
	public List<FlightSchedule> getDirectConnections(Schedule schedule, String departure, String arrival,
			LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {

		Utility utils = Utility.getInstance();

		int departureMonth = departureDateTime.getMonthValue();
		int departureYear = departureDateTime.getYear();
		int departureDay = departureDateTime.getDayOfMonth();
		int arrivalMonth = arrivalDateTime.getMonthValue();
		int arrivalYear = arrivalDateTime.getYear();
		int arrivalDay = arrivalDateTime.getDayOfMonth();

		List<DayFlight> dayFlightSchedule = schedule.getDays().stream().filter(d -> d.getDay() == departureDay)
				.collect(Collectors.toList());
		List<FlightSchedule> flightsAviable = new ArrayList<FlightSchedule>();
		FlightSchedule flightSchedule = new FlightSchedule();
		List<Leg> legs = new ArrayList<Leg>();
		flightSchedule.setStops(0);

		if (!dayFlightSchedule.isEmpty()) {
			for (DayFlight DayFlight : dayFlightSchedule) {
				if (!DayFlight.getFlights().isEmpty()) {
					log.info(String.format("we have %s flight on day %s", DayFlight.getFlights().size(),
							departureDay));
					for (Flight Flight : DayFlight.getFlights()) {
						if (departureDateTime.isBefore(utils.createLocalDateTime(departureYear, departureMonth, departureDay,Flight.getDepartureTime()))
							&& arrivalDateTime.isAfter(utils.createLocalDateTime(arrivalYear, arrivalMonth,arrivalDay, Flight.getArrivalTime()))) {
							Leg leg = new Leg(departure, arrival, 
											utils.createLocalDateTime(departureYear, departureMonth, departureDay,Flight.getDepartureTime()).toString(),
											utils.createLocalDateTime(departureYear, departureMonth, departureDay,Flight.getArrivalTime()).toString());
							legs.add(leg);
						}
					}
				}
			}
			flightSchedule.setLegs(legs);
			flightsAviable.add(flightSchedule);
		}
		return flightsAviable;

	}

}
