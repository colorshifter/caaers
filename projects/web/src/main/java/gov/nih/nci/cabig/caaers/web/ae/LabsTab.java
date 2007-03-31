package gov.nih.nci.cabig.caaers.web.ae;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Rhett Sutphin
 */
public class LabsTab extends AeTab {
    private RepeatingFieldGroupFactory fieldFactory;

    public LabsTab() {
        super("Diagnostic test and lab results", "Labs", "ae/labs");
        fieldFactory = new RepeatingFieldGroupFactory("lab", "aeReport.labs");
        fieldFactory.setDisplayNameCreator(new RepeatingFieldGroupFactory.DisplayNameCreator() {
            public String createDisplayName(int index) {
                char c = (char) ('A' + index);
                return "Lab " + c;
            }
        });
        fieldFactory.addField(new DefaultTextField("name", "Lab name", true));
        fieldFactory.addField(new DefaultTextField("units", "Units", true));
        addLabValueFields("baseline", "Baseline");
        addLabValueFields("nadir", "AE lab");
        addLabValueFields("recovery", "Recovery");
    }

    private void addLabValueFields(String propName, String displayName) {
        fieldFactory.addField(new DefaultTextField(propName + ".value", displayName + " value", false));
        fieldFactory.addField(new DefaultDateField(propName + ".date", displayName + " date", false));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Map<String, InputFieldGroup> createFieldGroups(AdverseEventInputCommand command) {
        InputFieldGroupMap groups = new InputFieldGroupMap();
        groups.addRepeatingFieldGroupFactory(fieldFactory, command.getAeReport().getLabs().size());
        return groups;
    }
}
