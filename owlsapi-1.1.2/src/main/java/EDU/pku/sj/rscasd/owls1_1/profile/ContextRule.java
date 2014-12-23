package EDU.pku.sj.rscasd.owls1_1.profile;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Object;

public interface ContextRule extends OWLS_Object {

	ContextList getContext();

	void setContext(ContextList context);

}
