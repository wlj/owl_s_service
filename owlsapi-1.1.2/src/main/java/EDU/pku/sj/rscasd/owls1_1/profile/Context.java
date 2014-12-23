package EDU.pku.sj.rscasd.owls1_1.profile;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Object;

public interface Context extends OWLS_Object {

	String getContextName();

	void setContextName(String contextName);

	public ContextType getContextType();

	public void setContextType(ContextType contextType);

	public ContextValue getContextValue();

	public void setContextValue(ContextValue contextValue);

	public ContextObject getContextObject();

	public void setContextObject(ContextObject contextObject);

}
