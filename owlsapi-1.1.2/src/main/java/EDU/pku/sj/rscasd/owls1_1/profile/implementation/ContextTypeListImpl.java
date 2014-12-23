package EDU.pku.sj.rscasd.owls1_1.profile.implementation;

import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_StoreImpl;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextType;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextTypeList;

public class ContextTypeListImpl extends OWLS_StoreImpl implements ContextTypeList {

	public void addContextType(ContextType contextType) {
		add(contextType);
	}

	public ContextType getNthContextType(int index) throws IndexOutOfBoundsException {
		return (ContextType) getNth(index);
	}

	public ContextType getContextType(String uri) {
		return (ContextType) get(uri);
	}

	public boolean removeContextType(String uri) {
		return remove(uri);
	}

	public boolean removeContextType(ContextType contextType) {
		return remove(contextType);
	}

}
