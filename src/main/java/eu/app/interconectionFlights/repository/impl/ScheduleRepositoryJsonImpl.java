package eu.app.interconectionFlights.repository.impl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import eu.app.interconectionFlights.model.Schedule;
import eu.app.interconectionFlights.repository.ScheduleRepository;

@Component("ScheduleRepositoryJsonImpl")

public class ScheduleRepositoryJsonImpl implements ScheduleRepository {
	private String ORDERS_FILE = "C:\\Desarrollo\\workspace_eclipse\\interconectionFlights\\src\\test\\resources\\schedule.json";
	private Schedule response;

	public Schedule get(String from, String to, int month, int year) {
		JsonReader reader = null;
		final Type REVIEW_TYPE = new TypeToken<List<Schedule>>() {
		}.getType();
		Gson gson = new Gson();
		
		try {
			reader = new JsonReader(new FileReader(ORDERS_FILE));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Schedule data = gson.fromJson(reader, Schedule.class); // contains the whole reviews list
		// data.toScreen(); // prints to screen some values
		return data;
	}
}
