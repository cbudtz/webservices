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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.BookFlightFault;
import org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.CancelFlightFault;
import org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.LameDuckPortType;
import org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.LameDuckWSDLService;
import org.netbeans.xml.schema.lameduckelements.BookFlightRequestType;
import org.netbeans.xml.schema.lameduckelements.CancelFlightRequestType;
import org.netbeans.xml.schema.lameduckelements.CreditCardInfoType;
import org.netbeans.xml.schema.lameduckelements.FlightInfoListType;
import org.netbeans.xml.schema.lameduckelements.FlightInformationType;
import org.netbeans.xml.schema.lameduckelements.GetFlightRequestType;

/**
 *
 * @author Christian
 */
public class LameDuckTest {
    DatatypeFactory df = new DatatypeFactoryImpl();
    LameDuckPortType lameDuck;
    
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
        try {
            LameDuckWSDLService wsdl = new LameDuckWSDLService(new URL("http://localhost:8080/LameDuckTest/LameDuckWSDLService?wsdl"));
            lameDuck = wsdl.getLameDuckPortTypeBindingPort();
        } catch (MalformedURLException ex) {
            Logger.getLogger(LameDuckTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void getFlightsTest() {
        getFlights(null);
        GetFlightRequestType flightRequest = new GetFlightRequestType();
        FlightInfoListType something = getFlights(flightRequest);
        flightRequest.setDestination("Test Dest");
        FlightInfoListType flights = getFlights(flightRequest);
        List<FlightInformationType> flightInfos = flights.getFlightInfo();
        assertTrue(flightInfos.size()>0);
        flightRequest.setFlightDate(df.newXMLGregorianCalendar(new BigInteger(String.valueOf(2016)), 1, 1, 1, 1, 1, BigDecimal.ZERO, 1));
        flights = getFlights(flightRequest);
        assertTrue(flights.getFlightInfo().get(0).getFlight().getOriginAirport().equals("Copenhagen"));
        assertTrue(flights.getFlightInfo().get(0).getFlight().getCarrier().equals("FlameDuck"));
        assertTrue(flights.getFlightInfo().get(0).getFlight().getDestAirport().equals("Test Dest"));
        assertEquals(flights.getFlightInfo().get(0).getFlight().getTakeOff(), flightRequest.getFlightDate());
        assertTrue(flights.getFlightInfo().get(0).getFlight().getTakeOff().equals(flightRequest.getFlightDate()));
        
        
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
        String succes="";
        GetFlightRequestType flightRequest = new GetFlightRequestType();
        flightRequest.setDestination("Mallorca");
        flightRequest.setFlightDate(df.newXMLGregorianCalendarDate(2016,1, 1, 1));
        FlightInfoListType flightlist = getFlights(flightRequest);
        FlightInformationType flight = flightlist.getFlightInfo().get(0);
        
        BookFlightRequestType bookRequest = new BookFlightRequestType();
        bookRequest.setBookingNumber(flight.getBookingNumber());
        CreditCardInfoType creditCard = new CreditCardInfoType();
        creditCard.setCardNumber(50408823);
//        XMLGregorianCalendar date = df.newXMLGregorianCalendar(new BigInteger(String.valueOf(2010)), 9, 1, 1, 1,1, BigDecimal.ZERO, 1);
//        int value = Integer.valueOf(String.valueOf(date.getYear()).substring(2, 4));
//        assertEquals("value fuckup", value, 10);
        creditCard.setExpirationDate(df.newXMLGregorianCalendar(new BigInteger(String.valueOf(2010)), 9, 1, 1, 1,1, BigDecimal.ZERO, 1));
        creditCard.setHolderName("Tobiasen Inge");
        bookRequest.setCreditcardInfo(creditCard); 
       boolean succes2=false;
        try {
            succes2 = bookFlight(bookRequest);
        } catch (BookFlightFault ex) {            
            Logger.getLogger(LameDuckTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("ARGH! fault: " + ex.getMessage() + "\n" + ex.getFaultInfo());
        }
        assertTrue(succes2);
        
        req.setBookingNumber(flight.getBookingNumber());
        req.setCreditCardInformation(creditCard);
        req.setPrice(flight.getFlightPrice());
        try {
            succes = cancelFlight(req);
        } catch (CancelFlightFault ex) {
            Logger.getLogger(LameDuckTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Should have worked!");
        }
        assertEquals("all ok", succes);     
    }

    private boolean bookFlight(org.netbeans.xml.schema.lameduckelements.BookFlightRequestType flightInfo) throws BookFlightFault {
        return lameDuck.bookFlight(flightInfo);
    }

    private String cancelFlight(org.netbeans.xml.schema.lameduckelements.CancelFlightRequestType flightInfo) throws CancelFlightFault {
        return lameDuck.cancelFlight(flightInfo);
    }

    private FlightInfoListType getFlights(org.netbeans.xml.schema.lameduckelements.GetFlightRequestType flightInfo) {
        return lameDuck.getFlights(flightInfo);
    }
}
