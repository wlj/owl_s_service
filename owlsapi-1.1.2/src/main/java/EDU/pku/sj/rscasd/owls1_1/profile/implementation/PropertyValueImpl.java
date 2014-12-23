package EDU.pku.sj.rscasd.owls1_1.profile.implementation;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_ObjectImpl;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextValueException;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfPropertyValueException;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextValue;
import EDU.pku.sj.rscasd.owls1_1.profile.PropertyValue;
import EDU.pku.sj.rscasd.owls1_1.uri.OWLSServiceContextURI;
import EDU.pku.sj.rscasd.owls1_1.utils.OWLIndividualUtils;
import EDU.pku.sj.rscasd.owls1_1.utils.OWLSServiceContextProperties;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class PropertyValueImpl extends OWLS_ObjectImpl implements PropertyValue {

	private ContextValue propertyContextValue;

	private String propertyType;

	static Logger logger = Logger.getLogger(PropertyValueImpl.class);

	public PropertyValueImpl(Individual instance) throws NotInstanceOfPropertyValueException {
		super(instance);

		// check whether the instance passed is the instance requested
		if (instance != null && !instance.hasRDFType(OWLSServiceContextURI.PropertyValue)) {
			throw new NotInstanceOfPropertyValueException("Instance " + instance.getURI()
					+ " not an instance of PropertyValueContext");
		}

		// extract context value
		OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();
		logger.debug("Extracting hasPropertyContextValue");
		Individual hasPropCtxValue = null;
		try {
			hasPropCtxValue = OWLIndividualUtils.getInstanceFromProperty(instance,
					OWLSServiceContextProperties.hasPropertyContextValue);
		} catch (Exception e) {
			// TODO check whether can be null
			logger.error(e.getMessage(), new NotInstanceOfPropertyValueException("hasPropertyContextValue", e));
		}
		if (hasPropCtxValue != null) {
			try {
				setPropertyContextValue(builder.createContextValue(hasPropCtxValue));
			} catch (NotInstanceOfContextValueException e) {
				logger.error(e.getMessage(), new NotInstanceOfPropertyValueException(
						"Property 'hasPropertyContextValue' not an instance of ContextValue ", e));
			}
			logger.debug("hasPropertyContextValue " + hasPropCtxValue.getURI());
		} else {
			logger.debug("PropertyValue " + getURI() + " has no hasPropertyContextValue property");
		}

		// extract property type
		RDFNode rdfNode = OWLIndividualUtils.extractPropertyValue(instance, OWLSServiceContextProperties.hasPropertyType);
		String propTypeUri = null;
		if (rdfNode != null) {
			propTypeUri = rdfNode.asNode().getURI();
		}

		if (propTypeUri == null || propTypeUri.trim().equals("")) {
			logger
					.warn("PropertyValue : " + instance.getURI()
							+ "value of property 'hasPropertyType' is not a literal");
		} else {
			setPropertyType(propTypeUri);
		}
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyTypeUri) {
		this.propertyType = propertyTypeUri;
	}

	public ContextValue getPropertyContextValue() {
		return propertyContextValue;
	}

	public void setPropertyContextValue(ContextValue propertyContextValue) {
		this.propertyContextValue = propertyContextValue;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("Property Value Context: ");
		sb.append("\n\nProperty Type : ");
		sb.append("\n\n" + getPropertyType());
		sb.append("\n\nProperty Context Value : ");
		sb.append("\n\n" + getPropertyContextValue());
		return sb.toString();
	}

	public String toString(String indent) {
		return toString();
	}
}
