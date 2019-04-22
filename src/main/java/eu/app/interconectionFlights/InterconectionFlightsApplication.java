package eu.app.interconectionFlights;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class InterconectionFlightsApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterconectionFlightsApplication.class, args);
	}

}
