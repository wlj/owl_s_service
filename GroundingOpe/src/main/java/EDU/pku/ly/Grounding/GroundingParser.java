package EDU.pku.ly.Grounding;

import java.io.Serializable;

import EDU.cmu.Atlas.owls1_1.grounding.OWLSGroundingModel;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGrounding;

public interface GroundingParser extends Serializable{

	public void GroundingParserEntry(WsdlGrounding grounding, int service_id);
	
	public void GroundingPersistence(OWLSGroundingModel owlsProcessModel, String groundingname);
	
}
