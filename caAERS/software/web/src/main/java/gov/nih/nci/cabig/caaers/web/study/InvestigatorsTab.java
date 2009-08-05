package gov.nih.nci.cabig.caaers.web.study;

import gov.nih.nci.cabig.caaers.domain.StudyInvestigator;
import gov.nih.nci.cabig.caaers.domain.StudyOrganization;
import gov.nih.nci.cabig.caaers.web.fields.DefaultInputFieldGroup;
import gov.nih.nci.cabig.caaers.web.fields.InputField;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldFactory;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroup;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroupMap;
import gov.nih.nci.cabig.caaers.web.fields.RepeatingFieldGroupFactory;
import gov.nih.nci.cabig.caaers.web.utils.WebUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanWrapper;
import org.springframework.validation.Errors;

/**
 * @author Ion C. Olaru
 * @author Biju Joseph
 */
class InvestigatorsTab extends StudyTab {
    private List<InputField> fields;

    public InvestigatorsTab() {
        super("Investigators", "Investigators", "study/study_investigators");
    }

    @Override
    public Map<String, Object> referenceData(HttpServletRequest request, StudyCommand command) {
        Map<String, Object> refdata = super.referenceData(request, command);
        return refdata;
    }

    @Override
    public void postProcess(HttpServletRequest request, StudyCommand command, Errors errors) {
        String action = request.getParameter("_action");
        String selectedInvestigator = request.getParameter("_selectedInvestigator");
        String prevSiteIndex = request.getParameter("_prevSite");
        int selectedIndex = command.getStudySiteIndex();
        int selectedInvestigatorIndex;

        if (request.getParameter("_selectedInvestigator") != null && !request.getParameter("_selectedInvestigator").equals(""))
            selectedInvestigatorIndex = Integer.parseInt(selectedInvestigator);
        else
            selectedInvestigatorIndex = -1;

        if ("removeInv".equals(action) && selectedIndex >= 0) {
        	int index = Integer.parseInt(selectedInvestigator);
        	command.deleteSiteInvestigatorAtIndex(selectedIndex, index);
        } else if ("changeSite".equals(action) && errors.hasErrors()) {
            int siteIndex = Integer.parseInt(prevSiteIndex);
            command.setStudySiteIndex(siteIndex);
            if (siteIndex >= 0) {
                command.getStudy().getActiveStudyOrganizations().get(siteIndex).getStudyInvestigators().get(0);
            }
        } else if ("activate".equals(action)) {
            command.getStudy().getActiveStudyOrganizations().get(selectedIndex).getActiveStudyInvestigators().get(selectedInvestigatorIndex).activate();
        } else if ("deactivate".equals(action)) {
            command.getStudy().getActiveStudyOrganizations().get(selectedIndex).getActiveStudyInvestigators().get(selectedInvestigatorIndex).deactivate();
        }
        
        //will sync the start and end date. 
        if (command.getStudySiteIndex() >= 0) {
            StudyOrganization so = command.getStudy().getActiveStudyOrganizations().get(command.getStudySiteIndex());
            for (StudyInvestigator si : so.getActiveStudyInvestigators()) {
                if (si.getId() == null) {
                  si.syncDates();
                }
            }
        }
    }

    @Override
    public Map<String, InputFieldGroup> createFieldGroups(StudyCommand command) {
        InputFieldGroupMap map = new InputFieldGroupMap();
        InputFieldGroup siteFieldGroup = new DefaultInputFieldGroup("site");
        siteFieldGroup.getFields().add(InputFieldFactory.createSelectField("studySiteIndex", "Site", true, WebUtils.collectOptions(collectStudyOrganizations(command.getStudy()), "code", "desc")));
        map.addInputFieldGroup(siteFieldGroup);

        if (fields == null) {
            fields = new ArrayList<InputField>();
            InputField investigatorField = InputFieldFactory.createAutocompleterField("siteInvestigator", "Investigator", true);
            fields.add(investigatorField);
            fields.add(InputFieldFactory.createSelectField("roleCode", "Role", true, WebUtils.collectOptions(command.getAllInvestigatorRoles(), "code", "name", "")));
            fields.add(InputFieldFactory.createLabelField("active", "Status", false));
        }

        int ssIndex = command.getStudySiteIndex();
        if (ssIndex >= 0) {
            RepeatingFieldGroupFactory rfgFactory = new RepeatingFieldGroupFactory("main", "study.activeStudyOrganizations[" + ssIndex + "].studyInvestigators");
            for (InputField f : fields) {
                rfgFactory.addField(f);
            }
            map.addRepeatingFieldGroupFactory(rfgFactory, command.getStudy().getActiveStudyOrganizations().get(ssIndex).getStudyInvestigators().size());
        }
        return map;
    }

    @Override
    protected void validate(StudyCommand command, BeanWrapper commandBean, Map<String, InputFieldGroup> fieldGroups, Errors errors) {
        super.validate(command, commandBean, fieldGroups, errors);

        if (command.getStudySiteIndex() >= 0) {
            HashSet<String> hSet = new HashSet<String>();
            HashSet<String> hSetPrincipal = new HashSet<String>();
            boolean hasSIDuplicates = false;

            StudyOrganization so = command.getStudy().getActiveStudyOrganizations().get(command.getStudySiteIndex());

            for (StudyInvestigator si : so.getStudyInvestigators()) {

                if (si.isActive())
                    if (!hSet.add(si.getRoleCode() + si.getSiteInvestigator().getInvestigator().getId().toString())) {
                        errors.reject("STU_012", new Object[]{si.getSiteInvestigator().getInvestigator().getFullName()}, "Duplicate entry");
                    }

                if (si.getRoleCode().equals("PI") && si.isActive()) {
                    if (!hSetPrincipal.add(si.getRoleCode())) {
                        si.deactivate();
                        hasSIDuplicates = true;
                    }
                }
            }

            if (hasSIDuplicates)
                errors.reject("STU_019");
                
        }

    }

}
