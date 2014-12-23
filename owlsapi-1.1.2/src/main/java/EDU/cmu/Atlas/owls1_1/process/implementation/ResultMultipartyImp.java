package EDU.cmu.Atlas.owls1_1.process.implementation;

import com.hp.hpl.jena.ontology.Individual;

import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfResultException;
import EDU.cmu.Atlas.owls1_1.process.MessageType;
import EDU.cmu.Atlas.owls1_1.process.ResultMultiparty;

public class ResultMultipartyImp extends ResultImpl implements ResultMultiparty {

	protected MessageType outMessageType;

	public ResultMultipartyImp(Individual individual)
			throws NotInstanceOfResultException {
		super(individual);
	}

	public ResultMultipartyImp(String uri) {
		super(uri);
	}

	public ResultMultipartyImp() {
		super();
	}

	/**
	 * @see EDU.cmu.Atlas.owls1_1.process.ResultMultiparty#getOutMessageType()
	 * @return Returns the outMessageType.
	 */
	public MessageType getOutMessageType() {
		return outMessageType;
	}

	/**
	 * @see EDU.cmu.Atlas.owls1_1.process.ResultMultiparty#setOutMessageType()
	 * @param outMessageType The outMessageType to set.
	 */
	public void setOutMessageType(MessageType outMessageType) {
		this.outMessageType = outMessageType;
	}

	
	
}
