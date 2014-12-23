/**
 * 
 */
package EDU.cmu.Atlas.owls1_1.process;


import com.hp.hpl.jena.ontology.OntClass;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Object;

/**
 * @author Roman Vaculin
 *
 */
public interface MessageType extends OWLS_Object {
    /**
     * Sets the service provider OWL class. 
     * @param providerClass	The class of the service provider. 
     */
	public void setMessageTypeClass(OntClass providerClass);

	/**
	 * Extracts the actual OWL class of the service provider.
	 * @return the OWL class of the servcice provider instance
	 */
	public OntClass getMessageTypeClass();
}
