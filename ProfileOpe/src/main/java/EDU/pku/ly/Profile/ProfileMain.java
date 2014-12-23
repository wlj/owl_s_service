package EDU.pku.ly.Profile;

import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;

public interface ProfileMain {
	
	public void ProfileDeleteEntry(int service_id);
	
	public void ProfilePublishEntry(String service_url);
	
	public void ProfilePublishEntry(Profile profile, int service_id);
	
	public ProfileList ProfileInquiryEntry(int service_id);
	
	public ProfileList ProfileInquiryEntry(Profile profile);
}
