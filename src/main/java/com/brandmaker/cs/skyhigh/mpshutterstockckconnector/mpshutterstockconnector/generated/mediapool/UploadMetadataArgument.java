
package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.generated.mediapool;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for uploadMetadataArgument complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="uploadMetadataArgument">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="addAssociations" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="approve" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="approveDescription" type="{http://brandmaker.com/webservices/MediaPool/}languageItem" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="articleDescription" type="{http://brandmaker.com/webservices/MediaPool/}languageItem" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="articleNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="associations" type="{http://brandmaker.com/webservices/MediaPool/}themeDto" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="countryCodes" type="{http://brandmaker.com/webservices/MediaPool/}code" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="creatorName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://brandmaker.com/webservices/MediaPool/}languageItem" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="designationType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fontChecked" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hideIfNotValid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isHires" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="keepIfEmtpy" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="keyword" type="{http://brandmaker.com/webservices/MediaPool/}languageItem" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="license" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="licenseName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mediaGuid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mediaHash" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mediaIsins" type="{http://brandmaker.com/webservices/MediaPool/}isin" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="mediaNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mediaTitle" type="{http://brandmaker.com/webservices/MediaPool/}languageItem" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="official" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="platform" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="programVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="selectedAffiliate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="show" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strict" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="structuredKeywords" type="{http://brandmaker.com/webservices/MediaPool/}structuredKeywords" minOccurs="0"/>
 *         &lt;element name="validDateFrom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="validDateTo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="virtualDbName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "uploadMetadataArgument", propOrder = {
    "addAssociations",
    "approve",
    "approveDescription",
    "articleDescription",
    "articleNumber",
    "associations",
    "countryCodes",
    "creatorName",
    "description",
    "designationType",
    "fileName",
    "fontChecked",
    "hideIfNotValid",
    "isHires",
    "keepIfEmtpy",
    "keyword",
    "license",
    "licenseName",
    "mediaGuid",
    "mediaHash",
    "mediaIsins",
    "mediaNumber",
    "mediaTitle",
    "official",
    "platform",
    "programVersion",
    "selectedAffiliate",
    "show",
    "strict",
    "structuredKeywords",
    "validDateFrom",
    "validDateTo",
    "virtualDbName"
})
@XmlSeeAlso({
    GetMediaDetailsResult.class
})
public class UploadMetadataArgument {

    protected Boolean addAssociations;
    protected String approve;
    @XmlElement(nillable = true)
    protected List<LanguageItem> approveDescription;
    @XmlElement(nillable = true)
    protected List<LanguageItem> articleDescription;
    protected String articleNumber;
    @XmlElement(nillable = true)
    protected List<ThemeDto> associations;
    @XmlElement(nillable = true)
    protected List<Code> countryCodes;
    protected String creatorName;
    @XmlElement(nillable = true)
    protected List<LanguageItem> description;
    protected String designationType;
    protected String fileName;
    protected String fontChecked;
    protected String hideIfNotValid;
    protected String isHires;
    protected Boolean keepIfEmtpy;
    @XmlElement(nillable = true)
    protected List<LanguageItem> keyword;
    protected String license;
    protected String licenseName;
    protected String mediaGuid;
    protected String mediaHash;
    @XmlElement(nillable = true)
    protected List<Isin> mediaIsins;
    protected String mediaNumber;
    @XmlElement(nillable = true)
    protected List<LanguageItem> mediaTitle;
    protected Boolean official;
    protected String platform;
    protected String programVersion;
    protected String selectedAffiliate;
    protected String show;
    protected Boolean strict;
    protected StructuredKeywords structuredKeywords;
    protected String validDateFrom;
    protected String validDateTo;
    protected String virtualDbName;

    /**
     * Gets the value of the addAssociations property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAddAssociations() {
        return addAssociations;
    }

    /**
     * Sets the value of the addAssociations property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAddAssociations(Boolean value) {
        this.addAssociations = value;
    }

    /**
     * Gets the value of the approve property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApprove() {
        return approve;
    }

    /**
     * Sets the value of the approve property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApprove(String value) {
        this.approve = value;
    }

    /**
     * Gets the value of the approveDescription property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the approveDescription property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApproveDescription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LanguageItem }
     * 
     * 
     */
    public List<LanguageItem> getApproveDescription() {
        if (approveDescription == null) {
            approveDescription = new ArrayList<LanguageItem>();
        }
        return this.approveDescription;
    }

    /**
     * Gets the value of the articleDescription property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the articleDescription property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getArticleDescription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LanguageItem }
     * 
     * 
     */
    public List<LanguageItem> getArticleDescription() {
        if (articleDescription == null) {
            articleDescription = new ArrayList<LanguageItem>();
        }
        return this.articleDescription;
    }

    /**
     * Gets the value of the articleNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArticleNumber() {
        return articleNumber;
    }

    /**
     * Sets the value of the articleNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArticleNumber(String value) {
        this.articleNumber = value;
    }

    /**
     * Gets the value of the associations property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the associations property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssociations().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ThemeDto }
     * 
     * 
     */
    public List<ThemeDto> getAssociations() {
        if (associations == null) {
            associations = new ArrayList<ThemeDto>();
        }
        return this.associations;
    }

    /**
     * Gets the value of the countryCodes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the countryCodes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCountryCodes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Code }
     * 
     * 
     */
    public List<Code> getCountryCodes() {
        if (countryCodes == null) {
            countryCodes = new ArrayList<Code>();
        }
        return this.countryCodes;
    }

    /**
     * Gets the value of the creatorName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * Sets the value of the creatorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreatorName(String value) {
        this.creatorName = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the description property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDescription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LanguageItem }
     * 
     * 
     */
    public List<LanguageItem> getDescription() {
        if (description == null) {
            description = new ArrayList<LanguageItem>();
        }
        return this.description;
    }

    /**
     * Gets the value of the designationType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesignationType() {
        return designationType;
    }

    /**
     * Sets the value of the designationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesignationType(String value) {
        this.designationType = value;
    }

    /**
     * Gets the value of the fileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the value of the fileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileName(String value) {
        this.fileName = value;
    }

    /**
     * Gets the value of the fontChecked property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFontChecked() {
        return fontChecked;
    }

    /**
     * Sets the value of the fontChecked property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFontChecked(String value) {
        this.fontChecked = value;
    }

    /**
     * Gets the value of the hideIfNotValid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHideIfNotValid() {
        return hideIfNotValid;
    }

    /**
     * Sets the value of the hideIfNotValid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHideIfNotValid(String value) {
        this.hideIfNotValid = value;
    }

    /**
     * Gets the value of the isHires property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsHires() {
        return isHires;
    }

    /**
     * Sets the value of the isHires property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsHires(String value) {
        this.isHires = value;
    }

    /**
     * Gets the value of the keepIfEmtpy property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isKeepIfEmtpy() {
        return keepIfEmtpy;
    }

    /**
     * Sets the value of the keepIfEmtpy property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setKeepIfEmtpy(Boolean value) {
        this.keepIfEmtpy = value;
    }

    /**
     * Gets the value of the keyword property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the keyword property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKeyword().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LanguageItem }
     * 
     * 
     */
    public List<LanguageItem> getKeyword() {
        if (keyword == null) {
            keyword = new ArrayList<LanguageItem>();
        }
        return this.keyword;
    }

    /**
     * Gets the value of the license property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLicense() {
        return license;
    }

    /**
     * Sets the value of the license property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLicense(String value) {
        this.license = value;
    }

    /**
     * Gets the value of the licenseName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLicenseName() {
        return licenseName;
    }

    /**
     * Sets the value of the licenseName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLicenseName(String value) {
        this.licenseName = value;
    }

    /**
     * Gets the value of the mediaGuid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMediaGuid() {
        return mediaGuid;
    }

    /**
     * Sets the value of the mediaGuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMediaGuid(String value) {
        this.mediaGuid = value;
    }

    /**
     * Gets the value of the mediaHash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMediaHash() {
        return mediaHash;
    }

    /**
     * Sets the value of the mediaHash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMediaHash(String value) {
        this.mediaHash = value;
    }

    /**
     * Gets the value of the mediaIsins property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mediaIsins property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMediaIsins().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Isin }
     * 
     * 
     */
    public List<Isin> getMediaIsins() {
        if (mediaIsins == null) {
            mediaIsins = new ArrayList<Isin>();
        }
        return this.mediaIsins;
    }

    /**
     * Gets the value of the mediaNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMediaNumber() {
        return mediaNumber;
    }

    /**
     * Sets the value of the mediaNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMediaNumber(String value) {
        this.mediaNumber = value;
    }

    /**
     * Gets the value of the mediaTitle property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mediaTitle property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMediaTitle().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LanguageItem }
     * 
     * 
     */
    public List<LanguageItem> getMediaTitle() {
        if (mediaTitle == null) {
            mediaTitle = new ArrayList<LanguageItem>();
        }
        return this.mediaTitle;
    }

    /**
     * Gets the value of the official property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOfficial() {
        return official;
    }

    /**
     * Sets the value of the official property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOfficial(Boolean value) {
        this.official = value;
    }

    /**
     * Gets the value of the platform property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * Sets the value of the platform property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlatform(String value) {
        this.platform = value;
    }

    /**
     * Gets the value of the programVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProgramVersion() {
        return programVersion;
    }

    /**
     * Sets the value of the programVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProgramVersion(String value) {
        this.programVersion = value;
    }

    /**
     * Gets the value of the selectedAffiliate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelectedAffiliate() {
        return selectedAffiliate;
    }

    /**
     * Sets the value of the selectedAffiliate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelectedAffiliate(String value) {
        this.selectedAffiliate = value;
    }

    /**
     * Gets the value of the show property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShow() {
        return show;
    }

    /**
     * Sets the value of the show property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShow(String value) {
        this.show = value;
    }

    /**
     * Gets the value of the strict property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isStrict() {
        return strict;
    }

    /**
     * Sets the value of the strict property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setStrict(Boolean value) {
        this.strict = value;
    }

    /**
     * Gets the value of the structuredKeywords property.
     * 
     * @return
     *     possible object is
     *     {@link StructuredKeywords }
     *     
     */
    public StructuredKeywords getStructuredKeywords() {
        return structuredKeywords;
    }

    /**
     * Sets the value of the structuredKeywords property.
     * 
     * @param value
     *     allowed object is
     *     {@link StructuredKeywords }
     *     
     */
    public void setStructuredKeywords(StructuredKeywords value) {
        this.structuredKeywords = value;
    }

    /**
     * Gets the value of the validDateFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValidDateFrom() {
        return validDateFrom;
    }

    /**
     * Sets the value of the validDateFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidDateFrom(String value) {
        this.validDateFrom = value;
    }

    /**
     * Gets the value of the validDateTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValidDateTo() {
        return validDateTo;
    }

    /**
     * Sets the value of the validDateTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidDateTo(String value) {
        this.validDateTo = value;
    }

    /**
     * Gets the value of the virtualDbName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVirtualDbName() {
        return virtualDbName;
    }

    /**
     * Sets the value of the virtualDbName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVirtualDbName(String value) {
        this.virtualDbName = value;
    }

}
