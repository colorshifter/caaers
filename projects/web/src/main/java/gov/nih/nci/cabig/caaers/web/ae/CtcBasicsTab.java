package gov.nih.nci.cabig.caaers.web.ae;

import gov.nih.nci.cabig.caaers.dao.CtcDao;
import gov.nih.nci.cabig.caaers.domain.AdverseEvent;
import gov.nih.nci.cabig.caaers.domain.CtcTerm;
import gov.nih.nci.cabig.caaers.web.fields.InputField;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldAttributes;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldFactory;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroup;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;

import java.util.Map;

/**
 * @author Rhett Sutphin
 */
public class CtcBasicsTab extends BasicsTab {
    private static final String CTC_TERM_FIELD_GROUP = "ctcTerm";
    private static final String CTC_OTHER_FIELD_GROUP = "ctcOther";

    private CtcDao ctcDao;

    public CtcBasicsTab() {
        super("Enter basic AE information", "Enter AEs", "ae/enterBasic");
    }

    @Override
    public Map<String, Object> referenceData(ExpeditedAdverseEventInputCommand command) {
        Map<String, Object> refdata = super.referenceData(command);
        refdata.put("ctcCategories", command.getAssignment().getStudySite().getStudy().getTerminology().getCtcVersion().getCategories());
        return refdata;
    }

    @Override
    protected void createFieldGroups(AeInputFieldCreator creator, ExpeditedAdverseEventInputCommand command) {
        super.createFieldGroups(creator, command);

        InputField ctcTermField = InputFieldFactory.createAutocompleterField("adverseEventCtcTerm.ctcTerm", "CTC term", true);
        InputFieldAttributes.setDetails(ctcTermField,
            "Type a portion of the CTC term you are looking for.  If you select a category, only terms in that category will be shown.");
        creator.createRepeatingFieldGroup(CTC_TERM_FIELD_GROUP, "adverseEvents", ctcTermField);

        creator.createRepeatingFieldGroup(CtcBasicsTab.CTC_OTHER_FIELD_GROUP, "adverseEvents",
            InputFieldFactory.createTextArea("detailsForOther", "Other (specify)", false)
        );
    }

    @Override
    protected void validateAdverseEvent(AdverseEvent ae, int index, Map<String, InputFieldGroup> groups, Errors errors) {
        CtcTerm ctcTerm = ae.getAdverseEventCtcTerm().getCtcTerm();

        if (ctcTerm != null && ctcTerm.isOtherRequired() && ae.getDetailsForOther() == null) {
            InputField field = groups.get(CTC_OTHER_FIELD_GROUP + index).getFields().get(0);
            errors.rejectValue(field.getPropertyName(), "REQUIRED", "Missing " + field.getDisplayName());
        }
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
}
