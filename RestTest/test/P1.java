/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ws.g15.dto.Conv;
import ws.g15.dto.FlightInformation;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import org.junit.Test;
import static org.junit.Assert.*;
import ws.g15.dto.Itinerary;
import static javax.ws.rs.core.MediaType.*;
import javax.ws.rs.core.Response;
import ws.g15.dto.CreditCardInfo;
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

   
   public void runTest() {
       
       // get flights
        WebTarget target = client.target(resourceFlight);
        target = addParam(target, GET_FLIGHT_PARAM, GET_FLIGHT_VALUE0);
        System.out.println("target: " + target.getUri());
        List<FlightInformation> flights = target.request().accept(APPLICATION_JSON).get(new GenericType<List<FlightInformation>>(){});             
       assertTrue("we expect list of minimum 1 element: ", flights.size() > 0);
       
       // initialize itinerary
       target = client.target(resourceItinerary);
       Itinerary itinerary =  requestPOST(target, Itinerary.class, null);
       assertEquals("should be in planning state: ", Itinerary.BookingState.PLANNING, itinerary.state);
       
       // add first flight to itinerary
       target = client.target(resourceItinerary + itinerary.id + "/flights/");
       Response res = requestPUT(target, flights.get(0));
       assertEquals("check response from adding first flight", 200, res.getStatus());
       
       // get itinerary and check if there is a flight
       target = client.target(resourceItinerary + itinerary.id);
       itinerary = target.request().accept(APPLICATION_JSON).get(Itinerary.class);//(Entity.entity(Itinerary.class, APPLICATION_JSON));
       assertTrue("expecting flights on itinerary: ", itinerary.flights.size() > 0);
       
       // get hotels
       target = client.target(resourceHotel);
       target = addParam(target, GET_HOTEL_PARAM, GET_HOTEL_VALUE0);
       List<HotelInformation> hotels = target.request().accept(APPLICATION_JSON).get(new GenericType<List<HotelInformation>>(){});
       assertTrue("we expect hotel list of minimum 1 element: ", hotels.size() > 0);
  
       // add first hotel to itinerary
       target = client.target(resourceItinerary + itinerary.id + "/hotels/");
       res = requestPUT(target, hotels.get(0));
       assertEquals("expect all went well status code 200: ", 200, res.getStatus());
       
       // get itinerary and check if there is a hotel
       target = client.target(resourceItinerary + itinerary.id);
       itinerary = target.request().accept(APPLICATION_JSON).get(Itinerary.class);
       assertTrue("expecting hotels on itinerary: ", itinerary.hotels.size() > 0);
       
       // search for another flight
       target = client.target(resourceFlight);
       target = addParam(target, GET_FLIGHT_PARAM, GET_FLIGHT_VALUE1);
       flights = target.request().accept(APPLICATION_JSON).get(new GenericType<List<FlightInformation>>(){});
       
       // add second flight to itinerary
       target = client.target(resourceItinerary  + itinerary.id + "/flights");
       res = requestPUT(target, flights.get(0));
       assertEquals("check status code of response: ", 200, res.getStatus());
       
       // search for another flight
       target = client.target(resourceFlight);
       target = addParam(target, GET_FLIGHT_PARAM, GET_FLIGHT_VALUE2);
       flights = target.request().accept(APPLICATION_JSON).get(new GenericType<List<FlightInformation>>(){});
       
       // add third flight
       target = client.target(resourceItinerary + itinerary.id + "/flights");
       res = requestPUT(target, flights.get(0));
       assertEquals("check status code of response", 200, res.getStatus());
       
       // search another hotel
       target = client.target(resourceHotel);
       target = addParam(target, GET_HOTEL_PARAM, GET_HOTEL_VALUE1);
       hotels = target.request().accept(APPLICATION_JSON).get(new GenericType<List<HotelInformation>>(){});
       
       // add second hotel
       target = client.target(resourceItinerary + itinerary.id + "/hotels");
       res = requestPUT(target, hotels.get(0));
       assertEquals("check status code of add hotel", 200, res.getStatus());
       
       // get itinerary
       target = client.target(resourceItinerary + itinerary.id);
       System.out.println("target: " + target.getUri());
       itinerary = target.request().accept(APPLICATION_JSON).get(Itinerary.class);
       assertEquals("check status of itinerary before book", Itinerary.BookingState.PLANNING, itinerary.state);
       
       // activate creditcard
       target = client.target(resourceItinerary + itinerary.id + "/creditcard");
       System.out.println("target: " + target.getUri());
       CreditCardInfo creditcard = Conv.getCreditcard(cardName0, cardNumber0, cardExpYear0, cardExpMonth0);
       res = requestPUT(target, creditcard);
       System.out.println("response: " + res.toString());
       assertEquals("check status code after put creditcard", 200, res.getStatus());
       
        // book itinerary
       target = client.target(resourceItinerary + itinerary.id + "/state");
       System.out.println("target: " + target.getUri());
//       Itinerary.BookingState book = Itinerary.BookingState.PAID;
       res = requestPUT(target, Itinerary.BookingState.PAID.name());
       System.out.println("response: " + res.toString());
       
       assertEquals("check status of book", 200, res.getStatus());
       target = client.target(resourceItinerary + itinerary.id);
       itinerary = target.request().accept(APPLICATION_JSON).get(Itinerary.class);
       assertEquals("check status of itinerary", Itinerary.BookingState.PAID ,itinerary.state);
       for(FlightInformation f : itinerary.flights){ 
           System.out.println("flight: " + f.flightPrice + "state: " + f.bookingState);
           
       }
       for(HotelInformation h : itinerary.hotels){
           System.out.println("hotel price: " + h.stayPrice + " state: " + h.state);
           
       }
       System.out.println("path: " + target.getUri());
       System.out.println("post: " + res.getLink("POST"));
       System.out.println("linksize: " +  res.getLinks().size());
       System.out.println("state: " + res.getStatus());
       System.out.println("returned: " + res.getEntity());
       
   }
   
  
}
