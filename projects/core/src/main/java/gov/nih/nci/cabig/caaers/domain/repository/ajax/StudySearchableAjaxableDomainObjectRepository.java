package gov.nih.nci.cabig.caaers.domain.repository.ajax;

import gov.nih.nci.cabig.caaers.dao.query.ajax.AbstractAjaxableDomainObjectQuery;
import gov.nih.nci.cabig.caaers.domain.ajax.StudySearchableAjaxableDomainObject;
import gov.nih.nci.cabig.caaers.domain.ajax.StudySiteAjaxableDomainObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Biju Joseph
 */

@Transactional(readOnly = true)
public class StudySearchableAjaxableDomainObjectRepository<T extends StudySearchableAjaxableDomainObject> extends AbstractAjaxableDomainObjectRepository {

    public List<T> findStudies(final AbstractAjaxableDomainObjectQuery query) {

        List<Object[]> objects = super.find(query);
        List<T> studySearchableAjaxableDomainObjects = new ArrayList<T>();

        for (Object[] o : objects) {
            T studySearchableAjaxableDomainObject = (T) getObjectById(studySearchableAjaxableDomainObjects, (Integer) o[0]);

            if (studySearchableAjaxableDomainObject == null) {
                studySearchableAjaxableDomainObject = (T) BeanUtils.instantiateClass(getObjectClass());
                studySearchableAjaxableDomainObject.setId((Integer) o[0]);
                studySearchableAjaxableDomainObject.setShortTitle((String) o[1]);
                if (o[3] != null && (Boolean) o[3]) {
                    studySearchableAjaxableDomainObject.setPrimaryIdentifierValue((String) o[2]);
                }

                addAdditionalProperties(studySearchableAjaxableDomainObject, o);

                studySearchableAjaxableDomainObjects.add(studySearchableAjaxableDomainObject);

            } else {
                updateAdditionalProperties(studySearchableAjaxableDomainObject, o);

            }


        }
        return studySearchableAjaxableDomainObjects;

    }

    protected void updateAdditionalProperties(T studySearchableAjaxableDomainObject, Object[] o) {
        if (o[3] != null && (Boolean) o[3]) {
            studySearchableAjaxableDomainObject.setPrimaryIdentifierValue((String) o[2]);
        }
        updateFundingSponsor(studySearchableAjaxableDomainObject, o);
        updateStudySite(studySearchableAjaxableDomainObject, o);


    }

    protected void addAdditionalProperties(StudySearchableAjaxableDomainObject studySearchableAjaxableDomainObject, Object[] o) {
        studySearchableAjaxableDomainObject.setPhaseCode((String) o[4]);
        studySearchableAjaxableDomainObject.setStatus((String) o[5]);
        updateFundingSponsor(studySearchableAjaxableDomainObject, o);

        updateStudySite(studySearchableAjaxableDomainObject, o);
    }

    private void updateStudySite(StudySearchableAjaxableDomainObject studySearchableAjaxableDomainObject, Object[] o) {
        if (!StringUtils.isBlank((String) o[7]) && StringUtils.equals((String) o[9], "SST")) {
            StudySiteAjaxableDomainObject studySiteAjaxableDomainObject = new StudySiteAjaxableDomainObject();
            studySiteAjaxableDomainObject.setId((Integer) o[8]);
            studySiteAjaxableDomainObject.setName((String) o[7]);
            studySearchableAjaxableDomainObject.addStudySite(studySiteAjaxableDomainObject);
        }
    }

    private void updateFundingSponsor(StudySearchableAjaxableDomainObject studySearchableAjaxableDomainObject, Object[] o) {
        if (!StringUtils.isBlank((String) o[6])) {
            studySearchableAjaxableDomainObject.setPrimarySponsorCode((String) o[6]);
        }
    }


    protected Class getObjectClass() {
        return StudySearchableAjaxableDomainObject.class;


    }


}