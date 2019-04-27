package eu.app.interconectionFlights.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Leg {

	private String departureAirport;
	private String arrivalAirport;
	private String departureDateTime;
	private String arrivalDateTime;

	public String getDepartureAirport() {
		return departureAirport;
	}

	public void setDepartureAirport(String departureAirport) {
		this.departureAirport = departureAirport;
	}

	public String getArrivalAirport() {
		return arrivalAirport;
	}

	public void setArrivalAirport(String arrivalAirport) {
		this.arrivalAirport = arrivalAirport;
	}

	public String getDepartureDateTime() {
		return departureDateTime;
	}

	public void setDepartureDateTime(String departureDateTime) {
		this.departureDateTime = departureDateTime;
	}

	public String getArrivalDateTime() {
		return arrivalDateTime;
	}

	public void setArrivalDateTime(String arrivalDateTime) {
		this.arrivalDateTime = arrivalDateTime;
	}

	public Leg(@JsonProperty("departureAirport") String departureAirport, @JsonProperty("arrivalAirport") String arrivalAirport,
			@JsonProperty("departureDateTime") String departureDateTime, @JsonProperty("arrivalDateTime") String arrivalDateTime) {
		super();
		this.departureAirport = departureAirport;
		this.arrivalAirport = arrivalAirport;
		this.departureDateTime = departureDateTime;
		this.arrivalDateTime = arrivalDateTime;
	}
	
	

}
