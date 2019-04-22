package eu.app.interconectionFlights.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	*/
	public FlightSchedule getSchedule(String departure, String arrival, int year, int month) {
		FlightSchedule flightSchedule = getDirectConnections(departure, arrival, year, month);
		
		return flightSchedule;
	}
	
	//TODO:sacarlo de aca
	
	public FlightSchedule getDirectConnections(String departure, String arrival, int year, int month) {
		
		return scheduleRepository.get(departure, arrival, month, year);
		
	}

}
