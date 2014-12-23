package EDU.cmu.Atlas.owls1_1.service.implementation;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;

import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_ObjectImpl;
import EDU.cmu.Atlas.owls1_1.service.ServiceProvider;

public class ServiceProviderImpl extends OWLS_ObjectImpl implements
		ServiceProvider {
	
	protected OntClass providerClass;
	
	// TODO: maybe disable this constructor
    public ServiceProviderImpl(Individual individual) {
        super(individual);
    }

    //  TODO: maybe disable this constructor
    public ServiceProviderImpl(String uri) {
        super(uri);
    }

    //  TODO: maybe disable this constructor
    public ServiceProviderImpl() {
        super();
    }
    // FIXME: change the constructor not to accept OntClass but rather URL of this class 
    // (to make API independent on Jena)
    public ServiceProviderImpl(String uri, OntClass providerClass) {
    	super(uri);
    	this.providerClass = providerClass;
    }

	public OntClass getProviderClass() {
		return providerClass;
	}

	public void setProviderClass(OntClass providerClass) {
		this.providerClass = providerClass;
	}

    public String toString() {
        return toString("");
    }

    public String toString(String indent) {
        StringBuffer sbuff = new StringBuffer();
        sbuff.append("Service Provider class : ");
        sbuff.append(getURI());
        sbuff.append("providerClass : ");
        if (providerClass!=null) {
        	sbuff.append(providerClass.getURI());
        } else {
        	sbuff.append("not set yet (null)");
        }
        return sbuff.toString();
    }
}
