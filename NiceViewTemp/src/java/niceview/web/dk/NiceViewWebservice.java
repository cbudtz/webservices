/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package niceview.web.dk;

import javax.jws.WebService;
import org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.BookHotelFault;
import org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.CancelHotelFault;

/**
 *
 * @author Runi
 */
@WebService(serviceName = "NiceViewWSDLService", portName = "NiceViewWSDLPortTypeBindingPort", endpointInterface = "org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.NiceViewWSDLPortType", targetNamespace = "http://j2ee.netbeans.org/wsdl/NiceViewTest/dk/NiceViewWSDL", wsdlLocation = "WEB-INF/wsdl/NiceViewWebservice/NiceViewWSDL.wsdl")
public class NiceViewWebservice {

    public org.netbeans.xml.schema.niceviewelements.HotelInformationListType getHotels(org.netbeans.xml.schema.niceviewelements.GetHotelsRequestType input) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean bookHotel(org.netbeans.xml.schema.niceviewelements.BookHotelRequestType input) throws BookHotelFault {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.lang.String cancelHotel(org.netbeans.xml.schema.niceviewelements.CancelHotelRequestType input) throws CancelHotelFault {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    
}
