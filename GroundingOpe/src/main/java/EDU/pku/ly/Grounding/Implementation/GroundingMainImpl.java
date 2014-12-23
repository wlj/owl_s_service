package EDU.pku.ly.Grounding.Implementation;

import EDU.cmu.Atlas.owls1_1.grounding.WsdlGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGroundingList;
import EDU.pku.ly.Grounding.GroundingDelete;
import EDU.pku.ly.Grounding.GroundingInquiry;
import EDU.pku.ly.Grounding.GroundingMain;
import EDU.pku.ly.Grounding.GroundingParser;

public class GroundingMainImpl implements GroundingMain {
	
	private static final long serialVersionUID = -1L;
	private String service_url = "";
	private String service_id = "";
	
	public GroundingMainImpl()
	{}
	
	public GroundingMainImpl(String service_url)
	{
		this.service_url = service_url;
	}
	
	public WsdlGroundingList GroundingInquiryEntry(int service_id) {
		// TODO Auto-generated method stub
		
		GroundingInquiry inquiry = new GroundingInquiryImpl();
		return inquiry.GroundingInquiryEntry(service_id);
	}

//	public Process GroundingInquiryEntry(String service_url) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	public void GroundingPublishEntry() {
//		// TODO Auto-generated method stub
//
//	}

	public void GroundingPublishEntry(WsdlGrounding grounding, int service_id) {
		// TODO Auto-generated method stub
		
		GroundingParser groundingParser = new GroundingParserImpl();
		groundingParser.GroundingParserEntry(grounding, service_id);
	}

	public void GroundingDeleteEntry(int service_id) {
		// TODO Auto-generated method stub
		
		GroundingDelete groundingdelete = new GroundingDeleteImpl();
		groundingdelete.GroundingDeleteEntry(service_id);
	}

}
