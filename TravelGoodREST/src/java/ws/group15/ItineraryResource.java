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
import ws.group15.dto.HotelInformation;
import ws.group15.dto.Itinerary;

/**
 *
 * @author Christian
 */
@Path("itineraries")
public class ItineraryResource {
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Itinerary> getItineraries(){
        List<Itinerary> l = new ArrayList<>();
        Itinerary i = new Itinerary();
        
        return l;
    } 
    
    
}
