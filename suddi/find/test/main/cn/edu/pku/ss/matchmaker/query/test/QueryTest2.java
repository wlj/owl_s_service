package cn.edu.pku.ss.matchmaker.query.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.parser.OWLSProcessParser;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProfileParser;
import EDU.cmu.Atlas.owls1_1.process.Input;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.Output;
import EDU.cmu.Atlas.owls1_1.process.OutputList;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.implementation.InputImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.InputListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.OutputImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.OutputListImpl;
import EDU.cmu.Atlas.owls1_1.profile.ActorsList;
import EDU.cmu.Atlas.owls1_1.profile.OWLSProfileModel;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ActorsListImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileListImpl;
import EDU.pku.ly.owlsservice.ExtendedService;
import EDU.pku.ly.owlsservice.implementation.ExtendedServiceImpl;
import cn.edu.pku.ss.matchmaker.index.Indexer;
import cn.edu.pku.ss.matchmaker.index.impl.IndexerImpl;
import cn.edu.pku.ss.matchmaker.index.impl.ServiceBody;
import cn.edu.pku.ss.matchmaker.query.QueryManager;
import cn.edu.pku.ss.matchmaker.query.ServiceRequestInfo;
import cn.edu.pku.ss.matchmaker.query.impl.QueryManagerImpl;
import cn.edu.pku.ss.matchmaker.reasoning.SemanticReasoner;
import cn.edu.pku.ss.matchmaker.reasoning.impl.SemanticReasonerImpl;

public class QueryTest2 {

	/**
	 * @param args
	 */
	 
		private static Logger logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.query.test.QueryTest.class);
		public static void main(String[] args) throws IOException {
			// TODO Auto-generated method stub
			
			SemanticReasoner reasoner = new SemanticReasonerImpl();
			String filePath = "H:\\juddi\\suddi\\suddi\\data\\ontologies.txt";
			if (reasoner.loadontologies(filePath) == false) {
				logger.debug("failed to load ontologies file");
				return;
			}
			
			String owlsPath = "http://localhost:8080/juddiv3/owl-s/1.1/BravoAirProfile.owl";
			Indexer indexer = new IndexerImpl(reasoner);
			
			ExtendedService service;
			if ((service = getProfileAndProcess(owlsPath)) == null) {
				logger.debug("failed to get profile and process");
				return;
			}
			
			
			List<ExtendedService> serviceList = new ArrayList<ExtendedService>();
			serviceList.add(service);
			
			indexer.setExtendedServiceList(serviceList);
			if (indexer.process() == false) {
				logger.debug("failed to process service info");
				return ;
			}
//			
//			if (indexer.processForTest(serviceList) == false) {
//				logger.debug("failed to process service info");
//				return ;
//			}
			
			logger.info("----------Query begin--------------");
			QueryManager manager = new QueryManagerImpl(indexer);
			
			
			Profile profile = getProfileForTest();
			
			Entry<String, ServiceBody>[] result = manager.getServices(profile, null);
			for (int i = 0; i < result.length; ++i)
				System.out.println("result: " + result[i].getKey());
			
			logger.info("----------Query end--------------");

		}
		
		
		public static Profile getProfileForTest() {
			Profile profile = new ProfileImpl();
			InputList inputList = new InputListImpl();
			Input input1 = new InputImpl();
			Input input2 = new InputImpl();
			Input input3 = new InputImpl();
			Input input4 = new InputImpl();
			Input input5 = new InputImpl();
			Input input6 = new InputImpl();
			Input input7 = new InputImpl();
			Input input8 = new InputImpl();
			input1.setParameterType("http://localhost:8080/juddiv3/owl-s/1.1/Concepts.owl#FlightDate");
			input2.setParameterType("http://localhost:8080/juddiv3/owl-s/1.1/Concepts.owl#FlightDate");
			input3.setParameterType("http://localhost:8080/juddiv3/owl-s/1.1/Concepts.owl#Password");
			input4.setParameterType("http://localhost:8080/juddiv3/owl-s/1.1/Concepts.owl#Airport");
			input5.setParameterType("http://localhost:8080/juddiv3/owl-s/1.1/Concepts.owl#AcctName");
			input6.setParameterType("http://localhost:8080/juddiv3/owl-s/1.1/Concepts.owl#Airport");
			input7.setParameterType("http://localhost:8080/juddiv3/owl-s/1.1/Concepts.owl#RoundTrip");
			input8.setParameterType("http://localhost:8080/juddiv3/owl-s/1.1/Concepts.owl#Confirmation");
			inputList.add(input1);
			inputList.add(input2);
			inputList.add(input3);
			inputList.add(input4);
			inputList.add(input5);
			inputList.add(input6);
			inputList.add(input7);
			inputList.add(input8);
			
			
			OutputList outputList = new OutputListImpl();
			Output output1 = new OutputImpl();
			Output output2 = new OutputImpl();
			Output output3 = new OutputImpl();
			output1.setParameterType("http://localhost:8080/juddiv3/owl-s/1.1/Concepts.owl#FlightItinerary");
			output2.setParameterType("http://localhost:8080/juddiv3/owl-s/1.1/Concepts.owl#FlightList");
			output3.setParameterType("http://localhost:8080/juddiv3/owl-s/1.1/Concepts.owl#ReservationNumber");
			outputList.add(output1);
			outputList.add(output2);
			outputList.add(output3);
			
			profile.setInputList(inputList);
			profile.setOutputList(outputList);
			
			return profile;
		}
		
		public static ExtendedService getProfileAndProcess(String owlsPath) {
			if (owlsPath == null) {
				logger.error("owl-s path is not corrent!");
				return null;
			}
			OWLSProfileParser profileParser = new OWLSProfileParser();
			OWLSProcessParser processParser = new OWLSProcessParser();
			try {
				OWLSProfileModel profileModel = profileParser.read(owlsPath);
//				OWLSProcessModel processModel = processParser.read(owlsPath);
				
				Process firstProcessFromProfile = null;
				ProfileList profileList = profileModel.getProfileList();
				if (profileList == null) {
					logger.error("failed to parse owls or get owls profile info!");
					return null;
				}
				// get the first Process from Profile
				Profile profile = null;
				if (profileList != null && profileList.size() > 0) {
					profile = (ProfileImpl)profileList.getNthServiceProfile(0);
					firstProcessFromProfile = profile.getHasProcess();
				}
				
				Process process = firstProcessFromProfile;
				ProfileList list = new ProfileListImpl();
				list.add(profile);
				
				ExtendedService service = new ExtendedServiceImpl();
				service.SetServiceKey("111111111111");
				service.SetServiceProfileList(list);
				service.SetServiceModel(process);
				
				return service;		
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;

		}

}
