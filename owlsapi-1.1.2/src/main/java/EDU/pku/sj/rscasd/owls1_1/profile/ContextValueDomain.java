package EDU.pku.sj.rscasd.owls1_1.profile;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Object;

public interface ContextValueDomain extends OWLS_Object {

	/**
	 * URI of target value domain individual
	 * 
	 * @return
	 */
	String getDomainValue();

	void setDomainValue(String domainValueUri);
	
}
