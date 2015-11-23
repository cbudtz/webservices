/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.group15;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.DELETE;
import static javax.ws.rs.HttpMethod.POST;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import ws.group15.dto.CreditCardInfo;
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
        return DataSingleton.getInstance().getItineraries();
    }
    
    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String createItinerary(){
        return DataSingleton.getInstance().createItinerary();
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}")
    public Itinerary getItineraryByID(@PathParam("itId") String itID){
        return DataSingleton.getInstance().getItineraryById(itID);
    }
    
    
    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}/creditcard")
    public void payWithCreditCard(@PathParam("itId") int itineraryID,@PathParam("UserId") int userid, CreditCardInfo card){
        System.out.println(itineraryID);
        System.out.println(userid);
        
    }
    
    @PUT
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}/putflights")
    public void putflights(@PathParam("itId") int itineraryID,@PathParam("UserId") int userid){
        System.out.println(itineraryID);
        System.out.println(userid);
        }
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}/{FNUM}/flights/")
    public void getflights(@PathParam("itId") int itineraryID,@PathParam("UserId") int userid, @PathParam("FNUM") int Fnum){
        System.out.println(itineraryID);
        System.out.println(userid);
        System.out.println(Fnum);
        }
    
    @DELETE
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}/{FNUM}/flights/")
    public void deleteflights(@PathParam("itId") int itineraryID,@PathParam("UserId") int userid, @PathParam("FNUM") int Fnum){
        System.out.println(itineraryID);
        System.out.println(userid);
        System.out.println(Fnum);
        }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}/{FNUM}/hotels/")
    public void gethotels(@PathParam("itId") int itineraryID,@PathParam("UserId") int userid, @PathParam("FNUM") int Fnum){
        System.out.println(itineraryID);
        System.out.println(userid);
        System.out.println(Fnum);
        }
    
    @DELETE
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}/{FNUM}/hotels/")
    public void deletehotels(@PathParam("itId") int itineraryID,@PathParam("UserId") int userid, @PathParam("FNUM") int Fnum){
        System.out.println(itineraryID);
        System.out.println(userid);
        System.out.println(Fnum);
        }
    
    
    
    @PUT
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}/puthotels")
    public void puthotels(@PathParam("itId") int itineraryID,@PathParam("UserId") int userid){
        System.out.println(itineraryID);
        System.out.println(userid);
        }
    
    
    
}
