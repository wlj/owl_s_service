package cn.edu.pku.ss.matchmaker.index.test;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.parser.OWLSProcessParser;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProfileParser;
import EDU.cmu.Atlas.owls1_1.process.OWLSProcessModel;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.profile.OWLSProfileModel;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileListImpl;
import EDU.pku.ly.owlsservice.ExtendedService;
import EDU.pku.ly.owlsservice.implementation.ExtendedServiceImpl;

import cn.edu.pku.ss.matchmaker.index.Indexer;
import cn.edu.pku.ss.matchmaker.index.impl.IndexerImpl;
import cn.edu.pku.ss.matchmaker.index.impl.SemanticDataModel;
import cn.edu.pku.ss.matchmaker.index.impl.ServiceDataModel;
import cn.edu.pku.ss.matchmaker.reasoning.MatchLevel;
import cn.edu.pku.ss.matchmaker.reasoning.SemanticReasoner;
import cn.edu.pku.ss.matchmaker.reasoning.impl.SemanticReasonerImpl;

public class IndexerTest {

	/**
	 * @param args
	 */
	
	private static Logger logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.index.test.IndexerTest.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SemanticReasoner reasoner = new SemanticReasonerImpl();
		String filePath = "H:\\juddi\\suddi\\suddi\\data\\ontologies.txt";
		if (reasoner.loadontologies(filePath) == false) {
			logger.debug("failed to load ontologies file");
			return;
		}
		
		String owlsPath = "http://localhost:8080/juddiv3/owl-s/1.1/BravoAirProfile.owl";
		IndexerImpl indexer = new IndexerImpl(reasoner);
		
		ExtendedService service;
		if ((service = getProfileAndProcess(owlsPath)) == null) {
			logger.debug("failed to get profile and process");
			return;
		}
		
		
		List<ExtendedService> serviceList = new ArrayList<ExtendedService>();
		serviceList.add(service);
		
		if (indexer.processForTest(serviceList) == false) {
			logger.debug("failed to process service info");
			return ;
		}
		

		
		Hashtable<String, ServiceDataModel> serviceIndexer = indexer.getServiceIndexer();
		Hashtable<String, Hashtable<String, SemanticDataModel>> semanticIndexer = indexer.getsemanticIndexer();
		Hashtable<String, Hashtable<MatchLevel, Set<String>> > matchLevelIndexer = indexer.getmatchLevelIndexer();
		Hashtable<Process, List<Process>> atomicProcesses = indexer.getAtomicProcesses();
		
		System.out.println("serviceIndexer" + serviceIndexer);
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
			OWLSProcessModel processModel = processParser.read(owlsPath);
			
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
			
//			// get all process from Process 
//			ProcessList processList = processModel.getProcessList();
//			if (processList == null) {
//				logger.error("failed to parse owls or get owls process info!");
//				return false;
//			}
//			
//			// find the specified process in Process
//			process = processList.getProcess(firstProcessFromProfile.getURI());			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
//		return true;
	}
}
