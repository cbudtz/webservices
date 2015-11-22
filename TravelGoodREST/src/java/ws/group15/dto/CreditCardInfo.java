/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.group15.dto;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Christian
 */
@XmlRootElement(name="creditcardInfo")
public class CreditCardInfo {
    public int cardNumber;
    public String holderName;
    public Date expirationDate;
    
   
}
