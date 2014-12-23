package EDU.cmu.Atlas.owls1_1.process;

/**
 * Atomic process with support for multi-party interactions.
 * Adds support for remote process (via involves property) & incoming message types. 
 * @author Roman Vaculin
 *
 */
public interface AtomicProcessMultiparty extends AtomicProcess {
	public MessageType getInMessageType();
	public void setInMessageType(MessageType messageType);
	public ProcessList getInvolves();
	public void setInvolves(ProcessList remoteProcesses);
}
