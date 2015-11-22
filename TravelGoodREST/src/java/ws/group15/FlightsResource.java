/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.group15;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import ws.group15.dto.FlightInformation;

/**
 *
 * @author Christian
 */
@Path("flights")
public class FlightsResource {
    
   @GET
    public String test(){
        return "Test";
    } 
    
   /* @GET
    @Produces("application/xml")
    public List<FlightInformation> getFlights(){
        List<FlightInformation> l = new ArrayList<>();
        l.add(new FlightInformation());
        return l;
    } 
 */
}
