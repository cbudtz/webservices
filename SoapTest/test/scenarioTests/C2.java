/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenarioTests;

import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.InitiateItineraryType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TGItineraryType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWsdlPortType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWsdlService;
import org.netbeans.xml.schema.lameduckelements.FlightInfoListType;
import org.netbeans.xml.schema.niceviewelements.HotelInformationListType;
import org.netbeans.xml.schema.travelgoodelements.ItineraryStateType;
import static org.junit.Assert.*;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.BookItineraryFault;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.BookItineraryRequestType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TGAddFlightToItineraryType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TGAddHotelToItineraryType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TGGetFlightRequestType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TGGetHotelsRequestType;
import org.netbeans.xml.schema.lameduckelements.CreditCardInfoType;
import org.netbeans.xml.schema.lameduckelements.FlightInformationType;
import org.netbeans.xml.schema.lameduckelements.FlightType;
import org.netbeans.xml.schema.lameduckelements.GetFlightRequestType;
import org.netbeans.xml.schema.niceviewelements.GetHotelsRequestType;
import org.netbeans.xml.schema.niceviewelements.HotelInformationType;
import org.netbeans.xml.schema.travelgoodelements.ItineraryType;

/**
 *
 * @author Runi
 * (planning and booking) Plan a trip by first planning a flight (i.e. getting a list of flights and then
adding a flight to the itinerary), then by planning a hotel, another flight, a third flight, and finally
a hotel. Ask for the itinerary and check that it is correct using JUnit’s assert statements – i.e.
assertEquals, assertTrue, . . . – in particular, that the booking status for each item is unconfirmed.
Book the itinerary and ask again for the itinerary. Check that each booking status is now confirmed

 */
public class C2 {
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
            System.out.println("init method");
            // Tell the server you want to start a session. 
            // It returns you a unique ID
            id = port.initiateItinerary();
        }
//        TravelGoodWsdlService service = new TravelGoodWsdlService();
//        port = service.getTravelGoodWsdlPortTypeBindingPort();
//        System.out.println("port: " + port);
//                InitiateItineraryType initItin = new InitiateItineraryType();
//                 TGItineraryType type = new TGItineraryType();
//        type.setFlights(new FlightInfoListType());
//        type.setHotels(new HotelInformationListType());
//        type.setId(id);
//        type.setState(new ItineraryStateType());
//        initItin.setItinerary(type);
//        initItin.setItineraryId(id);
//        int itinID = port.initiateItinerary(initItin);
//        assertEquals("should receive id: " + id, id, itinID);
        
    }
    
    @Test
    public void runTest(){
       
        
        // test initialize itinerary

        // get a flight
        TGGetFlightRequestType request = new TGGetFlightRequestType();
        GetFlightRequestType reqInfo = new GetFlightRequestType();
        reqInfo.setDestination("hawai");
        reqInfo.setFlightDate(df.newXMLGregorianCalendar(new BigInteger(String.valueOf(2016)), 1, 1, 1, 1, 1, BigDecimal.ZERO, 1));
        reqInfo.setOrigin("Danmark");
        request.setFlightRequest(reqInfo);
        request.setItineraryId(id);
//        TGGetFlightRequestType f = getGetFligthRequest("Danmark", "Mallorca", getDate(2016, 6, 15, 12, 0));
        FlightInfoListType flights = port.getFlights(request);
        assertTrue("expected list to have size > 0", flights.getFlightInfo().size() > 0);
        FlightInformationType flightSelect = flights.getFlightInfo().get(0);
        
        // add first flight
        TGItineraryType itinerary = port.addFlightToItinerary(convertAddFlightToItinerary(flightSelect, id));
        assertEquals("add first flight to itinerary failed: ", itinerary.getState(), STATE_UNCONFIRMED);
        assertEquals("check size of flights list. should be 1: ", itinerary.getFlights().getFlightInfo().size(), 1);
        
        HotelInformationListType hotels = port.getHotels(getGetHotelRequest("Mallorca Down Town", id));
        assertTrue("expected list to have size > 0", flights.getFlightInfo().size() > 0);
        HotelInformationType hotelSelect = hotels.getHotelInformations().get(0);
        
        // add first hotel
        itinerary = port.addHotelToItinerary(convertAddHotelToItinerary(hotelSelect, id));
        assertEquals("add first hotel to itinerary failed: ", itinerary.getState(), STATE_UNCONFIRMED);
        assertEquals("check size of hotels list. should be 1: ", itinerary.getHotels().getHotelInformations().size(), 1);
           
        // add second flight
        itinerary = port.addFlightToItinerary(convertAddFlightToItinerary(flightSelect, id));
        assertEquals("add second flight to itinerary failed: ", itinerary.getState(), STATE_UNCONFIRMED);
        assertEquals("check size of flights list. should be 2: ", itinerary.getFlights().getFlightInfo().size(), 2);

        // add third flight
        itinerary = port.addFlightToItinerary(convertAddFlightToItinerary(flightSelect, id));
        assertEquals("add third flight to itinerary failed: ", itinerary.getState(), STATE_UNCONFIRMED);
        assertEquals("check size of flights list. should be 3: ", itinerary.getFlights().getFlightInfo().size(), 3);
        
        // add second hotel
        itinerary = port.addHotelToItinerary(convertAddHotelToItinerary(hotelSelect, id));
        assertEquals("add first hotel to itinerary failed: ", itinerary.getState(), STATE_UNCONFIRMED);
        assertEquals("check size of hotels list. should be 2: ", itinerary.getHotels().getHotelInformations().size(), 2);
        
        try {
            TGItineraryType itin = port.bookItinerary(getBookRequest(getCreditcard(cardHolderName, cardNumber, year, month), id));
//            int count = 1;
            assertEquals("checking state of itinerary: ", itin.getState(), STATE_CONFIRMED);
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
