package eu.app.interconectionFlights.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Schedule implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int month;
    private List<DayFlight> days;
    //private Collection<DayFlight> days;
    
    
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public List<DayFlight> getDays() {
		return days;
	}
	public void setDays(List<DayFlight> days) {
		this.days = days;
	}
	
	public Schedule(@JsonProperty("month") int month, @JsonProperty("days")  List<DayFlight> days) {
		super();
		this.month = month;
		this.days = days;
	}
    
    
}
