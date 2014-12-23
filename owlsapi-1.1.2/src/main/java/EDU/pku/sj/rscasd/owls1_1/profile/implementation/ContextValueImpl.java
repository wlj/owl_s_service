package EDU.pku.sj.rscasd.owls1_1.profile.implementation;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_ObjectImpl;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextValueDomainException;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextValueException;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextValue;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextValueDomain;
import EDU.pku.sj.rscasd.owls1_1.uri.OWLSServiceContextURI;
import EDU.pku.sj.rscasd.owls1_1.utils.OWLIndividualUtils;
import EDU.pku.sj.rscasd.owls1_1.utils.OWLSServiceContextProperties;

import com.hp.hpl.jena.ontology.Individual;

public class ContextValueImpl extends OWLS_ObjectImpl implements ContextValue {

	private ContextValueDomain contextValueDomain;

	static Logger logger = Logger.getLogger(ContextValueImpl.class);

	public ContextValueImpl(Individual instance) throws NotInstanceOfContextValueException {
		super(instance);

		// check whether the instance passed is the instance requested
		if (instance != null && !instance.hasRDFType(OWLSServiceContextURI.ContextValue)) {
			throw new NotInstanceOfContextValueException("Instance " + instance.getURI()
					+ " not an instance of ContextValue");
		}

		OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();

		// extract object value
		logger.debug("Extracting hasDomain");
		Individual hasDomain = null;
		try {
			hasDomain = OWLIndividualUtils.getInstanceFromProperty(instance, OWLSServiceContextProperties.hasDomain);
		} catch (Exception e) {
			// TODO check whether can be null
			logger.error(e.getMessage(), new NotInstanceOfContextValueException("hasDomain", e));
		}
		if (hasDomain != null) {
			try {
				setContextValueDomain(builder.createContextValueDomain(hasDomain));
			} catch (NotInstanceOfContextValueDomainException e) {
				logger.error(new NotInstanceOfContextValueException(
						"Property 'hasDomain' not an instance of ContextValueDomain ", e));
			}
			logger.debug("hasDomain " + hasDomain.getURI());
		} else {
			logger.debug("ContextValue " + getURI() + " has no hasDomain property");
		}
	}

	public ContextValueDomain getContextValueDomain() {
		return contextValueDomain;
	}

	public void setContextValueDomain(ContextValueDomain contextValueDomain) {
		this.contextValueDomain = contextValueDomain;
	}

	public String toString(String indent) {
		return toString();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("Context Value: ");
		sb.append("\n\nObject Value Domain: ");
		sb.append("\n\n" + getContextValueDomain());
		return sb.toString();
	}

}
