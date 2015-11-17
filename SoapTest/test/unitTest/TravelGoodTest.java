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
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TGGetFlightRequestType;
import org.netbeans.xml.schema.lameduckelements.FlightInfoListType;
import org.netbeans.xml.schema.lameduckelements.GetFlightRequestType;

/**
 *
 * @author SharkGaming
 */
public class TravelGoodTest {
    DatatypeFactory df = new DatatypeFactoryImpl();
    
    
    @Test
    public void testingTest(){
        initiateItinerary(10);
        TGGetFlightRequestType request = new TGGetFlightRequestType();
        GetFlightRequestType reqInfo = new GetFlightRequestType();
        reqInfo.setDestination("hawai");
        reqInfo.setFlightDate(df.newXMLGregorianCalendar(new BigInteger(String.valueOf(2016)), 1, 1, 1, 1, 1, BigDecimal.ZERO, 1));
        reqInfo.setOrigin("Danmark");
        request.setFlightRequest(reqInfo);
        request.setItineraryId(10);
        getFlights(request);
    }

    private static int initiateItinerary(int itineraryId4) {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWsdlService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWsdlService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWsdlPortType port = service.getTravelGoodWsdlPortTypeBindingPort();
        return port.initiateItinerary(itineraryId4);
    }

    private static FlightInfoListType getFlights(org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TGGetFlightRequestType input1) {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWsdlService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWsdlService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWsdlPortType port = service.getTravelGoodWsdlPortTypeBindingPort();
        return port.getFlights(input1);
    }

    
}
