package gov.nih.nci.cabig.caaers.web.ae;

import gov.nih.nci.cabig.caaers.domain.AdverseEvent;
import gov.nih.nci.cabig.caaers.domain.ExpeditedAdverseEventReport;
import gov.nih.nci.cabig.caaers.domain.OutcomeType;
import gov.nih.nci.cabig.caaers.domain.Study;
import gov.nih.nci.cabig.caaers.domain.Term;
import gov.nih.nci.cabig.caaers.domain.report.Report;
import gov.nih.nci.cabig.caaers.domain.repository.AdverseEventRoutingAndReviewRepository;
import gov.nih.nci.cabig.caaers.web.fields.CompositeField;
import gov.nih.nci.cabig.caaers.web.fields.DefaultInputFieldGroup;
import gov.nih.nci.cabig.caaers.web.fields.InputField;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldAttributes;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldFactory;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroup;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroupMap;
import gov.nih.nci.cabig.caaers.web.fields.MultipleFieldGroupFactory;
import gov.nih.nci.cabig.caaers.web.fields.validators.FieldValidator;
import gov.nih.nci.cabig.caaers.web.utils.WebUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanWrapper;
import org.springframework.validation.Errors;

/**
 * @author Biju Joseph
 */
public class AdverseEventCaptureTab extends AdverseEventTab {
	
	// The names of the field groups
    private static final String MAIN_FIELD_GROUP = "main";
    private static final String OUTCOME_FIELD_GROUP ="outcomes";
    
    //max characters allowed for Verbatim
    private static final Integer VERBATIM_MAX_SIZE = 65;
    private AdverseEventRoutingAndReviewRepository adverseEventRoutingAndReviewRepository;
    
    public AdverseEventCaptureTab() {
        super("Enter Adverse Events", "Adverse Events", "ae/captureAdverseEvents");
        setAutoPopulateHelpKey(true);
    }


    /**
     * This method will create the fields to be displayed on the screen.
     *  For CTC/MedDRA Study the following fields will be created:
     *    0. Other(MedDRA) - [conditional, only appear for CTC studies, if term is otherspecify].
     *    1. Verbatim
     *    2. Grade
     *    3. StartDate
     *    4. End Date
     *    5. Attribution To Study
     *    6. Time Of Event
     *    7. Event Location
     *    8. Hospitalization
     *    9. Expectedness
     *   10. Outcome
     *   
     * Note:- We should run the adverse events against the index fixed list, since that list will have null items in it, we should skip if 'AdverseEvent' is null.
     */

    @Override
    public Map<String, InputFieldGroup> createFieldGroups(CaptureAdverseEventInputCommand cmd) {

        InputFieldGroupMap fieldGrpMap = new InputFieldGroupMap();
        MultipleFieldGroupFactory mainFieldFactory;
        
        if(cmd.getAdverseEventReportingPeriod() != null){
        	
        	Study study = cmd.getAdverseEventReportingPeriod().getStudy();
        	boolean isMeddraStudy = study.getAeTerminology().getTerm() == Term.MEDDRA;
        	int size = cmd.getAdverseEvents().size();
        	
        	//create one field group - One row of main groups
            mainFieldFactory = new MultipleFieldGroupFactory(MAIN_FIELD_GROUP, "adverseEvents");
            
        	for (int i = 0; i < size; i++) {
        		
                AdverseEvent ae = cmd.getAdverseEvents().get(i);
                if (ae == null) continue;
        		
                //other MedDRA
        		InputField otherMeddraField = (ae.getSolicited()) ? InputFieldFactory.createLabelField("lowLevelTerm.meddraTerm", "Other (MedDRA)", false) :
        															InputFieldFactory.createAutocompleterField("lowLevelTerm", "Other(MedDRA)", true);
        		//only add otherMedDRA on non MedDRA and otherRequired=true
                if(ae.getAdverseEventTerm().isOtherRequired()){
                	mainFieldFactory.addField(otherMeddraField);
                }
                
            	//verbatim
            	InputField verbatimField = InputFieldFactory.createTextField("detailsForOther", "Verbatim");
                InputFieldAttributes.setSize(verbatimField, 25);
                mainFieldFactory.addField(verbatimField);
                
                //grade
                InputField gradeField = InputFieldFactory.createLongSelectField("grade","Grade", !ae.getSolicited(), createGradeOptions(ae, isMeddraStudy ? "Meddra" : "Ctc"));
                mainFieldFactory.addField(gradeField);
                
                //startDate
                InputField startDateField = InputFieldFactory.createPastDateField("startDate", "Start date", false);
                mainFieldFactory.addField(startDateField);
                
                //endDate
                InputField endDateField = InputFieldFactory.createPastDateField("endDate", "End date", false);
                mainFieldFactory.addField(endDateField);
                
                //attribution
                InputField attributionField = InputFieldFactory.createSelectField("attributionSummary", "Attribution to study intervention", false, createAttributionOptions());
                mainFieldFactory.addField(attributionField);
                
                //Event time
                InputField hrField = InputFieldFactory.createTextField("hourString", "", FieldValidator.HOUR_VALIDATOR);
            	InputField mmField = InputFieldFactory.createTextField("minuteString"," ", FieldValidator.MINUTE_VALIDATOR);
            	LinkedHashMap< Object, Object> amPmOption = new LinkedHashMap<Object, Object>();
            	amPmOption.put("0", "AM");
            	amPmOption.put("1", "PM");
            	InputField amPmField = InputFieldFactory.createSelectField("type", "",false, amPmOption);
            	InputFieldAttributes.setSize(hrField, 2);
            	InputFieldAttributes.setSize(mmField, 2);
            	InputField timeOfEventField =  new CompositeField("eventApproximateTime", new DefaultInputFieldGroup(null,"Event time").addField(hrField).addField(mmField).addField(amPmField));
            	mainFieldFactory.addField(timeOfEventField);
            	
            	//EventLocation
                InputField eventLocationField = InputFieldFactory.createTextField("eventLocation", "Where was the patient when the event occurred?");
                mainFieldFactory.addField(eventLocationField);
                
                //Hospitalization
                InputField hospitalizationField = InputFieldFactory.createSelectField("hospitalization", "Did AE cause hospitalization?", false, createHospitalizationOptions());
                mainFieldFactory.addField(hospitalizationField);
                
                //expectedness
                InputField expectednessField = InputFieldFactory.createSelectField("expected", "Expected", false, createExpectedOptions());
                mainFieldFactory.addField(expectednessField);
                
                InputFieldGroup fieldGroup = mainFieldFactory.createGroup(i);
                mainFieldFactory.addFieldGroup(fieldGroup);
                mainFieldFactory.clearFields();
                
                //now add the fields related to outcomes
                InputFieldGroup outcomeFieldGrp = new DefaultInputFieldGroup(OUTCOME_FIELD_GROUP + i);
                List<InputField> outcomeFields = outcomeFieldGrp.getFields();
                Map<Integer, Boolean> oneOutcomeMap = cmd.getOutcomes().get(i);

                for (Integer code : oneOutcomeMap.keySet()) {
                    OutcomeType outcomeType = OutcomeType.getByCode(code);

                    outcomeFields.add(InputFieldFactory.createCheckboxField("outcomes[" + i + "][" + code + "]", outcomeType.getDisplayName()));

                    if (outcomeType == OutcomeType.OTHER_SERIOUS) {
                        outcomeFields.add(InputFieldFactory.createTextField("outcomeOtherDetails[" + i + "]", ""));
                    }
                }
                fieldGrpMap.addInputFieldGroup(outcomeFieldGrp);
        	}
        	
        	 fieldGrpMap.addMultipleFieldGroupFactory(mainFieldFactory);
        }
        
        return fieldGrpMap;
    }

    @Override
    public void beforeBind(HttpServletRequest request, CaptureAdverseEventInputCommand command) {
        super.beforeBind(request, command);
        command.reassociate();
    }

    @Override
    public Map<String, Object> referenceData(CaptureAdverseEventInputCommand command) {
    	 // Setup the categories list for aeTermQuery tag.
        boolean isCTCStudy = command.getStudy().getAeTerminology().getTerm() == Term.CTC;

        // initialize lazy
        if (isCTCStudy) {
            command.getCtcCategories();
            if (command.getAdverseEvents() != null)
                for (AdverseEvent ae: command.getAdverseEvents()) {
                	if(ae == null) continue;
                    ae.getAdverseEventTerm().isOtherRequired();
                    if(ae.getAdverseEventCtcTerm().getCtcTerm() != null){
                    	ae.getAdverseEventCtcTerm().getCtcTerm().isOtherRequired();
                        ae.getAdverseEventCtcTerm().getCtcTerm().getContextualGrades();
                    }
                }
        }
        
       

        //initalize the seriousness outcome indicators
        command.initializeOutcomes();

    	return super.referenceData(command);
    }



    @Override
    public void postProcess(HttpServletRequest request, CaptureAdverseEventInputCommand command, Errors errors) {
        if (findInRequest(request, CaptureAdverseEventController.AJAX_SUBVIEW_PARAMETER) != null || errors.hasErrors())
            return; //ignore if this is an ajax request
        
        //reset the reporting method and action
        command.set_action(null);
        command.setReportingMethod(null);
        command.setPrimaryAdverseEventId(null);
        
        //sync the seriousness outcomes
        command.synchronizeOutcome();
        
        command.initialize();
        // Amend the reports if AEs associated to it are modified and the user is OK with the amendment.
        String action = (String)findInRequest(request, "_action");
        if(action.equals("amendmentRequired")){
        	String reportIds = (String) findInRequest(request, "_amendReportIds");
        	StringTokenizer st = new StringTokenizer(reportIds, ",");
        	HashMap<Integer, Boolean> reportIdMap = new HashMap<Integer, Boolean>();
        	while(st.hasMoreTokens()){
        		String repId = st.nextToken();
        		if(!reportIdMap.containsKey(Integer.decode(repId)));
        			reportIdMap.put(Integer.parseInt(repId), Boolean.TRUE);
        	}
        	Set<ExpeditedAdverseEventReport> aeReportsAmmended = new HashSet<ExpeditedAdverseEventReport>();
        	
        	for(ExpeditedAdverseEventReport aeReport: command.getAdverseEventReportingPeriod().getAeReports()){
        		if(reportIdMap.containsKey(aeReport.getId())){
        			command.reassociate(aeReport);

                	//update the graded date on aes, as we are going to amend the report.
                	aeReport.updateAdverseEventGradedDate();
                	
        			Boolean useDefaultVersion = false;
        			List<Report> reportsOfAeReport = new ArrayList<Report>(aeReport.getReports());
        	    	for(Report report: reportsOfAeReport){
        	    		if(report.getReportDefinition().getAmendable() && report.getIsLatestVersion()){
        	    			reportRepository.createAndAmendReport(command.reassociateReportDefinition(report.getReportDefinition()), 
        	    					report, useDefaultVersion);
        	    			aeReportsAmmended.add(aeReport);
        	    			
        	    			// Set useDefaultVersion to true so that the reportVersionId is retained for all the reports 
        	    			// and just incremented for the 1st one in the list.
        	    			useDefaultVersion = true;
        	    		}
        	    	}
        		}
        	}
        	
        	if(command.getWorkflowEnabled() && command.isAssociatedToWorkflow()){
        		//update the workflow for every expedited report, whose Report is ammended
        		for(ExpeditedAdverseEventReport aeReport : aeReportsAmmended){
        			adverseEventRoutingAndReviewRepository.enactReportWorkflow(aeReport);
        		}
        	}
        	
        }
    }

    @Override
    public void onBind(HttpServletRequest request, CaptureAdverseEventInputCommand command, Errors errors) {
        String rpId = request.getParameter("adverseEventReportingPeriod");
        Set<String> paramNames = request.getParameterMap().keySet();
        boolean fromListPage = false;
        fromListPage = paramNames.contains("displayReportingPeriod");
        if (fromListPage)
            command.refreshAssignment(Integer.parseInt(rpId));
    }

    public AdverseEvent checkAEsUniqueness(CaptureAdverseEventInputCommand command) {
        List AEs = null;
        AEs = command.getAdverseEventReportingPeriod().getAdverseEvents();
        
        if (AEs == null || AEs.size() == 0) return null;

        Iterator it = AEs.iterator();
        List aes = new ArrayList();
        while (it.hasNext()) {
            AdverseEvent ae = (AdverseEvent)it.next();
            StringBuffer key = new StringBuffer(ae.getAdverseEventTerm().getTerm().getId().toString());
            if (ae.getAdverseEventTerm().isOtherRequired()) {
                if (ae.getLowLevelTerm() == null) continue;
                key.append(ae.getLowLevelTerm().getId().toString());
            }
            if (aes.contains(key.toString())) return ae;
            aes.add(key.toString());
        }

        return null;
    }
    
    @Override
    protected void validate(CaptureAdverseEventInputCommand command, BeanWrapper commandBean, Map<String, InputFieldGroup> fieldGroups, Errors errors) {

        // START -> AE VALIDATION //
        boolean isMeddraStudy = command.getStudy().getAeTerminology().getTerm() == Term.MEDDRA;
        AdverseEvent adverseEvent = checkAEsUniqueness(command);
        if (adverseEvent != null) {
            String name = null;
            name = adverseEvent.getAdverseEventTerm().getFullName();
            if (adverseEvent.getAdverseEventTerm().isOtherRequired()) name = name + ", " + adverseEvent.getLowLevelTerm().getMeddraTerm();
            errors.reject("DUPLICATE_EXPECTED_AE", new Object[]{name}, "ERR.");
        }
        // STOP -> AE VALIDATION //

        // CHECKING VERBATIM LENGTH
        short i = 0;
        for (AdverseEvent ae : command.getAdverseEventReportingPeriod().getAdverseEvents()) {

            if (ae.getDetailsForOther() != null && ae.getDetailsForOther().length() > VERBATIM_MAX_SIZE) {
                errors.rejectValue("adverseEvents[" + i + "].detailsForOther", "SAE_021", new Object[] {VERBATIM_MAX_SIZE}, "The size of the verbatim value should not exceed " +  VERBATIM_MAX_SIZE + " characters.");
            }

            // If grade is greater than 2 then hospitalization cannot be null.
            if(!command.getAdverseEventReportingPeriod().isBaselineReportingType()) {
                if (ae.getGrade() != null) {
    				if (ae.getGrade().getCode() > 2 && ae.getHospitalization() == null)
    					errors.rejectValue("adverseEvents[" + i + "].hospitalization", "CAE_004", "Hospitalization must be entered if grade is greater than 2");
    			}
            }
            
            i++;
        }

        command.setErrorsForFields(new HashMap<String, Boolean>());
        WebUtils.populateErrorFieldNames(command.getErrorsForFields(), errors);
    }
    
    public void setAdverseEventRoutingAndReviewRepository(AdverseEventRoutingAndReviewRepository adverseEventRoutingAndReviewRepository) {
		this.adverseEventRoutingAndReviewRepository = adverseEventRoutingAndReviewRepository;
	}
    
}