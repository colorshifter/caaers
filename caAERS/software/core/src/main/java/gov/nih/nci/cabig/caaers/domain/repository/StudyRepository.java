package gov.nih.nci.cabig.caaers.domain.repository;

import gov.nih.nci.cabig.caaers.dao.ResearchStaffDao;
import gov.nih.nci.cabig.caaers.dao.StudyDao;
import gov.nih.nci.cabig.caaers.dao.query.AbstractQuery;
import gov.nih.nci.cabig.caaers.dao.query.ResearchStaffQuery;
import gov.nih.nci.cabig.caaers.dao.query.StudyQuery;
import gov.nih.nci.cabig.caaers.domain.ResearchStaff;
import gov.nih.nci.cabig.caaers.domain.SiteResearchStaff;
import gov.nih.nci.cabig.caaers.domain.SiteResearchStaffRole;
import gov.nih.nci.cabig.caaers.domain.Study;
import gov.nih.nci.cabig.caaers.domain.StudyOrganization;
import gov.nih.nci.cabig.caaers.domain.StudyPersonnel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Biju Joseph
 */
@Transactional(readOnly = true)
public class StudyRepository {

    private StudyDao studyDao;
    private ResearchStaffDao researchStaffDao;

    /**
     * Search using a sample populate Study object
     *
     * @param study the study object
     * @return List of Study objects based on the sample study object
     * @throws Exception runtime exception object
     */
    public List<Study> search(Study study) throws Exception {
        return studyDao.searchByExample(study, true);
    }

    @Transactional(readOnly = false)
    public void synchronizeStudyPersonnel(Study study) {
        //Identify newly added StudyOrganizations to associate ResearchStaff
        //whose associateAllStudies flag is set to true.
        ResearchStaffQuery query = null;
        List<ResearchStaff> researchStaffs = null;
        for(StudyOrganization studyOrganization : study.getStudyOrganizations()){
            researchStaffs = new ArrayList<ResearchStaff>();
            query= new ResearchStaffQuery();
            query.filterByAssociateAllStudies(true);
            query.filterByOrganization(Integer.toString(studyOrganization.getOrganization().getId()));
            researchStaffs = researchStaffDao.getLocalResearchStaff(query);
            for(ResearchStaff researchStaff : researchStaffs){
            	study.syncStudyPersonnel(researchStaff);
            }
        }
    }

    /**
     * Saves a study object
     *
     * @param study the study object
     * @throws Exception runtime exception object
     */

    @Transactional(readOnly = false)
    public void save(Study study){
    	
    	//Save the study
        studyDao.save(study);
    }

    @Required
    public void setStudyDao(final StudyDao studyDao) {
        this.studyDao = studyDao;
    }

    public List<Study> find(final AbstractQuery query) {
        return studyDao.find(query);
    }
    
    //Associate the ResearchStaff to all the Studies 
    public void associateStudyPersonnel(ResearchStaff researchStaff) throws Exception{
    	StudyQuery query = null;
    	List<Study> studies = null;
    	for(SiteResearchStaff siteResearchStaff : researchStaff.getSiteResearchStaffs()){
    		query = new StudyQuery();
    		query.joinStudyOrganization();
    		query.filterByOrganizationId(siteResearchStaff.getOrganization().getId());
    		studies = studyDao.find(query);
    		for(Study study : studies){
    			study.syncStudyPersonnel(researchStaff);
    			studyDao.save(study);	
    		}
    	}
    }

	public void setResearchStaffDao(ResearchStaffDao researchStaffDao) {
		this.researchStaffDao = researchStaffDao;
	}
    
}
