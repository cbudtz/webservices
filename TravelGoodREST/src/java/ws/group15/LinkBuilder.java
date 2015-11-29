/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.group15;

import javax.ws.rs.core.Response;

/**
 *
 * @author Kristin
 */
public class LinkBuilder {
    public static final String baseUri = "http://localhost:8080/TravelGoodREST/";
    public static final String webresourceUrl = baseUri + "webresources/";
    public static final String hotelResource = webresourceUrl + "hotels";
    public static final String flightsResource = webresourceUrl + "flights";
    public static final String itineraryResource = webresourceUrl + "itineraries";
    
    public static Response.ResponseBuilder addCreateLink(Response.ResponseBuilder r) {
        return r.link(baseUri, webresourceUrl);
    }
    
    /**
     * Builds a ResponseBuilder object containing links associated with the confirmed state
     * @param r ResponseBuilder object which needs to have links applied to it
     * @return ResponseBuilder object with links for planning state
     */
    public static Response.ResponseBuilder addAllPlanningLinks(Response.ResponseBuilder r, String resourceId) {
        return r.link(hotelResource, baseUri + "index.html#GEThotels")
                .link(flightsResource, baseUri + "index.html#GETflights")
                .link(itineraryResource + "/" + resourceId + "/hotels", baseUri + "index.html#PUTitineraryHotels")
                .link(itineraryResource + "/" + resourceId + "/flights", baseUri + "index.html#PUTitineraryFlights")
                .link(itineraryResource + "/" + resourceId , baseUri + "index.html#getItinerary")
                .link(itineraryResource + "/" + resourceId + "/state", baseUri + "index.html#putState");
    }
    
    /**
     * Builds a ResponseBuilder object containing links associated with the confirmed state
     * @param r ResponseBuilder object which needs to have links applied to it
     * @param resourceId Itinerary id
     * @return ResponseBuilder object with links for planning state
     */
    public static Response.ResponseBuilder addPaidLinks(Response.ResponseBuilder r, String resourceId) {
        return r.link(itineraryResource + "/" + resourceId + "/state", baseUri + "index.html#putState")
                .link(itineraryResource + "/" + resourceId , baseUri + "index.html#getItinerary");
    }
    
    /**
     * Builds a ResponseBuilder object containing links associated with the confirmed state
     * @param r
     * @param resourceId
     * @return 
     */
    public static Response.ResponseBuilder addConfirmedLinks(Response.ResponseBuilder r, String resourceId) {
        return r.link(itineraryResource + "/" + resourceId , baseUri + "index.html#getItinerary");
    }
    
    /**
     * Builds a ResponseBuilder object containing links associated with the confirmed state
     * @param r ResponseBuilder object which needs to have links applied to it
     * @param resourceId Itinerary id
     * @return ResponseBuilder object with links for canceled state
     */
    public static Response.ResponseBuilder addCanceledLinks(Response.ResponseBuilder r, String resourceId) {
        return r.link(itineraryResource + "/" + resourceId , baseUri + "index.html#getItinerary");
    }
}
