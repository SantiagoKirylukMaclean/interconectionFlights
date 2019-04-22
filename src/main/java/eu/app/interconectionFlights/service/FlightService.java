package eu.app.interconectionFlights.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import eu.app.interconectionFlights.model.FlightSchedule;
import eu.app.interconectionFlights.model.Route;
import eu.app.interconectionFlights.model.Schedule;

@Service
public interface FlightService {
	

	public List<Route> getAllRoutes();

	public List<FlightSchedule> getFlights(String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime);
	

}
