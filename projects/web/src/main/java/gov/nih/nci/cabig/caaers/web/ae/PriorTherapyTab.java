package gov.nih.nci.cabig.caaers.web.ae;

import org.springframework.beans.BeanWrapper;
import org.springframework.validation.Errors;

import java.util.Map;
import java.util.ListIterator;

import gov.nih.nci.cabig.caaers.domain.AdverseEventPriorTherapy;
import gov.nih.nci.cabig.caaers.web.fields.DefaultTextField;
import gov.nih.nci.cabig.caaers.web.fields.DefaultDateField;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroup;
import gov.nih.nci.cabig.caaers.web.fields.AutocompleterField;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroupMap;
import gov.nih.nci.cabig.caaers.web.fields.RepeatingFieldGroupFactory;

/**
 * @author Rhett Sutphin
 */
public class PriorTherapyTab extends AeTab {
    private RepeatingFieldGroupFactory fieldFactory;
    private RepeatingFieldGroupFactory agentFactory;

    public PriorTherapyTab() {
        super("Prior Therapies", "Therapies", "ae/priorTherapies");
        fieldFactory = new RepeatingFieldGroupFactory("priorTherapy", "aeReport.adverseEventPriorTherapies");
        fieldFactory.setDisplayNameCreator(new RepeatingFieldGroupFactory.DisplayNameCreator() {
            public String createDisplayName(int index) {
                return "Medication " + (index + 1);
            }
        });
        fieldFactory.addField(new AutocompleterField("priorTherapy", "Therapy", false));
        fieldFactory.addField(new DefaultTextField("other", "Other", false));
        fieldFactory.addField(new DefaultDateField("startDate", "Start Date", false));
        fieldFactory.addField(new DefaultDateField("endDate", "End Date", false));
        fieldFactory.addField(new AutocompleterField("priorTherapyAgents.agent", "Agent", false));
        
        agentFactory = new RepeatingFieldGroupFactory("ptAgent", "aeReport.adverseEventPriorTherapies");
        //agentFactory.addField(new AutocompleterField("agent", "Agent", false));
        
        
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, InputFieldGroup> createFieldGroups(ExpeditedAdverseEventInputCommand command) {
    	
        InputFieldGroupMap groups = new InputFieldGroupMap();
        groups.addRepeatingFieldGroupFactory(fieldFactory, command.getAeReport().getAdverseEventPriorTherapies().size());
        groups.addRepeatingFieldGroupFactory(agentFactory);
        return groups;
    }

    @Override
    protected void validate(
        ExpeditedAdverseEventInputCommand command, BeanWrapper commandBean,
        Map<String, InputFieldGroup> fieldGroups, Errors errors
    ) {
        for (ListIterator<AdverseEventPriorTherapy> it = command.getAeReport().getAdverseEventPriorTherapies().listIterator(); it.hasNext();) {
            AdverseEventPriorTherapy aePriorTherapy = it.next();
            validatePriorTherapy(aePriorTherapy, it.previousIndex(), errors);
        }
    }
    
    private void validatePriorTherapy(AdverseEventPriorTherapy aePriorTherapy, int index, Errors errors) {
        if (aePriorTherapy.getPriorTherapy() == null && aePriorTherapy.getOther() == null) {
            errors.rejectValue(
                String.format("aeReport.adverseEventPriorTherapies[%d]", index),
                "REQUIRED",
                "Either Prior Therapy or Other is required"
            );
        }
    }
}
