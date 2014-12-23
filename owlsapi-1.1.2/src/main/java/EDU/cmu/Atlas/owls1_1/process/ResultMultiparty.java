package EDU.cmu.Atlas.owls1_1.process;

/**
 * Result with support for explicit message type specification.
 * @author Roman Vaculin
 *
 */
public interface ResultMultiparty extends Result {
	public MessageType getOutMessageType();
	public void setOutMessageType(MessageType messageType);
}
