package EDU.pku.sj.rscasd.owls1_1.profile;

public interface LocationContextList extends ContextList {
	public LocationContext getNthLocationContext(int index) throws IndexOutOfBoundsException;

	public boolean removeLocationContext(String uri);

	public boolean removeLocationContext(LocationContext locationContext);

	public LocationContext getLocationContext(String uri);

	public void addLocationContext(LocationContext locationContext);
}
