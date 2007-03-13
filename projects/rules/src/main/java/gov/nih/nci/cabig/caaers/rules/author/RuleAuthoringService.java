package gov.nih.nci.cabig.caaers.rules.author;

import gov.nih.nci.cabig.caaers.rules.brxml.Rule;
import gov.nih.nci.cabig.caaers.rules.brxml.RuleSet;

import java.rmi.RemoteException;

import javax.jws.WebService;

/**
 * Remote interface for Rule Provisioning. 
 * This interface can be exposed as a webservice.
 * 
 * @author Sujith Vellat Thayyilthodi
 * */
@WebService(
        serviceName = "RuleAuthoringService", endpointInterface = "gov.nih.nci.cabig.caaers.rules.author.RuleAuthoringService"
)
public interface RuleAuthoringService extends java.rmi.Remote {

	public void createRuleSet(RuleSet ruleSet) throws RemoteException;
	
	public String createRule(Rule rule) throws RemoteException;
	
	public void updateRule(Rule rule) throws RemoteException;
	
	public void updateRuleSet(RuleSet ruleSet) throws RemoteException;
	
	public RuleSet getRuleSet(String ruleSetId) throws RemoteException;

	public Rule getRule(String ruleId) throws RemoteException;

	public RuleSet[] getAllRuleSets() throws RemoteException;

}