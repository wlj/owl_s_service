import java.io.IOException;
import java.net.MalformedURLException;

import cn.edu.pku.ss.matchmaker.process.impl.ProcessGraphModelFactory;
import cn.edu.pku.ss.matchmaker.process.impl.SequenceGraphModelImpl;
import cn.edu.pku.ss.matchmaker.process.model.Node;


import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProfileException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfServiceException;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProcessParser;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProfileParser;
import EDU.cmu.Atlas.owls1_1.parser.OWLSServiceParser;
import EDU.cmu.Atlas.owls1_1.process.CompositeProcess;
import EDU.cmu.Atlas.owls1_1.process.ControlConstruct;
import EDU.cmu.Atlas.owls1_1.process.ControlConstructList;
import EDU.cmu.Atlas.owls1_1.process.OWLSProcessModel;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.ProcessList;
import EDU.cmu.Atlas.owls1_1.process.implementation.CompositeProcessImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ProcessListImpl;
import EDU.cmu.Atlas.owls1_1.profile.OWLSProfileModel;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;
import EDU.cmu.Atlas.owls1_1.service.OWLSServiceModel;
import EDU.cmu.Atlas.owls1_1.service.ServiceList;
import EDU.cmu.Atlas.owls1_1.shadowlist.List;


public class Test {

	/**
	 * @param args
	 * @throws NotInstanceOfServiceException 
	 * @throws IOException 
	 * @throws IOException 
	 * @throws NotInstanceOfProcessException 
	 * @throws NotInstanceOfProcessException 
	 * @throws NotInstanceOfProfileException 
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws IOException, NotInstanceOfServiceException, NotInstanceOfProcessException, NotInstanceOfProfileException {
		// TODO Auto-generated method stub
		String uri = "http://localhost:8080/juddiv3/owl-s/1.1/BravoAirProcess.owl";
//		OWLSServiceParser parser = new OWLSServiceParser();
//		OWLSServiceModel model = parser.read(uri);
//		
//		ServiceList serviceList = model.getServiceList();
		
		
		OWLSProcessParser processParser = new OWLSProcessParser();
		OWLSProcessModel processModel = processParser.read(uri);
		ProcessList processList = processModel.getProcessList();
		if (processList == null) {
			System.err.println("error");
			return;
		}
		
		if (processList.size() == 0) {
			System.err.println("size = 0");
			return;
		}
		
		for (int i = 0; i < processList.size(); ++i) {
			Process process  = processList.getNthProcess(i);
			System.out.println(process);
		}
		
		
//		OWLSProfileParser profileParser = new OWLSProfileParser();
//		OWLSProfileModel profileModel = profileParser.read(uri);
//		
//		ProfileList profileList = profileModel.getProfileList();
//		
//		if (profileList == null || profileList.size() < 1) {
//			System.out.println("profileList is null");
//			return ;
//		}
//		
//		Process process = profileList.getNthProfile(0).getHasProcess();
//		if (process == null) {
//			System.out.println("process is null");
//			return;
//		}
//		
//		System.out.println(process);
	}
}
