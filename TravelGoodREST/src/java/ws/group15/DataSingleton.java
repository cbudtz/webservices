/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.group15;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;
import ws.group15.dto.FlightInformation;
import ws.group15.dto.HotelInformation;
import ws.group15.dto.Itinerary;

/**
 *
 * @author Christian
 */
public class DataSingleton {
    private static DataSingleton instance;
    private Hashtable<String, Hashtable<String, Itinerary>> userItineraries; //<userId, <itineraryId,Itinea
    
    private DataSingleton(){
        userItineraries = new Hashtable<>();
    }
    
    public static synchronized DataSingleton getInstance(){
        if (instance == null) instance = new DataSingleton();
        return instance;
    }
    
    public String createNewUser(){
        String id = UUID.randomUUID().toString();
        userItineraries.put(id, new Hashtable<String, Itinerary>());
        return id;
    }
    
    public String createItinerary(String userID){
        String itineraryID = UUID.randomUUID().toString();
        Hashtable<String, Itinerary> itineraries = userItineraries.get(userID);
        if (itineraries==null) return null; //UserID was wrong :(
        // create an empty itinerary
        Itinerary it = new Itinerary();
        it.flights = new ArrayList<FlightInformation>();
        it.hotels = new ArrayList<HotelInformation>();
        it.id = itineraryID;
        it.state= Itinerary.ItineraryState.PLANNING;
        itineraries.put(itineraryID, it);
        return itineraryID;
        
    }
    
    public Itinerary getItineraryById(String userID, String itineraryID){
        return userItineraries.get(userID).get(itineraryID);
    }
    
    public List<Itinerary> getItineraries(String userID){
        return null;
    }
    
}
