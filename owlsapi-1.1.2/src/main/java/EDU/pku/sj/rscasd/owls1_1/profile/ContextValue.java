package EDU.pku.sj.rscasd.owls1_1.profile;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Object;

public interface ContextValue extends OWLS_Object {

	ContextValueDomain getContextValueDomain();

	void setContextValueDomain(ContextValueDomain contextValueDomain);

}
