package EDU.pku.sj.rscasd.owls1_1.profile.implementation;

import java.util.Vector;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_ObjectImpl;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextException;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextObjectException;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextTypeException;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextValueException;
import EDU.pku.sj.rscasd.owls1_1.profile.Context;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextObject;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextType;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextValue;
import EDU.pku.sj.rscasd.owls1_1.uri.OWLSServiceContextURI;
import EDU.pku.sj.rscasd.owls1_1.utils.OWLIndividualUtils;
import EDU.pku.sj.rscasd.owls1_1.utils.OWLSServiceContextProperties;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;

public class ContextImpl extends OWLS_ObjectImpl implements Context {

	static Logger logger = Logger.getLogger(ContextImpl.class);

	private String contextName;

	public ContextType contextType;

	public ContextValue contextValue;

	public ContextObject contextObject;

	public ContextImpl(Individual instance) throws NotInstanceOfContextException {
		super(instance);

		// check whether the instance passed is the instance requested
		if (instance != null && !instance.hasRDFType(OWLSServiceContextURI.Context)) {
			throw new NotInstanceOfContextException("Instance " + instance.getURI() + " not an instance of Context");
		}

		setURI(instance.getURI());

		// extract service context name
		Vector<Literal> tempVector = OWLIndividualUtils.extractPropertyValues(instance,
				OWLSServiceContextProperties.serviceContextName);
		setContextName(tempVector.size() > 0 ? tempVector.get(0).getString() : (instance.getURI() != null ? instance
				.getURI() : "Not-named context"));

		OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();
		// extract context value
		logger.debug("Extracting hasContextValue");
		Individual hasContextValue = null;
		try {
			hasContextValue = OWLIndividualUtils.getInstanceFromProperty(instance,
					OWLSServiceContextProperties.hasContextValue);
		} catch (Exception e) {
			logger.error(e.getMessage(), new NotInstanceOfContextException("hasContextValue", e));
		}
		if (hasContextValue != null) {
			try {
				setContextValue(builder.createContextValue(hasContextValue));
			} catch (NotInstanceOfContextValueException e) {
				logger.error(e.getMessage(), new NotInstanceOfContextException(
						"Property 'hasContextValue' not an instance of ContextValue ", e));
			}
			logger.debug("hasContextValue " + hasContextValue.getURI());
		} else {
			logger.debug("Context " + getURI() + " has no hasContextValue property");
		}

		// extract context object
		logger.debug("Extracting hasContextObject");
		Individual hasContextObject = null;
		try {
			hasContextObject = OWLIndividualUtils.getInstanceFromProperty(instance,
					OWLSServiceContextProperties.hasContextObject);
		} catch (Exception e) {
			logger.error(e.getMessage(), new NotInstanceOfContextException("hasContextObject", e));
		}
		if (hasContextObject != null) {
			try {
				setContextObject(builder.createContextObject(hasContextObject));
			} catch (NotInstanceOfContextObjectException e) {
				logger.error(e.getMessage(), new NotInstanceOfContextException(
						"Property 'hasContextObject' not an instance of ContextObject ", e));
			}
			logger.debug("hasContextObject " + hasContextObject.getURI());
		} else {
			logger.debug("Context " + getURI() + " has no hasContextObject property");
		}

		// extract context type
		logger.debug("Extracting hasContextType");
		Individual hasContextType = null;
		try {
			hasContextType = OWLIndividualUtils.getInstanceFromProperty(instance,
					OWLSServiceContextProperties.hasContextType);
		} catch (Exception e) {
			logger.error(e.getMessage(), new NotInstanceOfContextException("hasContextType", e));
		}
		if (hasContextType != null) {
			try {
				setContextType(builder.createContextType(hasContextType));
			} catch (NotInstanceOfContextTypeException e) {
				logger.error(e.getMessage(), new NotInstanceOfContextException(
						"Property 'hasContextType' not an instance of ContextType ", e));
			}
			logger.debug("hasContextType " + hasContextObject.getURI());
		} else {
			logger.debug("Context " + getURI() + " has no hasContextType property");
		}
	}

	public String getContextName() {
		return contextName;
	}

	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	public ContextType getContextType() {
		return contextType;
	}

	public void setContextType(ContextType contextType) {
		this.contextType = contextType;
	}

	public ContextValue getContextValue() {
		return contextValue;
	}

	public void setContextValue(ContextValue contextValue) {
		this.contextValue = contextValue;
	}

	public ContextObject getContextObject() {
		return contextObject;
	}

	public void setContextObject(ContextObject contextObject) {
		this.contextObject = contextObject;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("Service Context: \n");
		sb.append("\n\nContext Name : ");
		sb.append("\n\n" + getContextName());
		return sb.toString();
	}

	public String toString(String indent) {
		return toString("");
	}

}
