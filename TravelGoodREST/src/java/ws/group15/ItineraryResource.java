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
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public final String baseUri = "http://localhost:8080/TravelGoodREST/"; //Desciption file
    public final String webresourceUrl = baseUri + "webresources/";
    @Context
    UriInfo uriInfo;

    //Base request - Not specified in requirements - made for completeness and experience
    //Only allowed actions without an itinerary ID is get flights, get hotels and post itineraries
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getItineraries() {
        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        UriBuilder flightsUB = UriBuilder.fromResource(FlightsResource.class);
        String flightsURI = webresourceUrl + flightsUB.build().toString();
        UriBuilder hotelUB = UriBuilder.fromResource(HotelsResource.class);
        String hotelsURI = webresourceUrl + hotelUB.build().toString();
        String url = ub.build().toString(); //itineraryResource Link
        List<Itinerary> it = DataSingleton.getInstance().getItineraries();
        GenericEntity<List<Itinerary>> gen = new GenericEntity<List<Itinerary>>(it) {
        };
        Response r = Response.ok(gen)
                .link(url, baseUri + "index.html#POSTItinerary")//Create new itinerary
                .link(flightsURI, baseUri + "index.html#GETflights") //Get available flights remember query params
                .link(hotelsURI, baseUri + "index.html#GEThotels") // Get available hotels remember query params
                .build();
        return r;
    }

    //Create new itinerary - returns a link to resource

    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createItinerary() {
        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        Itinerary it = DataSingleton.getInstance().createItinerary();
        URI uri = ub.path(it.id).build();
        Response r = Response.created(uri)
                .link(uri, baseUri + "/index.html#getItinerary") //Get itinerary
                .link(uri.toString() + "/status", baseUri + "index.html#GETitineraryStatus") //Get status of Itinerary
                .link(uri.toString() + "/status/cancel", baseUri + "index.html#PUTitineraryCancel") //PUT to cancel itinerary
                .link(uri.toString() + "/flights", baseUri + "index.html#PUTitineraryFlights") //PUT to add flight to itinerary
                .link(uri.toString() + "/hotels", baseUri + "index.html#PUTitineraryHotels") //PUT to add hotel to itinerary
                .link(webresourceUrl + "flights", baseUri + "index.html#GETflights") // GET to get availableflights
                .link(webresourceUrl + "hotels", baseUri + "index.html#GEThotels") // GET to get availableflights
                .entity(it)
                .build();
        return r;
    }

    //Get specific Itinerary

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}")
    public Response getItineraryByID(@PathParam("itId") String itID) {
        Itinerary i = DataSingleton.getInstance().getItineraryById(itID);
        if (i == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("No such Itinerary ID!").build();
        }
        Response r = Response.ok(i)
                .link(itID, itID) //Add links                                          
                .build();
        return r;

    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}/creditcard")
    public Response putCreditCard(@PathParam("itId") String itineraryID, CreditCardInfo card) {
        if (DataSingleton.getInstance().setCreditCard(card, itineraryID)){
        return Response.ok().build(); //ADD allowed links
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build(); //add allowed links and meaningful message
        }

    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}/flights")
    public Response putflights(@PathParam("itId") String itineraryID, FlightInformation flightinformation) {
        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        String url = ub.build().toString();
        DataSingleton.getInstance().addFlightToItinerary(itineraryID, flightinformation);
        Response r = Response.ok()
                .link(url, POST)//Only allowed next action is to obtain some ID by creating an itinerary
                .build();
        return r;
    }

    //Receive Finfo to datasingleton
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}/flights/")
    public Response getflights(@PathParam("itId") String itineraryID) {
        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        String url = ub.build().toString();
        Itinerary itinerary = DataSingleton.getInstance().getItineraryById(itineraryID);
        List<FlightInformation> flights = itinerary.flights;
        GenericEntity<List<FlightInformation>> gen = new GenericEntity<List<FlightInformation>>(flights) {
        };
        Response r = Response.ok(gen)
                .link(url, POST)//Only allowed next action is to obtain some ID by creating an itinerary
                .build();
        return r;
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}/hotels/")
    public Response gethotels(@PathParam("itId") String itineraryID) {
        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        String url = ub.build().toString();
        Itinerary itinerary = DataSingleton.getInstance().getItineraryById(itineraryID);
        List<HotelInformation> hotels = itinerary.hotels;
        GenericEntity<List<HotelInformation>> gen = new GenericEntity<List<HotelInformation>>(hotels) {
        };
        Response r = Response.ok(gen)
                .link(url, POST)//Only allowed next action is to obtain some ID by creating an itinerary
                .build();
        return r;
    }

//    @DELETE
//    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    @Path("{itId}/{FNUM}/hotels/")
//    public void deletehotels(@PathParam("itId") String itineraryID, @PathParam("FNUM") int Fnum){
//        System.out.println(itineraryID);
//        System.out.println(Fnum);
//        }
    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}/hotels")
    public Response puthotels(@PathParam("itId") String itineraryID, HotelInformation hotelinformation) {
        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        String url = ub.build().toString();
        DataSingleton.getInstance().addHotelToItinerary(itineraryID, hotelinformation);
        Response r = Response.ok()
                .link(url, POST)//Only allowed next action is to obtain some ID by creating an itinerary
                .build();
        return r;

    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{itId}/state")
    public Response putstate(@PathParam("itId") String itineraryID, String state) {
        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        String url = ub.build().toString();
        boolean succes;
        switch (state) {
            case "CANCELLED":
                if(DataSingleton.getInstance().cancelItinerary(itineraryID)){
                return Response.ok(state)
                        .link(url, POST)//Only allowed next action is to obtain some ID by creating an itinerary
                        .build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("cannot cancel Itinerary - wrong state")
                            .build();
                }
            case "PAID": 
                try {
                    succes = DataSingleton.getInstance().bookItinerary(itineraryID);
                    return Response.ok().build(); //TODO add allowed links!!
                } catch (BookingException ex) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("cannot pay Itinerary - wrong state!")
                            .build();
                }
            }

        Response r = Response.status(Response.Status.METHOD_NOT_ALLOWED)
                .entity("Not allowed - only Cancelled and Paid may be pushed")
                .link(url, POST)//Only allowed next action is to obtain some ID by creating an itinerary
                .build();
        return r;

         //check all things including terrorists attacks 
    }

}
