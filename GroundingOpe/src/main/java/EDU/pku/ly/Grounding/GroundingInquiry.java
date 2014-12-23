package EDU.pku.ly.Grounding;

import EDU.cmu.Atlas.owls1_1.grounding.WsdlGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGroundingList;

public interface GroundingInquiry {
	
	public WsdlGroundingList GroundingInquiryEntry(int service_id);
}
