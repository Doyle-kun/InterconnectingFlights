package org.gzyrek.interconnecting.rest.model;

import java.util.Map;

public class InterconnectingSchedule {

    private Map<String, Schedule> arrivalSchedules;
    private Map<String, Schedule> departingSchedules;

    public Map<String, Schedule> getArrivalSchedules() {
        return arrivalSchedules;
    }

    public void setArrivalSchedules(Map<String, Schedule> arrivalSchedules) {
        this.arrivalSchedules = arrivalSchedules;
    }

    public Map<String, Schedule> getDepartingSchedules() {
        return departingSchedules;
    }

    public void setDepartingSchedules(Map<String, Schedule> departingSchedules) {
        this.departingSchedules = departingSchedules;
    }

    @Override
    public String toString() {
        return "InterconnectingSchedules [arrivalSchedules=" + arrivalSchedules + ", departingSchedules="
                + departingSchedules + "]";
    }

}
