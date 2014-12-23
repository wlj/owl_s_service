package EDU.pku.sj.rscasd.owls1_1.profile.implementation;

import org.apache.log4j.Logger;

import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextException;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfLocationContextException;
import EDU.pku.sj.rscasd.owls1_1.profile.LocationContext;
import EDU.pku.sj.rscasd.owls1_1.uri.OWLSServiceContextURI;

import com.hp.hpl.jena.ontology.Individual;

public class LocationContextImpl extends ContextImpl implements LocationContext {

	static Logger logger = Logger.getLogger(LocationContextImpl.class);

	public LocationContextImpl(Individual instance) throws NotInstanceOfContextException {
		super(instance);

		// check whether the instance passed is the instance requested
		if (instance != null && !instance.hasRDFType(OWLSServiceContextURI.LocationContext)) {
			throw new NotInstanceOfLocationContextException("Instance " + instance.getURI()
					+ " not an instance of LocationContext");
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString());
		return sb.toString();
	}
}
