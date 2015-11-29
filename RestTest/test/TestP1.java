/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ws.g15.dto.FlightInformation;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ws.g15.dto.Itinerary;
import static javax.ws.rs.core.MediaType.*;
import javax.ws.rs.core.Response;
import ws.g15.dto.HotelInformation;
/**
 *
 * @author Christian, Rúni
 */
public class TestP1 {
    private static String resourceRoot = "http://localhost:8080/TravelGoodREST/webresources/";
    private static String resourceFlight = resourceRoot + "flights";
    private static String resourceItinerary = resourceRoot + "itineraries/";
    private static String resourceHotel = resourceRoot + "hotels";
    
    private String[] GET_FLIGHT_PARAM = new String[]{"origin", "destination" ,"departure"};
    private String[] GET_FLIGHT_VALUE = new String[]{"denmark", "russia", "2014-06-12"};
    
    private String[] GET_HOTEL_PARAM = new String[]{"city", "arrival" ,"departure"};
    private String[] GET_HOTEL_VALUE = new String[]{"Langbortistan", "2016-12-12", "2018-06-12"};
   
////    private String[] ADD_FLIGHT_TO_ITIN_PARAM = new String[]{"origin", "destination" ,"departure"};
////    private String[] ADD_FLIGHT_TO_ITIN_VALUE = new String[]{"denmark", "russia", "2014-06-12"};
//    
//    private String[] ADD_FLIGHT_TO_ITIN_PARAM = new String[]{"origin", "destination" ,"departure"};
//    private String[] ADD_FLIGHT_TO_ITIN_VALUE = new String[]{"denmark", "russia", "2014-06-12"};
//    
    
     static Client client = ClientBuilder.newClient();
    public TestP1() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    //   * (planning and booking) Plan a trip by first planning a flight (i.e. getting a list of flights and then
    //adding a flight to the itinerary), then by planning a hotel, another flight, a third flight, and finally
    //a hotel. Ask for the itinerary and check that it is correct using JUnit’s assert statements – i.e.
    //assertEquals, assertTrue, . . . – in particular, that the booking status for each item is unconfirmed.
    //Book the itinerary and ask again for the itinerary. Check that each booking status is now confirmed

   @Test
   public void P1() {
       
        WebTarget target = client.target(resourceFlight);
        target = addParam(target, GET_FLIGHT_PARAM, GET_FLIGHT_VALUE);
        System.out.println("target: " + target.getUri());
        List<FlightInformation> flights = target.request().accept(APPLICATION_JSON).get(new GenericType<List<FlightInformation>>(){});             
       assertTrue("we expect list of minimum 1 element: ", flights.size() > 0);
       
       target = client.target(resourceItinerary);
       Itinerary itinerary =  requestPOST(target, Itinerary.class, null);

       assertEquals("should be in planning state: ", Itinerary.BookingState.PLANNING, itinerary.state);
       
       target = client.target(resourceItinerary + itinerary.id + "/flights/");
       Response res = requestPUT(target, flights.get(0));
       
       target = client.target(resourceItinerary + itinerary.id);
       itinerary = target.request().accept(APPLICATION_JSON).get(Itinerary.class);//(Entity.entity(Itinerary.class, APPLICATION_JSON));
       assertTrue("expecting flights on itinerary: ", itinerary.flights.size() > 0);
       
       target = client.target(resourceHotel);
       target = addParam(target, GET_HOTEL_PARAM, GET_HOTEL_PARAM);
       List<HotelInformation> hotels = target.request().accept(APPLICATION_JSON).get(new GenericType<List<HotelInformation>>(){});
       assertTrue("we expect hotel list of minimum 1 element: ", hotels.size() > 0);
       
       System.out.println("hotel: " + hotels.get(0).hotelAddress);
       System.out.println("hotelname: " + hotels.get(0).hotelName);
       
       target = client.target(resourceItinerary + itinerary.id + "/hotels/");
       Response res1 = requestPUT(target, hotels.get(0));
       assertEquals("expect all went wall status code 200: ", 200, res1.getStatus());
       
       target = client.target(resourceItinerary + itinerary.id);
       itinerary = target.request().accept(APPLICATION_JSON).get(Itinerary.class);
       assertTrue("expecting hotels on itinerary: ", itinerary.hotels.size() > 0);
       
       
       System.out.println("path: " + target.getUri());
       System.out.println("post: " + res.getLink("POST"));
       System.out.println("linksize: " +  res.getLinks().size());
       System.out.println("state: " + res.getStatus());
       System.out.println("returned: " + res.getEntity());
//       target = addParam(target, ADD_FLIGHT_TO_ITIN_PARAM, ADD_FLIGHT_TO_ITIN_VALUES);
       
   }
   
   public WebTarget addParam(WebTarget target, String[] param, String[] values){
       for(int i = 0; i < param.length; i++){
           target = target.queryParam(param[i], values[i]);
       }
       return target;
   }
   
   public <T> List<T> requestGET(WebTarget target, Class<T> type){
       return target.request()
               .accept(APPLICATION_JSON)
               .get(new GenericType<List<T>>(){});       
   }
   
   public<T> List<FlightInformation> requestGET1(WebTarget target, Class<T> type){
       return target.request()
               .accept(APPLICATION_JSON)
               .get(new GenericType<List<FlightInformation>>(){});       
   }
   
     public <T> T requestPOST(WebTarget target, Class<T> type, Object entity){
       return target.request()
                    .accept(APPLICATION_JSON)
                    .post(Entity.entity(entity, APPLICATION_JSON), type);
       
   }
       public Response requestPUT(WebTarget target, Object entity){
       return target.request().accept(APPLICATION_JSON).put(Entity.entity(entity, APPLICATION_JSON));
       
   }
}
