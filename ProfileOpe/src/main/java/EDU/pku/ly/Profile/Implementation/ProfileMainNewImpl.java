package EDU.pku.ly.Profile.Implementation;

import com.google.gson.Gson;

import EDU.cmu.Atlas.owls1_1.profile.OWLSProfileModel;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.pku.ly.Profile.ProfileDelete;
import EDU.pku.ly.Profile.ProfileInquiry;
import EDU.pku.ly.Profile.ProfileMainNew;
import EDU.pku.ly.Profile.ProfileParser;
import EDU.pku.ly.Profile.util.ProfileResource;

public class ProfileMainNewImpl implements ProfileMainNew{

	@Override
	public void ProfileDeleteEntry(int service_id) {
		// TODO Auto-generated method stub
		ProfileDelete profiledelete = new ProfileDeleteImpl();
		profiledelete.ProfileDeleteEntry(service_id);
	}

	@Override
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

//	@Override
//	public void ProfilePublishEntry(Profile profile, int service_id) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public String ProfileInquiryEntry(int service_id) {
		// TODO Auto-generated method stub
		ProfileInquiry inquiry = new ProfileInquiryImpl();
		Gson so = new Gson();
		return so.toJson(inquiry.ProfileInquiryEntry(service_id));
	}

	

}
