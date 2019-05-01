package eu.app.interconectionFlights.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Route implements Serializable {
	private static final long serialVersionUID = 1L;

	private String airportFrom;
	private String airportTo;
	private String connectingAirport;
	private boolean newRoute;
	private boolean seasonalRoute;
	private String operator;
	private String group;
	private String carrierCode;


	public Route(@JsonProperty("airportFrom") String airportFrom, @JsonProperty("airportTo") String airportTo,
			@JsonProperty("connectingAirport") String connectingAirport, @JsonProperty("newRoute") boolean newRoute,
			@JsonProperty("seasonalRoute") boolean seasonalRoute, @JsonProperty("operator") String operator,
			@JsonProperty("group") String group, @JsonProperty("carrierCode") String carrierCode) {
		super();
		this.airportFrom = airportFrom;
		this.airportTo = airportTo;
		this.connectingAirport = connectingAirport;
		this.newRoute = newRoute;
		this.seasonalRoute = seasonalRoute;
		this.operator = operator;
		this.group = group;
		this.carrierCode = carrierCode;
	}

	public String getAirportFrom() {
		return airportFrom;
	}

	public void setAirportFrom(String airportFrom) {
		this.airportFrom = airportFrom;
	}

	public String getAirportTo() {
		return airportTo;
	}

	public void setAirportTo(String airportTo) {
		this.airportTo = airportTo;
	}



	public String getConnectingAirport() {
		return connectingAirport;
	}

	public void setConnectingAirport(String connectingAirport) {
		this.connectingAirport = connectingAirport;
	}

	public boolean isNewRoute() {
		return newRoute;
	}

	public void setNewRoute(boolean newRoute) {
		this.newRoute = newRoute;
	}

	public boolean isSeasonalRoute() {
		return seasonalRoute;
	}

	public void setSeasonalRoute(boolean seasonalRoute) {
		this.seasonalRoute = seasonalRoute;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	@Override
	public String toString() {
		return "Route [airportFrom=" + airportFrom + ", airportTo=" + airportTo + ", connectingAirport="
				+ connectingAirport + ", newRoute=" + newRoute + ", seasonalRoute=" + seasonalRoute + ", operator="
				+ operator + ", group=" + group + ", carrierCode=" + carrierCode + "]";
	}

}
