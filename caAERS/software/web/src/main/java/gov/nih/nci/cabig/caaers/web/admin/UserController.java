package gov.nih.nci.cabig.caaers.web.admin;

import gov.nih.nci.cabig.caaers.dao.UserDao;
import gov.nih.nci.cabig.caaers.security.CSMUser;
import gov.nih.nci.cabig.caaers.security.CaaersSecurityFacade;
import gov.nih.nci.cabig.caaers.security.CaaersSecurityFacadeImpl;
import gov.nih.nci.cabig.caaers.tools.spring.tabbedflow.AutomaticSaveAjaxableFormController;
import gov.nih.nci.cabig.caaers.web.user.ResetPasswordController;
import gov.nih.nci.cabig.ctms.suite.authorization.ProvisioningSessionFactory;
import gov.nih.nci.cabig.ctms.suite.authorization.SuiteRoleMembership;
import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.FlowFactory;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.security.authorization.domainobjects.User;

import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.mail.MailException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class UserController<C extends UserCommand> extends AutomaticSaveAjaxableFormController<C, gov.nih.nci.cabig.caaers.domain.User, UserDao>  {
	
	protected CaaersSecurityFacadeImpl caaersSecurityFacade;
	protected ProvisioningSessionFactory proSessionFactory;
	
	@Override
    public FlowFactory<C> getFlowFactory() {
        return new FlowFactory<C>() {
            @SuppressWarnings("unchecked")
			public Flow<C> createFlow(C cmd) {
                Flow<C> flow = new Flow<C>("Create User");
                flow.addTab((Tab<C>)new UserTab());
                return flow;
        }
    };
    }
	
	public UserController() {
        setCommandClass(UserCommand.class);
    }
	
	@Override
    protected Object formBackingObject(final HttpServletRequest request) throws ServletException {
		UserCommand command = new UserCommand();
		command.setCsmUser(new CSMUser());
		return command;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView processFinish(HttpServletRequest request,HttpServletResponse response, Object userCommand, BindException errors) throws Exception {
		String emailSendingErrorMessage = "";
        UserCommand command = (UserCommand)userCommand;
        CSMUser user = command.getCsmUser();
        boolean isCreateMode = user == null || user.getId() == null;
        ModelAndView modelAndView = new ModelAndView("admin/user");
        User csmUser = null;
        try {
			//Create or Update User 
			csmUser = createOrUpdateCSMUser(request,user);
			//Process the RoleMemeberships for the User.
			processRoleMemberships(csmUser,command.getRoleMemberships());
        }catch (MailException e) {
        	processRoleMemberships(csmUser,command.getRoleMemberships());
            emailSendingErrorMessage = "Could not send email to user.";
            logger.error("Could not send email to user.", e);
        }
        if (!errors.hasErrors()) {
            String statusMessage = getMessageSource().getMessage(isCreateMode ? "MSG_USER.created" : "MSG_USER.updated", null, Locale.getDefault());
            
            if (!StringUtils.isBlank(emailSendingErrorMessage)) {
                statusMessage = statusMessage + getMessage("USR_017", "");
            }
            modelAndView.getModel().put("flashMessage", statusMessage);
        }
        modelAndView.addAllObjects(errors.getModel());
		return modelAndView;
	}
	
	/**
	 * This method processes the given UserCommand.
	 * Creates a user in CSM.
	 * Provisions all the Roles & Role-Memberships for the User in CSM
	 * @param userCommand
	 */
	public void processUserCommand(HttpServletRequest request,UserCommand userCommand){
		User csmUser = null;
		try{
			//Create or Update User 
			csmUser = createOrUpdateCSMUser(request,userCommand.getCsmUser());
			//Process the RoleMemeberships for the User.
			processRoleMemberships(csmUser,userCommand.getRoleMemberships());
		}catch(MailException mEx){
			//TODO
			//Handle exception appropriately for UI purposes.
			processRoleMemberships(csmUser,userCommand.getRoleMemberships());
			mEx.printStackTrace();
		}catch(Exception ex){
			//TODO
			//Handle exception appropriately for UI purposes.
			ex.printStackTrace();
		}
	}
	
	/**
	 * This method creates a User in CSM.
	 * @param request
	 * @param csmUser
	 * @return
	 */
	protected User createOrUpdateCSMUser(HttpServletRequest request,CSMUser caaersUser){
		
		return caaersSecurityFacade.createOrUpdateCSMUser(caaersUser, 
													   ResetPasswordController.getURL(request.getScheme(), 
																					   request.getServerName(),
																					   request.getServerPort(),
																					   request.getContextPath()));
	}
	
	/**
	 * This method delegates the call to CaaersSecurityFacade to provision all the RoleMemberships for the given User in CSM.
	 * @param csmUser
	 * @param roleMemberships
	 */
	protected void processRoleMemberships(User csmUser, List<SuiteRoleMembership> roleMemberships){
		if(roleMemberships != null && roleMemberships.size() > 0){
			caaersSecurityFacade.provisionRoleMemberships(csmUser, roleMemberships);
		}
	}
	
	//Setter & Getters.
	
	public void setCaaersSecurityFacade(CaaersSecurityFacade caaersSecurityFacade) {
		this.caaersSecurityFacade = (CaaersSecurityFacadeImpl)caaersSecurityFacade;
	}

	@Override
	protected CSMUser getPrimaryDomainObject(C command) {
		// TODO Auto-generated method stub
		return command.getCsmUser();
	}

	@Override
	protected UserDao getDao() {
		return null;
	}

	public ProvisioningSessionFactory getProSessionFactory() {
		return proSessionFactory;
	}

	public void setProSessionFactory(ProvisioningSessionFactory proSessionFactory) {
		this.proSessionFactory = proSessionFactory;
	}
}