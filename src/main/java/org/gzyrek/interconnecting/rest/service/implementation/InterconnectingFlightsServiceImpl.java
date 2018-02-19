package org.gzyrek.interconnecting.rest.service.implementation;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.gzyrek.interconnecting.rest.client.ExternalRestClient;
import org.gzyrek.interconnecting.rest.client.implementation.ExternalRestClientImpl;
import org.gzyrek.interconnecting.rest.controller.InterconnectingFlightsParameters;
import org.gzyrek.interconnecting.rest.model.Day;
import org.gzyrek.interconnecting.rest.model.Flight;
import org.gzyrek.interconnecting.rest.model.InterconnectingFlight;
import org.gzyrek.interconnecting.rest.model.InterconnectingFlightDetail;
import org.gzyrek.interconnecting.rest.model.InterconnectingRoute;
import org.gzyrek.interconnecting.rest.model.InterconnectingSchedule;
import org.gzyrek.interconnecting.rest.model.Route;
import org.gzyrek.interconnecting.rest.model.Schedule;
import org.gzyrek.interconnecting.rest.service.InterconnectingFlightsConverter;
import org.gzyrek.interconnecting.rest.service.InterconnectingFlightsService;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterconnectingFlightsServiceImpl implements InterconnectingFlightsService {

    private static final String ROUTES_URL = "https://api.ryanair.com/core/3/routes/";
    private static final String SCHEDULE_URL_BASE = "https://api.ryanair.com/timetable/3/schedules/";

    @Autowired
    ExternalRestClient externalRestConsumer;
    @Autowired
    InterconnectingFlightsConverter interconnectingFligthsConverterImpl;

    /* (non-Javadoc)
     * @see org.gzyrek.interconnecting.rest.service.InterconnectingFlightsService#getInterconnectingFlights(org.gzyrek.interconnecting.rest.controller.InterconnectingFlightsParameters)
     */
    @Override
    public List<InterconnectingFlight> getInterconnectingFlights(InterconnectingFlightsParameters interconnectingFlightsParameters) {
        ResteasyClient client = ExternalRestClientImpl.createClient();
        InterconnectingRoute interconnectingRoute = processRoutes(client, interconnectingFlightsParameters);
        InterconnectingSchedule interconnectingSchedule = processSchedule(client, interconnectingRoute, interconnectingFlightsParameters);
        List<InterconnectingFlight> interconnectingFlightList = createFlightsFromSchedule(interconnectingRoute, interconnectingSchedule,
                interconnectingFlightsParameters);
        System.out.println(interconnectingFlightList.toString());

        return interconnectingFlightList;
    }

    private InterconnectingRoute processRoutes(ResteasyClient client, InterconnectingFlightsParameters interconnectingFlightsParameters) {
        List<Route> routeList = externalRestConsumer.getRoutesFromExternalSource(ROUTES_URL, client, interconnectingFlightsParameters);
        List<Route> arrivalList = createArrivalFlightsList(routeList, interconnectingFlightsParameters);
        List<Route> departureList = createDepartureFlightsList(routeList, interconnectingFlightsParameters);
        InterconnectingRoute interconnectingRoute = new InterconnectingRoute();
        interconnectingRoute.setArrivalRouteList(arrivalList);
        interconnectingRoute.setDepartingRouteList(departureList);

        return interconnectingRoute;
    }

    private List<Route> createDepartureFlightsList(List<Route> routeList, InterconnectingFlightsParameters interconnectingFlightsParameters) {
        List<Route> departingFlights = routeList.stream().filter(s -> s.getAirportFrom().equals(interconnectingFlightsParameters.getDeparture()))
                .collect(Collectors.toList());
        return departingFlights;
    }

    private List<Route> createArrivalFlightsList(List<Route> routeList, InterconnectingFlightsParameters interconnectingFlightsParameters) {
        List<Route> arrivalFlights = routeList.stream().filter(s -> s.getAirportTo().equals(interconnectingFlightsParameters.getArrival()))
                .collect(Collectors.toList());
        return arrivalFlights;
    }

    private InterconnectingSchedule processSchedule(ResteasyClient client, InterconnectingRoute interconnectingRoute,
            InterconnectingFlightsParameters interconnectingFlightsParameters) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Map<String, Schedule> arrivalPointScheduleMap = new ConcurrentHashMap<>();
        for (Route route : interconnectingRoute.getArrivalRouteList()) {
            if (route != null) {
                LocalDateTime dateTime = interconnectingFligthsConverterImpl.convertISODateTimeToLocal(interconnectingFlightsParameters.getArrivalDateTime());
                String scheduleURL = externalRestConsumer.buildScheduleURL(SCHEDULE_URL_BASE, route.getAirportFrom(), route.getAirportTo(), dateTime);
                if (externalRestConsumer.pingScheduleFromExternalSource(scheduleURL, client) == true) {
                    executor.submit(() -> {
                        Schedule schedule = externalRestConsumer.getScheduleFromExternalSource(scheduleURL, client);
                        arrivalPointScheduleMap.put(route.getAirportFrom(), schedule);
                    });
                }
            }
        }

        Map<String, Schedule> departingPointScheduleMap = new ConcurrentHashMap<>();
        for (Route route : interconnectingRoute.getDepartingRouteList()) {
            if (route != null) {
                LocalDateTime dateTime = interconnectingFligthsConverterImpl.convertISODateTimeToLocal(interconnectingFlightsParameters.getDepartureDateTime());
                String scheduleURL = externalRestConsumer.buildScheduleURL(SCHEDULE_URL_BASE, route.getAirportFrom(), route.getAirportTo(), dateTime);
                if (externalRestConsumer.pingScheduleFromExternalSource(scheduleURL, client) == true) {
                    executor.submit(() -> {
                        Schedule schedule = externalRestConsumer.getScheduleFromExternalSource(scheduleURL, client);
                        departingPointScheduleMap.put(route.getAirportTo(), schedule);
                    });
                }
            }
        }
        client.close();
        InterconnectingSchedule interconnectingSchedule = new InterconnectingSchedule();
        interconnectingSchedule.setArrivalSchedules(arrivalPointScheduleMap);
        interconnectingSchedule.setDepartingSchedules(departingPointScheduleMap);

        return interconnectingSchedule;
    }

    /* From this place time is ticking really fast so it possibly might not look perfect */
    private List<InterconnectingFlight> createFlightsFromSchedule(InterconnectingRoute interconnectingRoute, InterconnectingSchedule interconnectingSchedule,
            InterconnectingFlightsParameters interconnectingFlightsParameters) {
        Map<String, Schedule> interconnectingFlightScheduleMap = interconnectingSchedule.getArrivalSchedules();
        Map<String, Schedule> departureFlightsScheduleMap = interconnectingSchedule.getDepartingSchedules();
        List<InterconnectingFlight> interconnectingFlightList = new ArrayList<>();
        List<InterconnectingFlight> interconnectingFlightWithStopsList = new ArrayList<>();
        for (Route route : interconnectingRoute.getDepartingRouteList()) {
            Schedule departure = departureFlightsScheduleMap.get(route.getAirportTo());
            Schedule interconnecting = interconnectingFlightScheduleMap.get(route.getAirportTo());
            if (route.getAirportTo().equals(interconnectingFlightsParameters.getArrival())) {
                InterconnectingFlight interconnectingFlight = createDirectFlights(departure, interconnectingFlightsParameters, route.getAirportFrom(),
                        route.getAirportTo());
                interconnectingFlightList.add(interconnectingFlight);
            } else {
                interconnectingFlightWithStopsList.addAll(createInterconnectingFlightsFromSchedule(departure, interconnecting, interconnectingFlightsParameters,
                        route.getAirportFrom(), route.getAirportTo()));
            }
        }
        interconnectingFlightList.addAll(interconnectingFlightWithStopsList);
        return interconnectingFlightList;
    }

    private InterconnectingFlight createDirectFlights(Schedule schedule, InterconnectingFlightsParameters interconnectingFlightsParameters,
            String departureAirport, String arrivalAirport) {
        InterconnectingFlight interconnectingFlight = new InterconnectingFlight();
        interconnectingFlight.setStops(0);
        interconnectingFlight.setLegs(createDirectFlightsFromSchedule(schedule, interconnectingFlightsParameters, departureAirport, arrivalAirport));
        return interconnectingFlight;
    };

    private List<InterconnectingFlightDetail> createDirectFlightsFromSchedule(Schedule schedule,
            InterconnectingFlightsParameters interconnectingFlightsParameters, String departureAirport, String arrivalAirport) {
        LocalDateTime departureDateTime = interconnectingFligthsConverterImpl
                .convertISODateTimeToLocal(interconnectingFlightsParameters.getDepartureDateTime());
        Day day = getDepartureDayFlight(getListOfDays(schedule, departureDateTime.getDayOfMonth()));
        if (day != null) {
            List<Flight> flightList = day.getFlightList();
            List<InterconnectingFlightDetail> directFlightList = new ArrayList<>();
            for (Flight flight : flightList) {
                String flightDepartureDateTime = interconnectingFligthsConverterImpl.convertHourToISODate(flight.getDepartureTime(),
                        interconnectingFlightsParameters.getDepartureDateTime());
                String flightArrivalDateTime = interconnectingFligthsConverterImpl.convertHourToISODate(flight.getArrivalTime(),
                        interconnectingFlightsParameters.getDepartureDateTime());
                if (calculateTimeDifference(flightDepartureDateTime, interconnectingFlightsParameters.getDepartureDateTime()) >= 0L
                        && calculateTimeDifference(flightArrivalDateTime, interconnectingFlightsParameters.getArrivalDateTime()) <= 0L) {
                    InterconnectingFlightDetail interconnectingFlightDetail = new InterconnectingFlightDetail();
                    interconnectingFlightDetail.setArrivalAirport(arrivalAirport);
                    interconnectingFlightDetail.setArrivalDateTime(interconnectingFligthsConverterImpl.convertHourToISODate(flight.getArrivalTime(),
                            interconnectingFlightsParameters.getArrivalDateTime()));
                    interconnectingFlightDetail.setDepartureAirport(departureAirport);
                    interconnectingFlightDetail.setDepartureDateTime(interconnectingFligthsConverterImpl.convertHourToISODate(flight.getDepartureTime(),
                            interconnectingFlightsParameters.getDepartureDateTime()));
                    directFlightList.add(interconnectingFlightDetail);
                }
            }
            return directFlightList;
        }
        return null;
    }

    private List<InterconnectingFlight> createInterconnectingFlightsFromSchedule(Schedule departure, Schedule interconnection,
            InterconnectingFlightsParameters interconnectingFlightsParameters, String departureAirport, String arrivalAirport) {
        List<InterconnectingFlightDetail> departureFlightList = createDirectFlightsFromSchedule(departure, interconnectingFlightsParameters, departureAirport,
                arrivalAirport);
        List<InterconnectingFlightDetail> interconnectingFlightList = createDirectFlightsFromSchedule(interconnection, interconnectingFlightsParameters,
                departureAirport, arrivalAirport);
        List<InterconnectingFlight> interconnectingFlightWithStopsList = new ArrayList<>();
        if ((departureFlightList!=null && departureFlightList.size()>0) && (interconnectingFlightList!=null && interconnectingFlightList.size()>0)) {
        for (InterconnectingFlightDetail departureFlight : departureFlightList) {
            InterconnectingFlight interconnectingFlight = new InterconnectingFlight();
            for (InterconnectingFlightDetail interconnectingFlightDetail : interconnectingFlightList) {
                List<InterconnectingFlightDetail> indirectFlightList = new ArrayList<>();
                if (calculateTimeDifference(departureFlight.getArrivalDateTime(), interconnectingFlightDetail.getDepartureDateTime()) >= 2L) {
                    indirectFlightList.add(departureFlight);
                    indirectFlightList.add(interconnectingFlightDetail);
                    interconnectingFlight.setStops(1);
                    interconnectingFlight.setLegs(interconnectingFlightList);
                    interconnectingFlightWithStopsList.add(interconnectingFlight);
                }
            }
        }
        }
        return interconnectingFlightWithStopsList;
    }

    private void filterDayOfSchedule(List<Day> dayList, Integer day) {
        dayList.removeIf(s -> s.getDay().equals(day) == false);
    }

    private List<Day> getListOfDays(Schedule schedule, Integer day) {
        List<Day> dayList = new ArrayList<>();
        if (schedule!= null && schedule.getDayList().size()!=0) {
            dayList = schedule.getDayList();
            if (dayList!=null) {
            filterDayOfSchedule(dayList, day);
            }
            return dayList;
        }
        return dayList;
    };

    private Day getDepartureDayFlight(List<Day> dayList) {
        if (dayList.size()>0) {
        return dayList.get(0);
        }
        return null;
    }

    private long calculateTimeDifference(String flightFirstDateTime, String flightSecondDateTime) {
        LocalDateTime parameterOneIsoDate = interconnectingFligthsConverterImpl.convertISODateTimeToLocal(flightFirstDateTime);
        LocalDateTime parameterTwoIsoDate = interconnectingFligthsConverterImpl.convertISODateTimeToLocal(flightSecondDateTime);
        Long hours = ChronoUnit.HOURS.between(parameterTwoIsoDate, parameterOneIsoDate);
        return hours;

    }

}
