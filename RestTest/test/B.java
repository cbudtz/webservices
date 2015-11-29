/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.junit.Test;
import static org.junit.Assert.*;
import static ws.g15.dto.Conv.*;
import ws.g15.dto.Itinerary;
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
import ws.g15.dto.Conv;
import ws.g15.dto.CreditCardInfo;
import ws.g15.dto.HotelInformation;

/**
 *
 * @author Runi (booking fails) Plan an itinerary with three bookings (mixed
 * flights and hotels). Get the itinerary and make sure that the booking status
 * is unconfirmed for each entry. Then book the itinerary. During booking, the
 * second booking should fail. Get the itinerary and check that the result of
 * the bookTrip operation records a failure and that the returned itinerary has
 * cancelled as the booking status of the first booking and unconfirmed for the
 * status of the second and third booking.
 */
public class B {

    static Client client = ClientBuilder.newClient();

    public B() {
    }

    public void runTest() {
        // init itinerary
        WebTarget target = client.target(resourceItinerary);
        Itinerary itinerary = requestPOST(target, Itinerary.class, null);
        assertEquals("check itinerary", Itinerary.BookingState.PLANNING, itinerary.state);

        // find flights
        List<FlightInformation> flights = getFlights(GET_FLIGHT_VALUE0);

        // add first flight
        addFlightToItinerary(flights.get(0), itinerary.id);

        // get itinerary
        itinerary = getItinerary(itinerary.id);

        // get another flight
        flights = getFlights(GET_FLIGHT_VALUE3);

        // add second flight
        addFlightToItinerary(flights.get(0), itinerary.id);

        // get hotels
        List<HotelInformation> hotels = getHotels(GET_HOTEL_VALUE0);

        // add first hotel
        addHotelToItinerary(hotels.get(0), itinerary.id);
        
        // set credit card
        setCreditcard(Conv.getCreditcard(cardName1, cardNumber1, cardExpYear1, cardExpMonth1), itinerary.id);

        // book itinerary
        setNewItineraryState(Itinerary.BookingState.PAID, itinerary.id);
        
        // get itinerary
        itinerary = getItinerary(itinerary.id);

        assertEquals("check status of itinerary", Itinerary.BookingState.CANCELLED, itinerary.state);
        checkItineraryStatus(itinerary, Itinerary.BookingState.CANCELLED);        
        
    }

  
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}