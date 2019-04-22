package eu.app.interconectionFlights.service;

import java.util.List;

import org.springframework.stereotype.Service;

import eu.app.interconectionFlights.model.FlightSchedule;
import eu.app.interconectionFlights.model.Route;
import eu.app.interconectionFlights.model.Schedule;

@Service
public interface FlightService {
	

	public List<Route> getAllRoutes();

	public FlightSchedule getSchedule(String departure, String arrival, int year, int month); 
	

}