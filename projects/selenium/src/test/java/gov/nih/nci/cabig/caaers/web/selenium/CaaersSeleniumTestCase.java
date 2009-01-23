package gov.nih.nci.cabig.caaers.web.selenium;
import com.thoughtworks.selenium.*;

import java.util.regex.Pattern;

public class CaaersSeleniumTestCase extends SeleneseTestCase {
	String studyId=null;
	AjaxWidgets aw;
	public void setUp() throws Exception {
		setUp("https://oracle.qa.semanticbits.com", "*chrome");
		aw=new AjaxWidgets(selenium);
		// DefaultSelenium selenium = new DefaultSelenium("localhost", 4444,
		// "*chrome", "http://localhost:8080/ctcae");
		// selenium.start();
		
	}

	public void testLogin() throws Exception {
		aw.login();
		assertTrue("Login Failure", true);
		assertTrue("Login Failure", selenium.isTextPresent("Regular Tasks"));
	}

	

	public void searchStudy(String studyId) throws InterruptedException {
		selenium.open("/caaers/pages/task");
		selenium.waitForPageToLoad("30000");
		selenium.click("firstlevelnav_searchStudyController");
		selenium.waitForPageToLoad("30000");
		selenium.select("searchCriteria[0].searchType", "label=Identifier");
		selenium.type("searchCriteria[0].searchText", studyId);
		selenium.click("//input[@value='Search']");
		aw.waitForElementPresent("//input[@name='ajaxTable_f_primaryIdentifierValue']");
		Thread.sleep(4000);
	}

	public void checkLogin() throws Exception {
		selenium.open("/caaers/pages/task");
		selenium.waitForPageToLoad("30000");
		if (selenium.isTextPresent("Please Log in")) {
			aw.login();
		}

	}

	public void editStudy() throws InterruptedException {
		aw.clickNext("flow-next");
		populateEditStudyDetails();
		aw.clickNext("flow-next");
		aw.clickNext("flow-next");
		populateEditStudyTreatmentAssignments();
		aw.clickNext("flow-next");
		populateEditStudyDisease();
		aw.clickNext("flow-next");
		populateEditStudyEvalPeriod();
		aw.clickNext("flow-next");
		//populateEditStudySites();
		aw.clickNext("flow-next");
		populateEditStudyInvestigators();
		aw.clickNext("flow-next");
		aw.clickNext("flow-next");
		populateEditStudyIdentifiers();
		aw.clickNext("flow-next");
	}

	public void populateEditStudyIdentifiers() throws InterruptedException {
		selenium.click("add-organization-section-row-button");
		aw.waitForElementPresent("study.identifiersLazy[2].value");
		selenium.type("study.identifiersLazy[2].value", "N027D-test1");
		selenium.select("study.identifiersLazy[2].type", "label=Site Identifier");
		selenium.click("//select[@id='study.identifiersLazy[2].type']/option[2]");
		aw.typeAutosuggest("study.identifiersLazy[2].organization-input", "mn003",
				"study.identifiersLazy[2].organization-choices");

		selenium.click("//img[@alt='delete']");
		selenium.waitForPageToLoad("30000");
	}

	public void populateEditStudyInvestigators() throws InterruptedException {
		selenium.select("studySiteIndex",
				"label=University of Alabama at Birmingham (Site)");
		Thread.sleep(5000);
		selenium.click("//img[@alt='delete']");
		selenium.waitForPageToLoad("30000");
		aw.confirmOK("^Do you really want to delete[\\s\\S]$");
		selenium.click("add-ssi-table-row-button");
		aw.waitForElementPresent("study.studyOrganizations[2].studyInvestigators[0].siteInvestigator-input");
		aw.typeAutosuggest(
				"study.studyOrganizations[2].studyInvestigators[0].siteInvestigator-input",
				"john",
				"study.studyOrganizations[2].studyInvestigators[0].siteInvestigator-choices");
		selenium.select("study.studyOrganizations[2].studyInvestigators[0].roleCode",
				"label=Site Principal Investigator");
		selenium.click("//option[@value='Site Principal Investigator']");
		selenium.select(
				"study.studyOrganizations[2].studyInvestigators[0].statusCode",
				"label=Active");
		selenium.click("//option[@value='Active']");
	}

	public void populateEditStudyEvalPeriod() throws InterruptedException {
		selenium.click("addSingleTermBtn");
		aw.typeAutosuggest("termCode-input", "vasovagal", "termCode-choices");

		selenium.click("addSingleTermBtn");
		Thread.sleep(4000);
		selenium.click("DELETE_3");
		aw.confirmOK("^Are you sure you want to delete this[\\s\\S]$");
		Thread.sleep(1000);
	}

	public void populateEditStudySites() throws InterruptedException {
		selenium.click("add-ss-section-button");
		aw.waitForElementPresent("study.studySites[1].organization-clear");

		selenium.click("study.studySites[1].organization-clear");
		aw.typeAutosuggest("study.studySites[1].organization-input", "mn003",
				"study.studySites[1].organization-choices");
		selenium.click("//a[@id='del-1']/img");
		selenium.waitForPageToLoad("30000");
		aw.confirmOK("^Do you really want to delete[\\s\\S]$");
	}

	public void populateEditStudyDisease() throws InterruptedException {
		Thread.sleep(4000);
		selenium.click("diseaseCategoryAsText-clear");
		aw.typeAutosuggest("diseaseCategoryAsText-input", "bone",
				"diseaseCategoryAsText-choices");

		Thread.sleep(3000);
		selenium.removeSelection("disease-sub-category", "label=All");
		selenium.addSelection("disease-sub-category", "label=Osteosarcoma");
		Thread.sleep(3000);
		selenium.removeSelection("disease-term", "label=All");
		selenium.addSelection("disease-term", "label=Osteosarcoma");
		selenium.click("//input[@value='Add disease']");
		selenium.waitForPageToLoad("30000");
		selenium
				.click("//div[@id='contentOf-']/center/table/tbody/tr[3]/td[3]/div/a/img");
		selenium.waitForPageToLoad("30000");
		aw.clickNext("flow-next");
		aw.typeAutosuggest("termCode-input", "nausea", "termCode-choices");

		selenium.click("addSingleTermBtn");
		aw.waitForElementPresent("button-3207");
		selenium.click("button-3207");
		aw.confirmOK("^Are you sure you want to delete this[\\s\\S]$");

	}

	public void populateEditStudyTreatmentAssignments()
			throws InterruptedException {
		selenium.click("select-agent-0");
		selenium.click("AddStudyAgent");
		aw.waitForElementPresent("//div[@id='sa-section-1']");

		selenium.click("select-agent-1");
		aw.typeAutosuggest("study.studyAgents[1].agent-input", "123127",
				"study.studyAgents[1].agent-choices");

		selenium.select("study.studyAgents[1].indType", "label=CTEP IND");
		selenium.click("//div[@id='sa-section-1']/div[1]/h3/div/a/img");
		aw.confirmOK("^Are you sure you want to delete this[\\s\\S]$");
		Thread.sleep(5000);
		aw.clickNext("flow-next");
		selenium.click("add-si-section-button");
		aw.waitForElementPresent("//label[@for='study.treatmentAssignments[1].code']");

		selenium.type("study.treatmentAssignments[1].code", "TAC2");
		selenium
				.type("study.treatmentAssignments[1].description", "TAC2 description");
		selenium.click("//div[@id='si-section-1']/div[1]/h3/div/a/img");
		aw.confirmOK("^Are you sure you want to delete this[\\s\\S]$");
		Thread.sleep(4000);
	}

	public void populateEditStudyDetails() {
		selenium.type("study.shortTitle", "Some new title");
		selenium.type("study.studyCoordinatingCenter.organization-input", "NC002");
	}

	public void createCTCStudy(String fundingSponsorIdentifier) throws InterruptedException {
		studyId = fundingSponsorIdentifier;
		populateCreateStudyDetails();
		aw.clickNext("flow-next");
		populateCreateStudyTherapies();
		aw.clickNext("flow-next");
		populateCreateStudyAgents();
		aw.clickNext("flow-next");
		populateCreateStudyTreatmentAssignments();
		aw.clickNext("flow-next");
		populateCreateStudyDiseases();
		aw.clickNext("flow-next");
		populateCreateStudyEvalPeriods("nausea");
		aw.clickNext("flow-next");
		populateCreateStudyExpectedAEs("vasculitis");
		aw.clickNext("flow-next");
		populateCreateStudySites();
		aw.clickNext("flow-next");
		populateCreateStudyInvestigators();
		aw.clickNext("flow-next");
		populateCreateStudyPersonnel();
		aw.clickNext("flow-next");
		populateCreateStudyIdentifiers();
		aw.clickNext("flow-next");
		aw.clickNext("flow-next");
	}
	
	public void createMeddraStudy(String fundingSponsorIdentifier) throws InterruptedException {
		studyId = fundingSponsorIdentifier;
		populateCreateStudyDetails();
		{
			selenium.select("study.aeTerminology.term", "label=MedDRA");
			selenium.click("//option[@value='MEDDRA']");
			Thread.sleep(1000);
			selenium.select("study.aeTerminology.meddraVersion", "label=MedDRA v9");
			
		}
		aw.clickNext("flow-next");
		populateCreateStudyTherapies();
		aw.clickNext("flow-next");
		populateCreateStudyAgents();
		aw.clickNext("flow-next");
		populateCreateStudyTreatmentAssignments();
		aw.clickNext("flow-next");
		populateCreateStudyDiseases();
		aw.clickNext("flow-next");
		populateCreateStudyEvalPeriods("Nervousness");
		aw.clickNext("flow-next");
		populateCreateStudyExpectedAEs("Deja-vu");
		aw.clickNext("flow-next");
		populateCreateStudySites();
		aw.clickNext("flow-next");
		populateCreateStudyInvestigators();
		aw.clickNext("flow-next");
		populateCreateStudyPersonnel();
		aw.clickNext("flow-next");
		populateCreateStudyIdentifiers();
		aw.clickNext("flow-next");
		aw.clickNext("flow-next");
	}
	public void populateCreateStudyIdentifiers() {
		selenium.click("study.identifiersLazy[0].primaryIndicator");
	}

	public void populateCreateStudyPersonnel() {
		;
	}

	public void populateCreateStudyInvestigators() throws InterruptedException {
		selenium.select("studySiteIndex",
				"label=University of Alabama at Birmingham (Site)");
		selenium.waitForPageToLoad("30000");
		selenium.click("add-ssi-table-row-button");
		aw.waitForElementPresent("study.studyOrganizations[2].studyInvestigators[0].siteInvestigator-input");
		aw.typeAutosuggest("study.studyOrganizations[2].studyInvestigators[0].siteInvestigator-input", "fiveash", "study.studyOrganizations[2].studyInvestigators[0].siteInvestigator-choices");
		selenium.select("study.studyOrganizations[2].studyInvestigators[0].roleCode",
				"label=Site Investigator");
		selenium.click("//option[@value='Site Investigator']");
		selenium.select(
				"study.studyOrganizations[2].studyInvestigators[0].statusCode",
				"label=Active");
		selenium.click("//option[@value='Active']");
	}

	public void populateCreateStudySites() throws InterruptedException {
		selenium.click("add-ss-section-button");
		aw.waitForElementPresent("study.studySites[0].organization-input");
		aw.typeAutosuggest("study.studySites[0].organization-input", "AL002",
				"study.studySites[0].organization-choices");
	}

	public void populateCreateStudyExpectedAEs(String aeTerm) throws InterruptedException {
		//typeAutosuggest("termCode-input", "vasculitis", "termCode-choices");
		aw.typeAutosuggest("termCode-input", aeTerm, "termCode-choices");
		selenium.click("addSingleTermBtn");
		if (selenium.isElementPresent("addMultiTermBtn")){
		selenium.click("addMultiTermBtn");
		aw.waitForElementPresent("//div[@id='categories-div-id']");
		selenium.removeSelection("categories", "label=AUDITORY/EAR");
		selenium.addSelection("categories", "label=CARDIAC GENERAL");
		aw.waitForElementPresent("//option[@title='Hypertension']");
		selenium.addSelection("terms", "label=Hypertension");
		selenium.addSelection("terms", "label=Hypotension");
		selenium.click("addTermsBtn");
		}
		
	}

	public void populateCreateStudyEvalPeriods(String aeTerm) throws InterruptedException {
		//typeAutosuggest("termCode-input", "nausea", "termCode-choices");
		aw.typeAutosuggest("termCode-input", aeTerm, "termCode-choices");
		selenium.click("addSingleTermBtn");
		Thread.sleep(5000);
		selenium.click("ck0");
		selenium.click("ck1");
		selenium.click("ck2");
	}

	public void populateCreateStudyDiseases() throws InterruptedException {
		selenium.click("diseaseCategoryAsText-clear");
		aw.typeAutosuggest("diseaseCategoryAsText-input", "soft",
				"diseaseCategoryAsText-choices");
		Thread.sleep(3000);
		selenium.addSelection("disease-sub-category", "label=All");
		Thread.sleep(2000);
		selenium.removeSelection("disease-term", "label=All");
		selenium.addSelection("disease-term", "label=Synovial sarcoma");
		
		selenium.click("//input[@value='Add disease']");
		selenium.waitForPageToLoad("30000");
	}

	public void populateCreateStudyAgents() throws InterruptedException {
		selenium.click("AddStudyAgent");
		aw.waitForElementPresent("study.studyAgents[0].agent-input");
		selenium.click("select-agent-0");
		aw.typeAutosuggest("study.studyAgents[0].agent-input", "683864",
				"study.studyAgents[0].agent-choices");
		selenium.select("study.studyAgents[0].indType", "label=CTEP IND");
		selenium.select("study.studyAgents[0].partOfLeadIND", "label=Yes");
			}

	public void populateCreateStudyTreatmentAssignments()
			throws InterruptedException {
		selenium.click("add-si-section-button");
		aw.waitForElementPresent("study.treatmentAssignments[0].code");
		selenium.type("study.treatmentAssignments[0].code", "A0");
		selenium
				.type(
						"study.treatmentAssignments[0].description",
						"Concomitant Treatment      Cycle = 6 weeks:    CCI-779:  25mg/wk IV over 30 min weekly RT:  200cGy 5 days a wk, starting 7-10 days after first CCI-779 dose TMZ:  75mg/m2 PO QD with RT  Adjuvant Treatment     Cycle = 28 days     (Max = 6 cycles) CCI-779:");
	}

	public void populateCreateStudyTherapies() {
		selenium.click("study.drugAdministrationTherapyType");
		selenium.click("study.radiationTherapyType");
	}

	public void populateCreateStudyDetails() throws InterruptedException {
		selenium.open("/caaers/pages/task");
		selenium.click("firstlevelnav_searchStudyController");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@id='secondlevelnav_createStudyController']/span");
		selenium.waitForPageToLoad("30000");
		selenium
				.type(
						"study.shortTitle",
						"Phase I Study of CCI-779 and Temozolomide in Combination with Radiation Therapy in Glioblastoma Multiforme");
		selenium
				.type(
						"study.longTitle",
						"A Phase I Study of CCI-779 and Temozolomide in Combination with Radiation Therapy in Glioblastoma Multiforme");
		selenium
				.type(
						"study.description",
						"A Phase I Study of CCI-779 and Temozolomide in Combination with Radiation Therapy in Glioblastoma Multiformexxxx");
		selenium.select("study.phaseCode", "label=Phase I Trial");
		selenium.select("study.status", "label=Active - Trial is open to accrual");
		selenium.select("study.multiInstitutionIndicator", "label=Yes");
		selenium.select("study.adeersReporting", "label=Yes");
		selenium.select("study.aeTerminology.ctcVersion", "label=CTCAE v3.0");
		selenium.select("study.otherMeddra", "label=MedDRA v9");
		selenium.click("study.caaersXMLType");
		selenium.click("study.adeersPDFType");
		aw.typeAutosuggest("study.studyCoordinatingCenter.organization-input", "ncctg",
				"study.studyCoordinatingCenter.organization-choices");
		selenium.type("study.identifiers[1].value", studyId);
		aw.typeAutosuggest("study.primaryFundingSponsorOrganization-input", "ctep",
				"study.primaryFundingSponsorOrganization-choices");
		selenium.type("study.identifiers[0].value", studyId);
	}

	public void createInvestigator() throws Exception {
		selenium.open("/caaers/pages/task");
		selenium.click("firstlevelnav_configurationController");
		selenium.waitForPageToLoad("30000");
		selenium
				.click("//a[@id='secondlevelnav_createInvestigatorController']/span");
		selenium.waitForPageToLoad("30000");
		selenium.type("firstName", "Monica");
		selenium.type("lastName", "Dubinsky");
		selenium.type("emailAddress", "monica.dubinsky@xxyy.com");
		selenium.type("phoneNumber", "0000000000");

		aw.typeAutosuggest("siteInvestigators[0].organization-input", "nci",
				"siteInvestigators[0].organization-choices");
		selenium.select("siteInvestigators[0].statusCode", "label=Active");
		selenium.click("flow-next");
		selenium.waitForPageToLoad("30000");

	}
	
	public void searchInvestigator(String firstName, String lastName) throws Exception {
		selenium.open("/caaers/pages/task");
		selenium.click("firstlevelnav_configurationController");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@id='secondlevelnav_createInvestigatorController']/span");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Search Investigator");
		selenium.waitForPageToLoad("30000");
		selenium.type("firstName", firstName);
		selenium.type("lastName", lastName);
		selenium.click("//input[@value='Search']");
		aw.waitForElementPresent("//td[@title='Sort By First Name']");
		Thread.sleep(2000);

				
	}
	
	//public void editInvestigator(String ){}
	/*public void testNew() throws Exception {

		selenium.click("firstlevelnav_listAdverseEventsController");
		selenium.waitForPageToLoad("30000");
		selenium.click("study-clear");
		selenium.type("study-input", "n");
		selenium.typeKeys("study-input", "027d");
		fail("test");
		selenium.waitForCondition(String.format("selenium.isTextPresent('%s')",
				"Temozolomide"), "10000");
		selenium.click("//div[@id='study-choices']/ul/li[1]");
		selenium.click("participant-input");
		selenium.click("participant-clear");
		selenium.typeKeys("participant-input", "Jones");
		selenium.waitForCondition(String.format("selenium.isTextPresent('%s')",
				"Catherine"), "10000");
		selenium.click("//div[@id='participant-choices']/ul/li[1]");
		aw.clickNext("flow-next");
	}*/
}
