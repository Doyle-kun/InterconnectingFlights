package org.gzyrek.interconnecting.rest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)

@JsonPropertyOrder({

    "day",

    "flights"
})
public class Day {
	@JsonProperty("day")
	private Integer day;
	@JsonProperty("flights")
	private List<Flight> flightList;

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public List<Flight> getFlightList() {
		return flightList;
	}

	public void setFlightList(List<Flight> flightList) {
		this.flightList = flightList;
	}

	@Override
	public String toString() {
		return "Day [day=" + day + ", flightList=" + flightList + "]";
	}
	
}
