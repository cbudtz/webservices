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
import ws.group15.dto.HotelInformation;

/**
 *
 * @author Christian
 */
@Path("hotels")
public class HotelsResource {
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<HotelInformation> getHotels(){
        List<HotelInformation> l = new ArrayList<>();
        
        HotelInformation h = new HotelInformation();
        h.bookingNumber=1234;
        h.creditCardGuaranteeRequired=true;
        h.hotelAddress="Lundtoftevej 21, 2700 Lyngby";
        h.hotelName="Den Totale Undergang";
        h.stayPrice=1000;
        h.serviceName="NiceDuck";
        l.add(h);
        return l;
    } 
    
}
