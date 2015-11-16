/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package niceview.web.dk;

import java.util.Calendar;
import javax.jws.WebService;
import org.netbeans.xml.schema.niceviewelements.*;
import org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.*;

/**
 *
 * @author Jeppe Dickow and Kristin
 */
@WebService(serviceName = "NiceViewWSDLService", portName = "NiceViewWSDLPortTypeBindingPort", endpointInterface = "org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.NiceViewWSDLPortType", targetNamespace = "http://j2ee.netbeans.org/wsdl/NiceViewTest/dk/NiceViewWSDL", wsdlLocation = "WEB-INF/wsdl/NiceViewWebservice/NiceViewWSDL.wsdl")
public class NiceViewWebservice {

    public HotelInformationListType getHotels(GetHotelsRequestType input) {
        HotelInformationListType hotelList = new HotelInformationListType();
        
        // Return empty list if not searching for hotels in Megaton
        if(!input.getCity().equals("Megaton"))
            return hotelList;
        
        // Everything has been booked 'till January 5 2016
        Calendar c = Calendar.getInstance();
        c.set(2016, 0, 5);
        
       // if(!request.getArrivalDate().toGregorianCalendar().after(c))
         //   return ghr;
        
        // Return test data if everything pans out...
        HotelInformationType h = new HotelInformationType();
        h.setBookingNumber(100100);
        h.setCreditCardGuaranteeRequired(true);
        h.setHotelAddress("Somewhere Fancy 50D");
        h.setHotelName("VawyFancy Hotel");
        h.setServiceName("NiceView");
        h.setStayPrice(12405);
        hotelList.getHotelInformations().add(0, h);
        
        h.setBookingNumber(100101);
        h.setCreditCardGuaranteeRequired(false);
        h.setHotelAddress("Notsofancy Place 50D");
        h.setHotelName("Meh Hotel");
        h.setServiceName("NiceView");
        h.setStayPrice(3401);
        hotelList.getHotelInformations().add(1, h);
        
        return hotelList;
    }

    public boolean bookHotel(BookHotelRequestType input) throws BookHotelFault {
        // Test data is 100100 and 100101 in regard of booking numbers
        // 100100 has creditcard requirement, but FastMoney hasn't been implemented yet
        // - should do validateCreditCard, but for now nothing...
        if(input.getBookingNumber() == 100100 || input.getBookingNumber() == 100101)
            return true;
        
        // If bookingnumber doesn't exist or, say that we actually did do a FastMoney-operation
        // and it was failing, this is where an exception should be thrown accomponied
        // by a appropiate failure message
        throw new BookHotelFault(
                String.format("Could not book hotel with booking number %d", 
                        input.getBookingNumber()), null);
    }

    public String cancelHotel(CancelHotelRequestType input) throws CancelHotelFault {
        // Remove and return true if parsed booking number exist
        if(input.getBookingNumber() == 100100 || input.getBookingNumber() == 100101)
            return "Successfully cancelled the Hotel";
        
        // If booking number doesn't exist throw exception
        throw new CancelHotelFault(
                String.format("Could not cancel hotel with request %d", 
                        input.getBookingNumber()), null);
    }
    
}
