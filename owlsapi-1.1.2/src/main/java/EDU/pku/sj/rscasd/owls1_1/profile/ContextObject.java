package EDU.pku.sj.rscasd.owls1_1.profile;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Object;

public interface ContextObject extends OWLS_Object {

	/**
	 * ContextValueDomain of target object individual
	 * 
	 * @return
	 */
	ContextValueDomain getContextValueDomain();

	void setContextValueDomain(ContextValueDomain contextValueDomain);
	
	PropertyValueList getPropertyValue();

	void setPropertyValue(PropertyValueList propertyValue);
}
