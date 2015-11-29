package ws.g15.dto;




import javax.xml.bind.annotation.XmlRootElement;
import ws.g15.dto.Itinerary.BookingState;

@XmlRootElement(name = "FlightInformation")
public class FlightInformation {

     public int bookingNumber;
    public int flightPrice;
    public String serviceName;
    public Flight flight;
    public BookingState bookingState = BookingState.PLANNING;

   
   
}
