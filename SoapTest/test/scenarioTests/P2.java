/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenarioTests;

import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;
import javax.xml.datatype.DatatypeFactory;
import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TGGetFlightRequestType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TGGetHotelsRequestType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TGItineraryType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWsdlPortType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWsdlService;
import org.netbeans.xml.schema.lameduckelements.FlightInfoListType;
import org.netbeans.xml.schema.lameduckelements.FlightInformationType;
/**
 *
 * @author Runi, Kristin
 */
public class P2 {
    
    DatatypeFactory df = new DatatypeFactoryImpl();
    static TravelGoodWsdlPortType port;
    private TravelGoodWsdlService service = new TravelGoodWsdlService();
    private static String id = "lolLigeGyldig";
    private static final int STATE_UNCONFIRMED = 0;
    private static final int STATE_CONFIRMED = 2;
    private static final int STATE_CANCELLED = 3;
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
    public void testCancelItinerary() {
        // Find flight between DK and HI
        TGGetFlightRequestType f = Conv.getGetFligthRequest("Danmark", "Hawaii", Conv.getDate(2016, 6, 15, 12, 30), id);
        
        // Find the flights, if any were available, between DK and HI
        // We just select the first possible flight (should more than 1 be available)
        System.out.println("Selecting a flight");
        FlightInfoListType flist = port.getFlights(f);
        assertTrue("List not > 0", !flist.getFlightInfo().isEmpty());
        System.out.println("... flights found");
        FlightInformationType fsel = flist.getFlightInfo().get(0);
        System.out.println("... selected flight from carrier " + flist.getFlightInfo().get(0).getFlight().getCarrier());
        
        // Initialize itinerary and add flight
        TGItineraryType it = null;
        System.out.println("Adding to itinerary...");
        it = port.addFlightToItinerary(Conv.convertAddFlightToItinerary(fsel, id));
        assertEquals("Adding flight to itinerary flight list failed.", it.getState(), STATE_UNCONFIRMED);
        assertTrue("Flight list size not as expected (1), failing.", !it.getFlights().getFlightInfo().isEmpty());
        System.out.println("... added to itinerary");
        
        // At this point we choose to cancel our planning of the itinerary
        System.out.println("Trying to cancel itinerary...");
        System.out.println("... itinerary id " + it.getId() + " is being cancelled");
        String m = port.cancelItinerary(it.getId());
        System.out.println("... " + m);
        System.out.println("... fetching current state of itinerary");
        it = port.getItinerary(id);
        assertEquals("... itinerary not cancelled!", it.getState(), STATE_CANCELLED);
        System.out.println("Itinerary state has been succesfully changed to cancelled.");
    }
}
