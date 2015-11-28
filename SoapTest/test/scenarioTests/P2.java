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
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWsdlPortType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWsdlService;
/**
 *
 * @author Runi
 */
public class P2 {
    
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
        public void testCancelItinerary(){
        
            
    
    }
}
