package gov.nih.nci.cabig.caaers.web.ae;

import java.util.Map;

/**
 * @author Rhett Sutphin
 */
public interface SelectField extends InputField {
    Map<Object, Object> getOptions();
}
