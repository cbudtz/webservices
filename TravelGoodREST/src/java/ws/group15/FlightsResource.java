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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.group15.dto.Flight;
import ws.group15.dto.FlightInformation;
/**
 *
 * @author Christian
 */
@Path("flights")
public class FlightsResource {
    //Only allowed action on fligts is GET!
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getFlights(@QueryParam("origin") String origin,
            @QueryParam("destination") String destination,
            @QueryParam("departure") XMLGregorianCalendar date){
        List<FlightInformation> flights = DataSingleton.getInstance().getFlights(origin, destination, date);
        GenericEntity<List<FlightInformation>> wrap = new GenericEntity<List<FlightInformation>>(flights){};
        Response response = Response.ok(wrap)
                .link("???", "!!!") //TODO: Add lots of allowed links
                .build();
        
        return response;
    } 
 
}
