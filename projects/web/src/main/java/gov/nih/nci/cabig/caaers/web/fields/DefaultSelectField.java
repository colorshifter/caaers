package gov.nih.nci.cabig.caaers.web.fields;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Map;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * @author Rhett Sutphin
 */
public class DefaultSelectField extends BaseSelectField {
    public DefaultSelectField() { }

    public DefaultSelectField(
        String propertyName, String displayName, boolean required, Map<Object, Object> options
    ) {
        super(propertyName, displayName, required, options);
    }

    @Override
    public Category getCategory() {
        return Category.SELECT;
    }
}
