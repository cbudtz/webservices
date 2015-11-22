


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Flight complex type.
 
 <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Flight">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="originAirport" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="destAirport" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="takeOff" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="arrival" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="carrier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FlightType", propOrder = {
    "originAirport",
    "destAirport",
    "takeOff",
    "arrival",
    "carrier"
})
public class Flight {

    @XmlElement(required = true)
    protected String originAirport;
    @XmlElement(required = true)
    protected String destAirport;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar takeOff;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar arrival;
    @XmlElement(required = true)
    protected String carrier;

    /**
     * Gets the value of the originAirport property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginAirport() {
        return originAirport;
    }

    /**
     * Sets the value of the originAirport property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginAirport(String value) {
        this.originAirport = value;
    }

    /**
     * Gets the value of the destAirport property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestAirport() {
        return destAirport;
    }

    /**
     * Sets the value of the destAirport property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestAirport(String value) {
        this.destAirport = value;
    }

    /**
     * Gets the value of the takeOff property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTakeOff() {
        return takeOff;
    }

    /**
     * Sets the value of the takeOff property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTakeOff(XMLGregorianCalendar value) {
        this.takeOff = value;
    }

    /**
     * Gets the value of the arrival property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getArrival() {
        return arrival;
    }

    /**
     * Sets the value of the arrival property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setArrival(XMLGregorianCalendar value) {
        this.arrival = value;
    }

    /**
     * Gets the value of the carrier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarrier() {
        return carrier;
    }

    /**
     * Sets the value of the carrier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarrier(String value) {
        this.carrier = value;
    }

}
