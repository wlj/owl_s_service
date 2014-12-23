package EDU.pku.ly.Profile.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProfileException;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProcessParser;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProfileParser;
import EDU.cmu.Atlas.owls1_1.process.OWLSProcessModel;
import EDU.cmu.Atlas.owls1_1.profile.OWLSProfileModel;
import EDU.pku.sj.rscasd.owls1_1.utils.OntModelRepository;
import edu.pku.sj.rscasd.utils.encoding.BASE64Util;

public class ProfileResource {
	
	private static final long serialVersionUID = -1L;
	
	public static OWLSProfileModel GetOWLSProfileModel(String orginal_url)
	{
		OWLSProfileModel model = null;

		//encode
		String encodedFileContent = BASE64Util.encodeFile(orginal_url);
		String decodedPlainOwls = BASE64Util.decodePlainText(encodedFileContent);
		
		//parse
		OWLSProfileParser owlsProfileParser = new OWLSProfileParser(OntModelRepository.getNewOntModel());
		
		//stream
		InputStream sbis = new ByteArrayInputStream(decodedPlainOwls.getBytes());
		InputStreamReader reader = new InputStreamReader(sbis);
		try 
		{
			model = owlsProfileParser.read(reader);
		} 
		catch (NotInstanceOfProfileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return model;
	}
}
