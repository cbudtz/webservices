/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package niceview.web.dk;

import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;
import dk.dtu.imm.fastmoney.BankPortType;
import dk.dtu.imm.fastmoney.BankService;
import dk.dtu.imm.fastmoney.CreditCardFaultMessage;
import dk.dtu.imm.fastmoney.types.AccountType;
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
import org.netbeans.xml.schema.niceviewelements.*;
import org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.*;

/**
 *
 * @author Jeppe Dickow and Kristin
 */
@WebService(serviceName = "NiceViewWSDLService", portName = "NiceViewWSDLPortTypeBindingPort", endpointInterface = "org.netbeans.j2ee.wsdl.niceviewtest.dk.niceviewwsdl.NiceViewWSDLPortType", targetNamespace = "http://j2ee.netbeans.org/wsdl/NiceViewTest/dk/NiceViewWSDL", wsdlLocation = "WEB-INF/wsdl/NiceViewWebservice/NiceViewWSDL.wsdl")
public class NiceViewWebservice {
    
    private final int DEF_PRIZE = 995;
    private final int GROUP_NO = 16;
    private final String DEF_HOTEL_ADDRESS = "Mallorca";
    private final String DEF_HOTEL_NAME = "WonderView";
    private final String DEF_SERVICE_NAME = "WonderView";
    private int BOOKING_NO = 1000000;
    private BankPortType bank = null;
    private List<HotelInformationType> hotelDatabase = new ArrayList<>();
    private List<HotelInformationType> bookedHotelsDatabase = new ArrayList<>();
    private final String NiceViewAccountName = "NiceView";
    private final String NiceViewAccountNumber = "50308815";

    
    
    public HotelInformationListType getHotels(GetHotelsRequestType hotelRequest) {
        if(hotelRequest == null) return new HotelInformationListType();
       
        HotelInformationListType l = new HotelInformationListType();
        ArrayList<HotelInformationType> matches = findHotels(hotelRequest.getCity(), hotelRequest.getArrivalDate(), hotelRequest.getDepartureDate());
        if(matches.isEmpty()){
        
        HotelInformationType hotel = new HotelInformationType();
        // set flight info 
        hotel.setCreditCardGuaranteeRequired(Math.random() > 0.5);
        
        hotel.setHotelAddress(hotelRequest.getCity() == null ? DEF_HOTEL_ADDRESS : hotelRequest.getCity());
        hotel.setHotelName(DEF_HOTEL_NAME);
        hotel.setServiceName(DEF_SERVICE_NAME);
        hotel.setStayPrice(DEF_PRIZE);
        hotel.setBookingNumber(BOOKING_NO++);
        
        
        hotelDatabase.add(hotel);
        l.getHotelInformations().add(hotel);
        }else{
            for(HotelInformationType f : matches){
                l.getHotelInformations().add(f);
            }
          }
        return l;
    }
    
    public ArrayList<HotelInformationType> findHotels(String city, XMLGregorianCalendar arrival, XMLGregorianCalendar departure){
        ArrayList<HotelInformationType> matches = new ArrayList<>();
        for(HotelInformationType hotel : hotelDatabase){
           
            String foundAddr = hotel.getHotelAddress();
            
           
            HotelInformationType h = new HotelInformationType();
            if(foundAddr != null && foundAddr.equals(city)){
                matches.add(hotel);
            }
        }
        return matches;
    }

    public boolean bookHotel(BookHotelRequestType hotelRequest) throws BookHotelFault {
        boolean match = false;
        if(hotelRequest == null) throw new BookHotelFault("No argument received. request was: " + hotelRequest, new BookHotelFaultType());
        if(hotelRequest.getCreditCardInformation() == null) throw new BookHotelFault("credit card info is " + hotelRequest.getCreditCardInformation() + ". Creditcard info must be valid", new BookHotelFaultType());
        if(hotelRequest.getCreditCardInformation().getExpirationDate() == null) throw new BookHotelFault("expiration date is " + hotelRequest.getCreditCardInformation().getExpirationDate() +  ". expiration date must be a valid date", new BookHotelFaultType());
        if(hotelRequest.getCreditCardInformation().getHolderName() == null) throw new BookHotelFault("holder name can not be null", new BookHotelFaultType());
        if(hotelRequest.getCreditCardInformation().getHolderName().equals("")) throw new BookHotelFault("holder name is empty", new BookHotelFaultType());
        
        
        for(HotelInformationType hotel : hotelDatabase){
            if(hotel.getBookingNumber() == hotelRequest.getBookingNumber()){
                dk.dtu.imm.fastmoney.types.CreditCardInfoType creditCard = convert(hotelRequest.getCreditCardInformation());
                try {
                    boolean valid = true;
                    if(hotel.isCreditCardGuaranteeRequired()){
                        valid = getBank().validateCreditCard(GROUP_NO, creditCard, hotel.getStayPrice());
                    }
                    if(valid){
                        
                        getBank().chargeCreditCard(GROUP_NO, convert(hotelRequest.getCreditCardInformation()), hotel.getStayPrice(), getAccount(NiceViewAccountName, NiceViewAccountNumber));
                        bookedHotelsDatabase.add(hotel);
                        hotelDatabase.remove(hotel);
                        match = true;
                    }
                    else{
                        // Should never end in this state
                        String name = hotelRequest.getCreditCardInformation().getHolderName();
                        String number = String.valueOf(hotelRequest.getCreditCardInformation().getCardNumber());
                        XMLGregorianCalendar expDate = hotelRequest.getCreditCardInformation().getExpirationDate();
                        throw new BookHotelFault("CreditCard info invalid or not enough money. cardholder: " + name + "\n cardnumber: " + number + "\n expDate: " + expDate + "\n amount: " + hotel.getStayPrice(), new BookHotelFaultType());
                    }
                } catch (CreditCardFaultMessage ex) {                    
                    Logger.getLogger(NiceViewWebservice.class.getName()).log(Level.SEVERE, null, ex);
                     String name = hotelRequest.getCreditCardInformation().getHolderName();
                        String number = String.valueOf(hotelRequest.getCreditCardInformation().getCardNumber());
                        XMLGregorianCalendar expDate = hotelRequest.getCreditCardInformation().getExpirationDate();
                        throw new BookHotelFault("CreditCard info invalid or not enough money. cardholder: " + name + "\n cardnumber: " + number + "\n expDate: " + expDate + "\n amount: " + hotel.getStayPrice(), new BookHotelFaultType());
                }
                break;
            }
        }
        if(!match){
            throw new BookHotelFault("Booking id does not exist. booking id: " + hotelRequest.getBookingNumber(), new BookHotelFaultType());
        }
        return true;
    }

    public String cancelFlight(CancelHotelRequestType hotelRequest) throws CancelHotelFault {
        if(hotelRequest == null) throw new CancelHotelFault("flightinfo is null. you should not do this. ", new CancelHotelFaultType());
      
        int bookingNo = hotelRequest.getBookingNumber();
        for(HotelInformationType hotel : bookedHotelsDatabase){
            if(hotel.getBookingNumber() == bookingNo){
               
                        hotelDatabase.add(hotel);
                        bookedHotelsDatabase.remove(hotel);
                        return "all ok";
            }
        }  
        return "all ok. no hotels to cancel";
    }
    
    private  XMLGregorianCalendar getDate(int year, int month, int day, int hour, int minute ){
        DatatypeFactory df = new DatatypeFactoryImpl();
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
                Logger.getLogger(NiceViewWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
        return bank;
    }
    private dk.dtu.imm.fastmoney.types.CreditCardInfoType convert(CreditCardInfoType info){
        dk.dtu.imm.fastmoney.types.CreditCardInfoType newType = new dk.dtu.imm.fastmoney.types.CreditCardInfoType();
        ExpirationDate expDate = new ExpirationDate();
        expDate.setMonth(info.getExpirationDate().getMonth());
        expDate.setYear(Integer.valueOf(String.valueOf(info.getExpirationDate().getYear()).substring(2, 4)));
        newType.setExpirationDate(expDate);
        newType.setName(info.getHolderName());
        newType.setNumber(String.valueOf(info.getCardNumber()));
        return newType;
    }
  
    
}
