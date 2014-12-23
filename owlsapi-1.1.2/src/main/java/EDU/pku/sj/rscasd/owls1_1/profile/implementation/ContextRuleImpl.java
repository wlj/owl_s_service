package EDU.pku.sj.rscasd.owls1_1.profile.implementation;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_ObjectImpl;
import EDU.cmu.Atlas.owls1_1.exception.OWLS_Store_Exception;
import EDU.cmu.Atlas.owls1_1.utils.OWLS_StoreUtil;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextRuleException;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextList;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextRule;
import EDU.pku.sj.rscasd.owls1_1.uri.OWLSServiceContextURI;
import EDU.pku.sj.rscasd.owls1_1.utils.OWLSServiceContextProperties;

import com.hp.hpl.jena.ontology.Individual;

public class ContextRuleImpl extends OWLS_ObjectImpl implements ContextRule {

	private ContextList context;

	static Logger logger = Logger.getLogger(ContextRuleImpl.class);

	public ContextRuleImpl(Individual instance) throws NotInstanceOfContextRuleException {
		super(instance);

		// check whether the instance passed is the instance requested
		if (instance != null && !instance.hasRDFType(OWLSServiceContextURI.ContextRule)) {
			throw new NotInstanceOfContextRuleException("Instance " + instance.getURI() + " not an instance of ContextRule");
		}

		// extract service context
		try {
			setContext((ContextList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(instance,
					OWLSServiceContextProperties.hasRuleContent, "createContext", ContextListImpl.class
							.getName()));
		} catch (OWLS_Store_Exception e2) {
			logger.error(e2.getMessage(), new NotInstanceOfContextRuleException("Context List in " + instance.getURI(), e2));
		}

	}

	public ContextList getContext() {
		return context;
	}

	public void setContext(ContextList context) {
		this.context = context;
	}

	public String toString(String indent) {
		return toString();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("Context Rule: ");
		sb.append("\n\nContext in Rule : ");
		sb.append("\n\n" + getContext());
		return sb.toString();
	}
}
