/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import org.junit.Test;
import static org.junit.Assert.*;
import static ws.g15.dto.Conv.*;
import ws.g15.dto.FlightInformation;
import ws.g15.dto.Itinerary;
import ws.g15.dto.Conv;
import ws.g15.dto.HotelInformation;

/**
 *
 * @author Runi 
 * (booking fails) Plan an itinerary with three bookings (mixed
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

    @Test
    public void runTest() {
        Response res = null;
        // init itinerary
        WebTarget target = client.target(resourceItinerary);
        Itinerary itinerary = requestPOST(target, Itinerary.class, null);
        assertEquals("check itinerary", Itinerary.BookingState.PLANNING, itinerary.state);

        // find flights
        List<FlightInformation> flights = getFlights(GET_FLIGHT_VALUE0);

        // add first flight
        res = addFlightToItinerary(flights.get(0), itinerary.id);
        Set<Link> links1 = res.getLinks();
        Iterator<Link> it1 = links1.iterator();
        while(it1.hasNext()){
            System.out.println("link: " + it1.next().getUri().getPath());
        }
        
        // get itinerary
        itinerary = getItinerary(itinerary.id);

        // get another flight
        flights = getFlights(GET_FLIGHT_VALUE_FAIL_ON_BOOK);

        // add second flight
        res = addFlightToItinerary(flights.get(0), itinerary.id);

        // get hotels
        List<HotelInformation> hotels = getHotels(GET_HOTEL_VALUE0);

        // add first hotel
        res = addHotelToItinerary(hotels.get(0), itinerary.id);
        
        // set credit card
        res = setCreditcard(Conv.getCreditcard(cardName1, cardNumber1, cardExpYear1, cardExpMonth1), itinerary.id);
        Set<Link> links = res.getLinks();
        Iterator<Link> it = links.iterator();
        while(it.hasNext()){
            System.out.println("link: " + it.next().toString());
        }
        
        // book itinerary
        res = setNewItineraryState(Itinerary.BookingState.PAID, itinerary.id);
        assertEquals("check if booking failed", 400, res.getStatus());
        System.out.println("response msg: " + res.toString());
        
        // get itinerary
        itinerary = getItinerary(itinerary.id);

        // check that state is cancelled on itinerary and bookings
        assertEquals("check status of itinerary", Itinerary.BookingState.CANCELLED, itinerary.state);
        checkItineraryStatus(itinerary, Itinerary.BookingState.CANCELLED);        
        
    }

}
