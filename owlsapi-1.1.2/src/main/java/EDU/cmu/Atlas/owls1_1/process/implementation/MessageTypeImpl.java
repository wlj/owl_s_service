package EDU.cmu.Atlas.owls1_1.process.implementation;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;

import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_ObjectImpl;
import EDU.cmu.Atlas.owls1_1.process.MessageType;

public class MessageTypeImpl extends OWLS_ObjectImpl implements
		MessageType {
	
	protected OntClass messageTypeClass;
	
	// TODO: maybe disable this constructor
    public MessageTypeImpl(Individual individual) {
        super(individual);
    }

    //  TODO: maybe disable this constructor
    public MessageTypeImpl(String uri) {
        super(uri);
    }

    //  TODO: maybe disable this constructor
    public MessageTypeImpl() {
        super();
    }
    // FIXME: change the constructor not to accept OntClass but rather URL of this class 
    // (to make API independent on Jena)
    public MessageTypeImpl(String uri, OntClass messageTypeClass) {
    	super(uri);
    	this.messageTypeClass = messageTypeClass;
    }

	public OntClass getMessageTypeClass() {
		return messageTypeClass;
	}

	public void setMessageTypeClass(OntClass messageTypeClass) {
		this.messageTypeClass = messageTypeClass;
	}

    public String toString() {
        return toString("");
    }

    public String toString(String indent) {
        StringBuffer sbuff = new StringBuffer();
        sbuff.append("MessageType class : ");
        sbuff.append(getURI());
        sbuff.append("messageTypeClass : ");
        if (messageTypeClass!=null) {
        	sbuff.append(messageTypeClass.getURI());
        } else {
        	sbuff.append("not set yet (null)");
        }
        return sbuff.toString();
    }
}
