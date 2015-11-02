/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package niceview.web.dk.ws;

import java.util.Calendar;
import javax.jws.WebService;
import niceview.niceviewelements.GetHotelsResponse;
import niceview.niceviewelements.HotelType;
import ws.hotelreservation.BookHotelFault;
import ws.hotelreservation.CancelHotelFault;

/**
 *
 * @author Jeppe and Kristin
 */
@WebService(serviceName = "HotelReservationService", portName = "HotelReservationBindingPort", endpointInterface = "ws.hotelreservation.HotelReservation", targetNamespace = "http://HotelReservation.ws", wsdlLocation = "WEB-INF/wsdl/NiceViewWebService/HotelReservation.wsdl")
public class NiceViewWebService {

    public niceview.niceviewelements.GetHotelsResponse getHotels(niceview.niceviewelements.GetHotelsRequest request) {
        GetHotelsResponse ghr = new GetHotelsResponse();
        
        // Return empty list if not searching for hotels in Megaton
        if(!request.getCity().equals("Megaton"))
            return ghr;
        
        // Everything has been booked 'till January 5 2016
        Calendar c = Calendar.getInstance();
        c.set(2016, 0, 5);
        
        if(!request.getArrivalDate().toGregorianCalendar().after(c))
            return ghr;
        
        // Return test data if everything pans out...
        HotelType h = new HotelType();
        h.setBookingNumber(100100);
        h.setCreditRequired(true);
        h.setHotelAddress("Somewhere Fancy 50D");
        h.setHotelName("VawyFancy Hotel");
        h.setServiceName("NiceView");
        h.setTotalPrice(12405.85);
        ghr.getHotels().add(h);
        
        h.setBookingNumber(100101);
        h.setCreditRequired(false);
        h.setHotelAddress("Notsofancy Place 50D");
        h.setHotelName("Meh Hotel");
        h.setServiceName("NiceView");
        h.setTotalPrice(3401.29);
        ghr.getHotels().add(h);
        
        return ghr;
    }

    public boolean cancelHotel(int request) throws CancelHotelFault {
        // Remove and return true if parsed booking number exist
        if(request == 100100 || request == 100101)
            return true;
        
        // If booking number doesn't exist throw exception
        throw new CancelHotelFault(
                String.format("Could not cancel hotel with request %d", 
                        request), null);
    }

    public boolean bookHotel(niceview.niceviewelements.BookHotelRequest request) throws BookHotelFault {
        // Test data is 100100 and 100101 in regard of booking numbers
        // 100100 has creditcard requirement, but FastMoney hasn't been implemented yet
        // - should do validateCreditCard, but for now nothing...
        if(request.getBookingNumber() == 100100 || request.getBookingNumber() == 100101)
            return true;
        
        // If bookingnumber doesn't exist or, say that we actually did do a FastMoney-operation
        // and it was failing, this is where an exception should be thrown accomponied
        // by a appropiate failure message
        throw new BookHotelFault(
                String.format("Could not book hotel with booking number %d", 
                        request.getBookingNumber()), null);
    }
    
}
