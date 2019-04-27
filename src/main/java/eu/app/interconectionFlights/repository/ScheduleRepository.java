package eu.app.interconectionFlights.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import eu.app.interconectionFlights.model.Schedule;

@Repository
public interface ScheduleRepository {
	
	public Schedule get(String from, String to, int month, int year);

}
