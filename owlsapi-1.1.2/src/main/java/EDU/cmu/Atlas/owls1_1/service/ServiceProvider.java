/**
 * 
 */
package EDU.cmu.Atlas.owls1_1.service;

import com.hp.hpl.jena.ontology.OntClass;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Object;

/**
 * Service can be provided by the provider which is specified by using the
 * providedBy property of the Service class. This property's range is not restricted
 * and thus the provider can be whatever OWL'S instance. Therefore the OWL class of 
 * the actual provider has to be specified besids the instance URI.
 * @author Roman Vaculin
 *
 */
public interface ServiceProvider extends OWLS_Object {
    /**
     * Sets the service provider OWL class. 
     * @param providerClass	The class of the service provider. 
     */
	public void setProviderClass(OntClass providerClass);

	/**
	 * Extracts the actual OWL class of the service provider.
	 * @return the OWL class of the servcice provider instance
	 */
	public OntClass getProviderClass();
}
