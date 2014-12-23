package EDU.pku.ly.Grounding;

import EDU.cmu.Atlas.owls1_1.grounding.WsdlGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGroundingList;

public interface GroundingMainNew {
	public void GroundingDeleteEntry(int service_id);
	
	//public void GroundingPublishEntry();
	
	public void GroundingPublishEntry(WsdlGrounding grounding, int service_id);
	
	public String GroundingInquiryEntry(int service_id);
}
