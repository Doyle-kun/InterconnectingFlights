package org.gzyrek.interconnecting.rest.controller;

import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gzyrek.interconnecting.rest.exceptions.ErrorResponse;
import org.gzyrek.interconnecting.rest.model.InterconnectingFlight;
import org.gzyrek.interconnecting.rest.service.InterconnectingFlightsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class InterconnectingFlightsControllerImpl {

    @Autowired
    InterconnectingFlightsServiceImpl interconnectingFlightsServiceImpl;

    @RequestMapping(value = "/interconnections", method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInterConnectingFlights(@BeanParam InterconnectingFlightsParameters interconnectingFlightsParameters) {
        List<InterconnectingFlight> interconnectingFlightList;
        interconnectingFlightList = interconnectingFlightsServiceImpl.getInterconnectingFlights(interconnectingFlightsParameters);
        return Response.ok(interconnectingFlightList).build();
    }
    
}