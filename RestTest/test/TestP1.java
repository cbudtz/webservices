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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author Christian
 */
public class TestP1 {
     Client client = ClientBuilder.newClient();
       WebTarget target = client.target("http://localhost:8080/TravelGoodREST/webresources/flights");
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
   @Test
   public void hello() {
       
       List<FlightInformation> s = target.request().get(new GenericType<List<FlightInformation>>(){});
       for (FlightInformation s1 : s) {
           System.out.println(s1.flight);
           System.out.println(s1.flight.arrival);
           System.out.println(s1.flight.carrier);
           System.out.println(s1.bookingNumber);
           
       }
   }
}
