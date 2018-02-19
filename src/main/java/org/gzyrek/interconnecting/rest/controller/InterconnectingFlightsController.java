package org.gzyrek.interconnecting.rest.controller;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface InterconnectingFlightsController {

    Response getInterConnectingFlights(InterconnectingFlightsParameters interconnectingFlightsParameters);

}