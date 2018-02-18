package org.gzyrek.interconnecting.rest.client;

import java.time.LocalDateTime;
import java.util.List;

import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.GenericType;


import org.gzyrek.interconnecting.rest.controller.InterconnectingFlightsParameters;
import org.gzyrek.interconnecting.rest.model.Route;
import org.gzyrek.interconnecting.rest.model.Schedule;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.springframework.stereotype.Component;


@Component
public class ExternalRestConsumer {

    public static ResteasyClient createClient() {
        ResteasyClient client = new ResteasyClientBuilder().connectionPoolSize(200).build();
        return client;
    }

    public List<Route> getRoutesFromExternalSource(String url, ResteasyClient client, InterconnectingFlightsParameters interconnectingFlightsParameters) {
        List<Route> routeList = client.target(url).request(MediaType.APPLICATION_JSON).get(new GenericType<List<Route>>() {
        });
        System.out.println(routeList.toString());
        return routeList;
    }

    public Schedule getScheduleFromExternalSource(String url, ResteasyClient client) {
        Schedule schedule = client.target(url).request(MediaType.APPLICATION_JSON).get(Schedule.class);
        return schedule;
    }

    public String buildScheduleURL(String urlBase, String departure, String arrival, LocalDateTime datetime) {
        String scheduleURL = urlBase + "/" + departure + "/" + arrival + "/years/" + datetime.getYear() + "/months/" + datetime.getMonthValue();
        return scheduleURL;
    }
}
