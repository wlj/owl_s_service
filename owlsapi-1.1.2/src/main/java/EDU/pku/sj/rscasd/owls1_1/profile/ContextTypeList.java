package EDU.pku.sj.rscasd.owls1_1.profile;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Store;

public interface ContextTypeList extends OWLS_Store {
	public ContextType getNthContextType(int index) throws IndexOutOfBoundsException;

	public boolean removeContextType(String uri);

	public boolean removeContextType(ContextType contextType);

	public ContextType getContextType(String uri);

	public void addContextType(ContextType contextType);
}
