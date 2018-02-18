package org.gzyrek.interconnecting.rest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)

@JsonPropertyOrder({

    "month",

    "days"
})
public class Schedule {
	
	@JsonProperty("month")
	private Integer month;
	@JsonProperty("days")
	private List<Day> dayList;
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public List<Day> getDayList() {
		return dayList;
	}
	public void setDayList(List<Day> dayList) {
		this.dayList = dayList;
	}
	@Override
	public String toString() {
		return "Schedule [month=" + month + ", dayList=" + dayList + "]";
	}

	
}
