package gov.nih.nci.cabig.caaers.web.ae;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;

import gov.nih.nci.cabig.caaers.dao.AdverseEventReportingPeriodDao;
import gov.nih.nci.cabig.caaers.domain.ExpeditedAdverseEventReport;
import gov.nih.nci.cabig.caaers.validation.validator.WebControllerValidator;
import gov.nih.nci.cabig.caaers.web.ControllerTools;
import gov.nih.nci.cabig.caaers.web.RenderDecisionManager;
import gov.nih.nci.cabig.ctms.web.chrome.Task;
import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.FlowFactory;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;

/**
 *
 * @author Sameer Sawant
 */
public class CreateAdverseEventController extends AbstractAdverseEventInputController {
	
	private Task task;
	
	protected WebControllerValidator webControllerValidator;
	
	public CreateAdverseEventController() {
			super();
	       setCommandClass(CreateExpeditedAdverseEventCommand.class);
	       setBindOnNewForm(true);
	   }

	   @Override
	   protected FlowFactory<ExpeditedAdverseEventInputCommand> createFlowFactory() {
	       return new CreateExpeditedFlowFactory("Create expedited report") {
	               
	               private String instructions = "In order to create an expedited AE report, you need to first select a participant and a\n" +
	           "study. You may start with either one. Once you have selected one, the options\n" +
	           "for the other will be automatically constrained.";
	               
	           @Override
	           protected void addPreBasicTabs(Flow<ExpeditedAdverseEventInputCommand> flow) {
	               flow.addTab(new CreateExpeditedReportBeginTab<ExpeditedAdverseEventInputCommand>(instructions));
	           }
	       };
	   }
	   
	   
	   @Override
	   protected Object formBackingObject(HttpServletRequest request) throws Exception {
		   
		   //remove the edit command from the session
		   request.getSession(true).removeAttribute(EditAdverseEventController.class.getName() + ".FORM.command");
		   request.getSession().removeAttribute(EditAdverseEventController.class.getName() + ".FORM.command.to-replace");
		   
		   RenderDecisionManager renderDecisionManager = renderDecisionManagerFactoryBean.getRenderDecisionManager();
		   CreateExpeditedAdverseEventCommand command = new CreateExpeditedAdverseEventCommand(reportDao, reportDefinitionDao,
				   reportingPeriodDao, expeditedReportTree,renderDecisionManager, evaluationService, reportRepository, studyDao);
		   command.getAeReport().setCreatedAt(nowFactory.getNowTimestamp());
		   command.setWorkflowEnabled(getConfiguration().get(getConfiguration().ENABLE_WORKFLOW));
	    	
	    	return command;
	   }
	   
	   @Override
	   protected boolean shouldSave(HttpServletRequest request,	ExpeditedAdverseEventInputCommand command,	Tab<ExpeditedAdverseEventInputCommand> tab) {
		   return super.shouldSave(request, command, tab) && command.getAeReport().getId() != null;
	   }

	   @Override
	   protected ExpeditedAdverseEventInputCommand save(ExpeditedAdverseEventInputCommand command, Errors errors) {
	       command.save();
	       return null;
	   }
	   
	   @Override
	    protected void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
	        super.onBind(request, command, errors);
	    }

	   @Override
	   protected boolean displaySummary(int page) {
	       return page != 0;
	   }
	   
	   public void setTask(Task task){
		   this.task = task;
	   }
	   
	   public Task getTask(){
		   return task;
	   }
	   
	   @Required
		public void setWebControllerValidator(WebControllerValidator webControllerValidator) {
		    this.webControllerValidator = webControllerValidator;
		}
	   
}