/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LameDuck.web.dk.service;

import javax.jws.WebService;

/**
 *
 * @author Jeppe Dickow
 */
@WebService(serviceName = "LameDuckWSDLService", portName = "LameDuckPortTypeBindingPort", endpointInterface = "org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.LameDuckPortType", targetNamespace = "http://j2ee.netbeans.org/wsdl/LameDuck/wsdl/LameDuckWSDL", wsdlLocation = "WEB-INF/wsdl/LameDuckWebService/LameDuckWSDL.wsdl")
public class LameDuckWebService {

    public org.netbeans.xml.schema.lameduckelements.FlightInfoListType getFlights(org.netbeans.xml.schema.lameduckelements.GetFlightRequestType flightInfo) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean bookFlight(org.netbeans.xml.schema.lameduckelements.BookFlightRequestType flightInfo) throws org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.BookFlightFault {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.lang.String cancelFlight(org.netbeans.xml.schema.lameduckelements.CancelFlightRequestType flightInfo) throws org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.CancelFlightFault {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    
}
