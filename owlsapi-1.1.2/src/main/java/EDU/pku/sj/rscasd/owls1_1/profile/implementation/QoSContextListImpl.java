package EDU.pku.sj.rscasd.owls1_1.profile.implementation;

import EDU.pku.sj.rscasd.owls1_1.profile.QoSContext;
import EDU.pku.sj.rscasd.owls1_1.profile.QoSContextList;

public class QoSContextListImpl extends ContextListImpl implements QoSContextList {

	public void addQoSContext(QoSContext qoSContext) {
		add(qoSContext);
	}

	public QoSContext getNthQoSContext(int index) throws IndexOutOfBoundsException {
		return (QoSContext) getNth(index);
	}

	public QoSContext getQoSContext(String uri) {
		return (QoSContext) get(uri);
	}

	public boolean removeQoSContext(String uri) {
		return remove(uri);
	}

	public boolean removeQoSContext(QoSContext qoSContext) {
		return remove(qoSContext);
	}

}
