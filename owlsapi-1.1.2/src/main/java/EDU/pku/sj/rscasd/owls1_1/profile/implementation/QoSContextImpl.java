package EDU.pku.sj.rscasd.owls1_1.profile.implementation;

import org.apache.log4j.Logger;

import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextException;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfQoSContextException;
import EDU.pku.sj.rscasd.owls1_1.profile.QoSContext;
import EDU.pku.sj.rscasd.owls1_1.uri.OWLSServiceContextURI;

import com.hp.hpl.jena.ontology.Individual;

public class QoSContextImpl extends ContextImpl implements QoSContext {

	static Logger logger = Logger.getLogger(QoSContextImpl.class);

	public QoSContextImpl(Individual instance) throws NotInstanceOfContextException {
		super(instance);

		// check whether the instance passed is the instance requested
		if (instance != null && !instance.hasRDFType(OWLSServiceContextURI.QoSContext)) {
			throw new NotInstanceOfQoSContextException("Instance " + instance.getURI()
					+ " not an instance of QoSContext");
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString());
		return sb.toString();
	}

}
