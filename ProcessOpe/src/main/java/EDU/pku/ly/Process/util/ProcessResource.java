package EDU.pku.ly.Process.util;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import edu.pku.sj.rscasd.utils.encoding.BASE64Util;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProfileException;
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
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileImpl;
import EDU.pku.sj.rscasd.owls1_1.utils.OntModelRepository;

public class ProcessResource {
	
	public static Process GetOWLSProcess(String original_url)
	{
		OWLSProfileModel model = GetOWLSProfileModel(original_url);
		Process process = null;
		
		ProfileList list = model.getProfileList();
		for(int i = 0; i < list.size(); i++)
		{
			Profile profile = (ProfileImpl)list.getNthProfile(i);
			return profile.getHasProcess();
		}

		return null;
	}
	
	public static OWLSProcessModel GetOWLSProcessModel(String orginal_url)
	{
		OWLSProcessModel model = null;

		//encode
		String encodedFileContent = BASE64Util.encodeFile(orginal_url);
		String decodedPlainOwls = BASE64Util.decodePlainText(encodedFileContent);
		
		//parse
		OWLSProcessParser owlsProcessParser = new OWLSProcessParser(OntModelRepository.getNewOntModel());
		
		//stream
		java.io.InputStream sbis = new ByteArrayInputStream(decodedPlainOwls.getBytes());
		InputStreamReader reader = new InputStreamReader(sbis);
		try 
		{
			model = owlsProcessParser.read(reader);
		} 
		catch (NotInstanceOfProcessException e) 
		{
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
	
	public static int GetCCType(Process process) {
		// TODO Auto-generated method stub
		
		if(!(process instanceof CompositeProcessImpl))
		{
			return 0;
		}
		
		ControlConstructImpl cci = ProcessResource.GetCCIFromProcess(process);
		
		if(cci instanceof SequenceImpl)
		{
			return 1;
		}
		else if(cci instanceof IfThenElseImpl)
		{
			return 2;
		}
		else if(cci instanceof SplitImpl)
		{
			return 3;
		}
		else if(cci instanceof SplitJoinImpl)
		{
			return 4;
		}
		else if(cci instanceof ChoiceImpl)
		{
			return 5;
		}
		else if(cci instanceof AnyOrderImpl)
		{
			return 6;
		}
		else if(cci instanceof RepeatUntilImpl)
		{
			return 8;
		}
		else if(cci instanceof RepeatWhileImpl)
		{
			return 9;
		}
		
		return 0;
	}
	
	public static ControlConstructImpl GetCCIFromProcess(Process process)
	{
		if(!(process instanceof CompositeProcessImpl))
		{
			return null;
		}
		
		CompositeProcess compositeprocess = (CompositeProcess)process;
		ControlConstructImpl cci = (ControlConstructImpl)compositeprocess.getComposedOf();
		
		return cci;
	}
	
	public static ControlConstructListImpl GetCCLIFromProcess(Process process)
	{
		if(!(process instanceof CompositeProcessImpl))
		{
			return null;
		}
		
		CompositeProcess compositeprocess = (CompositeProcess)process;
		ControlConstructImpl cci = (ControlConstructImpl)compositeprocess.getComposedOf();
		ControlConstructListImpl ccli = (ControlConstructListImpl)cci.getComponents();
		
		return ccli;
	}
}
