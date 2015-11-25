/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unitTest;

import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.*;
import org.netbeans.xml.schema.lameduckelements.GetFlightRequestType;
import org.netbeans.xml.schema.lameduckelements.*;
import org.netbeans.xml.schema.niceviewelements.*;
import org.netbeans.xml.schema.travelgoodelements.ItineraryType;

/**
 *
 * @author Jeppe Dickow
 */
public class TravelGoodTest {

    TravelGoodWsdlService service = new TravelGoodWsdlService();
    DatatypeFactory df = new DatatypeFactoryImpl();
  
    static TravelGoodWsdlPortType port;
    private final static String sessionId = "testti";
    public static boolean setupFinished = false;
    
//    @BeforeClass
//    public static void init(){
//        
//            TravelGoodWsdlService service = new TravelGoodWsdlService();
//            port = service.getTravelGoodWsdlPortTypeBindingPort();
//             InitiateItineraryType init = new InitiateItineraryType();
//            TGItineraryType itinerary = new TGItineraryType();
//            itinerary.setFlights(new FlightInfoListType());
//            itinerary.setHotels(new HotelInformationListType());
//            init.setItinerary(itinerary);
//            init.setItineraryId(sessionId);
//            port.initiateItinerary(init);
//        
//          
//    }

    @Before
    public void setUp() {
        port = service.getTravelGoodWsdlPortTypeBindingPort();

        if (!setupFinished) {
            setupFinished = true;
            // setup the itinerary on the server. .
            InitiateItineraryType init = new InitiateItineraryType();
            TGItineraryType itinerary = new TGItineraryType();
            itinerary.setFlights(new FlightInfoListType());
            itinerary.setHotels(new HotelInformationListType());
            init.setItinerary(itinerary);
            init.setItineraryId(sessionId);
            System.out.println("init method");
            // pass the information to the server
            // port.initiateItinerary(init);
        }
    }

   /* @Test
    public void testGetFlights() {
        TGGetFlightRequestType request = new TGGetFlightRequestType();
        GetFlightRequestType reqInfo = new GetFlightRequestType();
        reqInfo.setDestination("hawai");
        reqInfo.setFlightDate(df.newXMLGregorianCalendar(new BigInteger(String.valueOf(2016)), 1, 1, 1, 1, 1, BigDecimal.ZERO, 1));
        reqInfo.setOrigin("Danmark");
        request.setFlightRequest(reqInfo);
        request.setItineraryId(sessionId);
        FlightInfoListType flights = port.getFlights(request);
    }

    @Test
    public void testAddFlightToItinerary() {
        TGAddFlightToItineraryType input = new TGAddFlightToItineraryType();
        FlightInformationType flight 
                = new FlightInformationType();
        flight.setFlight(new FlightType());
        flight.setServiceName("Fucking service");
        input.setFlightInfo(flight);
        input.setItineraryId(sessionId);
        TGItineraryType itinerary = port.addFlightToItinerary(input);
    }

    @Test
     public void testHotels(){
     TGGetHotelsRequestType request = new TGGetHotelsRequestType();
     GetHotelsRequestType reqInfo = new GetHotelsRequestType();
     reqInfo.setArrivalDate(df.newXMLGregorianCalendar(new BigInteger(String.valueOf(2016)), 1, 1, 1, 1, 1, BigDecimal.ZERO, 1));
     reqInfo.setDepartureDate(df.newXMLGregorianCalendar(2016, 1, 1, 1, 1, 1, 1, 1));
     reqInfo.setCity("Havanna");
     request.setItineraryId(sessionId);
     request.setRequest(reqInfo);
     HotelInformationListType hotels = port.getHotels(request);
     
     // try and add the same hotel to the itinerary
     TGAddHotelToItineraryType addRequest = new TGAddHotelToItineraryType();
     addRequest.setHotel(hotels.getHotelInformations().get(0));
     addRequest.setItineraryId(sessionId);
     TGItineraryType itinerary = port.addHotelToItinerary(addRequest);
     }
    */
    @Test
    public void testGetItinerary() {
       TGItineraryType itinerary = port.getItinerary(sessionId);
       // port.cancelItinerary(sessionId);
    }
}
