/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.group15;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.group15.dto.Flight;
import ws.group15.dto.FlightInformation;
import ws.group15.dto.HotelInformation;
import ws.group15.dto.Itinerary;

/**
 *
 * @author Christian
 */
public class DataSingleton {
    private static DataSingleton instance;
    private Hashtable<String, Itinerary> itineraries; //<userId, <itineraryId,Itinea
    
    private DataSingleton(){
        itineraries = new Hashtable<>();
    }
    
    public static synchronized DataSingleton getInstance(){
        if (instance == null) instance = new DataSingleton();
        return instance;
    }
    
    //POST in itineraries
    public Itinerary createItinerary(){
        String itineraryID = UUID.randomUUID().toString();
        Itinerary it = new Itinerary();
        it.id = itineraryID;
        itineraries.put(itineraryID, it);
        return it;   
    }
    
    //GET on itineraries/{itId}
    public Itinerary getItineraryById(String itineraryID){
        return itineraries.get(itineraryID);
    }
    
    //GET on itineraries //Not part of requirements specification! Made for fun and practice
    public List<Itinerary> getItineraries(){
        ArrayList<Itinerary> itineraryList = new ArrayList<>();
        for (Map.Entry<String, Itinerary> entrySet : itineraries.entrySet()) {
                itineraryList.add(entrySet.getValue());
            }
        return itineraryList;
    }
    
    public boolean cancelItinerary(String itineraryID){
        //TODO lots of magic to cancel itinerary - return true if everyThing went well
        return true;
    }
    //PUT on flights
    public void addFlightToItinerary(String itineraryID, FlightInformation flightInfo){
        itineraries.get(itineraryID).flights.add(flightInfo);
    }
    
    //PUT on hotels
    public void addHotelToItinerary(String itineraryID, HotelInformation hotelInformation){
        itineraries.get(itineraryID).hotels.add(hotelInformation);
    }
    //Contact lameDuck and get some flights
    public List<FlightInformation> getFlights(String origin, String destination, XMLGregorianCalendar departure){
        return null; //TODO implement
    }
    //Contact NiceView and get som hotels
    public List<HotelInformation> getHotels(String city, XMLGregorianCalendar arrival, XMLGregorianCalendar departure){
        
        return null; //TODO implement
    }
}
