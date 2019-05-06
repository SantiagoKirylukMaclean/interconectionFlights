package eu.app.interconectionFlights.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import eu.app.interconectionFlights.model.Route;

@Repository
public interface RoutesRepository {

	public List<Route> getAll();



}
