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
/**
 *
 * @author Christian, Rúni
 */
public class TestP1 {
    private static String resourceRoot = "http://localhost:8080/TravelGoodREST/webresources/";
    private static String resourceFlight = resourceRoot + "flights";
    private static String resourceItinerary = resourceRoot + "itineraries/";
    
    private String[] GET_FLIGHT_PARAM = new String[]{"origin", "destination" ,"departure"};
    private String[] GET_FLIGHT_VALUE = new String[]{"denmark", "russia", "2014-06-12"};
    
    private String[] ADD_FLIGHT_TO_ITIN_PARAM = new String[]{"origin", "destination" ,"departure"};
    private String[] ADD_FLIGHT_TO_ITIN_VALUE = new String[]{"denmark", "russia", "2014-06-12"};
    
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

       List<FlightInformation> flights = target.request().get(new GenericType<List<FlightInformation>>(){});
       assertTrue("we expect list of minimum 1 element: ", flights.size() > 0);
       
       target = client.target(resourceItinerary);
       Itinerary itinerary = target.request()
                            .accept(APPLICATION_JSON)
                            .post(Entity.entity(null, APPLICATION_JSON), Itinerary.class);

//       assertEquals("should be in planning state: ", Itinerary.BookingState.PLANNING, itinerary.state);
       
       target = client.target(resourceItinerary + itinerary.id + "/flights/");
       Response res = target.request().accept(APPLICATION_JSON).put(Entity.entity(flights.get(0), APPLICATION_JSON));
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
}
