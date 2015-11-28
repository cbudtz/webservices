/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.group15;

import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.group15.dto.Flight;
import ws.group15.dto.FlightInformation;
import ws.group15.dto.HotelInformation;

/**
 *
 * @author Christian
 */
@Path("hotels")
public class HotelsResource {
    private static DatatypeFactory df = new DatatypeFactoryImpl();
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<HotelInformation> getHotels(@QueryParam("city") String city,
            @QueryParam("arrival") String arrivalString,
            @QueryParam("departure") String departureString){
        XMLGregorianCalendar arrival = parseDate(arrivalString);
        XMLGregorianCalendar departure = parseDate(departureString);
        return DataSingleton.getInstance().getHotels(city, arrival, departure);
    } 

    private XMLGregorianCalendar parseDate(String dateString) {
        dateString.replace("\"", "");
        String[] dateStrings = dateString.split("-");
        if (dateStrings.length>=3)
        return getDate(Integer.parseInt(dateStrings[0]), Integer.parseInt(dateStrings[1]), Integer.parseInt(dateStrings[2]), 0, 0);
        return getDate(2015, 1, 1, 0, 0); //return stupid date
    }
    
     public static  XMLGregorianCalendar getDate(int year, int month, int day, int hour, int minute ){
        return df.newXMLGregorianCalendar(new BigInteger(String.valueOf(year)), month, day, hour, minute, hour, BigDecimal.ZERO, minute);
    }
    
}
