/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenarioTests;

//import org.netbeans.xml.schema.niceviewelements.CreditCardInfoType;
import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.BookItineraryRequestType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TGAddFlightToItineraryType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TGAddHotelToItineraryType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TGGetFlightRequestType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TGGetHotelsRequestType;
import org.netbeans.xml.schema.lameduckelements.CreditCardInfoType;
import org.netbeans.xml.schema.lameduckelements.FlightInformationType;
import org.netbeans.xml.schema.lameduckelements.GetFlightRequestType;
import org.netbeans.xml.schema.niceviewelements.GetHotelsRequestType;
import org.netbeans.xml.schema.niceviewelements.HotelInformationType;

/**
 *
 * @author Runi
 */
public class Conv {
    private static DatatypeFactory df = new DatatypeFactoryImpl();
       public static CreditCardInfoType getCreditcard(String name, int number, int year, int month){
        CreditCardInfoType creditcard = new CreditCardInfoType();
        creditcard.setCardNumber(number);
        creditcard.setHolderName(name);
        creditcard.setExpirationDate(getDate(year, month, 1, 1, 1));
        return creditcard;
    }
    public static BookItineraryRequestType getBookRequest( CreditCardInfoType creditcard, String id){
        BookItineraryRequestType req = new BookItineraryRequestType();
        req.setCreditcard(creditcard);
        req.setItineraryId(id);
        return req;
    }
    public static TGAddFlightToItineraryType convertAddFlightToItinerary(FlightInformationType flight, String id){
        TGAddFlightToItineraryType newFlight = new TGAddFlightToItineraryType();
        org.netbeans.xml.schema.travelgoodelements.FlightInformationType type = new  org.netbeans.xml.schema.travelgoodelements.FlightInformationType();
        type.setBookingNumber(flight.getBookingNumber());
        type.setFlight(convertFlightType(flight));
        type.setFlightPrice(flight.getFlightPrice());
        type.setServiceName(flight.getServiceName());
        newFlight.setFlightInfo(flight);
        newFlight.setItineraryId(id);
        
        return newFlight;
    }
    
    public static TGAddHotelToItineraryType convertAddHotelToItinerary(HotelInformationType hotel, String id) {
        TGAddHotelToItineraryType newHotel = new TGAddHotelToItineraryType();
//        org.netbeans.xml.schema.travelgoodelements.HotelInformationType type = new org.netbeans.xml.schema.travelgoodelements.HotelInformationType();
//        type.setBookingNumber(hotel.getBookingNumber());
//        type.setCreditCardGuaranteeRequired(hotel.isCreditCardGuaranteeRequired());
//        type.setHotelAddress(hotel.getHotelAddress());
//        type.setHotelName(hotel.getHotelName());
//        type.setServiceName(hotel.getServiceName());
//        type.setTotalPrice(hotel.getStayPrice());
        newHotel.setHotel(hotel);
        newHotel.setItineraryId(id);
        return newHotel;
    }
    
    public static org.netbeans.xml.schema.travelgoodelements.FlightType convertFlightType(FlightInformationType flight){
        org.netbeans.xml.schema.travelgoodelements.FlightType newType = new org.netbeans.xml.schema.travelgoodelements.FlightType();
        newType.setArrival(flight.getFlight().getArrival());
        newType.setCarrier(flight.getServiceName());
        newType.setDestAirport(flight.getFlight().getDestAirport());
        newType.setOriginAirport(flight.getFlight().getOriginAirport());
        newType.setTakeOff(flight.getFlight().getTakeOff());
        return newType;
    }
    
    public static TGGetHotelsRequestType getGetHotelRequest(String city, String id){
        TGGetHotelsRequestType req = new TGGetHotelsRequestType();
        GetHotelsRequestType type = new GetHotelsRequestType();
        type.setCity(city);
        type.setArrivalDate(getDate(2015, 2, 1, 1, 1));
        type.setDepartureDate(getDate(2015,1,1,1,1));
        
        req.setRequest(type);
        req.setItineraryId(id);
        return req;
    }
    
    public static TGGetFlightRequestType getGetFligthRequest(String dest, String origin, XMLGregorianCalendar date, String id){
        TGGetFlightRequestType req = new TGGetFlightRequestType();
        GetFlightRequestType type = new GetFlightRequestType();
        type.setDestination(dest);
        type.setOrigin(origin);
        type.setFlightDate(date);
        req.setFlightRequest(type);
        req.setItineraryId(id);
        return req;
    }
    
        public static  XMLGregorianCalendar getDate(int year, int month, int day, int hour, int minute ){
        return df.newXMLGregorianCalendar(new BigInteger(String.valueOf(year)), month, day, hour, minute, hour, BigDecimal.ZERO, minute);
    }
}
