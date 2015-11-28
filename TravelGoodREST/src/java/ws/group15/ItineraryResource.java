/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.group15;

import java.net.URI;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import ws.group15.dto.CreditCardInfo;
import ws.group15.dto.FlightInformation;
import ws.group15.dto.HotelInformation;
import ws.group15.dto.Itinerary;

/**
 *
 * @author Christian
 */
@Path("itineraries")
public class ItineraryResource {
    
    @Context
    UriInfo uriInfo;
    
    //Base request 
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getItineraries(){
        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        String url = ub.build().toString();
        List<Itinerary> it = DataSingleton.getInstance().getItineraries();
        GenericEntity<List<Itinerary>> gen = new GenericEntity<List<Itinerary>>(it){};
        Response r = Response.ok(gen)
                .link(url, POST)//Only allowed next action is to obtain some ID by creating an itinerary
                .build();
        return r;
    }
    
    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createItinerary(){
        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        Itinerary it = DataSingleton.getInstance().createItinerary();
        URI uri = ub.path(it.id).build();
        Response r = Response.created(uri)
                .link(uri, "itineraty resource") //TODO: Figure out what to put in rel...
                .link(uri.toString()+ "/status/cancel", "status")
                .link(ub.clone().path("flights").build(), "Flights resource")
                .link(ub.clone().path("hotels").build(), "Hotel resource")
                .entity(it)
                .build();
        return r;
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}")
    public Response getItineraryByID(@PathParam("itId") String itID){
        Itinerary i = DataSingleton.getInstance().getItineraryById(itID);
        if (i == null) return Response.status(Response.Status.BAD_REQUEST).entity("No such Itinerary ID!").build();
        Response r = Response.ok(i).build();
        return r;         
    }
    
    
    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}/creditcard")
    public Response payWithCreditCard(@PathParam("itId") int itineraryID,CreditCardInfo card){
        System.out.println(itineraryID);
        Response r = Response.ok().build();
        return r;
        
    }
    
    @PUT
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}/putflights")
    public void putflights(@PathParam("itId") int itineraryID){
        System.out.println(itineraryID);
        
        }
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}/{FNUM}/flights/")
    public Response getflights(@PathParam("itId") int itineraryID, @PathParam("FNUM") int Fnum){
        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        String url = ub.build().toString();
        Itinerary itinerary = DataSingleton.getInstance().getItineraryById(String.format("%d",itineraryID));
        List<FlightInformation> flights = itinerary.flights;
        GenericEntity<List<FlightInformation>> gen = new GenericEntity<List<FlightInformation>>(flights){};
        Response r = Response.ok(gen)
                .link(url, POST)//Only allowed next action is to obtain some ID by creating an itinerary
                .build();
        return r;
        
    
 }
    
    @DELETE
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}/{FNUM}/flights/")
    public void deleteflights(@PathParam("itId") int itineraryID, @PathParam("FNUM") int Fnum){
        System.out.println(itineraryID);
        System.out.println(Fnum);
        }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}/{FNUM}/hotels/")
    public Response gethotels(@PathParam("itId") int itineraryID, @PathParam("FNUM") int Fnum){
        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        String url = ub.build().toString();
        Itinerary itinerary = DataSingleton.getInstance().getItineraryById(String.format("%d",itineraryID));
        List<HotelInformation> hotels = itinerary.hotels;
        GenericEntity<List<HotelInformation>> gen = new GenericEntity<List<HotelInformation>>(hotels){};
        Response r = Response.ok(gen)
                .link(url, POST)//Only allowed next action is to obtain some ID by creating an itinerary
                .build();
        return r;
        }

    @DELETE
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}/{FNUM}/hotels/")
    public void deletehotels(@PathParam("itId") int itineraryID, @PathParam("FNUM") int Fnum){
        System.out.println(itineraryID);
        System.out.println(Fnum);
        }
    
    
    
    @PUT
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}/puthotels")
    public void puthotels(@PathParam("itId") int itineraryID){
        System.out.println(itineraryID);
       
        }
    
    
    
}
