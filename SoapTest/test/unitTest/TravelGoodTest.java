/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unitTest;

import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.ws.Holder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.*;
import org.netbeans.xml.schema.lameduckelements.FlightInfoListType;
import org.netbeans.xml.schema.lameduckelements.GetFlightRequestType;
import org.netbeans.xml.schema.niceviewelements.GetHotelsRequestType;

/**
 *
 * @author SharkGaming
 */
public class TravelGoodTest {
    DatatypeFactory df = new DatatypeFactoryImpl();
    TravelGoodWsdlPortType port; 
    private int sessionId; 
    private Boolean setupFinished = false; 
    
    @Before
    public void setUp(){
        if(!setupFinished){
        TravelGoodWsdlService service = new TravelGoodWsdlService();
        port = service.getTravelGoodWsdlPortTypeBindingPort();
        sessionId = 11; 
        //port.initiateItinerary(sessionId);
        setupFinished = true; 
        }
    } 
    
    
    @Test
    public void testGetFlights(){
        TGGetFlightRequestType request = new TGGetFlightRequestType();
        GetFlightRequestType reqInfo = new GetFlightRequestType();
        reqInfo.setDestination("hawai");
        reqInfo.setFlightDate(df.newXMLGregorianCalendar(new BigInteger(String.valueOf(2016)), 1, 1, 1, 1, 1, BigDecimal.ZERO, 1));
        reqInfo.setOrigin("Danmark");
        request.setFlightRequest(reqInfo);
        request.setItineraryId(sessionId);
        port.getFlights(request);
    }
    
    @Test
    public void testGetHotels(){
        TGGetHotelsRequestType request = new TGGetHotelsRequestType();
        GetHotelsRequestType reqInfo = new GetHotelsRequestType();
        reqInfo.setArrivalDate(df.newXMLGregorianCalendar(new BigInteger(String.valueOf(2016)), 1, 1, 1, 1, 1, BigDecimal.ZERO, 1));
        reqInfo.setDepartureDate(df.newXMLGregorianCalendar(2016, 1, 1, 1, 1, 1, 1, 1));
        reqInfo.setCity("Havanna");
        request.setItineraryId(sessionId);
        request.setRequest(reqInfo);
        port.getHotels(request);
    }
    
    @Test
    public void testGetItinerary(){
        port.getItinerary(sessionId);
    }
}
