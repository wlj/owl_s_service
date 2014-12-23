package EDU.pku.sj.rscasd.owls1_1.profile;

public interface QoSContextList extends ContextList {
	public QoSContext getNthQoSContext(int index) throws IndexOutOfBoundsException;

	public boolean removeQoSContext(String uri);

	public boolean removeQoSContext(QoSContext qoSContext);

	public QoSContext getQoSContext(String uri);

	public void addQoSContext(QoSContext qoSContext);
}
