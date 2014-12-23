package EDU.pku.sj.rscasd.owls1_1.profile.implementation;

import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_StoreImpl;
import EDU.pku.sj.rscasd.owls1_1.profile.PropertyValue;
import EDU.pku.sj.rscasd.owls1_1.profile.PropertyValueList;

public class PropertyValueListImpl extends OWLS_StoreImpl implements PropertyValueList {

	public void addPropertyValue(PropertyValue propertyValue) {
		add(propertyValue);
	}

	public PropertyValue getNthPropertyValue(int index) throws IndexOutOfBoundsException {
		return (PropertyValue) getNth(index);
	}

	public PropertyValue getPropertyValue(String uri) {
		return (PropertyValue) get(uri);
	}

	public boolean removePropertyValue(String uri) {
		return remove(uri);
	}

	public boolean removePropertyValue(PropertyValue propertyValue) {
		return remove(propertyValue);
	}
}
