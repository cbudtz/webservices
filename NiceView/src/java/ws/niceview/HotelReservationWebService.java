/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.niceview;

import javax.jws.WebService;

/**
 *
 * @author Krish
 */
@WebService(serviceName = "HotelReservationService", portName = "HotelReservationPort", endpointInterface = "ws.hotelreservation.HotelReservation", targetNamespace = "http://HotelReservation.ws", wsdlLocation = "WEB-INF/wsdl/HotelReservationWebService/HotelReservation.wsdl")
public class HotelReservationWebService {

    public ws.hotelreservation.GetHotelsResponse getHotels(ws.hotelreservation.GetHotelsRequest request) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean cancelHotel(int request) throws ws.hotelreservation.CancelHotelFault {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean bookHotel(ws.hotelreservation.BookHotelRequest request) throws ws.hotelreservation.BookHotelFault {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    
}
