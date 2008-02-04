package gov.nih.nci.cabig.caaers.rules.author;

import gov.nih.nci.cabig.caaers.rules.brxml.Category;
import gov.nih.nci.cabig.caaers.rules.brxml.Rule;
import gov.nih.nci.cabig.caaers.rules.brxml.RuleSet;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/** 
 * Remote interface for Rule Provisioning. 
 * This interface can be exposed as a webservice.
 * 
 * @author Sujith Vellat Thayyilthodi
 * */

public interface RuleAuthoringService extends java.rmi.Remote {

	public void createCategory(Category category) throws RemoteException;
	
	public Category getCategory(String categoryPath) throws RemoteException;
	
	public void createRuleSet(RuleSet ruleSet) throws RemoteException;
	
	public String createRule(Rule rule) throws RemoteException;
	
	public void updateRule(Rule rule) throws RemoteException;
	
	public void updateRuleSet(RuleSet ruleSet) throws RemoteException;
	
	public RuleSet getRuleSet(String ruleSetId,boolean cached) throws RemoteException;

	public Rule getRule(String ruleId) throws RemoteException;
	
	public List<Rule> getRulesByCategory(String categoryPath) throws RemoteException;

	public List<RuleSet> getAllRuleSets() throws RemoteException;
	
	public void addRuleExecutionSet(final String bindUri,
				final InputStream resourceAsStream, final Map properties);
	
	// REVISIT: Should be removed once testing is done
	public void listPackages();
	
	public boolean containsRuleSet(String ruleSetName);
	
	public List<RuleSet> findRuleSetsForSponsor(String sponsorName);
	
	public List<RuleSet> findRuleSetsForStudy(String sponsorName, String studyName);
}