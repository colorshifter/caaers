/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.domain;

import gov.nih.nci.cabig.caaers.domain.meddra.LowLevelTerm;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;

 
/**
 * The Class AgentSpecificCtcTerm.
 *
 * @author Ion C. Olaru
 */
@Entity
@DiscriminatorValue("ctep")
public class AgentSpecificCtcTerm extends AgentSpecificTerm<CtcTerm> {

    /** The other meddra term. */
    private LowLevelTerm otherMeddraTerm;

    /* (non-Javadoc)
     * @see gov.nih.nci.cabig.caaers.domain.AgentSpecificTerm#getTerm()
     */
    @OneToOne
    @JoinColumn(name = "term_id", nullable = false)
    @Override
    public CtcTerm getTerm() {
        return super.getTerm();
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cabig.caaers.domain.AgentSpecificTerm#getFullName()
     */
    @Override
    @Transient
    public String getFullName() {
    	if(getTerm() == null) return "";
    	return getTerm().getFullName() + ((otherMeddraTerm != null) ? " " + otherMeddraTerm.getFullName() : "");
    }

    /**
     * Gets the ctc term.
     *
     * @return CtcTerm
     */
    @Transient
    public CtcTerm getCtcTerm() {
        return super.getTerm();
    }

    /**
     * Sets the ctc term.
     *
     * @param ctcTerm The CTC term
     */
    @Transient
    public void setCtcTerm(CtcTerm ctcTerm) {
        super.setTerm(ctcTerm);
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cabig.caaers.domain.AgentSpecificTerm#copy()
     */
    @Override
    public AgentSpecificCtcTerm copy() {
        return (AgentSpecificCtcTerm) super.copy();
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cabig.caaers.domain.AgentSpecificTerm#isMedDRA()
     */
    @Override
    @Transient
    public boolean isMedDRA() {
    	return false;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cabig.caaers.domain.AgentSpecificTerm#isOtherRequired()
     */
    @Override
    @Transient
    public boolean isOtherRequired() {
        if (getTerm() == null) return false;
        return getTerm().isOtherRequired();
    }

    /**
     * Gets the other meddra term.
     *
     * @return the other meddra term
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "low_level_term_id")
    public LowLevelTerm getOtherMeddraTerm() {
        return otherMeddraTerm;
    }

    /**
     * Sets the other meddra term.
     *
     * @param otherMeddraTerm the new other meddra term
     */
    public void setOtherMeddraTerm(LowLevelTerm otherMeddraTerm) {
        this.otherMeddraTerm = otherMeddraTerm;
    }

    @Override
    public boolean isOfSameTerm(String termName, String termCategory, String terminologyVersion, String otherToxicity, String otherMeddra) {
        
        //is term matching ?
        boolean termMatching = StringUtils.equals(getTerm().getTerm(), termName) ||  StringUtils.equals(getTerm().getCtepTerm(), termName);
        if(StringUtils.isNotEmpty(termName) && !termMatching) return false;
        
        //is category matching ?
        if(StringUtils.isNotEmpty(termCategory) && !StringUtils.equals(getTerm().getCategory().getName() , termCategory)) return false;
        
        //is terminology version matching ?
        if(StringUtils.isNotEmpty(terminologyVersion) && !StringUtils.equals(getTerm().getCategory().getCtc().getName().toString(), terminologyVersion)) return false;
        
        //is otherToxicity matching ?
        if(StringUtils.isNotEmpty(otherToxicity) && !StringUtils.equals(getOtherToxicity(), otherToxicity)) return false;

        //is other Meddra matching ?
        if(StringUtils.isNotEmpty(otherMeddra) && !StringUtils.equals(getOtherMeddraTerm().getTerm(), otherMeddra)) return false;

        return true;
    }
}
