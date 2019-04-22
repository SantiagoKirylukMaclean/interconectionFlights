package eu.app.interconectionFlights.service.impl;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.app.interconectionFlights.model.DayFlight;
import eu.app.interconectionFlights.model.FlightSchedule;
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
		
		int month = departureDateTime.getMonthValue();
		int year = departureDateTime.getYear();
		int day = departureDateTime.getDayOfMonth();
		Schedule schedule = scheduleRepository.get(departure, arrival, month, year);
		List<DayFlight> schedule2 = schedule.getDays().stream().filter(d -> d.getDay() == day).collect(Collectors.toList());
		return null;
		
	}
	

	


}
