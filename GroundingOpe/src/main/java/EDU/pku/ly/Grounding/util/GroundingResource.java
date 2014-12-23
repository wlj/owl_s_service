package EDU.pku.ly.Grounding.util;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import edu.pku.sj.rscasd.utils.encoding.BASE64Util;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProfileException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfWsdlGroundingException;
import EDU.cmu.Atlas.owls1_1.grounding.OWLSGroundingModel;
import EDU.cmu.Atlas.owls1_1.parser.OWLSGroundingParser;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProcessParser;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProfileParser;
import EDU.cmu.Atlas.owls1_1.process.CompositeProcess;
import EDU.cmu.Atlas.owls1_1.process.OWLSProcessModel;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.implementation.AnyOrderImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ChoiceImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.CompositeProcessImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ControlConstructImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ControlConstructListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.IfThenElseImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.RepeatUntilImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.RepeatWhileImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.SequenceImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.SplitImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.SplitJoinImpl;
import EDU.cmu.Atlas.owls1_1.profile.OWLSProfileModel;
import EDU.pku.sj.rscasd.owls1_1.utils.OntModelRepository;

public class GroundingResource {
	
	private static final long serialVersionUID = -1L;
	
	public static OWLSGroundingModel GetOWLSGroundingModel(String service_url)
	{
		OWLSGroundingModel model = null;

		//encode
		String encodedFileContent = BASE64Util.encodeFile(service_url);
		String decodedPlainOwls = BASE64Util.decodePlainText(encodedFileContent);
		
		//parse
		OWLSGroundingParser owlsGroundingParser = new OWLSGroundingParser(OntModelRepository.getNewOntModel());
		
		//stream
		java.io.InputStream sbis = new ByteArrayInputStream(decodedPlainOwls.getBytes());
		InputStreamReader reader = new InputStreamReader(sbis);
		try 
		{
			model = owlsGroundingParser.read(reader);
		} 
		catch (NotInstanceOfWsdlGroundingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return model;
	}
	
	public static OWLSProfileModel GetOWLSProfileModel(String original_url)
	{
		OWLSProfileModel model = null;

		//encode
		String encodedFileContent = BASE64Util.encodeFile(original_url);
		String decodedPlainOwls = BASE64Util.decodePlainText(encodedFileContent);
		
		//parse
		OWLSProfileParser owlsProfileParser = new OWLSProfileParser(OntModelRepository.getNewOntModel());
		
		//stream
		java.io.InputStream sbis = new ByteArrayInputStream(decodedPlainOwls.getBytes());
		InputStreamReader reader = new InputStreamReader(sbis);
		try 
		{
			model = owlsProfileParser.read(reader);
		} 
		catch (NotInstanceOfProfileException e) {
			e.printStackTrace();
		}
		
		return model;
	}
}
