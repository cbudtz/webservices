/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.niceview;

import java.util.Calendar;
import javax.jws.WebService;
import ws.hotelreservation.BookHotelFault;
import ws.hotelreservation.CancelHotelFault;
import ws.hotelreservation.GetHotelsResponse;
import ws.hotelreservation.HotelType;

/**
 *
 * @author Kristin
 */
@WebService(serviceName = "HotelReservationService", portName = "HotelReservationPort", endpointInterface = "ws.hotelreservation.HotelReservation", targetNamespace = "http://HotelReservation.ws", wsdlLocation = "WEB-INF/wsdl/HotelReservationWebService/HotelReservation.wsdl")
public class HotelReservationWebService {

    public ws.hotelreservation.GetHotelsResponse getHotels(ws.hotelreservation.GetHotelsRequest request) {
        GetHotelsResponse ghr = new GetHotelsResponse();
        
        // Return empty list if not searching for hotels in Megaton
        if(!request.getCity().equals("Megaton"))
            return ghr;
        
        // Everything has been booked 'till January 5 2016
        Calendar c = Calendar.getInstance();
        c.set(2016, 0, 5);
        
       // if(!request.getArrivalDate().toGregorianCalendar().after(c))
         //   return ghr;
        
        // Return test data if everything pans out...
        HotelType h = new HotelType();
        h.setBookingNumber(100100);
        h.setCreditRequired(true);
        h.setHotelAddress("Somewhere Fancy 50D");
        h.setHotelName("VawyFancy Hotel");
        h.setServiceName("NiceView");
        h.setTotalPrice(12405.85);
        ghr.getHotels().add(0, h);
        
        h.setBookingNumber(100101);
        h.setCreditRequired(false);
        h.setHotelAddress("Notsofancy Place 50D");
        h.setHotelName("Meh Hotel");
        h.setServiceName("NiceView");
        h.setTotalPrice(3401.29);
        ghr.getHotels().add(1, h);
        
        return ghr;
    }

    public boolean cancelHotel(int request) throws ws.hotelreservation.CancelHotelFault {
        // Remove and return true if parsed booking number exist
        if(request == 100100 || request == 100101)
            return true;
        
        // If booking number doesn't exist throw exception
        throw new CancelHotelFault(
                String.format("Could not cancel hotel with request %d", 
                        request), null);
    }

    public boolean bookHotel(ws.hotelreservation.BookHotelRequest request) throws ws.hotelreservation.BookHotelFault {
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
