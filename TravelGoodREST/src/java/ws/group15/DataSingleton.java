/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.group15;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.XMLGregorianCalendar;
import org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.BookFlightFault;
import org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.CancelFlightFault;
import org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.LameDuckPortType;
import org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.LameDuckWSDLService;
import org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.BookHotelFault;
import org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.CancelHotelFault;
import org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.NiceViewWSDLPortType;
import org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.NiceViewWSDLService;
import org.netbeans.xml.schema.lameduckelements.BookFlightRequestType;
import org.netbeans.xml.schema.lameduckelements.CancelFlightRequestType;
import org.netbeans.xml.schema.lameduckelements.CreditCardInfoType;
import org.netbeans.xml.schema.lameduckelements.FlightInfoListType;
import org.netbeans.xml.schema.lameduckelements.FlightInformationType;
import org.netbeans.xml.schema.lameduckelements.FlightType;
import org.netbeans.xml.schema.lameduckelements.GetFlightRequestType;
import org.netbeans.xml.schema.niceviewelements.BookHotelRequestType;
import org.netbeans.xml.schema.niceviewelements.CancelHotelRequestType;
import org.netbeans.xml.schema.niceviewelements.GetHotelsRequestType;
import org.netbeans.xml.schema.niceviewelements.HotelInformationListType;
import org.netbeans.xml.schema.niceviewelements.HotelInformationType;
import ws.group15.dto.CreditCardInfo;
import ws.group15.dto.Flight;
import ws.group15.dto.FlightInformation;
import ws.group15.dto.HotelInformation;
import ws.group15.dto.Itinerary;

/**
 *
 * @author Christian
 */
public class DataSingleton {

    private static DataSingleton instance;
    private Hashtable<String, Itinerary> itineraries; //<userId, <itineraryId,Itinea
    private static LameDuckPortType lameDuckPort;
    private static NiceViewWSDLPortType niceViewPort;

    private DataSingleton() {
        itineraries = new Hashtable<>();
        LameDuckWSDLService lameDuckService = new LameDuckWSDLService();
        lameDuckPort = lameDuckService.getLameDuckPortTypeBindingPort();
        NiceViewWSDLService niceViewService = new NiceViewWSDLService();
        niceViewPort = niceViewService.getNiceViewWSDLPortTypeBindingPort();
    }

    public static synchronized DataSingleton getInstance() {
        if (instance == null) {
            instance = new DataSingleton();
        }
        return instance;
    }

    //POST in itineraries
    public Itinerary createItinerary() {
        String itineraryID = UUID.randomUUID().toString();
        Itinerary it = new Itinerary();
        it.id = itineraryID;
        itineraries.put(itineraryID, it);
        return it;
    }

    //GET on itineraries/{itId}
    public Itinerary getItineraryById(String itineraryID) {
        return itineraries.get(itineraryID);
    }

    //GET on itineraries //Not part of requirements specification! Made for fun and practice
    public List<Itinerary> getItineraries() {
        ArrayList<Itinerary> itineraryList = new ArrayList<>();
        for (Map.Entry<String, Itinerary> entrySet : itineraries.entrySet()) {
            itineraryList.add(entrySet.getValue());
        }
        return itineraryList;
    }

    public boolean setCreditCard(CreditCardInfo creditCard, String itineraryID) {
        Itinerary it = itineraries.get(itineraryID);
        if (it == null) {
            return false;
        }
        it.creditCard = creditCard;
        return true;
    }

    public boolean bookItinerary(String itineraryID) throws BookingException {
        Itinerary it = itineraries.get(itineraryID);
        if (it.state == Itinerary.BookingState.CONFIRMED || it.state == Itinerary.BookingState.PAID) {
            throw new BookingException("Cannot book Itinerary - already booked");
        }
        if (it.state == Itinerary.BookingState.CANCELLED) {
            throw new BookingException("Cannot book Itinerary - is canceled");
        }
        if (it == null) {
            throw new BookingException("No such Itinerary!");
        }
        if (it.creditCard == null) {
            throw new BookingException("No creditcard entered");
        }
        //try to book flights!
        boolean succes = true;

        for (FlightInformation flight : it.flights) {
            BookFlightRequestType req = createRequestFromFlightInfo(flight, it.creditCard);
            try {
                lameDuckPort.bookFlight(req);
                flight.bookingState = Itinerary.BookingState.PAID;
            } catch (BookFlightFault ex) {
                //TODO: something useful with fault;
                succes = false;
                it.state = Itinerary.BookingState.CANCELLED;
                System.out.println("Booking failed");
                boolean compensationSucces = compensate(it);
                if (compensationSucces) {
                    throw new BookingException("Booking failed - Money refunded");
                } else {
                    throw new BookingException("Booking failed - Compensation failed :(");
                }
            }
        }
        if (succes) { //everything went well with the flight - now try the hotels
            for (HotelInformation hotel : it.hotels) {
                BookHotelRequestType req = createRequestFromHotelInfo(hotel, it.creditCard);
                try {
                    System.out.println("request: " + req.getBookingNumber() + ";" 
                            + req.getCreditCardInformation().getHolderName()
                            + req.getCreditCardInformation().getCardNumber()
                            + req.getCreditCardInformation().getExpirationDate());
                    niceViewPort.bookHotel(req);
                    hotel.state = Itinerary.BookingState.PAID;
                } catch (BookHotelFault ex) {
                    
                    //TODO something useful with try catch
                    System.out.println("Booking failed" + ex.getMessage());
                    boolean compensationSucces = compensate(it);
                    it.state = Itinerary.BookingState.CANCELLED;
                    if (compensationSucces) {
                        throw new BookingException("Booking failed - Money refunded");
                    } else {
                        throw new BookingException("Booking failed - Compensation failed :(");
                    }
                }
            }
        }
        it.state = Itinerary.BookingState.PAID;
        return true;
    }

    public boolean cancelItinerary(String itineraryID) {
        Itinerary itin = getItineraryById(itineraryID);
        switch(itin.state){
            case CANCELLED:
            case PLANNING:
                itin.state = Itinerary.BookingState.CANCELLED;
                break;
            case CONFIRMED:
                return false;
            case PAID:
               if(compensate(itin)){
                   itin.state = Itinerary.BookingState.CANCELLED;
                   return true;
               }else{
                   
                   return false;
               }
                
        }
        //TODO lots of magic to cancel itinerary - return true if everyThing went well
        return true;
    }

    //PUT on flights
    public void addFlightToItinerary(String itineraryID, FlightInformation flightInfo) {
        itineraries.get(itineraryID).flights.add(flightInfo);
    }

    //PUT on hotels
    public void addHotelToItinerary(String itineraryID, HotelInformation hotelInformation) {
        itineraries.get(itineraryID).hotels.add(hotelInformation);
    }

    //Contact lameDuck and get some flights
    public List<FlightInformation> getFlights(String origin, String destination, XMLGregorianCalendar departure) {
        GetFlightRequestType flightRequest = new GetFlightRequestType();
        flightRequest.setOrigin(origin);
        flightRequest.setDestination(destination);
        flightRequest.setFlightDate(departure);
        FlightInfoListType lameResponse = lameDuckPort.getFlights(flightRequest);
        ArrayList<FlightInformation> flightInfos = new ArrayList<>();
        for (FlightInformationType type : lameResponse.getFlightInfo()) {
            FlightInformation flightInfo = parseFlightInformationType(type);
            flightInfos.add(flightInfo);
        }
        return flightInfos; //TODO implement
    }

    //Contact NiceView and get som hotels
    public List<HotelInformation> getHotels(String city, XMLGregorianCalendar arrival, XMLGregorianCalendar departure) {
        GetHotelsRequestType request = new GetHotelsRequestType();
        request.setArrivalDate(arrival);
        request.setCity(city);
        request.setDepartureDate(departure);
        HotelInformationListType res = niceViewPort.getHotels(request);
        List<HotelInformation> list = new ArrayList<>();
        for (HotelInformationType hotel : res.getHotelInformations()) {
            HotelInformation h = parseHotelInformationTypeToHotelinformation(hotel);
//                    
            list.add(h);
        }
        return list;
    }

    private boolean compensate(Itinerary it) {
        boolean compensationSucces = true;
        //cancel flights
        for (FlightInformation flight : it.flights) {
            if (flight.bookingState == Itinerary.BookingState.PAID) {
                try {
                    lameDuckPort.cancelFlight(parseFlightInformationToCancelRequest(flight, it.creditCard));
                } catch (CancelFlightFault ex) {
                   compensationSucces = false;
                    System.out.println("cancelation of flight: " + flight.bookingNumber + "failed"); //Not part of requirements
                }
            }
        }
        for (HotelInformation hotel : it.hotels) {
            if(hotel.state == Itinerary.BookingState.PAID){
                try {
                    niceViewPort.cancelHotel(parseHotelInformationToCancelRequest(hotel));
                } catch (CancelHotelFault ex) {
                    compensationSucces = false;
                    System.out.println("cancelation of hotel: " + hotel.bookingNumber + "failed");
                }
            }
        }

         //cancel hotels
        
        return compensationSucces;
    }

    //Parsers----------------------
    private FlightInformation parseFlightInformationType(FlightInformationType type) {
        FlightInformation flightInformation = new FlightInformation();
        flightInformation.bookingNumber = type.getBookingNumber();
        flightInformation.bookingState = parseState(type.getState());
        flightInformation.flight = parseFlightType(type.getFlight());
        flightInformation.flightPrice = type.getFlightPrice();
        flightInformation.serviceName = type.getServiceName();
        return flightInformation;
    }

    private Itinerary.BookingState parseState(int stateInt) {
        switch (stateInt) {
            case 0:
                return Itinerary.BookingState.PLANNING;
            case 1:
                return Itinerary.BookingState.PAID;
            case 2:
                return Itinerary.BookingState.CONFIRMED;
            case 3:
                return Itinerary.BookingState.CANCELLED;
        }
        return Itinerary.BookingState.PLANNING;
    }

    private Flight parseFlightType(FlightType flightType) {
        Flight flight = new Flight();
        flight.setTakeOff(flightType.getTakeOff());
        flight.setArrival(flightType.getArrival());
        flight.setCarrier(flightType.getCarrier());
        flight.setDestAirport(flightType.getDestAirport());
        flight.setOriginAirport(flightType.getOriginAirport());
        return flight;
        //To change body of generated methods, choose Tools | Templates.
    }

    private BookFlightRequestType createRequestFromFlightInfo(FlightInformation flight, CreditCardInfo creditCard) {
        BookFlightRequestType request = new BookFlightRequestType();
        request.setBookingNumber(flight.bookingNumber);
        request.setCreditcardInfo(parseFlightCreditCardInfo(creditCard));
        return request;
    }

    private CreditCardInfoType parseFlightCreditCardInfo(CreditCardInfo creditCard) {
        CreditCardInfoType creditType = new CreditCardInfoType();
        creditType.setCardNumber(creditCard.cardNumber);
        creditType.setExpirationDate(creditCard.expirationDate);
        creditType.setHolderName(creditCard.holderName);
        return creditType;
    }

    private BookHotelRequestType createRequestFromHotelInfo(HotelInformation hotel, CreditCardInfo creditCard) {
        BookHotelRequestType hotelReqType = new BookHotelRequestType();
        hotelReqType.setBookingNumber(hotel.bookingNumber);
        hotelReqType.setCreditCardInformation(parseHotelCreditCardInfo(creditCard));
        return hotelReqType;
    }

    private org.netbeans.xml.schema.niceviewelements.CreditCardInfoType parseHotelCreditCardInfo(CreditCardInfo creditCard) {
        org.netbeans.xml.schema.niceviewelements.CreditCardInfoType hotelCreditCard = new org.netbeans.xml.schema.niceviewelements.CreditCardInfoType();
        hotelCreditCard.setCardNumber(creditCard.cardNumber);
        hotelCreditCard.setExpirationDate(creditCard.expirationDate);
        hotelCreditCard.setHolderName(creditCard.holderName);
        return hotelCreditCard;
    }

    private CancelFlightRequestType parseFlightInformationToCancelRequest(FlightInformation flight, CreditCardInfo creditCard) {
        CancelFlightRequestType req = new CancelFlightRequestType();
        req.setBookingNumber(flight.bookingNumber);
        req.setPrice(flight.flightPrice);
        req.setCreditCardInformation(parseFlightCreditCardInfo(creditCard));
        return req;
        
    }

    private CancelHotelRequestType parseHotelInformationToCancelRequest(HotelInformation hotel) {
        CancelHotelRequestType req = new CancelHotelRequestType();
        req.setBookingNumber(hotel.bookingNumber);
        return req;
    }

    private HotelInformation parseHotelInformationTypeToHotelinformation(HotelInformationType hotel) {
        HotelInformation h = new HotelInformation();
            h.bookingNumber = hotel.getBookingNumber();
            h.creditCardGuaranteeRequired = hotel.isCreditCardGuaranteeRequired();
            h.hotelAddress = hotel.getHotelAddress();
            h.hotelName = hotel.getHotelName();
            h.serviceName = hotel.getServiceName();
            h.state = parseState(hotel.getState());
            h.stayPrice = hotel.getStayPrice();
            return h;
    }

}
