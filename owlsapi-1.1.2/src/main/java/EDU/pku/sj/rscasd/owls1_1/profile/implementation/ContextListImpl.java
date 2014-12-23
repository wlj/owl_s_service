package EDU.pku.sj.rscasd.owls1_1.profile.implementation;

import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_StoreImpl;
import EDU.pku.sj.rscasd.owls1_1.profile.Context;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextList;

public class ContextListImpl extends OWLS_StoreImpl implements ContextList {

	public void addContext(Context context) {
		add(context);
	}

	public Context getContext(String uri) {
		return (Context) get(uri);
	}

	public Context getNthContext(int index) throws IndexOutOfBoundsException {
		return (Context) getNth(index);
	}

	public boolean removeContext(String uri) {
		return remove(uri);
	}

	public boolean removeContext(Context context) {
		return remove(context);
	}
}
