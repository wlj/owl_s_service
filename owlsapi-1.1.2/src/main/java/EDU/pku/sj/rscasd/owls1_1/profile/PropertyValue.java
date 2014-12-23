package EDU.pku.sj.rscasd.owls1_1.profile;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Object;

public interface PropertyValue extends OWLS_Object {

	/**
	 * URI of target property value.
	 * 
	 * @return String
	 */
	ContextValue getPropertyContextValue();

	void setPropertyContextValue(ContextValue contextValue);

	/**
	 * URI of target property type
	 * 
	 * @return String
	 */
	String getPropertyType();

	void setPropertyType(String propertyTypeUri);
}
