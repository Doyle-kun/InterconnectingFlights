package org.gzyrek.interconnecting.rest.service;

import java.util.List;

import org.gzyrek.interconnecting.rest.controller.InterconnectingFlightsParameters;
import org.gzyrek.interconnecting.rest.model.InterconnectingFlight;

public interface InterconnectingFlightsService {

    List<InterconnectingFlight> getInterconnectingFlights(InterconnectingFlightsParameters interconnectingFlightsParameters);

}