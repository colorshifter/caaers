package gov.nih.nci.cabig.caaers.web.ae;

import gov.nih.nci.cabig.caaers.dao.AdverseEventDao;
import gov.nih.nci.cabig.caaers.dao.AdverseEventReportingPeriodDao;
import gov.nih.nci.cabig.caaers.dao.CtcCategoryDao;
import gov.nih.nci.cabig.caaers.dao.CtcTermDao;
import gov.nih.nci.cabig.caaers.dao.ParticipantDao;
import gov.nih.nci.cabig.caaers.dao.StudyDao;
import gov.nih.nci.cabig.caaers.dao.StudyParticipantAssignmentDao;
import gov.nih.nci.cabig.caaers.dao.TreatmentAssignmentDao;
import gov.nih.nci.cabig.caaers.dao.meddra.LowLevelTermDao;
import gov.nih.nci.cabig.caaers.dao.report.ReportDefinitionDao;
import gov.nih.nci.cabig.caaers.domain.AdverseEventReportingPeriod;
import gov.nih.nci.cabig.caaers.domain.Attribution;
import gov.nih.nci.cabig.caaers.domain.Grade;
import gov.nih.nci.cabig.caaers.domain.Hospitalization;
import gov.nih.nci.cabig.caaers.service.EvaluationService;
import gov.nih.nci.cabig.caaers.tools.spring.tabbedflow.AutomaticSaveAjaxableFormController;
import gov.nih.nci.cabig.caaers.web.ControllerTools;
import gov.nih.nci.cabig.caaers.web.RenderDecisionManager;
import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.FlowFactory;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

public class CaptureAdverseEventController extends AutomaticSaveAjaxableFormController<CaptureAdverseEventInputCommand, AdverseEventReportingPeriod, AdverseEventReportingPeriodDao> {
	
	private static final String AJAX_SUBVIEW_PARAMETER = "subview";
	private static final int ADVERSE_EVENT_CONFIRMATION_TAB_NUMBER = 2;
	private static final String REPORT_ID_PARAMETER = "aeReportId";
	private static final String ACTION_PARAMETER = "action";
	private static final String REPORT_DEFN_LIST_PARAMETER ="reportDefnList";
	private static final String AE_LIST_PARAMETER = "adverseEventList";
	
	
	private ParticipantDao participantDao;
	private StudyDao studyDao;
	private StudyParticipantAssignmentDao assignmentDao;
	private TreatmentAssignmentDao treatmentAssignmentDao;
	private CtcTermDao ctcTermDao;
	private CtcCategoryDao ctcCategoryDao;
	private LowLevelTermDao lowLevelTermDao;
	private AdverseEventDao adverseEventDao;
	private AdverseEventReportingPeriodDao adverseEventReportingPeriodDao;
	private EvaluationService evaluationService;
	private ReportDefinitionDao reportDefinitionDao;
	
	private RenderDecisionManager renderDecisionManager;
	
	
	
	public CaptureAdverseEventController(){
		setBindOnNewForm(true);
		setCommandClass(CaptureAdverseEventInputCommand.class);
	}
	
    /*@Override
    protected void onBindAndValidate(HttpServletRequest request, Object command,
                    BindException errors, int page) throws Exception {
    	String action = (String) findInRequest(request, "_action");
		if(org.apache.commons.lang.StringUtils.isEmpty(action))
			super.onBindAndValidate(request, command, errors, page);
    }*/
	
	@Override
	protected AdverseEventReportingPeriodDao getDao() {
		return null;
	}
	
	@Override
	protected void onBindOnNewForm(HttpServletRequest request, Object command,BindException errors) throws Exception {
		super.onBindOnNewForm(request, command, errors);
		String rpId = request.getParameter("adverseEventReportingPeriod");
		CaptureAdverseEventInputCommand cmd = (CaptureAdverseEventInputCommand) command;
		if(!StringUtils.isEmpty(rpId)) {
			cmd.refreshAssignment(Integer.decode(rpId));
		}
	}
	
	/**
	 *  createBinder method is over-ridden. In this use-case, we need to bind only the adverseEventReportingPeriod to the command object
	 *  incase the submit occurs on change in the Select(reporting period) dropdown. So a hidden attribute "_action" is checked (which is 
	 *  set in the onchange handler of the select dropdown. Incase the submit occurs due to "Save" then the entire form alongwith the adverse
	 *  events will be bound to the command object.
	 *  
	 *  
	 *  NOTE - There are 2 flows from where the Capture AE page can be reached. One is from "Enter AEs" and other is from "Manage reports"
	 *  If we enter from "Manage Reports", the reportingPeriodId is passed as a parameter in the url and we have to bind it to the reporting
	 *  Period in command. So we check for the parameter "addReportingPeriodBinder" which is passed in the url while coming from "Manage 
	 *  reports" and allow/disallow AdverseEventReportingPeriod Binder based on its value.
	 */
	
	@Override
	protected ServletRequestDataBinder createBinder(HttpServletRequest request, Object command) throws Exception{
		CaptureAdverseEventInputCommand aeCommand = (CaptureAdverseEventInputCommand) command;
		ServletRequestDataBinder binder = super.createBinder(request, aeCommand);
		Set<String> paramNames = request.getParameterMap().keySet();
        boolean addReportingPeriodBinder = false;
        addReportingPeriodBinder = paramNames.contains("addReportingPeriodBinder");
        if(!addReportingPeriodBinder)
        	binder.setDisallowedFields(new String[]{"adverseEventReportingPeriod"});
		prepareBinder(binder);
		initBinder(request,binder, aeCommand);
		return binder;
	}

	
	@Override
    @SuppressWarnings("unchecked")
    protected boolean isFormSubmission(HttpServletRequest request) {
        Set<String> paramNames = request.getParameterMap().keySet();
        boolean fromListPage = false;
        fromListPage = paramNames.contains("displayReportingPeriod");
        if(fromListPage) 
        	return true;
        else
        	return super.isFormSubmission(request);
    }

	/**
	 * The Capture AE page can be entered from 2 flows. One is through "Enter AEs" and other is from "Manage Reports". If we enter from
	 * the later, there is a possibility that the form object is not found in the session and an exception is thrown. So the controll goes 
	 * to handleInvalidSubmit method. The default implementation in AbstractWizardFormController renders the first page of the flow. So we 
	 * need to override that method. It creates the command object, binds the attributes in the url and calls processFormSubmission.
	 */
	
	@Override
	protected ModelAndView handleInvalidSubmit(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Set<String> paramNames = request.getParameterMap().keySet();
        boolean fromListPage = false;
        fromListPage = paramNames.contains("displayReportingPeriod");
        if(fromListPage){
        	Object command = formBackingObject(request);
    		ServletRequestDataBinder binder = bindAndValidate(request, command);
    		BindException errors = new BindException(binder.getBindingResult());
    		return processFormSubmission(request, response, command, errors);
        }
        else
        	return super.handleInvalidSubmit(request, response);
	}
	
	/**
	 * Will return the {@link AdverseEventReportingPeriod} 
	 */
	@Override
	protected AdverseEventReportingPeriod getPrimaryDomainObject(CaptureAdverseEventInputCommand cmd) {
		return cmd.getAdverseEventReportingPeriod();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Map referenceData(HttpServletRequest request, Object o,	Errors errors, int page) throws Exception {
		CaptureAdverseEventInputCommand command  = (CaptureAdverseEventInputCommand) o;
		Map referenceData =  super.referenceData(request, command, errors, page);
		Map<String, String> summary = new LinkedHashMap<String, String>();
        summary.put("Participant", (command.getParticipant() == null) ? "" : command.getParticipant().getFullName() );
        summary.put("Study", (command.getStudy() == null) ? "" : command.getStudy().getLongTitle());
        //put summary only if page is greater than 0
        if(page > 0){
        	referenceData.put("aesummary", summary);
        }
        
        //hide for non DCP-AdEERS reporting enabled study
        if(!command.isDCPAdeersStudy()){
        	renderDecisionManager.conceal("adverseEvents[].serious");
        }
		return referenceData;
	}
	
	@Override
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object oCommand, BindException errors) throws Exception {

		CaptureAdverseEventInputCommand command = (CaptureAdverseEventInputCommand) oCommand;
		
		String action = (String)findInRequest(request, "_action");
		String reportIdString = (String)findInRequest(request, "_reportId");
		Integer reportId = Integer.parseInt(reportIdString);
		
		List reportDefnIdList = new ArrayList<Integer>();
		List adverseEventIdList = new ArrayList<Integer>();
		
		for(Integer id: command.getSelectedAesMap().keySet()){
			if(command.getSelectedAesMap().get(id).equals(Boolean.TRUE))
				adverseEventIdList.add(id);
		}
		
		for(Integer id: command.getReportDefinitionMap().keySet()){
			if(command.getReportDefinitionMap().get(id).equals(Boolean.TRUE))
				reportDefnIdList.add(id);
		}
		
		// Set the parameters in the session.
		request.getSession().setAttribute(ACTION_PARAMETER, action);
		if(reportId != null)
			request.getSession().setAttribute(REPORT_ID_PARAMETER, reportId);
		request.getSession().setAttribute(AE_LIST_PARAMETER, adverseEventIdList);
		request.getSession().setAttribute(REPORT_DEFN_LIST_PARAMETER, reportDefnIdList);
		
		
		
		Map<String, Object> model = new ModelMap("participant", command.getParticipant().getId());
	    model.put("study", command.getStudy().getId());
	    return new ModelAndView("redirectToExpeditedAeEdit", model);
	}
	
	/**
	 * The binder for reporting period, should look in the command and fetch the object.
	 * 
	 * @param request
	 * @param binder
	 * @param command
	 * @throws Exception
	 */
	protected void initBinder(final HttpServletRequest request,final ServletRequestDataBinder binder, final CaptureAdverseEventInputCommand command) throws Exception {
		ControllerTools.registerDomainObjectEditor(binder, "participant", participantDao);
        ControllerTools.registerDomainObjectEditor(binder, "study", studyDao);
        ControllerTools.registerDomainObjectEditor(binder, "adverseEvent", adverseEventDao);
        ControllerTools.registerDomainObjectEditor(binder, ctcTermDao);
        ControllerTools.registerDomainObjectEditor(binder, ctcCategoryDao);
        ControllerTools.registerDomainObjectEditor(binder, lowLevelTermDao);
        ControllerTools.registerDomainObjectEditor(binder, adverseEventReportingPeriodDao);
        binder.registerCustomEditor(Date.class, ControllerTools.getDateEditor(false));
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        ControllerTools.registerEnumEditor(binder, Grade.class);
        ControllerTools.registerEnumEditor(binder, Hospitalization.class);
        ControllerTools.registerEnumEditor(binder, Attribution.class);
        
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)	throws Exception {
		CaptureAdverseEventInputCommand cmd = new CaptureAdverseEventInputCommand(adverseEventReportingPeriodDao,assignmentDao, evaluationService, reportDefinitionDao);
		
		return cmd;
	}
	
	@Override
	public FlowFactory<CaptureAdverseEventInputCommand> getFlowFactory() {
		return new FlowFactory<CaptureAdverseEventInputCommand>() {
			public Flow<CaptureAdverseEventInputCommand> createFlow(CaptureAdverseEventInputCommand cmd) {
				Flow<CaptureAdverseEventInputCommand> flow = new Flow<CaptureAdverseEventInputCommand>("Capture Adverse Event Flow");
				flow.addTab(new BeginTab<CaptureAdverseEventInputCommand>());
				flow.addTab(new AdverseEventCaptureTab());
				flow.addTab(new AdverseEventConfirmTab("Review & Report", "Review & Report", "ae/ae_reviewsummary"));
				return flow;
			}
		};
	}
	
	/**
	 * Supress the validation in the following cases.
	 *   1 - When we go back
	 *   2 - When it is an Ajax request, which dont has form submission
	 */
	
	@Override
    protected boolean suppressValidation(final HttpServletRequest request) {

        Object isAjax = findInRequest(request, AJAX_SUBVIEW_PARAMETER);
        if (isAjax != null) return true;
        
        //Object isFromManageReport = findInRequest(request, "fromManageReport");
        //if(isFromManageReport != null) return true;
        //check current page and next page
        int currPage = getCurrentPage(request);
    	int targetPage = getTargetPage(request, currPage);
        return targetPage < currPage;

    }
	
	
	/**
     * Returns the value associated with the <code>attributeName</code>, if present in
     * HttpRequest parameter, if not available, will check in HttpRequest attribute map.
     */
    protected Object findInRequest(final ServletRequest request, final String attributName) {

        Object attr = request.getParameter(attributName);
        if (attr == null) {
            attr = request.getAttribute(attributName);
        }
        return attr;
    }
    
    
    /**
     * Adds ajax sub-page view capability. TODO: factor this into main tabbed flow controller.
     */
    @Override
    protected String getViewName(HttpServletRequest request, Object command, int page) {
        String subviewName = request.getParameter(AJAX_SUBVIEW_PARAMETER);
        if (subviewName != null) {
            // for side-effects:
            super.getViewName(request, command, page);
            return "ae/ajax/" + subviewName;
        } else {
            return super.getViewName(request, command, page);
        }
    }
    
   @Override
	protected boolean shouldSave(HttpServletRequest request,CaptureAdverseEventInputCommand command,Tab<CaptureAdverseEventInputCommand> tab) {
		Object isAjax = findInRequest(request, AJAX_SUBVIEW_PARAMETER);
        if (isAjax != null || 1 != getCurrentPage(request)) {
            return false;
        }
       return true;
	}
	
	@Override
	protected CaptureAdverseEventInputCommand save(final CaptureAdverseEventInputCommand command, final Errors errors){
		if(!errors.hasErrors())
			command.save();
		
		return command;
	}
	
	public ParticipantDao getParticipantDao() {
		return participantDao;
	}

	public void setParticipantDao(ParticipantDao participantDao) {
		this.participantDao = participantDao;
	}

	public StudyDao getStudyDao() {
		return studyDao;
	}

	public void setStudyDao(StudyDao studyDao) {
		this.studyDao = studyDao;
	}
	public void setAssignmentDao(StudyParticipantAssignmentDao assignmentDao){
		this.assignmentDao = assignmentDao;
	}
	
	public void setTreatmentAssignmentDao(
			TreatmentAssignmentDao treatmentAssignmentDao) {
		this.treatmentAssignmentDao = treatmentAssignmentDao;
	}
	
	public TreatmentAssignmentDao getTreatmentAssignmentDao() {
		return treatmentAssignmentDao;
	}
	
	public CtcTermDao getCtcTermDao() {
		return ctcTermDao;
	}
	
	public void setCtcTermDao(CtcTermDao ctcTermDao) {
		this.ctcTermDao = ctcTermDao;
	}
	
	public CtcCategoryDao getCtcCategoryDao() {
		return ctcCategoryDao;
	}
	
	public void setCtcCategoryDao(CtcCategoryDao ctcCategoryDao) {
		this.ctcCategoryDao = ctcCategoryDao;
	}
	
	public LowLevelTermDao getLowLevelTermDao() {
		return lowLevelTermDao;
	}
	
	public void setLowLevelTermDao(LowLevelTermDao lowLevelTermDao) {
		this.lowLevelTermDao = lowLevelTermDao;
	}
	
	public AdverseEventReportingPeriodDao getAdverseEventReportingPeriodDao() {
		return adverseEventReportingPeriodDao;
	}
	
	public void setAdverseEventReportingPeriodDao(
			AdverseEventReportingPeriodDao adverseEventReportingPeriodDao) {
		this.adverseEventReportingPeriodDao = adverseEventReportingPeriodDao;
	}
	
	public void setAdverseEventDao(AdverseEventDao adverseEventDao) {
		this.adverseEventDao = adverseEventDao;
	}
	
	public AdverseEventDao getAdverseEventDao() {
		return adverseEventDao;
	}
	
	public EvaluationService getEvaluationService() {
		return evaluationService;
	}
	
	public void setEvaluationService(EvaluationService evaluationService) {
		this.evaluationService = evaluationService;
	}
	
	public ReportDefinitionDao getReportDefinitionDao() {
		return reportDefinitionDao;
	}
	
	public void setReportDefinitionDao(ReportDefinitionDao reportDefinitionDao){
		this.reportDefinitionDao = reportDefinitionDao;
	}
	
	public void setRenderDecisionManager(RenderDecisionManager renderDecisionManager) {
		this.renderDecisionManager = renderDecisionManager;
	}
}
