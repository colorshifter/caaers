package gov.nih.nci.cabig.caaers.web.study;

import gov.nih.nci.cabig.caaers.dao.AgentDao;
import gov.nih.nci.cabig.caaers.dao.CtcDao;
import gov.nih.nci.cabig.caaers.dao.InvestigationalNewDrugDao;
import gov.nih.nci.cabig.caaers.dao.MeddraVersionDao;
import gov.nih.nci.cabig.caaers.dao.OrganizationDao;
import gov.nih.nci.cabig.caaers.dao.ResearchStaffDao;
import gov.nih.nci.cabig.caaers.dao.SiteInvestigatorDao;
import gov.nih.nci.cabig.caaers.dao.StudyDao;
import gov.nih.nci.cabig.caaers.domain.INDType;
import gov.nih.nci.cabig.caaers.domain.Study;
import gov.nih.nci.cabig.caaers.domain.StudyTherapy;
import gov.nih.nci.cabig.caaers.domain.StudyTherapyType;
import gov.nih.nci.cabig.caaers.domain.Term;
import gov.nih.nci.cabig.caaers.web.ControllerTools;
import gov.nih.nci.cabig.ctms.web.tabs.AutomaticSaveFlowFormController;
import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

/**
 * Base Controller class to handle the basic work flow in the Creation / Updation of a Study Design This uses
 * AbstractTabbedFlowFormController to implement tabbed workflow
 * @author Priyatam
 */
public abstract class StudyController<C extends Study> extends AutomaticSaveFlowFormController<C, Study, StudyDao> {
	private static final Log log = LogFactory.getLog(StudyController.class);

	protected StudyDao studyDao;

	private OrganizationDao organizationDao;

	private AgentDao agentDao;

	private SiteInvestigatorDao siteInvestigatorDao;

	private ResearchStaffDao researchStaffDao;

	private CtcDao ctcDao;

	private InvestigationalNewDrugDao investigationalNewDrugDao;

	private MeddraVersionDao meddraVersionDao;

	public StudyController() {
		setCommandClass(Study.class);
		Flow<C> flow = new Flow<C>("Create Study");
		layoutTabs(flow);
		setFlow(flow);
		setAllowDirtyBack(false);
		setAllowDirtyForward(false);
	}

	// /LOGIC
	@Override
	protected Study getPrimaryDomainObject(final C command) {
		return command;
	}

	@Override
	protected StudyDao getDao() {
		return studyDao;
	}

	/**
	 * Template method to let the subclass decide the order of tab
	 */
	protected abstract void layoutTabs(Flow<C> flow);

	@Override
	protected void initBinder(final HttpServletRequest request, final ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Date.class, ControllerTools.getDateEditor(false));
		ControllerTools.registerDomainObjectEditor(binder, organizationDao);
		ControllerTools.registerDomainObjectEditor(binder, agentDao);
		ControllerTools.registerDomainObjectEditor(binder, siteInvestigatorDao);
		ControllerTools.registerDomainObjectEditor(binder, researchStaffDao);
		ControllerTools.registerDomainObjectEditor(binder, ctcDao);
		ControllerTools.registerDomainObjectEditor(binder, investigationalNewDrugDao);
		ControllerTools.registerDomainObjectEditor(binder, meddraVersionDao);
		ControllerTools.registerEnumEditor(binder, Term.class);
		ControllerTools.registerEnumEditor(binder, INDType.class);
	}

	/**
	 * Override this in sub controller if summary is needed
	 * @return
	 */
	protected boolean isSummaryEnabled() {
		return false;
	}

	@Override
	protected ModelAndView processFinish(final HttpServletRequest request, final HttpServletResponse response,
			final Object command, final BindException errors) throws Exception {
		Study study = (Study) command;
		studyDao.save(study);

		ModelAndView modelAndView = new ModelAndView("study_confirmation");
		modelAndView.addAllObjects(errors.getModel());
		response.sendRedirect("view?studyName=" + study.getShortTitle() + "&type=confirm");
		return null;
	}

	@Override
	protected String getViewName(final HttpServletRequest request, final Object command, final int page) {
		Object subviewName = findInRequest(request, "_subview");
		if (subviewName != null) {
			return "study/ajax/" + subviewName;
		}
		else {
			return super.getViewName(request, command, page);
		}
	}

	/**
	 * Returns the value associated with the <code>attributeName</code>, if present in HttpRequest parameter, if not available, will
	 * check in HttpRequest attribute map.
	 */
	protected Object findInRequest(final HttpServletRequest request, final String attributName) {

		Object attr = request.getParameter(attributName);
		if (attr == null) {
			attr = request.getAttribute(attributName);
		}
		return attr;
	}

	@Override
	protected boolean suppressValidation(final HttpServletRequest request, final Object command) {
		// supress for ajax and delete requests
		Object isAjax = findInRequest(request, "_isAjax");
		if (isAjax != null) {
			return true;
		}
		String action = (String) findInRequest(request, "_action");
		if (StringUtils.isNotEmpty(action)) {
			return true;
		}
		return super.suppressValidation(request, command);
	}

	@Override
	protected boolean shouldSave(final HttpServletRequest request, final C command, final Tab<C> tab) {
		return super.shouldSave(request, command, tab) && findInRequest(request, "_isAjax") == null;
	}

	// /BEAN PROPERTIES

	public AgentDao getAgentDao() {
		return agentDao;
	}

	public void setAgentDao(final AgentDao agentDao) {
		this.agentDao = agentDao;
	}

	public ResearchStaffDao getResearchStaffDao() {
		return researchStaffDao;
	}

	public void setResearchStaffDao(final ResearchStaffDao researchStaffDao) {
		this.researchStaffDao = researchStaffDao;
	}

	public OrganizationDao getOrganizationDao() {
		return organizationDao;
	}

	public void setOrganizationDao(final OrganizationDao organizationDao) {
		this.organizationDao = organizationDao;
	}

	public SiteInvestigatorDao getSiteInvestigatorDao() {
		return siteInvestigatorDao;
	}

	public void setSiteInvestigatorDao(final SiteInvestigatorDao siteInvestigatorDao) {
		this.siteInvestigatorDao = siteInvestigatorDao;
	}

	public StudyDao getStudyDao() {
		return studyDao;
	}

	public void setStudyDao(final StudyDao studyDao) {
		this.studyDao = studyDao;
	}

	public CtcDao getCtcDao() {
		return ctcDao;
	}

	public void setCtcDao(final CtcDao ctcDao) {
		this.ctcDao = ctcDao;
	}

	public MeddraVersionDao getMeddraVersionDao() {
		return meddraVersionDao;
	}

	public void setMeddraVersionDao(final MeddraVersionDao meddraVersionDao) {
		this.meddraVersionDao = meddraVersionDao;
	}

	public InvestigationalNewDrugDao getInvestigationalNewDrugDao() {
		return investigationalNewDrugDao;
	}

	public void setInvestigationalNewDrugDao(final InvestigationalNewDrugDao investigationalNewDrugDao) {
		this.investigationalNewDrugDao = investigationalNewDrugDao;
	}

	protected void updateStudyTherapies(final Study study) {
		List<StudyTherapy> studyTherapies = study.getStudyTherapies();

		if (study.getDrugAdministrationTherapyType()
				&& study.getStudyTherapy(StudyTherapyType.DRUG_ADMINISTRATION) == null) {
			StudyTherapy drugAdministrationTherapy = new StudyTherapy();
			drugAdministrationTherapy.setStudy(study);
			drugAdministrationTherapy.setStudyTherapyType(StudyTherapyType.DRUG_ADMINISTRATION);
			study.getStudyTherapies().add(drugAdministrationTherapy);
		}
		else if (!study.getDrugAdministrationTherapyType()
				&& study.getStudyTherapy(StudyTherapyType.DRUG_ADMINISTRATION) != null) {
			studyTherapies.remove(study.getStudyTherapy(StudyTherapyType.DRUG_ADMINISTRATION));
		}
		if (study.getDeviceTherapyType() && study.getStudyTherapy(StudyTherapyType.DEVICE) == null) {
			StudyTherapy deviceTherapy = new StudyTherapy();
			deviceTherapy.setStudy(study);
			deviceTherapy.setStudyTherapyType(StudyTherapyType.DEVICE);
			study.getStudyTherapies().add(deviceTherapy);
		}
		else if (!study.getDeviceTherapyType() && study.getStudyTherapy(StudyTherapyType.DEVICE) != null) {
			studyTherapies.remove(study.getStudyTherapy(StudyTherapyType.DEVICE));
		}
		if (study.getRadiationTherapyType() && study.getStudyTherapy(StudyTherapyType.RADIATION) == null) {
			StudyTherapy radiationTherapy = new StudyTherapy();
			radiationTherapy.setStudy(study);
			radiationTherapy.setStudyTherapyType(StudyTherapyType.RADIATION);
			study.getStudyTherapies().add(radiationTherapy);
		}
		else if (!study.getRadiationTherapyType() && study.getStudyTherapy(StudyTherapyType.RADIATION) != null) {
			studyTherapies.remove(study.getStudyTherapy(StudyTherapyType.RADIATION));
		}
		if (study.getSurgeryTherapyType() && study.getStudyTherapy(StudyTherapyType.SURGERY) == null) {
			StudyTherapy surgeryTherapy = new StudyTherapy();
			surgeryTherapy.setStudy(study);
			surgeryTherapy.setStudyTherapyType(StudyTherapyType.SURGERY);
			study.getStudyTherapies().add(surgeryTherapy);
		}
		else if (!study.getSurgeryTherapyType() && study.getStudyTherapy(StudyTherapyType.SURGERY) != null) {
			studyTherapies.remove(study.getStudyTherapy(StudyTherapyType.SURGERY));
		}
		if (study.getBehavioralTherapyType() && study.getStudyTherapy(StudyTherapyType.BEHAVIORAL) == null) {
			StudyTherapy behavioralTherapy = new StudyTherapy();
			behavioralTherapy.setStudy(study);
			behavioralTherapy.setStudyTherapyType(StudyTherapyType.BEHAVIORAL);
			study.getStudyTherapies().add(behavioralTherapy);
		}
		else if (!study.getBehavioralTherapyType() && study.getStudyTherapy(StudyTherapyType.BEHAVIORAL) != null) {
			studyTherapies.remove(study.getStudyTherapy(StudyTherapyType.BEHAVIORAL));
		}
	}

}
