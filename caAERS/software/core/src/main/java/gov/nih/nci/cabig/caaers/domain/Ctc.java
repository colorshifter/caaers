/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.domain;

import gov.nih.nci.cabig.ctms.domain.AbstractImmutableDomainObject;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;

 
/**
 * This class represents the Ctc domain object associated with the Adverse event report.
 * 
 * @author Rhett Sutphin
 */
@Entity
@Table(name = "ctc_versions")
public class Ctc extends AbstractImmutableDomainObject {
    
	/** The CT c_ v2. */
	public static int CTC_V2 = 2;
	
	/** The CT c_ v3. */
	public static int CTC_V3 = 3;
	
	/** The CT c_ v4. */
	public static int CTC_V4 = 4;

    /** The name. */
    private String name;
    
    /** The categories. */
    private List<CtcCategory> categories;

    // //// BEAN PROPERTIES

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the categories.
     *
     * @return the categories
     */
    @OneToMany(mappedBy = "ctc")
    @OrderBy
    // by ID for consistency
    @Cascade(value = { CascadeType.LOCK })
    @Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public List<CtcCategory> getCategories() {
        return categories;
    }

    /**
     * Sets the categories.
     *
     * @param categories the new categories
     */
    public void setCategories(List<CtcCategory> categories) {
        this.categories = categories;
    }
}
