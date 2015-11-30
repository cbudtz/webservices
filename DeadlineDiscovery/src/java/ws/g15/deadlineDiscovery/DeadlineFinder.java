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
     * finds deadline for itinery
     *
     * @param flights
     * @return deadline as string
     */
    @WebMethod(operationName = "findDeadline")
    public String findDeadline(@WebParam(name = "flights") FlightInfoListType flights) {
        XMLGregorianCalendar d = null;
        for (FlightInformationType flight : flights.getFlightInfo()) {
            XMLGregorianCalendar curDeadline = null;
            try {
                curDeadline = flight.getFlight().getTakeOff();
                if (d == null || d.compare(curDeadline) == DatatypeConstants.GREATER) {

                    d = curDeadline;
                }
            } catch (NullPointerException e) {
                System.out.println(e.getStackTrace().toString());
            }

        }
        return d == null ? "" : "P" + d.getYear() + "Y"
                + d.getMonth() + "M"
                + d.getDay() + "DT"
                + d.getHour() + "H"
                + d.getMinute() + "M"
                + d.getSecond() + "S";
    }
}
