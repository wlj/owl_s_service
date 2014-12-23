package EDU.pku.sj.rscasd.owls1_1.profile.implementation;

import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_StoreImpl;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextRule;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextRuleList;

public class ContextRuleListImpl extends OWLS_StoreImpl implements ContextRuleList {

	public void addContextRule(ContextRule contextRule) {
		add(contextRule);
	}

	public ContextRule getNthContextRule(int index) throws IndexOutOfBoundsException {
		return (ContextRule) getNth(index);
	}

	public ContextRule getContextRule(String uri) {
		return (ContextRule) get(uri);
	}

	public boolean removeContextRule(String uri) {
		return remove(uri);
	}

	public boolean removeContextRule(ContextRule contextRule) {
		return remove(contextRule);
	}

}
