//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-793 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.10.06 at 03:06:21 PM CDT 
//


package logicrepository.parser.logicrepositorysyntax;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MonitorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MonitorType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Monitor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CreationEvents" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MonitorType", propOrder = {
    "type",
    "monitor",
    "creationEvents"
})
public class MonitorType {

    @XmlElement(name = "Type", required = true)
    protected String type;
    @XmlElement(name = "Monitor", required = true)
    protected String monitor;
    @XmlElement(name = "CreationEvents", required = true)
    protected String creationEvents;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the monitor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMonitor() {
        return monitor;
    }

    /**
     * Sets the value of the monitor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMonitor(String value) {
        this.monitor = value;
    }

    /**
     * Gets the value of the creationEvents property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreationEvents() {
        return creationEvents;
    }

    /**
     * Sets the value of the creationEvents property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreationEvents(String value) {
        this.creationEvents = value;
    }

}
