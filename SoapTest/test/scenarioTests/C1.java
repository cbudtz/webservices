/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenarioTests;

import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.BookItineraryFault;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.BookItineraryRequestType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TGGetFlightRequestType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TGGetHotelsRequestType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TGItineraryType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWsdlPortType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWsdlService;
import org.netbeans.xml.schema.lameduckelements.CreditCardInfoType;
import org.netbeans.xml.schema.lameduckelements.FlightInfoListType;
import org.netbeans.xml.schema.lameduckelements.FlightInformationType;
import org.netbeans.xml.schema.niceviewelements.HotelInformationListType;

/**
 *
 * @author Kristin
 */
public class C1 {
    DatatypeFactory df = new DatatypeFactoryImpl();
    static TravelGoodWsdlPortType port;
    private TravelGoodWsdlService service;
    private static String id = "lolLigeGyldig";
    private static final int STATE_UNCONFIRMED = 0;
    private static final int STATE_PAID = 1;
    private static final int STATE_CONFIRMED = 2;
    private static final int STATE_CANCELLED = 3;
    private String cardHolderName = "Thor-Jensen Claus";
    private int cardNumber = 50408825;
    private int year = 9;
    private int month = 5;
    private static boolean setupFinished = false;
    
    @Before
    public void setup() {
        service = new TravelGoodWsdlService();
        port = service.getTravelGoodWsdlPortTypeBindingPort();

        if (!setupFinished) {
            setupFinished = true;
            System.out.println("init method");
            // Tell the server you want to start a session. 
            // It returns you a unique ID
            id = port.initiateItinerary();
        }
    }
    
    @Test
    public void runTest() {
        // Get flights
        System.out.println("Fetching flights...");
        TGGetFlightRequestType flight1 = Conv.getGetFligthRequest("Spain", "Danmark", Conv.getDate(2016, 6, 15, 12, 30), id);
        TGGetFlightRequestType flight2 = Conv.getGetFligthRequest("Washington DC", "Faroe Island", Conv.getDate(2016, 6, 15, 12, 30), id);
        TGGetFlightRequestType flight3 = Conv.getGetFligthRequest("Neverland", "US and A", Conv.getDate(2016, 6, 15, 12, 30), id);
        
        // ... and hotels
        System.out.println("Fetching hotels...");
        TGGetHotelsRequestType h1 = Conv.getGetHotelRequest("Mallorca", id);
        TGGetHotelsRequestType h2 = Conv.getGetHotelRequest("Megaton", id);
        TGGetHotelsRequestType h3 = Conv.getGetHotelRequest("Neverwood", id);
        
        // Initialize itinerary
        System.out.println("Initializing itinerary...");
        TGItineraryType it = null;
        
        // ... add all flights to a list
        System.out.println("Adding fetched flights...");
        FlightInfoListType flist = port.getFlights(flight1);
        assertTrue("No flights found...", !flist.getFlightInfo().isEmpty());
        System.out.println("... first flight added");
        flist.getFlightInfo().add(port.getFlights(flight2).getFlightInfo().get(0));
        assertTrue("No flight was found for second entry", flist.getFlightInfo().size() == 2);
        System.out.println("... seccond flight added");
        flist.getFlightInfo().add(port.getFlights(flight3).getFlightInfo().get(0));
        assertTrue("No flight was found for third entry", flist.getFlightInfo().size() == 3);
        System.out.println("... third flight added");
        
        // ... add all hotels to a list
        System.out.println("Adding fetched hotels...");
        HotelInformationListType hlist = port.getHotels(h1);
        assertTrue("No hotels found...", !hlist.getHotelInformations().isEmpty());
        System.out.println("... first hotel added");
        hlist.getHotelInformations().add(port.getHotels(h2).getHotelInformations().get(0));
        assertTrue("No hotel was found for second entry", hlist.getHotelInformations().size() == 2);
        System.out.println("... second hotel added");
        hlist.getHotelInformations().add(port.getHotels(h3).getHotelInformations().get(0));
        assertTrue("No hotel was found for third entry", hlist.getHotelInformations().size() == 3);
        System.out.println("... third hotel added");
        
        // Adding everything to itinerary
        System.out.println("Adding flights and hotels to itinerary...");
        for(int i = 0; i < flist.getFlightInfo().size(); i++) {
            it = port.addFlightToItinerary(Conv.convertAddFlightToItinerary(flist.getFlightInfo().get(i), id));
            assertEquals(STATE_UNCONFIRMED, it.getState());
            System.out.println("... added flight with carrier " + flist.getFlightInfo().get(i).getFlight().getDestAirport() + " to itinerary");
            it = port.addHotelToItinerary(Conv.convertAddHotelToItinerary(hlist.getHotelInformations().get(i), id));
            assertEquals(STATE_UNCONFIRMED, it.getState());
            System.out.println("... added hotel, " + hlist.getHotelInformations().get(i).getHotelAddress() + ", to itinerary");
        }
        
        // Try to book and confirm each trip
        BookItineraryRequestType b = new BookItineraryRequestType();
        CreditCardInfoType c = new CreditCardInfoType();
        
        c.setCardNumber(cardNumber);
        c.setHolderName(cardHolderName);
        c.setExpirationDate(Conv.getDate(year, month, 1, 1, 1));
        
        b.setCreditcard(c);
        b.setItineraryId(id);
        
        try {
            it = port.bookItinerary(b);
        } catch(BookItineraryFault e) {
            Logger.getLogger(C1.class.getName()).log(Level.SEVERE, null, e);
        }
        
        // Check if bookings are confirmed
        System.out.println("Checking flight and hotel state...");
        for(int i = 0; i < flist.getFlightInfo().size(); i++) {
            System.out.println("... " + (i+1));
            assertEquals("Flight " + (i+1) + " was not confirmed", it.getFlights().getFlightInfo().get(i).getState(), STATE_PAID);
            System.out.println("...... flight to " + it.getFlights().getFlightInfo().get(i).getFlight().getDestAirport() + " is paid");
            assertEquals("Hotel " + (i+1) + " was not cofirmed", it.getHotels().getHotelInformations().get(i).getState(), STATE_PAID);
            System.out.println("...... hotel in " + it.getHotels().getHotelInformations().get(i).getHotelAddress() + " is paid");
        }
        
        // Cancel entire itinerary
        System.out.println("Cancelling itinerary " + it.getId());
        String s = port.cancelItinerary(id);
        System.out.println("... " + s);
        System.out.println("Checking state of itinerary " + port.getItinerary(id).getId());
        assertEquals("Itenerary was not cancelled - state: " + port.getItinerary(id).getState(), Conv.TGCANCELLED, port.getItinerary(id).getState());
        System.out.println("... itinerary id " + port.getItinerary(id).getId() + " succesfully cancelled");
        
        it = port.getItinerary(id);
        // Check each flight and hotel if they've been cancelled
        // Check if bookings are confirmed
        System.out.println("Checking flight and hotel state...");
        for(int i = 0; i < flist.getFlightInfo().size(); i++) {
            System.out.println("... " + (i+1));
            assertEquals("Flight " + (i+1) + " was not cancelled", it.getFlights().getFlightInfo().get(i).getState(), Conv.CANCELLED);
            System.out.println("...... flight to " + it.getFlights().getFlightInfo().get(i).getFlight().getDestAirport() + " is cancelled");
            assertEquals("Hotel " + (i+1) + " was not cofirmed", it.getHotels().getHotelInformations().get(i).getState(), Conv.CANCELLED);
            System.out.println("...... hotel in " + it.getHotels().getHotelInformations().get(i).getHotelAddress() + " is cancelle");
        }
    }
}
