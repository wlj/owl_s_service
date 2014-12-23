package EDU.pku.sj.rscasd.owls1_1.profile;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Store;

public interface ContextValueList extends OWLS_Store {
	public ContextValue getNthContextValue(int index) throws IndexOutOfBoundsException;

	public boolean removeContextValue(String uri);

	public boolean removeContextValue(ContextValue contextValue);

	public ContextValue getContextValue(String uri);

	public void addContextValue(ContextValue contextValue);
}
