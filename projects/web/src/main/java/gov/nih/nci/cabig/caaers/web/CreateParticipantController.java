package gov.nih.nci.cabig.caaers.web;

//java imports
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// java servlet imports
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// commons imports 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
// spring mvc imports  
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
// caaers imports
import gov.nih.nci.cabig.caaers.dao.ParticipantDao;
import gov.nih.nci.cabig.caaers.dao.StudySiteDao;
import gov.nih.nci.cabig.caaers.domain.Participant;
import gov.nih.nci.cabig.caaers.domain.Study;
import gov.nih.nci.cabig.caaers.domain.Identifier;
import gov.nih.nci.cabig.caaers.domain.StudyParticipantAssignment;
import gov.nih.nci.cabig.caaers.domain.StudySite;
import gov.nih.nci.cabig.caaers.service.ParticipantService;
import gov.nih.nci.cabig.caaers.service.StudyService;
import gov.nih.nci.cabig.caaers.web.tabbedflow.AbstractTabbedFlowFormController;
import gov.nih.nci.cabig.caaers.web.tabbedflow.Flow;
import gov.nih.nci.cabig.caaers.web.tabbedflow.Tab;

public class CreateParticipantController extends AbstractTabbedFlowFormController {
    private static Log log = LogFactory.getLog(RegistrationController.class);
    private StudySiteDao studySiteDao;
    private StudyService studyService;
    private ParticipantDao participantDao;
    
    public ParticipantDao getParticipantDao() {
		return participantDao;
	}
    
    public void setParticipantDao(ParticipantDao participantDao) {
		this.participantDao = participantDao;
	}

	public StudyService getStudyService() {
		return studyService;
	}

	public void setStudyService(StudyService studyService) {
		this.studyService = studyService;
	}
    
    public StudySiteDao getStudySiteDao() {
		return studySiteDao;
	}

	public void setStudySiteDao(StudySiteDao studySiteDao) {
		this.studySiteDao = studySiteDao;
	}
    
    public CreateParticipantController() {
        setCommandClass(NewParticipantCommand.class);
        setFlow(new Flow("Create Participant", Arrays.asList(	
            new Tab(0, "Enter Participant Information", "New Participant", "par_create_participant") {
                public Map<String, Object> referenceData() {
                    Map<String, Object> refdata = super.referenceData();
                    Map<String, String> genders = new HashMap<String, String>();
                    Map<String, String> sources  = new HashMap<String, String>();
                    genders.put("Female", "Female");
                    genders.put("Male", "Male");
                    sources.put("Duke", "Duke");
                    sources.put("NorthWestern","NorthWestern");
                    refdata.put("genders", genders);
                    refdata.put("sources", sources);
                    refdata.put("action", "New");
                    return refdata;
                }
            },
            new Tab(1, "Choose Study", "Choose Study", "par_choose_study") {
                public Map<String, Object> referenceData() {
                    Map<String, Object> refdata = super.referenceData();
                    refdata.put("searchType", getSearchType());
                    return refdata;
                }
            },
            new Tab(2, "Confirmation", "Confirmation", "par_confirmation") {
                public Map<String, Object> referenceData() {
                    Map<String, Object> refdata = super.referenceData();
                    return refdata;
                }
            }
        )));
    }
    
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
    	log.debug("Entering formBackingObject ...");
        NewParticipantCommand participantCommand = new NewParticipantCommand(); 
        StudySite studySite=null;
        for (int i = 0; i < 5; i++) {
        	participantCommand.getIdentifiers().add(new Identifier());
		}
        if(request.getParameter("studySiteId")!=null){
			log.debug("Parameters found as.."+request.getParameter("studySiteId"));
			studySite=studySiteDao.getById(Integer.parseInt(request.getParameter("studySiteId")));
			
			participantCommand.getStudyParticipantAssignment().setStudySite(studySite);
			participantCommand.getStudyParticipantAssignment().setDateOfEnrollment(new Date());
		}
		return participantCommand;
    }
    
    protected void onBind(HttpServletRequest request, Object command,BindException errors)throws Exception {
    	log.debug("Entering onBind...");
    	NewParticipantCommand participantCommand = (NewParticipantCommand)command;
    	String searchtext = participantCommand.getSearchTypeText();
    	String type       = participantCommand.getSearchType();
    	if (searchtext != null && type != null && !searchtext.equals(""))
    	{
    		log.debug("Search text : " + searchtext + "Type : " + type);
    		Study  study = new Study();
    		participantCommand.setStudies(new ArrayList<Study>());
    		if ("st".equals(type))
    			study.setShortTitle(searchtext);
    		else if ("lt".equals(type))
    			study.setLongTitle(searchtext);
    		else if ("d".equals(type))
    			study.setDescription(searchtext);
    		else if ("pic".equals(type))
    			study.setPrincipalInvestigatorCode(searchtext);
    		else if ("pin".equals(type))
    			study.setPrincipalInvestigatorName(searchtext);
    		else if ("psc".equals(type))
    			study.setPrimarySponsorCode(searchtext);
    		else if ("psn".equals(type))
    			study.setPrimarySponsorName(searchtext);
    		else if ("pc".equals(type))
    			study.setPhaseCode(searchtext);

    		List<Study> studies = studyService.search(study);
    		participantCommand.setStudies(studies);
    		participantCommand.setSearchTypeText("");
    		participantCommand.setSearchType("");
    	}
    		
    }
    
    @Override
	protected boolean isFinishRequest(HttpServletRequest request) {
    	log.debug("Entering isFinishRequest ...");
		if(request.getParameter("nextView")==null||request.getParameter("nextView").equals(""))
			return false;
		String viewName=request.getParameter("nextView");
		if(viewName.equalsIgnoreCase("processFinish"))
			return true;
		return false;
	}
    
    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
    	log.debug("Entering Process Finish ...");
    	NewParticipantCommand participantCommand = (NewParticipantCommand)command;
    	List<StudySite> studySites = new ArrayList<StudySite>();
    	for(String st : participantCommand.getStudySiteArray())
    	{
    		StudySite studySite=studySiteDao.getById(Integer.parseInt(st));
    		studySites.add(studySite);
    	}
    	Participant participant = participantCommand.createParticipant(studySites);
        participantDao.save(participant);
        
        ModelAndView modelAndView = new ModelAndView("kk");
        modelAndView.addObject("participant", participant);
		modelAndView.addAllObjects(errors.getModel());
		return modelAndView;
    }
    private List<LOV> getSearchType() {
		List<LOV> col = new ArrayList<LOV>();
		LOV lov1 = new LOV("st",  "Short Title");
		LOV lov2 = new LOV("lt",  "Long Title");
		LOV lov3 = new LOV("d",   "Description");
		LOV lov4 = new LOV("pic", "Principal Investigator Code");
		LOV lov5 = new LOV("pin", "Principal Investigator Name");
		LOV lov6 = new LOV("psc", "Primary Sponsor Code");
		LOV lov7 = new LOV("psn", "Primary Sponsor Name");
		LOV lov8 = new LOV("pc",  "Phase Code");
		
		col.add(lov1);
		col.add(lov2);
		col.add(lov3);
		col.add(lov4);
		col.add(lov5);
		col.add(lov6);
		col.add(lov7);
		col.add(lov8);

		return col;
	}

	public class LOV {

		private String code;

		private String desc;

		LOV(String code, String desc) {
			this.code = code;
			this.desc = desc;

		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}	
}
