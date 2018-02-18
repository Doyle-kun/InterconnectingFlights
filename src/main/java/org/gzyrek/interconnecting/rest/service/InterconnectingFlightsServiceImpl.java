package org.gzyrek.interconnecting.rest.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.management.remote.rmi.RMIIIOPServerImpl;

import org.gzyrek.interconnecting.rest.client.ExternalRestConsumer;
import org.gzyrek.interconnecting.rest.controller.InterconnectingFlightsParameters;
import org.gzyrek.interconnecting.rest.model.InterconnectingFlight;
import org.gzyrek.interconnecting.rest.model.InterconnectingRoute;
import org.gzyrek.interconnecting.rest.model.InterconnectingSchedule;
import org.gzyrek.interconnecting.rest.model.Route;
import org.gzyrek.interconnecting.rest.model.Schedule;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterconnectingFlightsServiceImpl {

    private static final String ROUTES_URL = "https://api.ryanair.com/core/3/routes/";
    private static final String SCHEDULE_URL_BASE = "https://api.ryanair.com/timetable/3/schedules/";

    @Autowired
    ExternalRestConsumer externalRestConsumer;
    @Autowired
    InterconnectingFligthsConverterImpl interconnectingFligthsConverterImpl;

    public List<InterconnectingFlight> getInterconnectingFlights(InterconnectingFlightsParameters interconnectingFlightsParameters) {
        ResteasyClient client = ExternalRestConsumer.createClient();
        InterconnectingRoute interconnectingRoute = processRoutes(client, interconnectingFlightsParameters);
        System.out.println(interconnectingRoute.toString());
        processSchedule(client, interconnectingRoute, interconnectingFlightsParameters);
        return null;
    }

    public InterconnectingRoute processRoutes(ResteasyClient client, InterconnectingFlightsParameters interconnectingFlightsParameters) {
        List<Route> routeList = externalRestConsumer.getRoutesFromExternalSource(ROUTES_URL, client, interconnectingFlightsParameters);
        List<Route> departureList = createDepartingFlightsList(routeList, interconnectingFlightsParameters);

        removeAllExistingFromList(routeList, departureList);

        InterconnectingRoute interconnectingRoute = new InterconnectingRoute();
        interconnectingRoute.setDepartingRouteList(departureList);

        removeNotOccuringArrivalsFromRouteList(routeList, interconnectingFlightsParameters);
      //  removeNotOccuringInterconnectionsFromRouteList(routeList, departureList);
        interconnectingRoute.setArrivalRouteList(routeList);

        return interconnectingRoute;
    }

    private List<Route> createDepartingFlightsList(List<Route> routeList, InterconnectingFlightsParameters interconnectingFlightsParameters) {
        List<Route> departingFlights = routeList.stream().filter(s -> s.getAirportFrom().equals(interconnectingFlightsParameters.getDeparture()))
                .collect(Collectors.toList());
        return departingFlights;
    }

    private void removeAllExistingFromList(List<?> list, List<?> itemToRemoveList) {
        list.removeAll(itemToRemoveList);
    }

    private void removeNotOccuringArrivalsFromRouteList(List<Route> routeList, InterconnectingFlightsParameters interconnectingFlightsParameters) {
        routeList.removeIf(s -> s.getAirportTo().equals(interconnectingFlightsParameters.getArrival()) == false);
    }
    private void removeNotOccuringInterconnectionsFromRouteList(List<Route> routeList, List<Route> departureList) {
        for(Route route : departureList){
            routeList.removeIf(s -> s.getAirportFrom().equals(route.getAirportTo()) == false);
        }
    }

    private InterconnectingSchedule processSchedule(ResteasyClient client, InterconnectingRoute interconnectingRoute,
            InterconnectingFlightsParameters interconnectingFlightsParameters) {
        /* Tutaj wchodzi concurrency */
        // ExecutorService executor = Executors.newCachedThreadPool();
        // executor.
        Map<String, Schedule> arrivalPointScheduleMap = new ConcurrentHashMap<>();
        for (Route route : interconnectingRoute.getArrivalRouteList()) {
            if(route != null) {
            LocalDateTime dateTime = interconnectingFligthsConverterImpl.convertISODateTimeToLocal(interconnectingFlightsParameters.getArrivalDateTime());
            String scheduleURL = externalRestConsumer.buildScheduleURL(SCHEDULE_URL_BASE, route.getAirportFrom(), route.getAirportTo(), dateTime);
            Schedule schedule = externalRestConsumer.getScheduleFromExternalSource(scheduleURL, client);
            arrivalPointScheduleMap.put(route.getAirportFrom(), schedule);
            }
        }
        Map<String, Schedule> departingPointScheduleMap = new ConcurrentHashMap<>();
        for (Route route : interconnectingRoute.getDepartingRouteList()) {
            if(route != null) {
            LocalDateTime dateTime = interconnectingFligthsConverterImpl.convertISODateTimeToLocal(interconnectingFlightsParameters.getDepartureDateTime());
            String scheduleURL = externalRestConsumer.buildScheduleURL(SCHEDULE_URL_BASE, route.getAirportFrom(), route.getAirportTo(), dateTime);
            Schedule schedule = externalRestConsumer.getScheduleFromExternalSource(scheduleURL, client);
            departingPointScheduleMap.put(route.getAirportTo(), schedule);
            }
        }
        InterconnectingSchedule interconnectingSchedule = new InterconnectingSchedule();
        interconnectingSchedule.setArrivalSchedules(arrivalPointScheduleMap);
        interconnectingSchedule.setDepartingSchedules(departingPointScheduleMap);
        System.out.println(arrivalPointScheduleMap.toString());
        System.out.println(departingPointScheduleMap.toString());
        return interconnectingSchedule;
    }
    private InterconnectingSchedule getScheduleForAllFlights() {
        return null;
    }
    private void filterScheduleByDestination(InterconnectingSchedule schedule, InterconnectingRoute interconnectingRoute, InterconnectingFlightsParameters interconnectingFlightsParameters){
       for(Route route : interconnectingRoute.getDepartingRouteList()) {
           
       }
     }
}
