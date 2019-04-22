package eu.app.interconectionFlights.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.app.interconectionFlights.model.FlightSchedule;
import eu.app.interconectionFlights.model.Route;
import eu.app.interconectionFlights.model.Schedule;
import eu.app.interconectionFlights.service.FlightService;

@RestController
public class FlightController {

	@Autowired
	private FlightService flightService;

	@RequestMapping("/api/v1/routes/")
	public List<Route> findAllRoutes() throws Exception {
		return flightService.getAllRoutes();
	}

	/*
	 * @RequestMapping("/api/v1/interconnections") public Schedule
	 * findFlights(@RequestParam("departure") String departure,
	 * 
	 * @RequestParam("arrival") String arrival, @RequestParam("year") int
	 * year, @RequestParam("month") int month) { System.out.println("departure: " +
	 * departure); System.out.println("arrival: " + arrival); return
	 * flightService.getSchedule(departure, arrival, year, month); }
	 */

	@RequestMapping("/api/v1/interconnections")
	public List<FlightSchedule> findFlights(@RequestParam("departure") String departure,
			@RequestParam("arrival") String arrival,
			@RequestParam("departureDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureDateTime,
			@RequestParam("arrivalDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime arrivalDateTime) {
		System.out.println("departure: " + departure);
		System.out.println("arrival: " + arrival);
		System.out.println("departureDateTime" + departureDateTime);
		System.out.println("arrivalDateTime: " + arrivalDateTime);
		return flightService.getFlights(departure, arrival, departureDateTime, arrivalDateTime);
	}
}
