/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LameDuck.web.dk;

import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;
import dk.dtu.imm.fastmoney.BankPortType;
import dk.dtu.imm.fastmoney.BankService;
import dk.dtu.imm.fastmoney.CreditCardFaultMessage;
import dk.dtu.imm.fastmoney.types.AccountType;
import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import dk.dtu.imm.fastmoney.types.CreditCardInfoType.ExpirationDate;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.netbeans.xml.schema.lameduckelements.*;
import org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.*;
/**
 *
 * @author Rúni Vørmadal, Jeppe Dickow
 */
@WebService(serviceName = "LameDuckWSDLService", 
            portName = "LameDuckPortTypeBindingPort", 
            endpointInterface = "org.netbeans.j2ee.wsdl.lameduck.wsdl.lameduckwsdl.LameDuckPortType", 
            targetNamespace = "http://j2ee.netbeans.org/wsdl/LameDuck/wsdl/LameDuckWSDL", 
            wsdlLocation = "WEB-INF/wsdl/LameDuckWebService/LameDuckWSDL.wsdl")
public class LameDuckWebservice {

    public  final int DEF_PRIZE = 995;
    public  final int GROUP_NO = 16;
    public  final String DEF_DESTINATION = "Mallorca";
    public  final XMLGregorianCalendar DEF_ARRIVAL_DATE = getDate(2016, 6, 10, 12, 30);
    public  final int DEF_FLIGHT_DURATION = 2;
    private  int BOOKING_NO = 1000000;
    private  final DatatypeFactory df = new DatatypeFactoryImpl();
    private  BankPortType bank = null;
    public  List<FlightInformationType> flightDatabase = new ArrayList<>();
    public  List<FlightInformationType> bookedFlightsDatabase = new ArrayList<>();
    private  final String lameDuckAccountName = "LameDuck";
    private  final String LameDuckAccountNumber = "50208812";

    
    
    public FlightInfoListType getFlights(GetFlightRequestType flightInfo) {
        FlightInfoListType l = new FlightInfoListType();
        ArrayList<FlightInformationType> matches = findFlights(flightInfo.getDestination(), flightInfo.getOrigin(), flightInfo.getFlightDate());
        if(matches.isEmpty()){
        
        FlightInformationType flight = new FlightInformationType();
        // set flight info 
        flight.setFlightPrice(DEF_PRIZE);
        flight.setBookingNumber(BOOKING_NO++);
        
        // set flight type for flight info
        FlightType flightType = new FlightType();     
        flightType.setTakeOff(flightInfo.getFlightDate());
        flightType.setOriginAirport(flightInfo.getOrigin());
        flightType.setDestAirport(flightInfo.getDestination());
        // set arrival time to departure time + DEF_FLIGHT_DURATION
        XMLGregorianCalendar arrival = flightInfo.getFlightDate();
        arrival.setHour(arrival.getHour() + DEF_FLIGHT_DURATION);
        flightType.setArrival(arrival);
        flight.setFlight(flightType);     
        flightDatabase.add(flight);
        l.getFlightInfo().add(flight);
        }else{
            for(FlightInformationType f : matches){
                l.getFlightInfo().add(f);
            }
          }
        return l;
    }
    
    public ArrayList<FlightInformationType> findFlights(String dest, String origin, XMLGregorianCalendar date){
        ArrayList<FlightInformationType> matches = new ArrayList<>();
        for(FlightInformationType flight : flightDatabase){
            String foundDest = flight.getFlight().getDestAirport();
            String foundOrigin = flight.getFlight().getOriginAirport();
            XMLGregorianCalendar foundDate = flight.getFlight().getTakeOff();
            
            if((foundDest != null && foundDest.equalsIgnoreCase(dest)) || dest == null){
                if((foundOrigin != null && foundOrigin.equalsIgnoreCase(origin)) || origin == null){
                    if((foundDate != null && foundDate.equals(date)) || date == null){
                        matches.add(flight);
                    }
                }
            }
        }
        return matches;
    }

    public boolean bookFlight(BookFlightRequestType flightInfo) throws BookFlightFault {
        boolean match = false;
        for(FlightInformationType flight : flightDatabase){
            if(flight.getBookingNumber() == flightInfo.getBookingNumber()){
                dk.dtu.imm.fastmoney.types.CreditCardInfoType creditCard = convert(flightInfo.getCreditcardInfo());
                try {
                    if(getBank().validateCreditCard(GROUP_NO, creditCard, flight.getFlightPrice())){
                        
                        getBank().chargeCreditCard(GROUP_NO, convert(flightInfo.getCreditcardInfo()), flight.getFlightPrice(), getAccount(lameDuckAccountName, LameDuckAccountNumber));
                        bookedFlightsDatabase.add(flight);
                        flightDatabase.remove(flight);
                        match = true;
                    }
                    else{
                        // Should never end in this state
                        String name = flightInfo.getCreditcardInfo().getHolderName();
                        String number = String.valueOf(flightInfo.getCreditcardInfo().getCardNumber());
                        XMLGregorianCalendar expDate = flightInfo.getCreditcardInfo().getExpirationDate();
                        throw new BookFlightFault("CreditCard info invalid or not enough money. ", "cardholder: " + name + "\n cardnumber: " + number + "\n expDate: " + expDate + "\n amount: " + flight.getFlightPrice());
                    }
                } catch (CreditCardFaultMessage ex) {
                    
                    Logger.getLogger(LameDuckWebservice.class.getName()).log(Level.SEVERE, null, ex);
                     String name = flightInfo.getCreditcardInfo().getHolderName();
                        String number = String.valueOf(flightInfo.getCreditcardInfo().getCardNumber());
                        XMLGregorianCalendar expDate = flightInfo.getCreditcardInfo().getExpirationDate();
                        throw new BookFlightFault("CreditCard info invalid or not enough money. ", "cardholder: " + name + "\n cardnumber: " + number + "\n expDate: " + expDate + "\n amount: " + flight.getFlightPrice());
                }
                break;
            }
        }
        if(!match){
            throw new BookFlightFault("Booking id does not exist. ", "booking id: " + flightInfo.getBookingNumber());
        }
        return true;
    }

    public String cancelFlight(CancelFlightRequestType flightInfo) throws CancelFlightFault {
        dk.dtu.imm.fastmoney.types.CreditCardInfoType creditcard = convert(flightInfo.getCreditCardInformation());
        
        try {
            if(!getBank().validateCreditCard(GROUP_NO, creditcard, 0)){
                String name = flightInfo.getCreditCardInformation().getHolderName();
                throw new CancelFlightFault("invalid credit card. ", "name: " + name + "\n creditcard number: " + creditcard.getName() + "\n expiration date: " + creditcard.getExpirationDate().getMonth() + "/" + creditcard.getExpirationDate().getYear());
            }
        } catch (CreditCardFaultMessage ex) {
            Logger.getLogger(LameDuckWebservice.class.getName()).log(Level.SEVERE, null, ex);
        }
        int bookingNo = flightInfo.getBookingNumber();
        for(FlightInformationType flight : bookedFlightsDatabase){
            if(flight.getBookingNumber() == bookingNo){
                AccountType test = new AccountType();

                test.setName(flightInfo.getCreditCardInformation().getHolderName());
                test.setNumber(String.valueOf(flightInfo.getCreditCardInformation().getCardNumber()));
                
                try {
                    if(getBank().refundCreditCard(GROUP_NO, creditcard, flightInfo.getPrice()/2, getAccount(lameDuckAccountName, LameDuckAccountNumber))){
                        flightDatabase.add(flight);
                        bookedFlightsDatabase.remove(flight);
                        return "all ok";
                    }else{
                        throw new CancelFlightFault("refunding failed.", "");
                    }
                } catch (CreditCardFaultMessage ex) {           
                    Logger.getLogger(LameDuckWebservice.class.getName()).log(Level.SEVERE, null, ex);
                    throw new CancelFlightFault("refunding failed.", "creditcard no: " + creditcard.getNumber() + "\n creditcardholder: " + creditcard.getName());
                }
            }
        }     
        return "all ok";
    }
    
    private  XMLGregorianCalendar getDate(int year, int month, int day, int hour, int minute ){
        return df.newXMLGregorianCalendar(new BigInteger(String.valueOf(year)), month, day, hour, minute, hour, BigDecimal.ZERO, minute);
    }
    
    private AccountType getAccount(String name, String number){
        AccountType account = new AccountType();
        account.setName(name);
        account.setNumber(number);
        return account;
    }
    
    private BankPortType getBank(){
        if(bank == null){
            try {
                BankService bankService = new BankService(new URL("http://fastmoney.compute.dtu.dk:8080/BankService?wsdl"));
                bank =  bankService.getBankPort();
            } catch (MalformedURLException ex) {
                Logger.getLogger(LameDuckWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
        return bank;
    }
    private dk.dtu.imm.fastmoney.types.CreditCardInfoType convert(org.netbeans.xml.schema.lameduckelements.CreditCardInfoType info){
        dk.dtu.imm.fastmoney.types.CreditCardInfoType newType = new CreditCardInfoType();
        ExpirationDate expDate = new ExpirationDate();
        expDate.setMonth(info.getExpirationDate().getMonth());
        expDate.setYear(info.getExpirationDate().getYear());
        newType.setExpirationDate(expDate);
        newType.setName(info.getHolderName());
        newType.setNumber(String.valueOf(info.getCardNumber()));
        return newType;
    }
  
}
