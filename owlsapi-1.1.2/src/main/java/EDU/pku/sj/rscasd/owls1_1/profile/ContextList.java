package EDU.pku.sj.rscasd.owls1_1.profile;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Store;

public interface ContextList extends OWLS_Store {
	public Context getNthContext(int index) throws IndexOutOfBoundsException;

	public boolean removeContext(String uri);

	public boolean removeContext(Context context);

	public Context getContext(String uri);

	public void addContext(Context context);
}
