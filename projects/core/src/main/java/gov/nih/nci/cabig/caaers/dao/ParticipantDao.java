package gov.nih.nci.cabig.caaers.dao;

import edu.nwu.bioinformatics.commons.CollectionUtils;
import gov.nih.nci.cabig.caaers.domain.Identifier;
import gov.nih.nci.cabig.caaers.domain.Participant;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.util.Arrays;
import java.util.List;


/**
 * @author Rhett Sutphin
 */
public class ParticipantDao extends GridIdentifiableDao<Participant> {
    // these are for getBySubnames
    private static final List<String> SUBSTRING_MATCH_PROPERTIES
        = Arrays.asList("firstName", "lastName");
    private static final List<String> EXACT_MATCH_PROPERTIES
        = Arrays.asList("institutionalPatientNumber");

    @Override
    public Class<Participant> domainClass() {
        return Participant.class;
    }

    public void save(Participant participant) {
        getHibernateTemplate().saveOrUpdate(participant);
    }

    @SuppressWarnings("unchecked")
    public List<Participant> getAll() {
        return getHibernateTemplate().find("from Participant p order by p.lastName, p.firstName");
    }

    /**
     * @param subnames a set of substrings to match
     * @return a list of participants such that each entry in <code>subnames</code> is a
     *  case-insensitive substring match of the participant's name or other identifier
     */
    @SuppressWarnings("unchecked")
    public List<Participant> getBySubnames(String[] subnames) {
        return findBySubname(subnames, SUBSTRING_MATCH_PROPERTIES, EXACT_MATCH_PROPERTIES);
    }

    public Participant getByIdentifier(Identifier identifier) {

    	Criteria criteria = getSession().createCriteria(domainClass());
    	criteria = criteria.createCriteria("identifiers");
    	
    	if(identifier.getType() != null) {
    		criteria.add(Restrictions.eq("type", identifier.getType()));
    	}
    	
    	if(identifier.getSource() != null) {
    		criteria.add(Restrictions.eq("source", identifier.getSource()));
    	}
    	
    	if(identifier.getValue() != null) {
    		criteria.add(Restrictions.eq("value", identifier.getValue()));
    	}    			
    	return (Participant) CollectionUtils.firstElement(criteria.list());		
    	
	}

    
    
}

