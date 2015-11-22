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
import javax.ws.rs.core.MediaType;
import ws.group15.dto.Flight;
import ws.group15.dto.FlightInformation;
/**
 *
 * @author Christian
 */
@Path("flights")
public class FlightsResource {
    
 //  @GET
 //  @Produces(MediaType.APPLICATION_XML)
   // public Wrapper test(){
  //      return new Wrapper();
  //  } 
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<FlightInformation> getFlights(){
        List<FlightInformation> l = new ArrayList<>();
        FlightInformation f = new FlightInformation();
        f.bookingNumber=1223;
        Flight ff = new Flight();
        ff.setCarrier("TestCarrier");
        f.flight = ff;
        l.add(f);
        l.add(f);
        return l;
    } 
 
}
