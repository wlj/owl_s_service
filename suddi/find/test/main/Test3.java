import EDU.cmu.Atlas.owls1_1.parser.OWLSProcessParser;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProfileParser;
import EDU.cmu.Atlas.owls1_1.process.Input;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.OWLSProcessModel;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.implementation.InputImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.InputListImpl;
import EDU.cmu.Atlas.owls1_1.profile.OWLSProfileModel;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileImpl;


public class Test3 {
	public static void main(String[] args) {
		Profile profile = new ProfileImpl();
		profile.setServiceName("yangwei");
		Input input = new InputImpl();
		input.setParameterType("types");
		InputList inputList = new InputListImpl();
		inputList.add(input);
		profile.setInputList(inputList);
		
		System.out.println("profile: " + profile);
//		String owlsPath = "http://localhost:8080/juddiv3/owl-s/1.1/GetWeather.owl";
//		OWLSProfileParser profileParser = new OWLSProfileParser();
//		OWLSProcessParser processParser = new OWLSProcessParser();
//		try {
//			OWLSProfileModel profileModel = profileParser.read(owlsPath);
//			OWLSProcessModel processModel = processParser.read(owlsPath);
//			
//			Process firstProcessFromProfile = null;
//			ProfileList profileList = profileModel.getProfileList();
//			if (profileList == null) {
//				System.err.println("failed to parse owls or get owls profile info!");
//				return ;
//			}
//			// get the first Process from Profile
//			Profile profile = null;
//			if (profileList != null && profileList.size() > 0) {
//				profile = (ProfileImpl)profileList.getNthServiceProfile(0);
//				firstProcessFromProfile = profile.getHasProcess();
//			}
//			
//			Process process = firstProcessFromProfile;
//			if (process == null) 
//				System.out.println("process is null");
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
	}
}