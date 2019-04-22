package eu.app.interconectionFlights.repository.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;

import eu.app.interconectionFlights.model.FlightSchedule;
import eu.app.interconectionFlights.model.Route;
import eu.app.interconectionFlights.repository.RoutesRepository;

@Repository
@CacheConfig(cacheNames = "APIRoutesCache")
public class RoutesRepositoryImpl implements RoutesRepository {
	
	@Value("${url.api.routes}")
	String APIRoutesUrl;
	
	@Cacheable
    public List<Route> getAll() {
        URI uri = null;
        try {
            uri = new URI(APIRoutesUrl);
            return getRoutes(uri);
        } catch (URISyntaxException e) {
            //LOGGER.error(String.format("URISyntaxException: %s", e.getMessage()));
            return Collections.emptyList();
        }
    }
	
    private List<Route> getRoutes(URI uri) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<List<Route>> response = restTemplate.exchange(uri, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Route>>() {
                    });
            if (response.getStatusCode().is2xxSuccessful()) {
                //LOGGER.info(String.format("%d routes has been found!", response.getBody().size()));
                Predicate<Route> connectingAirportIsNull = p -> Strings.isNullOrEmpty(p.getConnectingAirport());
                return response.getBody().stream().filter(connectingAirportIsNull).collect(Collectors.toList());
            }
        } catch (final HttpClientErrorException e) {
            //LOGGER.error(String.format("%s: %s", e.getStatusCode(), e.getResponseBodyAsString()));
        }
        return Collections.emptyList();
    }



}
