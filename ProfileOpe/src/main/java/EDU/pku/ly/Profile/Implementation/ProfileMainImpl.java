package EDU.pku.ly.Profile.Implementation;

import EDU.cmu.Atlas.owls1_1.profile.OWLSProfileModel;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileListImpl;
import EDU.pku.ly.Profile.ProfileDelete;
import EDU.pku.ly.Profile.ProfileInquiry;
import EDU.pku.ly.Profile.ProfileMain;
import EDU.pku.ly.Profile.ProfileParser;
import EDU.pku.ly.Profile.util.ProfileResource;

public class ProfileMainImpl implements ProfileMain {

	private static final long serialVersionUID = -1L;
	
	public ProfileList ProfileInquiryEntry(int service_id) {
		// TODO Auto-generated method stub
		
		ProfileInquiry inquiry = new ProfileInquiryImpl();
		return inquiry.ProfileInquiryEntry(service_id);
	}

	public ProfileList ProfileInquiryEntry(Profile profile) {
		// TODO Auto-generated method stub
		return null;
	}

	public void ProfilePublishEntry(String service_url) {
		// TODO Auto-generated method stub

		ProfileParser parser = new ProfileParserImpl();
		
		OWLSProfileModel model = ProfileResource.GetOWLSProfileModel(service_url);
		Profile profile = model.getProfileList().getNthProfile(0);
		
		long t1 = System.currentTimeMillis();
		for(int i = 1; i < 100; i++)
		{
			parser.ProfileParserEntry(profile, i);
		}
		long t2 = System.currentTimeMillis();
		
		System.out.println("time:" + (t2 - t1));
	}

	public void ProfilePublishEntry(Profile profile, int service_id) {
		// TODO Auto-generated method stub
		
		ProfileParser parser = new ProfileParserImpl();
		parser.ProfileParserEntry(profile, service_id);
	}

	public void ProfileDeleteEntry(int service_id) {
		// TODO Auto-generated method stub
		
		ProfileDelete profiledelete = new ProfileDeleteImpl();
		profiledelete.ProfileDeleteEntry(service_id);
	}
	
	/*public static void main(String[] args)
	{
		String orginal_url = "C:\\luoshan\\SourceCode\\juddi\\running\\juddi-portal-bundle-3.0.2\\webapps\\juddiv3\\owl-s\\1.1\\BravoAirService.owl";
		OWLSProfileModel model = ProfileResource.GetOWLSProfileModel(orginal_url);
		
		int i = 0;
	}*/
}
