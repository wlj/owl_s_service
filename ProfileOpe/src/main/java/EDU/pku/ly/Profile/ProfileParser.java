package EDU.pku.ly.Profile;

import EDU.cmu.Atlas.owls1_1.grounding.OWLSGroundingModel;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGrounding;
import EDU.cmu.Atlas.owls1_1.profile.Profile;

public interface ProfileParser {
	
	public void ProfileParserEntry(String servcie_url);
	
	public void ProfileParserEntry(Profile profile, int service_id);

}
