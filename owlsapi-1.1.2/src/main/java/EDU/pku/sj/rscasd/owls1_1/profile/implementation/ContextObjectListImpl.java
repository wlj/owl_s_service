package EDU.pku.sj.rscasd.owls1_1.profile.implementation;

import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_StoreImpl;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextObject;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextObjectList;

public class ContextObjectListImpl extends OWLS_StoreImpl implements ContextObjectList {

	public void addContextObject(ContextObject contextObject) {
		add(contextObject);
	}

	public ContextObject getNthContextObject(int index) throws IndexOutOfBoundsException {
		return (ContextObject) getNth(index);
	}

	public ContextObject getContextObject(String uri) {
		return (ContextObject) get(uri);
	}

	public boolean removeContextObject(String uri) {
		return remove(uri);
	}

	public boolean removeContextObject(ContextObject contextObject) {
		return remove(contextObject);
	}
}
