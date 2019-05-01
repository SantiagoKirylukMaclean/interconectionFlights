package eu.app.interconectionFlights.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.app.interconectionFlights.model.FlightSchedule;
import eu.app.interconectionFlights.model.Route;
import eu.app.interconectionFlights.service.FlightService;



@RestController
public class FlightController {
	private static Logger log = LogManager.getLogger(FlightController.class);

	@Autowired
	private FlightService flightService;

    /**
     * Return a list of availables routes
     * 
     * @param 
     * @return List<Rute>
     */
	@RequestMapping("/api/v1/routes/")
	public List<Route> findAllRoutes() throws Exception {
		return flightService.getAllRoutes();
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
	@RequestMapping("/api/v1/interconnections")
	public ResponseEntity<?> findFlights(@RequestParam("departure") String departure,
			@RequestParam("arrival") String arrival,
			@RequestParam("departureDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureDateTime,
			@RequestParam("arrivalDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime arrivalDateTime) {
		
		 if (arrivalDateTime.isBefore(departureDateTime)) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	        }

	        if (departureDateTime.isBefore(LocalDateTime.now())) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	        }

	        if (departureDateTime.plusHours(2L).isAfter(arrivalDateTime)) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	        }

	        log.info(String.format("Searching flights from %s to %s - Departure %s - Arrival %s", departure, arrival,
	                departureDateTime, arrivalDateTime));
	        
	        List<FlightSchedule> flightSchedule = flightService.getFlights(departure, arrival, departureDateTime, arrivalDateTime);
	        
	        if (flightSchedule.isEmpty()) {
		        log.error(String.format("Flights from %s to %s - Departure %s - Arrival %s, are empty", departure, arrival,
		                departureDateTime, arrivalDateTime));
	            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	        }

	        return ResponseEntity.status(HttpStatus.OK).body(flightSchedule);
	}
}
