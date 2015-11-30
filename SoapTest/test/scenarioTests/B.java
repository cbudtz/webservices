/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenarioTests;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.XMLGregorianCalendar;
import junit.framework.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.*;
import org.netbeans.xml.schema.lameduckelements.*;
import org.netbeans.xml.schema.niceviewelements.*;
import static scenarioTests.P1.port;

/**
 *
 * @author Runi and Jeppe
 */

/*
(booking fails) Plan an itinerary with three bookings (mixed 
flights amd hotels). Get the itinerary
and make sure that the booking status is unconfirmed for each entry. Then book the itinerary.
During booking, the second booking should fail. Get the itinerary and check that the result of the
bookTrip operation records a failure and that the returned itinerary has cancelled as the booking
status of the first booking and unconfirmed for the status of the second and third booking.
*/
public class B {
    
    private static TravelGoodWsdlPortType port;
    private TravelGoodWsdlService service = new TravelGoodWsdlService();
    private static Boolean setupFinished = false; 
    private static String id; 
    private String cardHolderName = "Thor-Jensen Claus";
    private int cardNumber = 50408825;
    public int month = 5;
    public int year = 9;
    
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
    public void testScenarioB(){
        TGItineraryType itinerary;
        
        TGGetFlightRequestType flightRequest = Conv.getGetFligthRequest("Ålborg", "Odense", Conv.getDate(2016, 6, 15, 12, 30), id);
        // the hotel returned from the call will crash the booking
        TGGetHotelsRequestType hotelRequest = Conv.getGetHotelRequest("failBook", id);
        TGGetHotelsRequestType hotelRequest2 = Conv.getGetHotelRequest("Odense", id);
        
        // get a flight to book
        FlightInfoListType flights = port.getFlights(flightRequest);
        
        TGAddFlightToItineraryType addFlightRequest = Conv.convertAddFlightToItinerary(flights.getFlightInfo().get(0), id);
        
        // add the flight to the itinerary
        port.addFlightToItinerary(addFlightRequest);
        
        // get some hotels
        HotelInformationListType hotels = port.getHotels(hotelRequest);
        TGAddHotelToItineraryType addHotelRequest = Conv.convertAddHotelToItinerary(hotels.getHotelInformations().get(0), id);    
        
        // add the hotel to the itinerary
        port.addHotelToItinerary(addHotelRequest);
        
        // get more hotels 
        hotels = port.getHotels(hotelRequest2);
        
        // add this hotel to the itinerary
        addHotelRequest = Conv.convertAddHotelToItinerary(hotels.getHotelInformations().get(0), id);
        
        itinerary = port.addHotelToItinerary(addHotelRequest);
        
        // do the asertions now, and then book the itinerary
        
        // check the status of the itinerary
        System.out.println("Checking states of the flights and hotels....");
        Assert.assertEquals(Conv.TGPLANNINGPHASE, itinerary.getState());
        Assert.assertEquals(Conv.UNCONFIRMED, itinerary.getFlights().getFlightInfo().get(0).getState());
        Assert.assertEquals(Conv.UNCONFIRMED, itinerary.getHotels().getHotelInformations().get(0).getState());
        Assert.assertEquals(Conv.UNCONFIRMED, itinerary.getHotels().getHotelInformations().get(1).getState());
        
        
        // now book the itinerary, the booking should fail so figure out a way to do that. 
        try {
            System.out.println("Try to book the itinerary......");
            itinerary = port.bookItinerary(Conv.getBookRequest(Conv.getCreditcard(cardHolderName, cardNumber, year, month), id));   
            fail("An error occured in the BPEL process, this should not happen");
        } catch (BookItineraryFault ex) {
            // the booking failed which is nice. 
        }
    }
}
