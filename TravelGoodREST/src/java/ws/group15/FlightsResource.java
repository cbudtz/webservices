/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.group15;

import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.group15.dto.Flight;
import ws.group15.dto.FlightInformation;
/**
 *
 * @author Christian
 */
@Path("flights")
public class FlightsResource {
    private static DatatypeFactory df = new DatatypeFactoryImpl();
    //Only allowed action on fligts is GET!
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getFlights(@QueryParam("origin") String origin,
            @QueryParam("destination") String destination,
            @QueryParam("departure") String dateString){
        dateString = dateString.replace("\"", "");
        String[] dateStrings = dateString.split("-");
        System.out.println(dateStrings[0] + ", " + dateStrings[1] + ", " + dateStrings[2]);
        XMLGregorianCalendar date = getDate(Integer.parseInt(dateStrings[0]), Integer.parseInt(dateStrings[1]), Integer.parseInt(dateStrings[2]), 0, 0);
        List<FlightInformation> flights = DataSingleton.getInstance().getFlights(origin, destination, date);
        if (flights == null) flights = new ArrayList<FlightInformation>();
        GenericEntity<List<FlightInformation>> wrap = new GenericEntity<List<FlightInformation>>(flights){};
        Response response = Response.ok(wrap)
                .link("???", "!!!") //TODO: Add lots of allowed links
                .build();
        
        return response;
    }
 
    public static  XMLGregorianCalendar getDate(int year, int month, int day, int hour, int minute ){
        return df.newXMLGregorianCalendar(new BigInteger(String.valueOf(year)), month, day, hour, minute, hour, BigDecimal.ZERO, minute);
    }
}
