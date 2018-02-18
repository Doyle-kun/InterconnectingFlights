package org.gzyrek.interconnecting.rest.controller;

//import javax.validation.constraints.NotNull;
import javax.ws.rs.QueryParam;

public class InterconnectingFlightsParameters {

    @QueryParam(value = "departure")
   // @NotNull(message = "Parameter departure is required and cannot be empty.")
 //   @ApiParam(value = "Minimalne saldo:")
	private String departure;
    @QueryParam(value = "departureDateTime")
  //  @NotNull(message = "Parameter departureDateTime is required and cannot be empty.")
	private String departureDateTime;
    @QueryParam(value = "arrival")
  //  @NotNull(message = "Parameter arrival is required and cannot be empty.")
	private String arrival;
    @QueryParam(value = "arrivalDateTime")
   // @NotNull(message = "Parameter arrivalDateTime is required and cannot be empty.")
	private String arrivalDateTime;
	
	public String getDeparture() {
		return departure;
	}
	public void setDeparture(String departure) {
		this.departure = departure;
	}
	public String getDepartureDateTime() {
		return departureDateTime;
	}
	public void setDepartureDateTime(String departureDateTime) {
		this.departureDateTime = departureDateTime;
	}
	public String getArrival() {
		return arrival;
	}
	public void setArrival(String arrival) {
		this.arrival = arrival;
	}
	public String getArrivalDateTime() {
		return arrivalDateTime;
	}
	public void setArrivalDateTime(String arrivalDateTime) {
		this.arrivalDateTime = arrivalDateTime;
	}
	@Override
	public String toString() {
		return "InterconnectingFlightsParameters [departure=" + departure + ", departureDateTime=" + departureDateTime
				+ ", arrival=" + arrival + ", arrivalDateTime=" + arrivalDateTime + "]";
	}
	
	
	
}
