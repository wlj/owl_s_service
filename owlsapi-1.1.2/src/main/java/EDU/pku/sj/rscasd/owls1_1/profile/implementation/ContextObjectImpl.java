package EDU.pku.sj.rscasd.owls1_1.profile.implementation;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_ObjectImpl;
import EDU.cmu.Atlas.owls1_1.exception.OWLS_Store_Exception;
import EDU.cmu.Atlas.owls1_1.utils.OWLS_StoreUtil;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextObjectException;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextValueDomainException;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextObject;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextValueDomain;
import EDU.pku.sj.rscasd.owls1_1.profile.PropertyValueList;
import EDU.pku.sj.rscasd.owls1_1.uri.OWLSServiceContextURI;
import EDU.pku.sj.rscasd.owls1_1.utils.OWLIndividualUtils;
import EDU.pku.sj.rscasd.owls1_1.utils.OWLSServiceContextProperties;

import com.hp.hpl.jena.ontology.Individual;

public class ContextObjectImpl extends OWLS_ObjectImpl implements ContextObject {

	private PropertyValueList propertyValue;

	private ContextValueDomain contextValueDomain;

	static Logger logger = Logger.getLogger(ContextObjectImpl.class);

	public ContextObjectImpl(Individual instance) throws NotInstanceOfContextObjectException {
		super(instance);

		// check whether the instance passed is the instance requested
		if (instance != null && !instance.hasRDFType(OWLSServiceContextURI.ContextObject)) {
			throw new NotInstanceOfContextObjectException("Instance " + instance.getURI()
					+ " not an instance of ContextObject");
		}

		OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();

		// extract object value
		try {
			setPropertyValue((PropertyValueList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(instance,
					OWLSServiceContextProperties.hasPropertyValue, "createPropertyValue", PropertyValueListImpl.class
							.getName()));
		} catch (OWLS_Store_Exception e2) {
			logger.error(e2.getMessage(), new NotInstanceOfContextObjectException("PropertyValue List in "
					+ instance.getURI(), e2));
		}

		logger.debug("Extracting hasDomain");
		Individual hasDomain = null;
		try {
			hasDomain = OWLIndividualUtils.getInstanceFromProperty(instance, OWLSServiceContextProperties.hasDomain);
		} catch (Exception e) {
			logger.error(e.getMessage(), new NotInstanceOfContextObjectException("hasDomain", e));
		}
		if (hasDomain != null) {
			try {
				setContextValueDomain(builder.createContextValueDomain(hasDomain));
			} catch (NotInstanceOfContextValueDomainException e) {
				logger.error(e.getMessage(), new NotInstanceOfContextObjectException(
						"Property 'hasDomain' not an instance of ContextValueDomain ", e));
			}
			logger.debug("hasServiceContextType " + hasDomain.getURI());
		} else {
			logger.debug("ServiceContextObject " + getURI() + " has no hasServiceContextType property");
		}
	}

	public PropertyValueList getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(PropertyValueList propertyValue) {
		this.propertyValue = propertyValue;
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
		StringBuffer sb = new StringBuffer("Service Context Object: ");
		sb.append("\n\nProperty Value Context : ");
		sb.append("\n\n" + getPropertyValue());
		sb.append("\n\nContext Value Domain: ");
		sb.append("\n\n" + getContextValueDomain());
		return sb.toString();
	}

}
