package EDU.pku.sj.rscasd.owls1_1.profile.implementation;

import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_StoreImpl;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextValue;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextValueList;

public class ContextValueListImpl extends OWLS_StoreImpl implements ContextValueList {

	public void addContextValue(ContextValue contextValue) {
		add(contextValue);
	}

	public ContextValue getNthContextValue(int index) throws IndexOutOfBoundsException {
		return (ContextValue) getNth(index);
	}

	public ContextValue getContextValue(String uri) {
		return (ContextValue) get(uri);
	}

	public boolean removeContextValue(String uri) {
		return remove(uri);
	}

	public boolean removeContextValue(ContextValue contextValue) {
		return remove(contextValue);
	}

}
