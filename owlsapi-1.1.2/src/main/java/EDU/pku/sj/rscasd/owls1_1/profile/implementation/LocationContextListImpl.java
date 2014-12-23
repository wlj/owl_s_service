package EDU.pku.sj.rscasd.owls1_1.profile.implementation;

import EDU.pku.sj.rscasd.owls1_1.profile.LocationContext;
import EDU.pku.sj.rscasd.owls1_1.profile.LocationContextList;

public class LocationContextListImpl extends ContextListImpl implements LocationContextList {

	public void addLocationContext(LocationContext locationContext) {
		add(locationContext);
	}

	public LocationContext getLocationContext(String uri) {
		return (LocationContext) get(uri);
	}

	public LocationContext getNthLocationContext(int index) throws IndexOutOfBoundsException {
		return (LocationContext) getNth(index);
	}

	public boolean removeLocationContext(String uri) {
		return remove(uri);
	}

	public boolean removeLocationContext(LocationContext locationContext) {
		return remove(locationContext);
	}

}
