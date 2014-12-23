package EDU.pku.sj.rscasd.owls1_1.profile;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Store;

public interface PropertyValueList extends OWLS_Store {
	public PropertyValue getNthPropertyValue(int index) throws IndexOutOfBoundsException;

	public boolean removePropertyValue(String uri);

	public boolean removePropertyValue(PropertyValue propertyValue);

	public PropertyValue getPropertyValue(String uri);

	public void addPropertyValue(PropertyValue propertyValue);
}
