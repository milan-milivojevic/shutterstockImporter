
package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.generated.mediapool;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setOfficial complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setOfficial">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="setOfficialArgument" type="{http://brandmaker.com/webservices/MediaPool/}setOfficialArgument" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setOfficial", propOrder = {
    "setOfficialArgument"
})
public class SetOfficial {

    @XmlElement(namespace = "http://brandmaker.com/webservices/MediaPool/")
    protected SetOfficialArgument setOfficialArgument;

    /**
     * Gets the value of the setOfficialArgument property.
     * 
     * @return
     *     possible object is
     *     {@link SetOfficialArgument }
     *     
     */
    public SetOfficialArgument getSetOfficialArgument() {
        return setOfficialArgument;
    }

    /**
     * Sets the value of the setOfficialArgument property.
     * 
     * @param value
     *     allowed object is
     *     {@link SetOfficialArgument }
     *     
     */
    public void setSetOfficialArgument(SetOfficialArgument value) {
        this.setOfficialArgument = value;
    }

}
