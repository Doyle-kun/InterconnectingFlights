package org.gzyrek.interconnecting.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)

@JsonPropertyOrder({

    "airportFrom",

    "airportTo",

    "connectingAirport",

    "newRoute",

    "seasonalRoute",
    
    "group"

})
public class Route {
		
		    @JsonProperty("airportFrom")
			private String airportFrom;
		    @JsonProperty("airportTo")
			private String airportTo;
		    @JsonProperty("connectingAirport")
			private String connectingAirport;
		    @JsonProperty("newRoute")
			private Boolean newRoute;
		    @JsonProperty("seasonalRoute")
			private Boolean seasonalRoute;
		    @JsonProperty("group")
		    private String group;
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
			public Boolean getNewRoute() {
				return newRoute;
			}
			public void setNewRoute(Boolean newRoute) {
				this.newRoute = newRoute;
			}
			public Boolean getSeasonalRoute() {
				return seasonalRoute;
			}
			public void setSeasonalRoute(Boolean seasonalRoute) {
				this.seasonalRoute = seasonalRoute;
			}
			public String getGroup() {
				return group;
			}
			public void setGroup(String group) {
				this.group = group;
			}
	@Override
	public String toString() {
		return "Route [airportFrom=" + airportFrom + ", airportTo=" + airportTo + ", newRoute=" + newRoute
				+ ", seasonalRoute=" + seasonalRoute + "]";
	}
	
}
