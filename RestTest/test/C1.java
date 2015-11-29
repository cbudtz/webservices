/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.print.Book;
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
 * (cancel booking) Create an itinerary with three bookings (mixed 
flights and hotels) and book it.
Get the itinerary and make sure that the booking status is confirmed for each entry. Cancel the
trip and check that now the booking status is cancelled for all bookings of the itinerary.
 */
public class C1 {
    static Client client = ClientBuilder.newClient();
    public C1() {
    }

    
    public void runTest(){
                
        // init itinerary
        WebTarget target = client.target(resourceItinerary);
        Itinerary itinerary = requestPOST(target, Itinerary.class, null);
        assertEquals("check itinerary", Itinerary.BookingState.PLANNING, itinerary.state);
        
        // add first hotel
        List<HotelInformation> hotels = getHotels(GET_HOTEL_VALUE0);
        addHotelToItinerary(hotels.get(0), itinerary.id);
        
        // add first flight
        List<FlightInformation> flights = getFlights(GET_FLIGHT_VALUE0);
        addFlightToItinerary(flights.get(0), itinerary.id);
        
        // add second hotel
        hotels = getHotels(GET_HOTEL_VALUE1);
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
        checkItineraryStatus(itinerary, Itinerary.BookingState.CANCELLED);
        assertEquals("check itinerary status after cancel", Itinerary.BookingState.CANCELLED, itinerary.state);
        

    }

}
