package EDU.pku.sj.rscasd.owls1_1.profile;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Store;

public interface ContextValueDomainList extends OWLS_Store {
	public ContextValueDomain getNthContextValueDomain(int index) throws IndexOutOfBoundsException;

	public boolean removeContextValueDomain(String uri);

	public boolean removeContextValueDomain(ContextValueDomain contextValueDomain);

	public ContextValueDomain getContextValueDomain(String uri);

	public void addContextValueDomain(ContextValueDomain contextValueDomain);
}
