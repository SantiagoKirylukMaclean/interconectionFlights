package eu.app.interconectionFlights.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.app.interconectionFlights.model.FlightSchedule;
import eu.app.interconectionFlights.model.Route;
import eu.app.interconectionFlights.model.Schedule;
import eu.app.interconectionFlights.repository.RoutesRepository;
import eu.app.interconectionFlights.repository.ScheduleRepository;
import eu.app.interconectionFlights.service.FlightService;
import eu.app.interconectionFlights.utils.DirectFlights;
import eu.app.interconectionFlights.utils.NonDirectFlights;
import eu.app.interconectionFlights.utils.Utility;

@Service
public class FlightServiceImpl implements FlightService {
	private static Logger log = LogManager.getLogger(FlightServiceImpl.class);

	@Autowired
	private RoutesRepository routesRepository;

	@Autowired
	private ScheduleRepository scheduleRepository;

    /**
     * Return a list of availables routes
     * 
     * @param 
     * @return List<Rute>
     */
	public List<Route> getAllRoutes() {
		return routesRepository.getAll();
	}

    /**
     * Return a list of availables Flights
     * 
     * @param departure
     * @param arrival
     * @param departureDateTime
     * @param arrivalDateTime
     * @return List<FlightSchedule>
     */
	public List<FlightSchedule> getFlights(String departure, String arrival, LocalDateTime departureDateTime,
			LocalDateTime arrivalDateTime) {
		DirectFlights directFlights = DirectFlights.getInstance();
		NonDirectFlights nonDirectFlights = NonDirectFlights.getInstance();
		
		List<FlightSchedule> flightSchedule = new ArrayList<FlightSchedule>();

		Schedule schedule = scheduleRepository.get(departure, arrival, departureDateTime.getMonthValue(),
				arrivalDateTime.getYear());
		
		if (schedule != null) {
			flightSchedule.addAll(directFlights.getDirectConnections(schedule, departure, arrival, departureDateTime,
					arrivalDateTime));
			flightSchedule.addAll(nonDirectFlights.getNonDirectConnections(scheduleRepository, routesRepository,
					departure, arrival, departureDateTime, arrivalDateTime));
		}
		
		return flightSchedule;
	}

}
