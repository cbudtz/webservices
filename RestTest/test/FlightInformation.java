


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FlightInformation")
public class FlightInformation {

    public int bookingNumber;
    public int flightPrice;
    public String serviceName;
    public Flight flight;

   
}
