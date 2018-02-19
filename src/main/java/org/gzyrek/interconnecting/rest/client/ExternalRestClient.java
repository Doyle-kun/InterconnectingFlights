package org.gzyrek.interconnecting.rest.client;

import java.time.LocalDateTime;
import java.util.List;

import org.gzyrek.interconnecting.rest.controller.InterconnectingFlightsParameters;
import org.gzyrek.interconnecting.rest.model.Route;
import org.gzyrek.interconnecting.rest.model.Schedule;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;

public interface ExternalRestClient {

    List<Route> getRoutesFromExternalSource(String url, ResteasyClient client, InterconnectingFlightsParameters interconnectingFlightsParameters);

    Boolean pingScheduleFromExternalSource(String url, ResteasyClient client);

    Schedule getScheduleFromExternalSource(String url, ResteasyClient client);

    String buildScheduleURL(String urlBase, String departure, String arrival, LocalDateTime datetime);

}