package EDU.pku.sj.rscasd.owls1_1.profile;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Object;

public interface ContextType extends OWLS_Object {

	/**
	 * set of URI string for target context value domain
	 * 
	 * @return ContextValueDomain
	 */
	ContextValueDomain getContextValueDomain();

	void setContextValueDomain(ContextValueDomain contextValueDomain);
	
}
