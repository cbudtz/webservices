/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.group15.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Christian
 */
@XmlRootElement(name="itinerary")
public class Itinerary {
    public List<FlightInformation> flights;
    public List<HotelInformation> hotels;
    public enum ItineraryState {PLANNING,PAID,CANCELLED,CONFIRMED};
    public ItineraryState state;
}
