package EDU.pku.ly.Profile;

import EDU.cmu.Atlas.owls1_1.profile.Profile;

public interface ProfileMainNew {
	public void ProfileDeleteEntry(int service_id);
	
	public void ProfilePublishEntry(String service_url);
	
	//public void ProfilePublishEntry(Profile profile, int service_id);
	
	public String ProfileInquiryEntry(int service_id);
	
}
