package grounding;

import static org.junit.Assert.*;

import org.junit.Test;

import EDU.cmu.Atlas.owls1_1.grounding.OWLSGroundingModel;
import EDU.pku.ly.Grounding.util.GroundingResource;

public class GroundingResourceTest {

	@Test
	public void test() {
		String url="E:\\apache-tomcat-7.0.57\\webapps\\juddiv3\\owl-s\\1.1\\BravoAirService.owl";
		OWLSGroundingModel model=GroundingResource.GetOWLSGroundingModel(url);
		System.out.println(model.toString());
	}

}
