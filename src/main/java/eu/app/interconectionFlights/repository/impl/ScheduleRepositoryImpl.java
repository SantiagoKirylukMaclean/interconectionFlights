package eu.app.interconectionFlights.repository.impl;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import eu.app.interconectionFlights.model.Schedule;
import eu.app.interconectionFlights.repository.ScheduleRepository;


@Repository
@CacheConfig(cacheNames = "APIRoutesCache")
public class ScheduleRepositoryImpl implements ScheduleRepository{
	
	@Value("${url.api.schedules}")
    String APIRoutesUrl;

    
    @Cacheable
    public Schedule get(String from, String to, int month, int year) {
        URI uri = null;
        StringBuilder fullURL = new StringBuilder().append(APIRoutesUrl).append("/").append(from).append("/")
                .append(to).append("/years/").append(year).append("/months/").append(month);
        System.out.println(fullURL);
        //LOGGER.info(String.format("Looking for flights from %s to %s on %d/%d ...", from, to, month, year));
        try {
            uri = new URI(fullURL.toString());
            //LOGGER.info(String.format("URL Request => %s", fullURL.toString()));
            return getSchedule(uri, from, to, month, year);
        } catch (URISyntaxException e) {
           //LOGGER.error(String.format("URISyntaxException: %s", e.getMessage()));
        }
        return null;
    }

    private Schedule getSchedule(URI uri, String from, String to, int month, int year) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Schedule> response = restTemplate.exchange(uri, HttpMethod.GET, null,
                    new ParameterizedTypeReference<Schedule>() {
                    });
            if (response.getStatusCode().is2xxSuccessful()) {
                //LOGGER.info(String.format("Flights from %s to %s on %d/%d has been found!", from, to, month, year));
                return response.getBody();
            }
        } catch (final HttpClientErrorException e) {
            //LOGGER.error(String.format("%s: %s", e.getStatusCode(), e.getResponseBodyAsString()));
        }
        return null;
    }

}
