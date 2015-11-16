/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unitTest;

import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;
import java.util.jar.Pack200;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.BookFlightFault;
import org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.CancelFlightFault;
import org.netbeans.xml.schema.lameduckelements.BookFlightRequestType;
import org.netbeans.xml.schema.lameduckelements.CancelFlightRequestType;
import org.netbeans.xml.schema.lameduckelements.CreditCardInfoType;

/**
 *
 * @author Christian
 */
public class LameDuckTest {
    DatatypeFactory df = new DatatypeFactoryImpl();
    
    public LameDuckTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void getFlightsTest() {
        fail("Not implemented yet");
    }
    
    @Test 
    public void bookFlightTest(){
        boolean failed = false;
        try {
            
            failed = bookFlight(new BookFlightRequestType());
            fail("Should have returned exception");
        } catch (BookFlightFault ex) {
            
            Logger.getLogger(LameDuckTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        BookFlightRequestType req = new BookFlightRequestType();
        req.setBookingNumber(123456);
        try {
            bookFlight(req);
        } catch (BookFlightFault ex) {
            Logger.getLogger(LameDuckTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        req.setCreditcardInfo(new CreditCardInfoType());
        try {
            bookFlight(req);
        } catch (BookFlightFault ex) {
            Logger.getLogger(LameDuckTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Test
    public void cancelFlightTest(){
        try {
            cancelFlight(null);
            fail("Should have returned a CancelFlightFault!");
        } catch (CancelFlightFault ex) {
            Logger.getLogger(LameDuckTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            cancelFlight(new CancelFlightRequestType());
            fail("Should have returned a CancelFlightFault!");
        } catch (CancelFlightFault ex) {
            Logger.getLogger(LameDuckTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        CancelFlightRequestType req = new CancelFlightRequestType();
        req.setBookingNumber(123456);
        try {
            cancelFlight(req);
            fail("Should have thrown CancelFlightFault");
        } catch (CancelFlightFault ex) {
            Logger.getLogger(LameDuckTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        req.setCreditCardInformation(new CreditCardInfoType());
        try {
            cancelFlight(req);
            fail("Should have thrown CancelFlightFault");
        } catch (CancelFlightFault ex) {
            Logger.getLogger(LameDuckTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        CreditCardInfoType cred = new CreditCardInfoType();
                cred.setCardNumber(1234124);
                cred.setExpirationDate(df.newXMLGregorianCalendar(2016, 1, 1, 1, 1, 1, 1, 1));
                cred.setHolderName("Prins Valiant");
        req.setCreditCardInformation(new CreditCardInfoType());
        try {
            cancelFlight(req);
            fail("Should have thrown CancelFlightFault");
        } catch (CancelFlightFault ex) {
            Logger.getLogger(LameDuckTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        req.setPrice(1000000000);
        try {
            cancelFlight(req);
        } catch (CancelFlightFault ex) {
            Logger.getLogger(LameDuckTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
    }

    private static boolean bookFlight(org.netbeans.xml.schema.lameduckelements.BookFlightRequestType flightInfo) throws BookFlightFault {
        org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.LameDuckWSDLService service = new org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.LameDuckWSDLService();
        org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        return port.bookFlight(flightInfo);
    }

    private static String cancelFlight(org.netbeans.xml.schema.lameduckelements.CancelFlightRequestType flightInfo) throws CancelFlightFault {
        org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.LameDuckWSDLService service = new org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.LameDuckWSDLService();
        org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        return port.cancelFlight(flightInfo);
    }
}
