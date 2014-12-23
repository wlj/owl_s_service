/**
 * 
 */
package EDU.cmu.Atlas.owls1_1.process.implementation;

import com.hp.hpl.jena.ontology.Individual;
import EDU.cmu.Atlas.owls1_1.process.AtomicProcessMultiparty;
import EDU.cmu.Atlas.owls1_1.process.MessageType;
import EDU.cmu.Atlas.owls1_1.process.ProcessList;

/**
 * @author Roman Vaculin
 *
 */
public class AtomicProcessMultipartyImpl extends AtomicProcessImpl implements
		AtomicProcessMultiparty {
	
	protected MessageType messageType;
	protected ProcessList remoteProcesses;
	
	public AtomicProcessMultipartyImpl(Individual instance) {
		super(instance);
	}

	public AtomicProcessMultipartyImpl() {
		super();
	}

	public AtomicProcessMultipartyImpl(String uri) {
		super(uri);
	}

	/* (non-Javadoc)
	 * @see EDU.cmu.Atlas.owls1_1.process.AtomicProcessMultiparty#getInMessageType()
	 */
	public MessageType getInMessageType() {
		return messageType;
	}

	/* (non-Javadoc)
	 * @see EDU.cmu.Atlas.owls1_1.process.AtomicProcessMultiparty#getInvolves()
	 */
	public ProcessList getInvolves() {
		return remoteProcesses;
	}

	/* (non-Javadoc)
	 * @see EDU.cmu.Atlas.owls1_1.process.AtomicProcessMultiparty#setInMessageType(java.lang.String)
	 */
	public void setInMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	/* (non-Javadoc)
	 * @see EDU.cmu.Atlas.owls1_1.process.AtomicProcessMultiparty#setInvolves(EDU.cmu.Atlas.owls1_1.process.ProcessList)
	 */
	public void setInvolves(ProcessList remoteProcesses) {
		this.remoteProcesses = remoteProcesses;
	}
}
