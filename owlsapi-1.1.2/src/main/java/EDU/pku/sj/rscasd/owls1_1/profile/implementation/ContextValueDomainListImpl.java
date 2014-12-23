package EDU.pku.sj.rscasd.owls1_1.profile.implementation;

import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_StoreImpl;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextValueDomain;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextValueDomainList;

public class ContextValueDomainListImpl extends OWLS_StoreImpl implements ContextValueDomainList {

	public void addContextValueDomain(ContextValueDomain contextValueDomain) {
		add(contextValueDomain);
	}

	public ContextValueDomain getNthContextValueDomain(int index) throws IndexOutOfBoundsException {
		return (ContextValueDomain) getNth(index);
	}

	public ContextValueDomain getContextValueDomain(String uri) {
		return (ContextValueDomain) get(uri);
	}

	public boolean removeContextValueDomain(String uri) {
		return remove(uri);
	}

	public boolean removeContextValueDomain(ContextValueDomain contextValueDomain) {
		return remove(contextValueDomain);
	}

}
