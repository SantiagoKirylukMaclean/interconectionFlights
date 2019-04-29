package eu.app.interconectionFlights.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import eu.app.interconectionFlights.model.DayFlight;
import eu.app.interconectionFlights.model.Flight;
import eu.app.interconectionFlights.model.FlightSchedule;
import eu.app.interconectionFlights.model.Leg;
import eu.app.interconectionFlights.model.Route;
import eu.app.interconectionFlights.model.Schedule;
import eu.app.interconectionFlights.repository.RoutesRepository;
import eu.app.interconectionFlights.repository.ScheduleRepository;
import eu.app.interconectionFlights.service.FlightService;


@Service
public class FlightServiceImpl implements FlightService{
	
	@Autowired
	private RoutesRepository routesRepository;
	
	@Autowired
	@Qualifier(value = "ScheduleRepositoryImpl")
	private ScheduleRepository scheduleRepository;
	
	public List<Route> getAllRoutes() {
		return routesRepository.getAll();
	}
	
	/*
	public Schedule getSchedule(String departure, String arrival, int year, int month) {
		return scheduleRepository.get(departure, arrival, month, year);
	}
	
	public List<FlightSchedule> getSchedule(String departure, String arrival, int year, int month) {
		 List<FlightSchedule> flightSchedule = getDirectConnections(departure, arrival, year, month);
		
		return flightSchedule;
	}
	*/
	public List<FlightSchedule> getFlights(String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
		 
		List<FlightSchedule> flightSchedule = getDirectConnections(departure, arrival, departureDateTime, arrivalDateTime);
		
		return flightSchedule;
	}
	
	//TODO:sacarlo de aca
	
	public List<FlightSchedule> getDirectConnections(String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
		
		int departureMonth = departureDateTime.getMonthValue();
		int departureYear = departureDateTime.getYear();
		int departureDay = departureDateTime.getDayOfMonth();
		int departureHour = departureDateTime.getHour();
		int departureMinute = departureDateTime.getMinute();
		int arrivalHour = arrivalDateTime.getHour();
		int arrivalMinute = arrivalDateTime.getMinute();
		boolean test = departureDateTime.isBefore(arrivalDateTime); 
		Schedule schedule = scheduleRepository.get(departure, arrival, departureMonth, departureYear);
		List<DayFlight> schedule2 = schedule.getDays().stream().filter(d -> d.getDay() == departureDay).collect(Collectors.toList());
		List<FlightSchedule> flightsAviable = new ArrayList<FlightSchedule>();
		FlightSchedule flightSchedule = new FlightSchedule();
		List<Leg> legs = new ArrayList<Leg>();
		flightSchedule.setStops(0);
		for (DayFlight DayFlight : schedule2) {
			for (Flight Flight : DayFlight.getFlights()) {
		
				Leg leg = new Leg(departure, arrival, Flight.getDepartureTime(), Flight.getArrivalTime());
				legs.add(leg);
			}
		}
		flightSchedule.setLegs(legs);
		flightsAviable.add(flightSchedule);
		return flightsAviable;
		
	}
	

	


}
