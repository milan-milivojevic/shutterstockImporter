
package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.generated.mediapool;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for deleteExternalDownloadLinkByMediaGuid complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="deleteExternalDownloadLinkByMediaGuid">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mediaGuid" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deleteExternalDownloadLinkByMediaGuid", propOrder = {
    "mediaGuid"
})
public class DeleteExternalDownloadLinkByMediaGuid {

    protected Integer mediaGuid;

    /**
     * Gets the value of the mediaGuid property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMediaGuid() {
        return mediaGuid;
    }

    /**
     * Sets the value of the mediaGuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMediaGuid(Integer value) {
        this.mediaGuid = value;
    }

}
