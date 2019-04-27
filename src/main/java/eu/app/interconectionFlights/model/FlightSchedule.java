package eu.app.interconectionFlights.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FlightSchedule {
	
	private int stops;
	private List<Leg> legs;
	
	
	public int getStops() {
		return stops;
	}
	public void setStops(int stops) {
		this.stops = stops;
	}
	public List<Leg> getLegs() {
		return legs;
	}
	public void setLegs(List<Leg> legs) {
		this.legs = legs;
	}
	
	public FlightSchedule(@JsonProperty("stops") int stops, @JsonProperty("legs") List<Leg> legs) {
		super();
		this.stops = stops;
		this.legs = legs;
	}
	
	public FlightSchedule() {
		super();
	}
	


	
	

}
