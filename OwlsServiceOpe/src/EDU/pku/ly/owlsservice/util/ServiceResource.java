package EDU.pku.ly.owlsservice.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfServiceException;
import EDU.cmu.Atlas.owls1_1.parser.OWLSServiceParser;
import EDU.cmu.Atlas.owls1_1.service.OWLSServiceModel;
import EDU.pku.sj.rscasd.owls1_1.utils.OntModelRepository;
import edu.pku.sj.rscasd.utils.encoding.BASE64Util;

public class ServiceResource {
	
	public static OWLSServiceModel GetOWLSServiceModel(String service_url)
	{
		OWLSServiceModel model = null;
		
		String encodedFileContent = BASE64Util.encodeFile(service_url);
		String decodedPlainOwls = BASE64Util.decodePlainText(encodedFileContent);
		
		OWLSServiceParser parser = new OWLSServiceParser(OntModelRepository.getNewOntModel());
		
		//stream
		InputStream sbis = new ByteArrayInputStream(decodedPlainOwls.getBytes());
		InputStreamReader reader = new InputStreamReader(sbis);
		try 
		{
			model = parser.read(reader);
		} 
		catch (NotInstanceOfServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return model;
	}
}
