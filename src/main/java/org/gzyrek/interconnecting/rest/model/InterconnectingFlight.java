package org.gzyrek.interconnecting.rest.model;

import java.util.List;

public class InterconnectingFlight {
	
	private Integer stops;
	
	private List<InterconnectingFlightDetail> legs;
	
	public Integer getStops() {
		return stops;
	}
	public void setStops(Integer stops) {
		this.stops = stops;
	}
	public List<InterconnectingFlightDetail> getLegs() {
		return legs;
	}
	public void setLegs(List<InterconnectingFlightDetail> legs) {
		this.legs = legs;
	}
	@Override
	public String toString() {
		return "InterconnectingFlight [stops=" + stops + ", legs=" + legs + "]";
	}
	
	
	
}
