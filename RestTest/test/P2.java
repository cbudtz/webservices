/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.junit.Test;
import static org.junit.Assert.*;
import static ws.g15.dto.Conv.*;
import ws.g15.dto.FlightInformation;
import ws.g15.dto.Itinerary;

/**
 *
 * @author Runi (cancel planning) Plan a trip by first getting a list of flights
 * and then adding a flight to the itinerary. Then cancel planning.
 */
public class P2 {

    static Client client = ClientBuilder.newClient();

    @Test
    public void runTest() {
        // initialize itinerary
        WebTarget target = client.target(resourceItinerary);
        Itinerary itinerary = requestPOST(target, Itinerary.class, null);
        assertEquals("should be in planning state: ", Itinerary.BookingState.PLANNING, itinerary.state);

        // search for flights
        List<FlightInformation> flights = getFlights(GET_FLIGHT_VALUE0);
        assertTrue("check size of flights found", flights.size() > 0);
        
        // add flight
        addFlightToItinerary(flights.get(0), itinerary.id);
        
        // cancel itinerary
        setNewItineraryState(Itinerary.BookingState.CANCELLED, itinerary.id);

        // get itinerary and check if it is cancelled
        itinerary = getItinerary(itinerary.id);
        checkItineraryStatus(itinerary, Itinerary.BookingState.CANCELLED);
        assertEquals("check status of itinerary", Itinerary.BookingState.CANCELLED, itinerary.state);

    }

}
