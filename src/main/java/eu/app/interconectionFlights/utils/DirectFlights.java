package eu.app.interconectionFlights.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.app.interconectionFlights.model.DayFlight;
import eu.app.interconectionFlights.model.Flight;
import eu.app.interconectionFlights.model.FlightSchedule;
import eu.app.interconectionFlights.model.Leg;
import eu.app.interconectionFlights.model.Schedule;

public class DirectFlights {
	private static Logger log = LogManager.getLogger(DirectFlights.class);
	 private static final DirectFlights INSTANCE = new DirectFlights();

    private DirectFlights() {
    }

    public static DirectFlights getInstance() {
        return INSTANCE;
    }
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

		Utility utility = Utility.getInstance();

		int departureMonth = departureDateTime.getMonthValue();
		int departureYear = departureDateTime.getYear();
		int departureDay = departureDateTime.getDayOfMonth();
		int arrivalMonth = arrivalDateTime.getMonthValue();
		int arrivalYear = arrivalDateTime.getYear();
		int arrivalDay = arrivalDateTime.getDayOfMonth();

		List<DayFlight> dayFlightSchedule = schedule.getDays().stream().filter(d -> d.getDay() == departureDay)
				.collect(Collectors.toList());
		List<FlightSchedule> flightsAviable = new ArrayList<FlightSchedule>();
		List<Leg> legs = new ArrayList<Leg>();
		

		if (!dayFlightSchedule.isEmpty()) {
			for (DayFlight DayFlight : dayFlightSchedule) {
				if (!DayFlight.getFlights().isEmpty()) {
					log.info(String.format("we have %s flight on day %s", DayFlight.getFlights().size(),
							departureDay));
					for (Flight Flight : DayFlight.getFlights()) {

						if (departureDateTime.isBefore(utility.createLocalDateTime(departureYear, departureMonth, departureDay,Flight.getDepartureTime()))
							&& arrivalDateTime.isAfter(utility.createLocalDateTime(arrivalYear, arrivalMonth,arrivalDay, Flight.getArrivalTime()))) {
							flightsAviable.add(utility.createFlightResult(departure, arrival, 
																		  utility.createLocalDateTime(departureYear, departureMonth, departureDay,Flight.getDepartureTime()),
																		  utility.createLocalDateTime(departureYear, departureMonth, departureDay,Flight.getArrivalTime())));
						}
					}
				}
			}
		}
		return flightsAviable;

	}

}
