/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.g15.deadlineDiscovery;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import java.util.GregorianCalendar;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;
import org.netbeans.xml.schema.lameduckelements.FlightInfoListType;
import org.netbeans.xml.schema.lameduckelements.FlightInformationType;

/**
 *
 * @author Runi
 */
@WebService(serviceName = "DeadlineDiscoveryService")
public class DeadlineFinder {

    /**
     * This is a sample web service operation
     * @param flights
     * @return deadline as string
     */
    @WebMethod(operationName="findDeadline")
    public String findDeadline(@WebParam(name="flights") FlightInfoListType flights) {
        XMLGregorianCalendar d = null;
        for (FlightInformationType Flightinfo : flights.getFlightInfo()) {   
            XMLGregorianCalendar curDeadline = Flightinfo.getFlight().getTakeOff();
            curDeadline.setDay(curDeadline.getDay()-1);
            XMLGregorianCalendar now = new XMLGregorianCalendarImpl(new GregorianCalendar());
            if (now.compare(d) == DatatypeConstants.LESSER){
                d = curDeadline;
            }
        }
//        'P1Y3M4DT2H4M3.0S'
        
        return d == null ? "":  "P" + d.getYear() + "Y"
                                + d.getMonth() + "M"
                                + d.getDay() + "DT"
                                + d.getHour() + "H" 
                                + d.getMinute() + "M"
                                + d.getSecond() + "S";
        
    }
}
