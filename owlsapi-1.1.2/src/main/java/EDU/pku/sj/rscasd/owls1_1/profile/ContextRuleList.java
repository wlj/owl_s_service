package EDU.pku.sj.rscasd.owls1_1.profile;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Store;

public interface ContextRuleList extends OWLS_Store {
	public ContextRule getNthContextRule(int index) throws IndexOutOfBoundsException;

	public boolean removeContextRule(String uri);

	public boolean removeContextRule(ContextRule contextRule);

	public ContextRule getContextRule(String uri);

	public void addContextRule(ContextRule contextRule);
}
