/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unitTest;

import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Test;
import static org.junit.Assert.*;
import org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.BookHotelFault;
import org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.CancelHotelFault;
import org.netbeans.xml.schema.niceviewelements.BookHotelRequestType;
import org.netbeans.xml.schema.niceviewelements.CancelHotelRequestType;
import org.netbeans.xml.schema.niceviewelements.CreditCardInfoType;
import org.netbeans.xml.schema.niceviewelements.GetHotelsRequestType;
import org.netbeans.xml.schema.niceviewelements.HotelInformationListType;
import org.netbeans.xml.schema.niceviewelements.HotelInformationType;

/**
 *
 * @author Kristin
 */
public class NiceViewTest {
    
    public NiceViewTest() {
    }
    
    @Test
    public void testGetHotels() {
        DatatypeFactory df = new DatatypeFactoryImpl();
        
        GetHotelsRequestType ghrt = null;
        
        assertTrue(getHotels(ghrt).getHotelInformations().isEmpty());
        
        ghrt = getHotelsRequest(null, null, null);
        
        assertTrue(getHotels(ghrt).getHotelInformations().size() == 1);
        
        ghrt = getHotelsRequest("Mallorca", df.newXMLGregorianCalendarDate(2016, 1, 5, 0), df.newXMLGregorianCalendarDate(2016, 2, 1, 0));
        
        HotelInformationListType hotels = getHotels(ghrt);
        List<HotelInformationType> list = hotels.getHotelInformations();
        
        System.out.println(list.size());
        
        assertTrue(list.size() >= 1);
        
        ghrt = getHotelsRequest("Somwehere that does not exist", df.newXMLGregorianCalendarDate(2017, 4, 7, 0), df.newXMLGregorianCalendarDate(2017, 4, 14, 0));
        
        hotels = getHotels(ghrt);
        list = hotels.getHotelInformations();
        
        assertTrue(list.size() == 1);
    }
    
    @Test
    public void testBookHotel() {
        DatatypeFactory df = new DatatypeFactoryImpl();
        CreditCardInfoType c = getCreditCardType(50408816, df.newXMLGregorianCalendarDate(2009, 5, 1, 0), "Anne Strandberg");
        BookHotelRequestType breq;
        boolean s;
        
        try {
            try {
                breq = getBookRequest(1000000, null);
                bookHotel(breq);
                fail("Oh noes");
            } catch(BookHotelFault e) {
                Logger.getLogger(NiceViewTest.class.getName()).log(Level.SEVERE, null, e);
            }
            
            try {
                breq = getBookRequest(2903, null);
                bookHotel(breq);
                fail("Oh noes");
            } catch(BookHotelFault e) {
                Logger.getLogger(NiceViewTest.class.getName()).log(Level.SEVERE, null, e);
            }
            
            breq = getBookRequest(98543, c);
            s = bookHotel(breq);
            assertEquals(s, false);
            
            breq = getBookRequest(1000000, c);
            s = bookHotel(breq);
            assertEquals(s, true);
        } catch(BookHotelFault e) {
            Logger.getLogger(NiceViewTest.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    @Test
    public void testCancelHotel() {
        DatatypeFactory df = new DatatypeFactoryImpl();
        CreditCardInfoType c = getCreditCardType(50408816, df.newXMLGregorianCalendarDate(2009, 5, 1, 0), "Anne Strandberg");
        BookHotelRequestType breq;
        String str;
        CancelHotelRequestType creq;
        
        String ok = "all ok";
        String no = "all ok. no hotels to cancel";
        
        try {
            try {
                creq = null;
                cancelHotel(creq);
                fail("Oh noes");
            } catch(CancelHotelFault e) {
                Logger.getLogger(NiceViewTest.class.getName()).log(Level.SEVERE, null, e);
            }
            
            breq = getBookRequest(1000000, c);
            bookHotel(breq);
            
            creq = getCancelRequest(1000000);
            str = cancelHotel(creq);
            assertEquals(str, ok);
            
            creq.setBookingNumber(5);
            str = cancelHotel(creq);
            assertEquals(str, no);
        } catch(CancelHotelFault e) {
            Logger.getLogger(NiceViewTest.class.getName()).log(Level.SEVERE, null, e);
        } catch(BookHotelFault e) {
            Logger.getLogger(NiceViewTest.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private static boolean bookHotel(org.netbeans.xml.schema.niceviewelements.BookHotelRequestType input) throws BookHotelFault {
        org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.NiceViewWSDLService service = new org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.NiceViewWSDLService();
        org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.NiceViewWSDLPortType port = service.getNiceViewWSDLPortTypeBindingPort();
        return port.bookHotel(input);
    }

    private static String cancelHotel(org.netbeans.xml.schema.niceviewelements.CancelHotelRequestType input) throws CancelHotelFault {
        org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.NiceViewWSDLService service = new org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.NiceViewWSDLService();
        org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.NiceViewWSDLPortType port = service.getNiceViewWSDLPortTypeBindingPort();
        return port.cancelHotel(input);
    }

    private static HotelInformationListType getHotels(org.netbeans.xml.schema.niceviewelements.GetHotelsRequestType input) {
        org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.NiceViewWSDLService service = new org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.NiceViewWSDLService();
        org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.NiceViewWSDLPortType port = service.getNiceViewWSDLPortTypeBindingPort();
        return port.getHotels(input);
    }
    
    private GetHotelsRequestType getHotelsRequest(String c, XMLGregorianCalendar a, XMLGregorianCalendar d){
        GetHotelsRequestType r = new GetHotelsRequestType();
        
        r.setArrivalDate(a);
        r.setDepartureDate(d);
        r.setCity(c);
        
        return r;
    }
    
    private BookHotelRequestType getBookRequest(int n, CreditCardInfoType c) {
        BookHotelRequestType r = new BookHotelRequestType();
        
        r.setBookingNumber(n);
        r.setCreditCardInformation(c);
        
        return r;
    }
    
    private CreditCardInfoType getCreditCardType(int n, XMLGregorianCalendar x, String s) {
        CreditCardInfoType c = new CreditCardInfoType();
        
        c.setCardNumber(n);
        c.setExpirationDate(x);
        c.setHolderName(s);
        
        return c;
    }

    private CancelHotelRequestType getCancelRequest(int i) {
        CancelHotelRequestType r = new CancelHotelRequestType();
        
        r.setBookingNumber(i);
        
        return r;
    }
}