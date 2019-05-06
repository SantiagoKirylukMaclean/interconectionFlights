package eu.app.interconectionFlights.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class DayFlight implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int day;
    private List<Flight> flights;

    
    
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public List<Flight> getFlights() {
		return flights;
	}
	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}
	public DayFlight(@JsonProperty("day") int day, @JsonProperty("flights") List<Flight> flights) {
		super();
		this.day = day;
		this.flights = flights;
	}
    
    

}
