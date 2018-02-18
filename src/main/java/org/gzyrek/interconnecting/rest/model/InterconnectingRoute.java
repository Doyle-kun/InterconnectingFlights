package org.gzyrek.interconnecting.rest.model;

import java.util.List;

public class InterconnectingRoute {

	private List<Route> arrivalRouteList;
	
	private List<Route> departingRouteList;

	public List<Route> getArrivalRouteList() {
		return arrivalRouteList;
	}

	public void setArrivalRouteList(List<Route> arrivalRouteList) {
		this.arrivalRouteList = arrivalRouteList;
	}

	public List<Route> getDepartingRouteList() {
		return departingRouteList;
	}

	public void setDepartingRouteList(List<Route> departingRouteList) {
		this.departingRouteList = departingRouteList;
	}

	@Override
	public String toString() {
		return "InterconnectingRoute [arrivalRouteList=" + arrivalRouteList + ", departingRouteList="
				+ departingRouteList + "]";
	}
	
	
	
	
}
