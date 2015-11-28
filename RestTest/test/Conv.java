/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


//import org.netbeans.xml.schema.niceviewelements.CreditCardInfoType;
import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.netbeans.xml.schema.lameduckelements.CreditCardInfoType;
import org.netbeans.xml.schema.lameduckelements.FlightInformationType;
import org.netbeans.xml.schema.lameduckelements.GetFlightRequestType;
import org.netbeans.xml.schema.niceviewelements.GetHotelsRequestType;
import org.netbeans.xml.schema.niceviewelements.HotelInformationType;

/**
 *
 * @author Runi
 */
public class Conv {
    private static DatatypeFactory df = new DatatypeFactoryImpl();
       public static CreditCardInfoType getCreditcard(String name, int number, int year, int month){
        CreditCardInfoType creditcard = new CreditCardInfoType();
        creditcard.setCardNumber(number);
        creditcard.setHolderName(name);
        creditcard.setExpirationDate(getDate(year, month, 1, 1, 1));
        return creditcard;
    }

    
    
    
    
        public static  XMLGregorianCalendar getDate(int year, int month, int day, int hour, int minute ){
        return df.newXMLGregorianCalendar(new BigInteger(String.valueOf(year)), month, day, hour, minute, hour, BigDecimal.ZERO, minute);
    }
}
