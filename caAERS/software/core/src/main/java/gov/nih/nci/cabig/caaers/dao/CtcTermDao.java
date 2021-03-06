/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.dao;

import gov.nih.nci.cabig.caaers.domain.Ctc;
import gov.nih.nci.cabig.caaers.domain.CtcTerm;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class implements the Data access related operations for the CtcTerm domain object.
 * 
 * @author Rhett Sutphin
 */
@Transactional(readOnly = true)
public class CtcTermDao extends CaaersDao<CtcTerm> {
    private static final List<String> SUBSTRING_MATCH_PROPERTIES = Arrays.asList("term", "ctepTerm", "select");
    private static final List<String> EMPTY_PROPERTIES = Collections.emptyList();
    private static final List<String> EXACT_MATCH_PROPERTIES = Arrays.asList("term", "ctepCode");

    /**
     * Get the Class representation of the domain object that this DAO is representing.
     * 
     * @return Class representation of the domain object that this DAO is representing.
     */
    @Override
    @Transactional(readOnly = true, propagation= Propagation.NOT_SUPPORTED)
    public Class<CtcTerm> domainClass() {
        return CtcTerm.class;
    }

    /**
     * Search for CTC term by identifier.
     * 
     * @param id
     *                The CTC term identifer.
     * @return The CTC term for the given identifier.
     */
    @Override
    public CtcTerm getById(int id) {
        return super.getById(id);
    }

    /**
     * Get the list of CTC terms matching the name fragments.
     * 
     * @param subnames
     *                the name fragments to search on.
     * @param ctcVersionId
     *                The CTC version being used.
     * @param ctcCategoryId
     *                The CTC category of the CTC term.
     * @return List of matching CTC terms.
     */

    public List<CtcTerm> getBySubname(String[] subnames, Integer ctcVersionId, Integer ctcCategoryId) {
        List<Object> extraParams = new LinkedList<Object>();
        StringBuilder extraConds = new StringBuilder("");
        if (ctcVersionId != null) {
            extraConds.append("o.category.ctc.id = ?");
            extraParams.add(ctcVersionId);
        }
        if (ctcCategoryId != null) {
            extraConds.append(" and o.category.id = ?");
            extraParams.add(ctcCategoryId);
        }
        return findBySubname(subnames, extraConds.toString(), extraParams, SUBSTRING_MATCH_PROPERTIES, EMPTY_PROPERTIES);
    }
    
    /**
     * Get the List if CtcTerms matching the provided ctepCode and Version
     * @param ctepCode
     * @param versionName
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CtcTerm> getByCtepCodeandVersion(String ctepCode, String versionName){
    	return getHibernateTemplate().find("select term from CtcTerm term join term.category as category join category.ctc as ctcversion where term.ctepCode = ? and ctcversion.name = ? ", new Object[] {ctepCode,versionName});
    }

    /**
     * 
     * @param subnames
     *                the name fragments to search on.
     * @param ctcVersionId
     *                The CTC version being used.
     * @param ctcCategoryId
     *                The CTC category of the CTC term.
     * @return The matching CTC term.
     */
    public CtcTerm getCtcTerm(String[] subnames, Integer ctcVersionId, Integer ctcCategoryId) {
        List<Object> extraParams = new LinkedList<Object>();
        StringBuilder extraConds = new StringBuilder("");
        if (ctcVersionId != null) {
            extraConds.append("o.category.ctc.id = ?");
            extraParams.add(ctcVersionId);
        }
        if (ctcCategoryId != null) {
            extraConds.append(" and o.category.id = ?");
            extraParams.add(ctcCategoryId);
        }
        List<CtcTerm> ctcTerms = findBySubname(subnames, extraConds.toString(), extraParams,
                        EMPTY_PROPERTIES, EXACT_MATCH_PROPERTIES);
        return ctcTerms.isEmpty() ? null : ctcTerms.get(0);
    }

    /**
     * 
     * @param subnames
     *                subnames the name fragments to search on.
     * @return The matching CTC term.
     */
    @SuppressWarnings("unchecked")
    public CtcTerm getCtcTerm(final String[] subnames) {
        List<CtcTerm> ctcTerms = findBySubname(subnames, EMPTY_PROPERTIES, EXACT_MATCH_PROPERTIES);
        return ctcTerms.isEmpty() ? null : ctcTerms.get(0);
    }

    /**
     * Get the list of all CTC terms.
     * 
     * @return return the list of CTC terms.
     */
    @SuppressWarnings("unchecked")
    public List<CtcTerm> getAll() {
        return getHibernateTemplate().find("from CtcTerm");
    }

    /*
    * @author Ion C. Olaru
    * Get the Entity Ctc Term By Category name, ctcaeVersion & termName
    * 
    * */
    public List<CtcTerm> getCtcTerm(String categoryName, String ctcVersionName, String termName) {
        return getHibernateTemplate().find("from CtcTerm t where t.term = ? and t.category.name = ? and t.category.ctc.name = ?", new Object[] {termName, categoryName, ctcVersionName});
    }
    
    public CtcTerm getByCtepCodeandVersion(String ctepCode , Ctc ctc) {
    	List<CtcTerm> ctcTerms = getHibernateTemplate().find("from CtcTerm t where t.ctepCode = ? and t.category.ctc.id = ?", new Object[] {ctepCode, ctc.getId()});
        return ctcTerms.isEmpty() ? null : ctcTerms.get(0);
    }
    
}
