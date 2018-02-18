package org.gzyrek.interconnecting.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)

@JsonPropertyOrder({

    "number",

    "departureTime",
    
    "arrivalTime"
})
public class Flight {
@JsonProperty("number")
private long number;
@JsonProperty("departureTime")
private String departureTime;
@JsonProperty("arrivalTime")
private String arrivalTime;
public long getNumber() {
	return number;
}
public void setNumber(long number) {
	this.number = number;
}
public String getDepartureTime() {
	return departureTime;
}
public void setDepartureTime(String departureTime) {
	this.departureTime = departureTime;
}
public String getArrivalTime() {
	return arrivalTime;
}
public void setArrivalTime(String arrivalTime) {
	this.arrivalTime = arrivalTime;
}
@Override
public String toString() {
	return "Flight [number=" + number + ", departureTime=" + departureTime + ", arrivalTime=" + arrivalTime + "]";
}

}
