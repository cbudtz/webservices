/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package niceview.web.dk.ws;

import javax.jws.WebService;

/**
 *
 * @author Kristin
 */
@WebService(serviceName = "HotelReservationService", portName = "HotelReservationBindingPort", endpointInterface = "ws.hotelreservation.HotelReservation", targetNamespace = "http://HotelReservation.ws", wsdlLocation = "WEB-INF/wsdl/NewWebServiceFromWSDL/HotelReservation.wsdl")
public class HotelReservationService {

    public ws.hotelreservation.GetHotelsResponse getHotels(ws.hotelreservation.GetHotelsRequest request) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean bookHotel(ws.hotelreservation.BookHotelRequest request) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public ws.hotelreservation.CancelHotelResponse cancelHotel(ws.hotelreservation.CancelHotelRequest request) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    
}
