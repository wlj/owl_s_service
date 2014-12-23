package EDU.pku.sj.rscasd.owls1_1.profile;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Store;

public interface ContextObjectList extends OWLS_Store {
	public ContextObject getNthContextObject(int index) throws IndexOutOfBoundsException;

	public boolean removeContextObject(String uri);

	public boolean removeContextObject(ContextObject contextObject);

	public ContextObject getContextObject(String uri);

	public void addContextObject(ContextObject contextObject);
}
