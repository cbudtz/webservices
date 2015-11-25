/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.group15.dto;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Christian
 */
@XmlRootElement(name="itinerary")
public class Itinerary {
    public String id;
    public List<FlightInformation> flights = new ArrayList<>();
    public List<HotelInformation> hotels = new ArrayList<>();
    public CreditCardInfo creditCard;
    public enum BookingState {PLANNING,PAID,CANCELLED,CONFIRMED};
    public BookingState state;

    public Itinerary() {
    }
    
}
