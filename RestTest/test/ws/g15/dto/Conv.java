package ws.g15.dto;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//import org.netbeans.xml.schema.niceviewelements.CreditCardInfoType;
import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import static org.junit.Assert.assertEquals;
import org.netbeans.xml.schema.lameduckelements.CreditCardInfoType;
import org.netbeans.xml.schema.lameduckelements.FlightInformationType;
import org.netbeans.xml.schema.lameduckelements.GetFlightRequestType;
import org.netbeans.xml.schema.niceviewelements.GetHotelsRequestType;
import org.netbeans.xml.schema.niceviewelements.HotelInformationType;
import ws.g15.dto.CreditCardInfo;
import ws.g15.dto.FlightInformation;

/**
 *
 * @author Runi
 */
public class Conv {

    static Client client = ClientBuilder.newClient();
    private final static String resourceRoot = "http://localhost:8080/TravelGoodREST/webresources/";
    public final static String resourceFlight = resourceRoot + "flights";
    public final static String resourceItinerary = resourceRoot + "itineraries/";
    public final static String resourceHotel = resourceRoot + "hotels";
    
    public final static String[] GET_FLIGHT_PARAM = new String[]{"origin", "destination" ,"departure"};
    public final static String[] GET_FLIGHT_VALUE0 = new String[]{"denmark", "russia", "2016-06-12"};
    public final static String[] GET_FLIGHT_VALUE1 = new String[]{"Faroe Islands", "US and A", "2016-07-12"};
    public final static String[] GET_FLIGHT_VALUE2 = new String[]{"England", "Langbortistan", "2016-03-12"};
    public final static String[] GET_FLIGHT_VALUE3 = new String[]{"failbook", "failbook", "2016-03-12"};
    public final static String[] GET_FLIGHT_VALUE4 = new String[]{"failcancel", "failcancel", "2016-03-12"};
    
    public final static String[] GET_HOTEL_PARAM = new String[]{"city", "arrival" ,"departure"};
    public final static String[] GET_HOTEL_VALUE0 = new String[]{"Langbortistan", "2016-12-12", "2018-06-12"};
    public final static String[] GET_HOTEL_VALUE1 = new String[]{"My Island (Ireland)", "2016-12-12", "2018-06-12"};
    
    public final static String cardName0 = "Thor-Jensen Claus";
    public final static int cardNumber0 = 50408825;
    public final static int cardExpYear0 = 9;
    public final static int cardExpMonth0 = 5;
    
//    Bech Camilla	50408822	7	9	1000
    public final static String cardName1 = "Bech Camilla";
    public final static int cardNumber1 = 50408822;
    public final static int cardExpYear1 = 9;
    public final static int cardExpMonth1 = 7;
    private static DatatypeFactory df = new DatatypeFactoryImpl();

    public static CreditCardInfo getCreditcard(String name, int number, int year, int month) {
        CreditCardInfo creditcard = new CreditCardInfo();
        creditcard.cardNumber = number;
        creditcard.holderName = name;
        creditcard.expirationDate = getDate(year, month, 1, 1, 1);
        return creditcard;
    }

    public static XMLGregorianCalendar getDate(int year, int month, int day, int hour, int minute) {
        return df.newXMLGregorianCalendar(new BigInteger(String.valueOf(year)), month, day, hour, minute, hour, BigDecimal.ZERO, minute);
    }

    public static WebTarget addParam(WebTarget target, String[] param, String[] values) {
        for (int i = 0; i < param.length; i++) {
            target = target.queryParam(param[i], values[i]);
        }
        return target;
    }

    public static <T> List<T> requestGET(WebTarget target, Class<T> type) {
        return target.request()
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<T>>() {
                });
    }

    public static <T> List<FlightInformation> requestGET1(WebTarget target, Class<T> type) {
        return target.request()
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<FlightInformation>>() {
                });
    }

    public static <T> T requestPOST(WebTarget target, Class<T> type, Object entity) {
        return target.request()
                .accept(APPLICATION_JSON)
                .post(Entity.entity(entity, APPLICATION_JSON), type);

    }

    public static Response requestPUT(WebTarget target, Object entity) {
        return target.request().accept(APPLICATION_JSON).put(Entity.entity(entity, APPLICATION_JSON));

    }
    
      public static void checkItineraryStatus(Itinerary itinerary, Itinerary.BookingState expectedState) {
        for (FlightInformation f : itinerary.flights) {
            assertEquals("check state of flight", expectedState, f.bookingState);
        }
        for (HotelInformation h : itinerary.hotels) {
            assertEquals("check state of hotel", expectedState, h.state);
        }
    }

    public static void setNewItineraryState(Itinerary.BookingState state, String itinId){
        WebTarget target = client.target(resourceItinerary + itinId + "/state");
        Response res = requestPUT(target, state.name());
//        assertEquals("check status code after trying update state", 200, res.getStatus());

    }
    public static Itinerary getItinerary(String id) {
        // get itinerary
        WebTarget target = client.target(resourceItinerary + id);
        return target.request().accept(APPLICATION_JSON).get(Itinerary.class);
    }

    public static void setCreditcard(CreditCardInfo creditcard, String itinId){
        WebTarget target = client.target(resourceItinerary + itinId + "/creditcard");
        Response res = requestPUT(target, creditcard);
        assertEquals("check status code after put creditcard", 200, res.getStatus());
    }
    public static List<FlightInformation> getFlights(String[] vals) {
        WebTarget target = client.target(resourceFlight);
        target = addParam(target, GET_FLIGHT_PARAM, vals);
        return target.request().accept(APPLICATION_JSON).get(new GenericType<List<FlightInformation>>() {
        });

    }

    public static void addFlightToItinerary(FlightInformation flight, String itinId) {
        WebTarget target = client.target(resourceItinerary + itinId + "/flights");
        Response res = requestPUT(target, flight);
        assertEquals("check status code after adding flight", 200, res.getStatus());
    }

    public static List<HotelInformation> getHotels(String[] vals) {
        WebTarget target = client.target(resourceHotel);
        target = addParam(target, GET_HOTEL_PARAM, vals);
        return target.request().accept(APPLICATION_JSON).get(new GenericType<List<HotelInformation>>() {
        });

    }

    public static void addHotelToItinerary(HotelInformation hotel, String itinId) {
        WebTarget target = client.target(resourceItinerary + itinId + "/hotels");
        Response res = requestPUT(target, hotel);
        assertEquals("check status code after adding hotel", 200, res.getStatus());
    }
}
