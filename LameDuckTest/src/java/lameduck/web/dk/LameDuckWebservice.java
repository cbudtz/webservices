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

    public final int DEF_PRIZE = 995;
    public final int GROUP_NO = 15;
    public final String DEF_DESTINATION = "Mallorca";
    public final String DEF_ORIGIN = "Copenhagen";
    public final XMLGregorianCalendar DEF_ARRIVAL_DATE = getDate(2016, 6, 10, 12, 30);
    public final int DEF_FLIGHT_DURATION = 2;
    public final String DEF_CARRIER = "FlameDuck";
    private int BOOKING_NO = 1000000;
    private BankPortType bank = null;
    public static List<FlightInformationType> flightDatabase = new ArrayList<>();
    public static List<FlightInformationType> bookedFlightsDatabase = new ArrayList<>();
    private final String lameDuckAccountName = "LameDuck";
    private final String LameDuckAccountNumber = "50208812";
    private final int failAndCrashNumCancel = 99999999;
    private final int failAndCrashNumBook = 66666666;
    private final String failCancel = "FailCancel";
    private final String failBook = "FailBook";

    public FlightInfoListType getFlights(GetFlightRequestType flightInfo) {
        if (flightInfo == null) {
            return new FlightInfoListType();
        }

        FlightInfoListType l = new FlightInfoListType();
        ArrayList<FlightInformationType> matches = findFlights(flightInfo.getDestination(), flightInfo.getOrigin(), flightInfo.getFlightDate());
        if (matches.isEmpty()) {

            FlightInformationType flight = new FlightInformationType();
            // set flight info 
            flight.setFlightPrice(DEF_PRIZE);
            if(flightInfo.getDestination().equalsIgnoreCase(failCancel)){
                flight.setBookingNumber(failAndCrashNumCancel);
            }
            else if(flightInfo.getDestination().equalsIgnoreCase(failBook)){
                flight.setBookingNumber(failAndCrashNumBook);
            }
            else{
                flight.setBookingNumber(BOOKING_NO++);
            }
            // set flight type for flight info
            FlightType flightType = new FlightType();
            flightType.setTakeOff(flightInfo.getFlightDate() == null ? DEF_ARRIVAL_DATE : flightInfo.getFlightDate());
            flightType.setOriginAirport(flightInfo.getOrigin() == null ? DEF_ORIGIN : flightInfo.getOrigin());
            flightType.setDestAirport(flightInfo.getDestination() == null ? DEF_DESTINATION : flightInfo.getDestination());
            // set arrival time to departure time + DEF_FLIGHT_DURATION
            XMLGregorianCalendar arrival = flightInfo.getFlightDate() == null ? DEF_ARRIVAL_DATE : (XMLGregorianCalendar) flightInfo.getFlightDate().clone();
            arrival.setHour(arrival.getHour() + DEF_FLIGHT_DURATION);
            flightType.setArrival(arrival);
            flightType.setCarrier(DEF_CARRIER);
            flight.setFlight(flightType);
            flightDatabase.add(flight);
            l.getFlightInfo().add(flight);
        } else {
            for (FlightInformationType f : matches) {
                l.getFlightInfo().add(f);
            }
        }
        return l;
    }

    public ArrayList<FlightInformationType> findFlights(String dest, String origin, XMLGregorianCalendar date) {
        ArrayList<FlightInformationType> matches = new ArrayList<>();
        for (FlightInformationType flight : flightDatabase) {
            String foundDest = flight.getFlight().getDestAirport();
            String foundOrigin = flight.getFlight().getOriginAirport();
            XMLGregorianCalendar foundDate = flight.getFlight().getTakeOff();

            if ((foundDest != null && foundDest.equalsIgnoreCase(dest)) || dest == null) {
                if ((foundOrigin != null && foundOrigin.equalsIgnoreCase(origin)) || origin == null) {
                    if ((foundDate != null && foundDate.equals(date)) || date == null) {
                        matches.add(flight);
                    }
                }
            }
        }
        return matches;
    }

    public boolean bookFlight(BookFlightRequestType flightInfo) throws BookFlightFault {
        boolean match = false;
        if (flightInfo == null) {
            throw new BookFlightFault("No argument received.", "request was: " + flightInfo);
        }
        if (flightInfo.getCreditcardInfo() == null) {
            throw new BookFlightFault("credit card info is " + flightInfo.getCreditcardInfo(), "Creditcard info must be valid");
        }
        if (flightInfo.getCreditcardInfo().getExpirationDate() == null) {
            throw new BookFlightFault("expiration date is " + flightInfo.getCreditcardInfo().getExpirationDate(), "expiration date must be a valid date");
        }
        if (flightInfo.getCreditcardInfo().getHolderName() == null) {
            throw new BookFlightFault("holder name can not be null", "pull yourself together");
        }
        if (flightInfo.getCreditcardInfo().getHolderName().equals("")) {
            throw new BookFlightFault("holder name is empty", "");
        }

        if(flightInfo.getBookingNumber() == failAndCrashNumBook){
            throw new BookFlightFault("Booking failed", "Booking failed");
        }
        
        for (FlightInformationType flight : flightDatabase) {
            System.out.println("check booking id: " + flight.getBookingNumber() + "==" + flightInfo.getBookingNumber());
            if (flight.getBookingNumber() == flightInfo.getBookingNumber()) {
                dk.dtu.imm.fastmoney.types.CreditCardInfoType creditCard = convert(flightInfo.getCreditcardInfo());
                System.out.println("match found: " + (flight.getBookingNumber()-flightInfo.getBookingNumber()));
                try {
                    getBank().chargeCreditCard(GROUP_NO, convert(flightInfo.getCreditcardInfo()), flight.getFlightPrice(), getAccount(lameDuckAccountName, LameDuckAccountNumber));
                    bookedFlightsDatabase.add(flight);
                    flightDatabase.remove(flight);
                    match = true;
                } catch (CreditCardFaultMessage ex) {
                    
                    Logger.getLogger(LameDuckWebservice.class.getName()).log(Level.SEVERE, null, ex);
                    throw new BookFlightFault("charge creditcard threw an exception. ", "cardholder: " + flightInfo.getCreditcardInfo().getHolderName()
                                                +   " \n cardno: " + flightInfo.getCreditcardInfo().getCardNumber()
                                                +   " \n exp date: " + flightInfo.getCreditcardInfo().getExpirationDate() );
                }

                break;
            }
        }
        System.out.println("match bool is: " + match);
        if(!match){
            throw new BookFlightFault("Booking id does not exist. ", "could not find booking id: " + flightInfo.getBookingNumber());
        }
        return true;
    }

    public String cancelFlight(CancelFlightRequestType flightInfo) throws CancelFlightFault {
        if (flightInfo == null) {
            throw new CancelFlightFault("flightinfo is null", "you should not do this. ");
        }
        if (flightInfo.getCreditCardInformation() == null) {
            throw new CancelFlightFault("creditcardinfo is null", "you will not get any money. hahahaha.");
        }
        if (flightInfo.getCreditCardInformation().getExpirationDate() == null) {
            throw new CancelFlightFault("exp date is null. it is not valid. you cant get any money", "");
        }
        
        if(flightInfo.getBookingNumber() == failAndCrashNumCancel){
            throw new CancelFlightFault("Cancelling failed", "Cancelling failed");
        }
        
        dk.dtu.imm.fastmoney.types.CreditCardInfoType creditcard = convert(flightInfo.getCreditCardInformation());

        try {
            if (!getBank().validateCreditCard(GROUP_NO, creditcard, 0)) {
                String name = flightInfo.getCreditCardInformation().getHolderName();
                throw new CancelFlightFault("invalid credit card. ", "name: " + name + "\n creditcard number: " + creditcard.getName() + "\n expiration date: " + creditcard.getExpirationDate().getMonth() + "/" + creditcard.getExpirationDate().getYear());
            }
        } catch (CreditCardFaultMessage ex) {
            Logger.getLogger(LameDuckWebservice.class.getName()).log(Level.SEVERE, null, ex);
        }
        int bookingNo = flightInfo.getBookingNumber();
        for (FlightInformationType flight : bookedFlightsDatabase) {
            if (flight.getBookingNumber() == bookingNo) {
                AccountType test = new AccountType();

                test.setName(flightInfo.getCreditCardInformation().getHolderName());
                test.setNumber(String.valueOf(flightInfo.getCreditCardInformation().getCardNumber()));

                try {
                    if (getBank().refundCreditCard(GROUP_NO, creditcard, flightInfo.getPrice() / 2, getAccount(lameDuckAccountName, LameDuckAccountNumber))) {
                        flightDatabase.add(flight);
                        bookedFlightsDatabase.remove(flight);
                        return "all ok";
                    } else {
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

    private XMLGregorianCalendar getDate(int year, int month, int day, int hour, int minute) {
        DatatypeFactory df = new DatatypeFactoryImpl();
        return df.newXMLGregorianCalendar(new BigInteger(String.valueOf(year)), month, day, hour, minute, hour, BigDecimal.ZERO, minute);
    }

    private AccountType getAccount(String name, String number) {
        AccountType account = new AccountType();
        account.setName(name);
        account.setNumber(number);
        return account;
    }

    private BankPortType getBank() {
        if (bank == null) {
            try {
                BankService bankService = new BankService(new URL("http://fastmoney.compute.dtu.dk:8080/BankService?wsdl"));
                bank = bankService.getBankPort();
            } catch (MalformedURLException ex) {
                Logger.getLogger(LameDuckWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return bank;
    }

    private dk.dtu.imm.fastmoney.types.CreditCardInfoType convert(org.netbeans.xml.schema.lameduckelements.CreditCardInfoType info) {
        dk.dtu.imm.fastmoney.types.CreditCardInfoType newType = new CreditCardInfoType();
        ExpirationDate expDate = new ExpirationDate();
        expDate.setMonth(info.getExpirationDate().getMonth());
        String yearStr = "00" + String.valueOf(info.getExpirationDate().getYear());
        expDate.setYear(Integer.valueOf(yearStr.substring(yearStr.length() - 2, yearStr.length())));
        newType.setExpirationDate(expDate);
        newType.setName(info.getHolderName());
        newType.setNumber(String.valueOf(info.getCardNumber()));
        return newType;
    }

}
