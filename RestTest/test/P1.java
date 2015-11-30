/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ws.g15.dto.FlightInformation;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.junit.Test;
import static org.junit.Assert.*;
import ws.g15.dto.Itinerary;
import ws.g15.dto.HotelInformation;
import static ws.g15.dto.Conv.*;
/**
 *
 * @author Christian, Rúni
 */
public class P1 {

    
     static Client client = ClientBuilder.newClient();
    


    //   * (planning and booking) Plan a trip by first planning a flight (i.e. getting a list of flights and then
    //adding a flight to the itinerary), then by planning a hotel, another flight, a third flight, and finally
    //a hotel. Ask for the itinerary and check that it is correct using JUnit’s assert statements – i.e.
    //assertEquals, assertTrue, . . . – in particular, that the booking status for each item is unconfirmed.
    //Book the itinerary and ask again for the itinerary. Check that each booking status is now confirmed

   @Test
   public void runTest() {
       
       // initialize itinerary
       WebTarget target = client.target(resourceItinerary);
       Itinerary itinerary =  requestPOST(target, Itinerary.class, null);
       assertEquals("should be in planning state: ", Itinerary.BookingState.PLANNING, itinerary.state);
       
       // get flights
       List<FlightInformation> flights = getFlights(GET_FLIGHT_VALUE0);
       assertTrue("we expect list of minimum 1 element: ", flights.size() > 0);
       
       // add first flight to itinerary
       addFlightToItinerary(flights.get(0), itinerary.id);
       
       // get itinerary and check if there is a flight
       itinerary = getItinerary(itinerary.id);
       assertTrue("expecting flights on itinerary: ", itinerary.flights.size() > 0);
       
       // get hotels
       List<HotelInformation> hotels = getHotels(GET_HOTEL_VALUE0);
       assertTrue("we expect hotel list of minimum 1 element: ", hotels.size() > 0);
  
       // add first hotel to itinerary
       addHotelToItinerary(hotels.get(0), itinerary.id);
       
       // get itinerary and check if there is a hotel
       itinerary = getItinerary(itinerary.id);
       assertTrue("expecting hotels on itinerary: ", itinerary.hotels.size() > 0);
       
       // search for another flight
       flights = getFlights(GET_FLIGHT_VALUE1);
       
       // add second flight to itinerary
       addFlightToItinerary(flights.get(0), itinerary.id);
       
       // search for another flight
       flights = getFlights(GET_FLIGHT_VALUE2);
       
       // add third flight
       addFlightToItinerary(flights.get(0), itinerary.id);
       
       // search another hotel
       hotels = getHotels(GET_HOTEL_VALUE1);
       
       // add second hotel
       addHotelToItinerary(hotels.get(0), itinerary.id);
       
       // get itinerary
       itinerary = getItinerary(itinerary.id);
       assertEquals("check status of itinerary before book", Itinerary.BookingState.PLANNING, itinerary.state);
       
       // activate creditcard
       setCreditcard(getCreditcard(cardName0, cardNumber0, cardExpYear0, cardExpMonth0), itinerary.id);
       
        // book itinerary
       Response res = setNewItineraryState(Itinerary.BookingState.PAID, itinerary.id);
       assertEquals("check if booking went well", 200, res.getStatus());
       
       // get itinerary and check if all states are PAID
       itinerary = getItinerary(itinerary.id);
       assertEquals("check status of itinerary", Itinerary.BookingState.PAID ,itinerary.state);
       checkItineraryStatus(itinerary, Itinerary.BookingState.PAID);
       
       for(FlightInformation f : itinerary.flights){ 
           System.out.println("flight: " + f.flightPrice + "state: " + f.bookingState);
           
       }
       for(HotelInformation h : itinerary.hotels){
           System.out.println("hotel price: " + h.stayPrice + " state: " + h.state);
           
       }
       
   }
   
  
}
