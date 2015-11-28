/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.group15;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.xml.datatype.XMLGregorianCalendar;
import org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.BookFlightFault;
import org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.LameDuckPortType;
import org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.LameDuckWSDLService;
import org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.BookHotelFault;
import org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.NiceViewWSDLPortType;
import org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.NiceViewWSDLService;
import org.netbeans.xml.schema.lameduckelements.BookFlightRequestType;
import org.netbeans.xml.schema.lameduckelements.CreditCardInfoType;
import org.netbeans.xml.schema.lameduckelements.FlightInfoListType;
import org.netbeans.xml.schema.lameduckelements.FlightInformationType;
import org.netbeans.xml.schema.lameduckelements.FlightType;
import org.netbeans.xml.schema.lameduckelements.GetFlightRequestType;
import org.netbeans.xml.schema.niceviewelements.BookHotelRequestType;
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
                boolean compensationSucces = true; //compensate(it);
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
                    niceViewPort.bookHotel(req);
                    hotel.state = Itinerary.BookingState.PAID;
                } catch (BookHotelFault ex) {
                    //TODO something useful with try catch
                    System.out.println("Booking failed");
                    boolean compensationSucces = true; //compensate(it);
                    it.state= Itinerary.BookingState.CANCELLED;
                    if (compensationSucces) {
                        throw new BookingException("Booking failed - Money refunded");
                    } else {
                        throw new BookingException("Booking failed - Compensation failed :(");
                    }
                }
            }
        }

        return true;
    }

    public boolean cancelItinerary(String itineraryID) {
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

        return null; //TODO implement
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
        return hotelCreditCard;
    }

}
