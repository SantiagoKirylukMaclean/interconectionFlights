package eu.app.interconectionFlights.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.app.interconectionFlights.model.DayFlight;
import eu.app.interconectionFlights.model.Flight;
import eu.app.interconectionFlights.model.FlightSchedule;
import eu.app.interconectionFlights.model.Leg;
import eu.app.interconectionFlights.model.Schedule;
import eu.app.interconectionFlights.repository.ScheduleRepository;

public class Utility {
	private static Logger log = LogManager.getLogger(Utility.class);

	private static final Utility INSTANCE = new Utility();

	private Utility() {
	}

	public static Utility getInstance() {
		return INSTANCE;
	}

	/**
	 * Return a list of availables flights
	 * 
	 * @param from
	 * @param to
	 * @param dateTime
	 * @return
	 */
	public List<FlightSchedule> getFlightsAvailables(ScheduleRepository scheduleFinderService, String from, String to,
			LocalDateTime startDateTime, LocalDateTime endDateTime) {
		List<FlightSchedule> flightsAvailables = new ArrayList<FlightSchedule>();
		int day = 0;
		int month = 0;
		int year = startDateTime.getYear();
		LocalDateTime departureDateTime, arrivalDateTime;
		FlightSchedule flightResult;
		List<Schedule> schedules = getSchedules(scheduleFinderService, from, to, startDateTime, endDateTime);
		if (!schedules.isEmpty()) {
			List<DayFlight> flightsDays = schedules.stream().flatMap(schedule -> schedule.getDays().stream()).collect(Collectors.toList());
			List<DayFlight> flightsOfDay = flightsDays.stream().filter(getDay(startDateTime)).collect(Collectors.toList());
			List<Flight> flights = flightsOfDay.stream().flatMap(flight -> flight.getFlights().stream()).collect(Collectors.toList());
			month = startDateTime.getMonthValue();
			day = startDateTime.getDayOfMonth();
			for (Flight flight : flights) {
				departureDateTime = createLocalDateTime(year, month, day, flight.getDepartureTime());
				arrivalDateTime = createLocalDateTime(year, month, day, flight.getArrivalTime());
				if (validFlight(startDateTime, endDateTime, departureDateTime, arrivalDateTime)) {
					log.info(String.format("Create Fligt Result: from %s, to %s, departureTime %s, arrivalTime %s",
							from, to, departureDateTime, arrivalDateTime));
					flightResult = createFlightResult(from, to, departureDateTime, arrivalDateTime);
					flightsAvailables.add(flightResult);
				}
			}

		}
		return flightsAvailables;
	}
    
    
	/**
	 * Scheduled flights list
	 * 
	 * @param from
	 * @param to
	 * @param startDateTime
	 * @param endDateTime
	 * @return
	 */
	public List<Schedule> getSchedules(ScheduleRepository scheduleFinderService, String from, String to,
			LocalDateTime startDateTime, LocalDateTime endDateTime) {
		List<Schedule> schedules = new ArrayList<Schedule>();
		Schedule schedule = null;
		int startYear = startDateTime.getYear();
		int startMonth = startDateTime.getMonthValue();
		int endYear = endDateTime.getYear();
		int endMonth = endDateTime.getMonthValue();
		int month = startMonth;
		int year = startYear;
		if (endYear > startYear) {
			LocalDate endDate = LocalDate.of(endYear, Month.of(endMonth), endDateTime.getDayOfMonth()), date;
			while ((year <= endYear)) {
				schedule = scheduleFinderService.get(from, to, month, year);
				if (schedule != null) {
					schedules.add(schedule);
				}
				month++;
				if (month > 12) {
					month = 1;
					year++;
				}
				date = LocalDate.of(year, Month.of(month), endDateTime.getDayOfMonth());
				if (date.isAfter(endDate)) {
					break;
				}
			}
		} else {
			for (month = startMonth; month <= endMonth; month++) {
				schedule = scheduleFinderService.get(from, to, month, startYear);
				if (schedule != null) {
					schedules.add(schedule);
				}
			}
		}
		return schedules;
	}

	/**
	 * Return a LocalDateTime
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @param time
	 * @return
	 */
	public LocalDateTime createLocalDateTime(int year, int month, int day, String time) {
		int hour = Integer.valueOf(time.split(":")[0]);
		int minute = Integer.valueOf(time.split(":")[1]);
		return LocalDateTime.of(year, month, day, hour, minute);
	}

	/**
	 * Check if this is a valid flight
	 * 
	 * @param startDateTime
	 * @param endDateTime
	 * @param departureDateTime
	 * @param arrivalDateTime
	 * @return
	 */
	public boolean validFlight(LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime departureDateTime,
			LocalDateTime arrivalDateTime) {
		if (departureDateTime.isBefore(startDateTime)) {
			return false;
		}
		if (arrivalDateTime.isAfter(endDateTime)) {
			return false;
		}
		return true;
	}

	/**
	 * Create a flight result
	 * 
	 * @param from
	 * @param to
	 * @param departureDateTime
	 * @param arrivalDateTime
	 * @return
	 */
	public FlightSchedule createFlightResult(String from, String to, LocalDateTime departureDateTime,
			LocalDateTime arrivalDateTime) {
		FlightSchedule flightResult = new FlightSchedule();
		flightResult.setStops(0);
		List<Leg> legs = new ArrayList<Leg>();
		legs.add(new Leg(from, to, departureDateTime.toString(), arrivalDateTime.toString()));
		flightResult.setLegs(legs);
		return flightResult;
	}

	/**
	 * Create a FlightResult from a pair of Leg
	 * 
	 * @param departureToStop
	 * @param stopToArrival
	 * @return
	 */
	public FlightSchedule createFlightResult(Leg departureToStop, Leg stopToArrival) {
		FlightSchedule flightResult = new FlightSchedule();
		flightResult.setStops(1);
		List<Leg> legs = new ArrayList<Leg>();
		legs.add(departureToStop);
		legs.add(stopToArrival);
		flightResult.setLegs(legs);
		return flightResult;
	}
	
    /**
     * return all flight of particular day
     * 
     * @param startDateTime
     * @return Predicate<DayFlight>
     */
    private Predicate<DayFlight> getDay(LocalDateTime startDateTime) {
        return dayFlight -> dayFlight.getDay() == startDateTime.getDayOfMonth();
    }
}
