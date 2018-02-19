package org.gzyrek.interconnecting.rest.client.implementation;

import java.time.LocalDateTime;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.GenericType;

import org.gzyrek.interconnecting.rest.client.ExternalRestClient;
import org.gzyrek.interconnecting.rest.controller.InterconnectingFlightsParameters;
import org.gzyrek.interconnecting.rest.model.Route;
import org.gzyrek.interconnecting.rest.model.Schedule;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.springframework.stereotype.Component;


@Component
public class ExternalRestClientImpl implements ExternalRestClient {

    public static ResteasyClient createClient() {
        ResteasyClient client = new ResteasyClientBuilder().connectionPoolSize(255).build();
        return client;
    }

    /* (non-Javadoc)
     * @see org.gzyrek.interconnecting.rest.client.ExternalRestClient#getRoutesFromExternalSource(java.lang.String, org.jboss.resteasy.client.jaxrs.ResteasyClient, org.gzyrek.interconnecting.rest.controller.InterconnectingFlightsParameters)
     */
    @Override
    public List<Route> getRoutesFromExternalSource(String url, ResteasyClient client, InterconnectingFlightsParameters interconnectingFlightsParameters) {
        List<Route> routeList = client.target(url).request(MediaType.APPLICATION_JSON).get(new GenericType<List<Route>>() {
        });
        return routeList;
    }
    /* (non-Javadoc)
     * @see org.gzyrek.interconnecting.rest.client.ExternalRestClient#pingScheduleFromExternalSource(java.lang.String, org.jboss.resteasy.client.jaxrs.ResteasyClient)
     */
    @Override
    public Boolean pingScheduleFromExternalSource(String url, ResteasyClient client) {
        Response response = client.target(url).request().get(); 
        if (response.getStatus()!=200) {
        return false;
        } else {
            return true;
        }
    }
    /* (non-Javadoc)
     * @see org.gzyrek.interconnecting.rest.client.ExternalRestClient#getScheduleFromExternalSource(java.lang.String, org.jboss.resteasy.client.jaxrs.ResteasyClient)
     */
    @Override
    public Schedule getScheduleFromExternalSource(String url, ResteasyClient client) {
        Schedule schedule = client.target(url).request(MediaType.APPLICATION_JSON).get(Schedule.class);
        return schedule;
    }

    /* (non-Javadoc)
     * @see org.gzyrek.interconnecting.rest.client.ExternalRestClient#buildScheduleURL(java.lang.String, java.lang.String, java.lang.String, java.time.LocalDateTime)
     */
    @Override
    public String buildScheduleURL(String urlBase, String departure, String arrival, LocalDateTime datetime) {
        String scheduleURL = urlBase + departure + "/" + arrival + "/years/" + datetime.getYear() + "/months/" + datetime.getMonthValue();
        return scheduleURL;
    }
}
