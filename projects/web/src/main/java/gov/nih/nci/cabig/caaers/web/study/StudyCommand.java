package gov.nih.nci.cabig.caaers.web.study;

import org.hibernate.annotations.*;
import org.apache.commons.collections15.functors.InstantiateFactory;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.CascadeType;

import gov.nih.nci.cabig.caaers.domain.*;
import gov.nih.nci.cabig.caaers.utils.ProjectedList;
import gov.nih.nci.cabig.caaers.validation.annotation.UniqueObjectInCollection;
import gov.nih.nci.cabig.caaers.validation.annotation.UniqueIdentifierForStudy;
import gov.nih.nci.cabig.ctms.collections.LazyListHelper;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import gov.nih.nci.cabig.caaers.utils.ProjectedList;
import gov.nih.nci.cabig.caaers.validation.annotation.UniqueIdentifierForStudy;
import gov.nih.nci.cabig.caaers.validation.annotation.UniqueObjectInCollection;
import gov.nih.nci.cabig.ctms.collections.LazyListHelper;
import org.apache.commons.collections15.functors.InstantiateFactory;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.*;

/**
 * User: Ion C. Olaru
 * Date: Dec 22, 2008 - 10:44:55 AM
 */

public class StudyCommand {

    private Study study;
    private int studySiteIndex = -1; 
    private String[] diseaseTermIds;
    private String diseaseCategoryAsText;
    private String diseaseLlt;
    private String condition;

    // ----------------------------------------------------------------------------------------------------------------
    
    public static final String STATUS_ADMINISTRATIVELY_COMPLETE = "Administratively Complete";
    public static final String STATUS_ACTIVE = "Active - Trial is open to accrual";

    private String shortTitle;
    private String longTitle;
    private String description;
    private String precis;
    private String phaseCode;
    private AeTerminology aeTerminology;
    private DiseaseTerminology diseaseTerminology;
    private String status;



    private Boolean drugAdministrationTherapyType = Boolean.FALSE;
    private Boolean radiationTherapyType = Boolean.FALSE;
    private Boolean deviceTherapyType = Boolean.FALSE;
    private Boolean surgeryTherapyType = Boolean.FALSE;
    private Boolean behavioralTherapyType = Boolean.FALSE;
    private Boolean caaersXMLType = Boolean.FALSE;
    private Boolean adeersPDFType = Boolean.FALSE;
    private Boolean medwatchPDFType = Boolean.FALSE;
    private Boolean dcpSAEPDFType = Boolean.FALSE;
    private Boolean ciomsPDFType = Boolean.FALSE;
    private Boolean ciomsSaePDFType = Boolean.FALSE;
    private Integer loadStatus = LoadStatus.COMPLETE.getCode();

    // Used to facilitate import of a coordinating center / funding sponsor
    private FundingSponsor fundingSponsor;
    private CoordinatingCenter coordinatingCenter;

    // DCP specific properties
    private Design design;

    private List<Epoch> epochs=new ArrayList<Epoch>();

    public StudyCommand() {
    }

    // TODO: this stuff should really, really not be in here. It's web-view/entry specific.

    public String getDescription() {
        return description;
    }

    public void setDescription(final String descriptionText) {
        description = descriptionText;
    }

    public String getLongTitle() {
        return longTitle;
    }

    public void setLongTitle(final String longTitleText) {
        longTitle = longTitleText;
    }

    public String getPhaseCode() {
        return phaseCode;
    }

    public void setPhaseCode(final String phaseCode) {
        this.phaseCode = phaseCode;
    }

    public String getPrecis() {
        return precis;
    }

    public void setPrecis(final String precisText) {
        precis = precisText;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(final String shortTitleText) {
        shortTitle = shortTitleText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public Integer getLoadStatus() {
        return loadStatus;
    }

    public void setLoadStatus(Integer loadStatus) {
        this.loadStatus = loadStatus;
    }

    public void setSurgeryTherapyType(final Boolean surgeryTherapyType) {
        this.surgeryTherapyType = surgeryTherapyType;
    }

    @Transient
    public Boolean getBehavioralTherapyType() {
        return behavioralTherapyType;
    }

    public void setBehavioralTherapyType(final Boolean behavioralTherapyType) {
        this.behavioralTherapyType = behavioralTherapyType;
    }

    @Transient
    public CoordinatingCenter getCoordinatingCenter() {
        return coordinatingCenter;
    }

    @Transient
    public FundingSponsor getFundingSponsor() {
        return fundingSponsor;
    }

    public void setCoordinatingCenter(CoordinatingCenter coordinatingCenter) {
        this.coordinatingCenter = coordinatingCenter;
    }

    public void setFundingSponsor(FundingSponsor fundingSponsor) {
        this.fundingSponsor = fundingSponsor;
    }

    // ----------------------------------------------------------------------------------------------------------------

    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    @Transient
    public int getStudySiteIndex() {
        return studySiteIndex; 
    }

    public void setStudySiteIndex(final int studySiteIndex) {
        this.studySiteIndex = studySiteIndex;
    }

    @Transient
    public String[] getDiseaseTermIds() {
        return diseaseTermIds;
    }

    public void setDiseaseTermIds(final String[] diseaseTermIds) {
        this.diseaseTermIds = diseaseTermIds;
    }

    @Transient
    public String getDiseaseCategoryAsText() {
        return diseaseCategoryAsText;
    }

    public void setDiseaseCategoryAsText(final String diseaseCategoryAsText) {
        this.diseaseCategoryAsText = diseaseCategoryAsText;
    }

    @Transient
    public String getDiseaseLlt() {
        return diseaseLlt;
    }

    public void setDiseaseLlt(final String diseaseLlt) {
        this.diseaseLlt = diseaseLlt;
    }

    @Transient
    public String getCondition() {
        return condition;
    }

    public void setCondition(final String condition) {
        this.condition = condition;
    }


}
