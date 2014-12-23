/**
 * 
 */
package EDU.cmu.Atlas.owls1_1.builder;

import com.hp.hpl.jena.ontology.OntClass;

import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfMessageTypeException;
import EDU.cmu.Atlas.owls1_1.process.MessageType;

/**
 * @author Roman Vaculin
 *
 */
public interface OWLS_Object_BuilderMultiparty extends OWLS_Object_Builder {

	// multiparty
    public MessageType createMessageType(String URI, OntClass messageTypeClass) throws NotInstanceOfMessageTypeException;

}
