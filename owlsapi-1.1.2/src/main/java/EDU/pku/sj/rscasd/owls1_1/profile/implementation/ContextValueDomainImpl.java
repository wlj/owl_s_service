package EDU.pku.sj.rscasd.owls1_1.profile.implementation;

import java.util.Vector;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_ObjectImpl;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextValueDomainException;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextValueDomain;
import EDU.pku.sj.rscasd.owls1_1.uri.OWLSServiceContextURI;
import EDU.pku.sj.rscasd.owls1_1.utils.OWLIndividualUtils;
import EDU.pku.sj.rscasd.owls1_1.utils.OWLSServiceContextProperties;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;

public class ContextValueDomainImpl extends OWLS_ObjectImpl implements ContextValueDomain {

	private String domainValue;

	static Logger logger = Logger.getLogger(ContextValueDomainImpl.class);

	public ContextValueDomainImpl(Individual instance) throws NotInstanceOfContextValueDomainException {
		super(instance);

		// check whether the instance passed is the instance requested
		if (instance != null && !instance.hasRDFType(OWLSServiceContextURI.ContextValueDomain)) {
			throw new NotInstanceOfContextValueDomainException("Instance " + instance.getURI()
					+ " not an instance of ContextValueDomain");
		}

		Literal domainValueUri;
		Vector<Literal> tempVector = OWLIndividualUtils.extractPropertyValues(instance,
				OWLSServiceContextProperties.hasDomainValue);
		if (tempVector != null && tempVector.size() > 0) {
			for (int i = 0; i < tempVector.size(); i++) {
				domainValueUri = tempVector.get(i);
				setDomainValue(domainValueUri.getString().trim());
			}
			logger.debug("Context Value Domain: " + getDomainValue());
		} else {
			logger.warn("ContextValueDomain " + instance.getURI() + " has no hasDomainValue property");
		}
	}

	public String toString(String indent) {
		return toString();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("Service Context Value Domain: ");
		sb.append("\n\nObject Domain : ");
		sb.append("\n\n" + getDomainValue());
		return sb.toString();
	}

	public String getDomainValue() {
		return domainValue;
	}

	public void setDomainValue(String domainValueUri) {
		this.domainValue = domainValueUri;
	}

}
