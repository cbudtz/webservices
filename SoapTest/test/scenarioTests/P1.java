/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenarioTests;

import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.BookItineraryFault;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.BookItineraryRequestType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TGAddFlightToItineraryType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TGAddHotelToItineraryType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TGGetFlightRequestType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TGGetHotelsRequestType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TGItineraryType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWsdlPortType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWsdlService;
import org.netbeans.xml.schema.lameduckelements.CreditCardInfoType;
import org.netbeans.xml.schema.lameduckelements.FlightInfoListType;
import org.netbeans.xml.schema.lameduckelements.FlightInformationType;
import org.netbeans.xml.schema.lameduckelements.GetFlightRequestType;
import org.netbeans.xml.schema.niceviewelements.GetHotelsRequestType;
import org.netbeans.xml.schema.niceviewelements.HotelInformationListType;
import org.netbeans.xml.schema.niceviewelements.HotelInformationType;

/**
 *
 * @author Runi
 */
public class P1 {
        DatatypeFactory df = new DatatypeFactoryImpl();
    static TravelGoodWsdlPortType port;
    private TravelGoodWsdlService service = new TravelGoodWsdlService();
    private static String id = "lolLigeGyldig";
    private static final int STATE_UNCONFIRMED = 0;
    private static final int STATE_CONFIRMED = 2;
    public String cardHolderName = "Thor-Jensen Claus";
    public int cardNumber = 50408825;
    public int year = 9;
    public int month = 5;
    public static boolean setupFinished = false;
    
    @Before
    public void setup(){
        port = service.getTravelGoodWsdlPortTypeBindingPort();

        if (!setupFinished) {
            setupFinished = true;
            System.out.println("initializing...");
            // Tell the server you want to start a session. 
            // It returns you a unique ID
            id = port.initiateItinerary();
            System.out.println("init done!");
        }

    }
    
    @Test
    public void runTest(){
       
        
        // test initialize itinerary
        TGGetFlightRequestType flight1 = getGetFligthRequest("Danmark", "Hawai", getDate(2016, 6, 15, 12, 30));
        TGGetFlightRequestType flight2 = getGetFligthRequest("Faroe Island", "Hawai", getDate(2016, 6, 15, 12, 30));
        TGGetFlightRequestType flight3 = getGetFligthRequest("US and A", "Hawai", getDate(2016, 6, 15, 12, 30));
        
        TGGetHotelsRequestType hotel1 = getGetHotelRequest("Hawaii", id);
        TGGetHotelsRequestType hotel2 = getGetHotelRequest("Tel Vviv", id);
        
        // get a flight
        FlightInfoListType flights = port.getFlights(flight1);
        assertTrue("expected list to have size > 0", flights.getFlightInfo().size() > 0);
        FlightInformationType flightSelect = flights.getFlightInfo().get(0);
        
        // add first flight
        TGItineraryType itinerary = null;
        itinerary = port.addFlightToItinerary(convertAddFlightToItinerary(flightSelect, id));
        assertEquals("add first flight to itinerary failed: ", itinerary.getState(), STATE_UNCONFIRMED);
        assertTrue("check size of flights list. should be 1: ", itinerary.getFlights().getFlightInfo().size() <= 1);
        
        // get hotel
        HotelInformationListType hotels = port.getHotels(hotel1);
        assertTrue("expected list to have size > 0", flights.getFlightInfo().size() > 0);
        HotelInformationType hotelSelect = hotels.getHotelInformations().get(0);
        
        // add first hotel
        itinerary = port.addHotelToItinerary(convertAddHotelToItinerary(hotelSelect, id));
        assertEquals("add first hotel to itinerary failed: ", STATE_UNCONFIRMED, itinerary.getState());
        assertTrue("check size of hotels list. should be at least 1: ", itinerary.getHotels().getHotelInformations().size() > 0);
           
        // add second flight
        flights = port.getFlights(flight2);
        flightSelect = flights.getFlightInfo().get(flights.getFlightInfo().size()-1);
        itinerary = port.addFlightToItinerary(convertAddFlightToItinerary(flightSelect, id));
        assertEquals("add second flight to itinerary failed: ", STATE_UNCONFIRMED, itinerary.getState());
        assertTrue("check size of hotels list. should be at least 1: ", itinerary.getFlights().getFlightInfo().size() > 0);

        // add third flight
        flights = port.getFlights(flight3);
        flightSelect = flights.getFlightInfo().get(flights.getFlightInfo().size()-1);
        itinerary = port.addFlightToItinerary(convertAddFlightToItinerary(flightSelect, id));
        assertEquals("add third flight to itinerary failed: ", itinerary.getState(), STATE_UNCONFIRMED);
        assertTrue("check size of hotels list. should be at least 1: ", itinerary.getFlights().getFlightInfo().size() > 0);
        
        // add second hotel
        hotels = port.getHotels(hotel2);
        hotelSelect = hotels.getHotelInformations().get(hotels.getHotelInformations().size()-1);
        itinerary = port.addHotelToItinerary(convertAddHotelToItinerary(hotelSelect, id));
        assertEquals("add first hotel to itinerary failed: ", itinerary.getState(), STATE_UNCONFIRMED);
        assertTrue("check size of hotels list. should be at least 1: ", itinerary.getHotels().getHotelInformations().size() > 0);
        
        try {
            TGItineraryType itin = port.bookItinerary(getBookRequest(getCreditcard(cardHolderName, cardNumber, year, month), id));
//            int count = 1;
            assertEquals("checking state of itinerary: ", STATE_CONFIRMED, itin.getState());
//            for(org.netbeans.xml.schema.travelgoodelements.HotelInformationType hotel : itin.getHotels()){
//                assertEquals("check state of hotel: " + (count++), hotel.ge½, hotel);
//            }
        } catch (BookItineraryFault ex) {
            fail(ex.getMessage() + "\n" + ex.getFaultInfo());
            Logger.getLogger(C2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public CreditCardInfoType getCreditcard(String name, int number, int year, int month){
        CreditCardInfoType creditcard = new CreditCardInfoType();
        creditcard.setCardNumber(number);
        creditcard.setHolderName(name);
        creditcard.setExpirationDate(getDate(year, month, 1, 1, 1));
        return creditcard;
    }
    public BookItineraryRequestType getBookRequest( CreditCardInfoType creditcard, String id){
        BookItineraryRequestType req = new BookItineraryRequestType();
        req.setCreditcard(creditcard);
        req.setItineraryId(id);
        return req;
    }
    public TGAddFlightToItineraryType convertAddFlightToItinerary(FlightInformationType flight, String id){
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
    
    private TGAddHotelToItineraryType convertAddHotelToItinerary(HotelInformationType hotel, String id) {
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
    
    public org.netbeans.xml.schema.travelgoodelements.FlightType convertFlightType(FlightInformationType flight){
        org.netbeans.xml.schema.travelgoodelements.FlightType newType = new org.netbeans.xml.schema.travelgoodelements.FlightType();
        newType.setArrival(flight.getFlight().getArrival());
        newType.setCarrier(flight.getServiceName());
        newType.setDestAirport(flight.getFlight().getDestAirport());
        newType.setOriginAirport(flight.getFlight().getOriginAirport());
        newType.setTakeOff(flight.getFlight().getTakeOff());
        return newType;
    }
    
    public TGGetHotelsRequestType getGetHotelRequest(String city, String id){
        TGGetHotelsRequestType req = new TGGetHotelsRequestType();
        GetHotelsRequestType type = new GetHotelsRequestType();
        type.setCity(city);
        type.setArrivalDate(getDate(2015, 2, 1, 1, 1));
        type.setDepartureDate(getDate(2015,1,1,1,1));
        
        req.setRequest(type);
        req.setItineraryId(id);
        return req;
    }
    
    public TGGetFlightRequestType getGetFligthRequest(String dest, String origin, XMLGregorianCalendar date){
        TGGetFlightRequestType req = new TGGetFlightRequestType();
        GetFlightRequestType type = new GetFlightRequestType();
        type.setDestination(dest);
        type.setOrigin(origin);
        type.setFlightDate(date);
        req.setFlightRequest(type);
        req.setItineraryId(id);
        return req;
    }
    
        private  XMLGregorianCalendar getDate(int year, int month, int day, int hour, int minute ){
        return df.newXMLGregorianCalendar(new BigInteger(String.valueOf(year)), month, day, hour, minute, hour, BigDecimal.ZERO, minute);
    }
}
