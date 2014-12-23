package EDU.pku.ly.Grounding.Implementation;

import com.google.gson.Gson;

import EDU.cmu.Atlas.owls1_1.grounding.WsdlGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGroundingList;
import EDU.pku.ly.Grounding.GroundingDelete;
import EDU.pku.ly.Grounding.GroundingInquiry;
import EDU.pku.ly.Grounding.GroundingMainNew;
import EDU.pku.ly.Grounding.GroundingParser;

public class GroundingMainNewImpl implements GroundingMainNew {

	@Override
	public void GroundingDeleteEntry(int service_id) {
		// TODO Auto-generated method stub
		GroundingDelete groundingdelete = new GroundingDeleteImpl();
		groundingdelete.GroundingDeleteEntry(service_id);
	}

	@Override
	public void GroundingPublishEntry(WsdlGrounding grounding, int service_id) {
		// TODO Auto-generated method stub
		GroundingParser groundingParser = new GroundingParserImpl();
		groundingParser.GroundingParserEntry(grounding, service_id);
	}

	@Override
	public String GroundingInquiryEntry(int service_id) {
		// TODO Auto-generated method stub
		GroundingInquiry inquiry = new GroundingInquiryImpl();
		WsdlGroundingList ss = inquiry.GroundingInquiryEntry(service_id);
		Gson gson = new Gson();
		String str = gson.toJson(ss);
		return str;
	}
	
}
