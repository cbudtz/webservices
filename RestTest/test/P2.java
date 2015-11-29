/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import org.junit.Test;
import static org.junit.Assert.*;
import static ws.g15.dto.Conv.*;
import ws.g15.dto.FlightInformation;
import ws.g15.dto.Itinerary;
import javax.ws.rs.core.Response;

/**
 *
 * @author Runi (cancel planning) Plan a trip by first getting a list of flights
 * and then adding a flight to the itinerary. Then cancel planning.
 */
public class P2 {

    static Client client = ClientBuilder.newClient();

    public void runTest() {
        // initialize itinerary
        WebTarget target = client.target(resourceItinerary);
        Itinerary itinerary = requestPOST(target, Itinerary.class, null);
        assertEquals("should be in planning state: ", Itinerary.BookingState.PLANNING, itinerary.state);

        // search for flights
        target = client.target(resourceFlight);
        target = addParam(target, GET_FLIGHT_PARAM, GET_FLIGHT_VALUE0);
        List<FlightInformation> flights = target.request().accept(APPLICATION_JSON).get(new GenericType<List<FlightInformation>>() {});
        assertTrue("check size of flights found", flights.size() > 0);
        
        // add flight
        target = client.target(resourceItinerary + itinerary.id + "/flights");
        Response res = requestPUT(target, flights.get(0));
        assertEquals("check status of response", 200, res.getStatus());
        
        // cancel itinerary
        target = client.target(resourceItinerary + itinerary.id + "/state");
        res = requestPUT(target, Itinerary.BookingState.CANCELLED.name());
        assertEquals("check if response for cancel is ok", 200, res.getStatus());
        
        target = client.target(resourceItinerary + itinerary.id);
        itinerary = target.request().accept(APPLICATION_JSON).get(Itinerary.class);
        assertEquals("check status of itinerary", Itinerary.BookingState.CANCELLED, itinerary.state);

    }

}
