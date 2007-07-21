package gov.nih.nci.cabig.caaers.web.fields;

/**
 * @author Rhett Sutphin
 */
public class DefaultTextField extends AbstractInputField {

    public DefaultTextField() { }

    public DefaultTextField(String propertyName, String displayName) {
        this(propertyName, displayName, false);
    }

    public DefaultTextField(String propertyName, String displayName, boolean required) {
        super(propertyName, displayName, required);
    }

    @Override
    public Category getCategory() {
        return Category.TEXT;
    }
    
    public void setSize(int size){
    	getAttributes().put(InputField.SIZE, size);
    }
    public int getSize(){
    	Integer i = (Integer)getAttributes().get(InputField.SIZE);
    	return (i == null) ? 0 : i.intValue();
    }
}
