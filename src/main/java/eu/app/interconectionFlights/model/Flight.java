package eu.app.interconectionFlights.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Flight implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String number;
    private String departureTime;
    private String arrivalTime;
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
	public String getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public Flight(@JsonProperty("number") String number, @JsonProperty("departureTime") String departureTime, @JsonProperty("arrivalTime") String arrivalTime) {
		super();
		this.number = number;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
	}
    
    

}
