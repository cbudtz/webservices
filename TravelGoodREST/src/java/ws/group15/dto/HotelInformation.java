/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.group15.dto;

import javax.xml.bind.annotation.XmlRootElement;
import ws.group15.dto.Itinerary.BookingState;

/**
 *
 * @author Christian
 */
@XmlRootElement(name="hotelinformation")
public class HotelInformation {
    public String hotelName;
    public String hotelAddress;
    public int bookingNumber;
    public int stayPrice;
    public boolean creditCardGuaranteeRequired;
    public String serviceName;
    public BookingState state = BookingState.PLANNING;
     
    
}
