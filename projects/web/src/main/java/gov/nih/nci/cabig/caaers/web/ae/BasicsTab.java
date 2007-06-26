package gov.nih.nci.cabig.caaers.web.ae;

import static gov.nih.nci.cabig.caaers.web.fields.BaseSelectField.collectOptions;
import gov.nih.nci.cabig.caaers.dao.CtcDao;
import gov.nih.nci.cabig.caaers.domain.Grade;
import gov.nih.nci.cabig.caaers.domain.CtcTerm;
import gov.nih.nci.cabig.caaers.domain.AdverseEvent;
import gov.nih.nci.cabig.caaers.domain.Hospitalization;
import gov.nih.nci.cabig.caaers.domain.Attribution;
import gov.nih.nci.cabig.caaers.web.fields.AutocompleterField;
import gov.nih.nci.cabig.caaers.web.fields.BooleanSelectField;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroup;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroupMap;
import gov.nih.nci.cabig.caaers.web.fields.DefaultTextArea;
import gov.nih.nci.cabig.caaers.web.fields.RepeatingFieldGroupFactory;
import gov.nih.nci.cabig.caaers.web.fields.InputField;
import gov.nih.nci.cabig.caaers.web.fields.DefaultInputFieldGroup;
import gov.nih.nci.cabig.caaers.web.fields.DefaultDateField;
import gov.nih.nci.cabig.caaers.web.fields.DefaultSelectField;
import gov.nih.nci.cabig.caaers.web.fields.LongSelectField;
import gov.nih.nci.cabig.caaers.web.fields.BaseSelectField;
import gov.nih.nci.cabig.caaers.service.EvaluationService;

import java.util.Map;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.BeanWrapper;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Rhett Sutphin
 */
public class BasicsTab extends AeTab {
    private static final String REPORT_FIELD_GROUP = "report";
    private static final String MAIN_FIELD_GROUP = "main";
    private static final String CTC_TERM_FIELD_GROUP = "ctcTerm";
    private static final String CTC_OTHER_FIELD_GROUP = "ctcOther";

    private CtcDao ctcDao;
    private EvaluationService evaluationService;

    private InputFieldGroup reportFieldGroup;
    private RepeatingFieldGroupFactory mainFieldFactory, ctcTermFieldFactory, ctcOtherFieldFactory;

    public BasicsTab() {
        super("Enter basic AE information", "AEs", "ae/enterBasic");

        reportFieldGroup = new DefaultInputFieldGroup(REPORT_FIELD_GROUP);
        reportFieldGroup.getFields().add(new DefaultDateField(
            "aeReport.detectionDate", "Detection date", true));

        mainFieldFactory = new RepeatingFieldGroupFactory(MAIN_FIELD_GROUP, "aeReport.adverseEvents");
        mainFieldFactory.addField(new LongSelectField("grade", "Grade", true,
                collectOptions(Arrays.asList(Grade.values()), "name", null)));
        DefaultSelectField attributionField = new DefaultSelectField(
            "attributionSummary", "Attribution to study", false, createAttributionOptions());
        attributionField.getAttributes().put(InputField.DETAILS,
            "Indicate the likelihood that this adverse event is attributable to any element of the study protocol.");
        mainFieldFactory.addField(attributionField);
        mainFieldFactory.addField(new DefaultSelectField(
            "hospitalization", "Hospitalization", true,
                collectOptions(Arrays.asList(Hospitalization.values()), "name", "displayName")));
        mainFieldFactory.addField(new BooleanSelectField(
            "expected", "Expected", true));
        mainFieldFactory.addField(new DefaultTextArea(
            "comments", "Comments", false));

        ctcTermFieldFactory = new RepeatingFieldGroupFactory(CTC_TERM_FIELD_GROUP, "aeReport.adverseEvents");
        AutocompleterField ctcTermField = new AutocompleterField("ctcTerm", "CTC term", true);
        ctcTermField.getAttributes().put(InputField.DETAILS,
            "Type a portion of the CTC term you are looking for.  " +
            "If you select a category, only terms in that category will be shown.");
        ctcTermFieldFactory.addField(ctcTermField);

        ctcOtherFieldFactory = new RepeatingFieldGroupFactory(CTC_OTHER_FIELD_GROUP, "aeReport.adverseEvents");
        ctcOtherFieldFactory.addField(new DefaultTextArea("detailsForOther", "Other (specify)", false));
    }

    private Map<Object, Object> createAttributionOptions() {
        Map<Object, Object> attributionOptions = new LinkedHashMap<Object, Object>();
        attributionOptions.put("", "Please select");
        attributionOptions.putAll(
            BaseSelectField.collectOptions(Arrays.asList(Attribution.values()), "name", null));
        return attributionOptions;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, InputFieldGroup> createFieldGroups(ExpeditedAdverseEventInputCommand command) {
        InputFieldGroupMap map = new InputFieldGroupMap();
        map.addInputFieldGroup(reportFieldGroup);
        int aeCount = command.getAeReport().getAdverseEvents().size();
        map.addRepeatingFieldGroupFactory(mainFieldFactory, aeCount);
        map.addRepeatingFieldGroupFactory(ctcTermFieldFactory, aeCount);
        map.addRepeatingFieldGroupFactory(ctcOtherFieldFactory, aeCount);
        return map;
    }

    @Override
    public Map<String, Object> referenceData(ExpeditedAdverseEventInputCommand command) {
        Map<String, Object> refdata = super.referenceData(command);
        refdata.put("ctcCategories", command.getAssignment().getStudySite().getStudy().getCtcVersion().getCategories());
        return refdata;
    }

    @Override
    public boolean isAllowDirtyForward() {
        return false;
    }

    @Override
    protected void validate(
        ExpeditedAdverseEventInputCommand command, BeanWrapper commandBean,
        Map<String, InputFieldGroup> fieldGroups, Errors errors
    ) {
        // TODO: validate that there is at least one AE
        for (ListIterator<AdverseEvent> lit = command.getAeReport().getAdverseEvents().listIterator(); lit.hasNext();) {
            AdverseEvent ae =  lit.next();
            validateAdverseEvent(ae, lit.previousIndex(), fieldGroups, errors);
        }
    }

    private void validateAdverseEvent(AdverseEvent ae, int index, Map<String, InputFieldGroup> groups, Errors errors) {
        CtcTerm ctcTerm = ae.getCtcTerm();
        if (ctcTerm != null && ctcTerm.isOtherRequired() && ae.getDetailsForOther() == null) {
            InputField field = groups.get(CTC_OTHER_FIELD_GROUP + index).getFields().get(0);
            errors.rejectValue(field.getPropertyName(), "REQUIRED", "Missing " + field.getDisplayName());
        }
    }

    @Override
    public void postProcess(HttpServletRequest request, ExpeditedAdverseEventInputCommand command, Errors errors) {
        super.postProcess(request, command, errors);
        boolean severe = false;
        for (AdverseEvent event : command.getAeReport().getAdverseEvents()) {
            severe |= evaluationService.isSevere(command.getAssignment(), event);
        }
        request.setAttribute("oneOrMoreSevere", severe);
    }

    ////// CONFIGURATION

    @Required
    public void setCtcDao(CtcDao ctcDao) {
        this.ctcDao = ctcDao;
    }

    // for testing
    CtcDao getCtcDao() {
        return ctcDao;
    }

    @Required
    public void setEvaluationService(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }
}
