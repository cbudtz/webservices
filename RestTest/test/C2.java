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
import ws.g15.dto.HotelInformation;
import ws.g15.dto.Itinerary;
/**
 *
 * @author Runi
 * (cancelling fails) Create an itinerary with three bookings and book it. Make sure that the booking
    status is confirmed for each entry. During cancelling of the trip, the cancellation of the second
    booking should fail. Check that the cancelling resulted in an error condition (e.g. value of status
    variable, exception, HTTP status code). Get the itinerary and check that the returned itinerary
    has cancelled as the first and third booking and confirmed for the second booking.
 */
public class C2 {
    static Client client = ClientBuilder.newClient();
    public C2() {
    }

    @Test
    public void runTest(){
                
        // init itinerary
        WebTarget target = client.target(resourceItinerary);
        Itinerary itinerary = requestPOST(target, Itinerary.class, null);
        assertEquals("check itinerary", Itinerary.BookingState.PLANNING, itinerary.state);
        
         // add first flight
        List<FlightInformation> flights = getFlights(GET_FLIGHT_VALUE0);
        addFlightToItinerary(flights.get(0), itinerary.id);
        
        // add second flight
        flights = getFlights(GET_FLIGHT_VALUE_FAIL_ON_CANCEL);
        addFlightToItinerary(flights.get(0), itinerary.id);
        
        // add first hotel
        List<HotelInformation> hotels = getHotels(GET_HOTEL_VALUE0);
        addHotelToItinerary(hotels.get(0), itinerary.id);
        
        setCreditcard(getCreditcard(cardName0, cardNumber0, cardExpYear0, cardExpMonth0), itinerary.id);
        
        // book itinerary and then get it
        setNewItineraryState(Itinerary.BookingState.PAID, itinerary.id);
        itinerary = getItinerary(itinerary.id);
        // check if state is PAID for all flights and hotel
        checkItineraryStatus(itinerary, Itinerary.BookingState.PAID);
        assertEquals("check itinerary status", Itinerary.BookingState.PAID, itinerary.state);
        
        setNewItineraryState(Itinerary.BookingState.CANCELLED, itinerary.id);
        itinerary = getItinerary(itinerary.id);
        assertEquals("check first booking", itinerary.flights.get(0).bookingState, Itinerary.BookingState.CANCELLED);
        assertEquals("check second booking", itinerary.flights.get(1).bookingState, Itinerary.BookingState.PAID);
        assertEquals("check third booking", itinerary.hotels.get(0).state, Itinerary.BookingState.CANCELLED);
        assertEquals("check itinerary status after cancel", Itinerary.BookingState.CANCELLED, itinerary.state);
        

    }

}
