package cn.edu.pku.ss.matchmaker.query.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

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
import cn.edu.pku.ss.matchmaker.index.impl.ServiceBody;
import cn.edu.pku.ss.matchmaker.query.QueryManager;
import cn.edu.pku.ss.matchmaker.query.ServiceRequestInfo;
import cn.edu.pku.ss.matchmaker.query.impl.QueryManagerImpl;
import cn.edu.pku.ss.matchmaker.reasoning.SemanticReasoner;
import cn.edu.pku.ss.matchmaker.reasoning.impl.SemanticReasonerImpl;

public class QueryTest {

	/**
	 * @param args
	 */
	private static Logger logger = Logger
			.getLogger(cn.edu.pku.ss.matchmaker.query.test.QueryTest.class);

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

		if (indexer.processForTest(serviceList) == false) {
			logger.debug("failed to process service info");
			return;
		}

		logger.info("----------Query begin--------------");
		QueryManager manager = new QueryManagerImpl(indexer);

		String owls = "H:\\juddi\\suddi\\suddi\\data\\BravoAirService.owl";
		FileReader fileInputStream = new FileReader(owls);
		BufferedReader in = new BufferedReader(fileInputStream);
		String tmp;
		StringBuffer sbBuffer = new StringBuffer();
		while ((tmp = in.readLine()) != null) {
			sbBuffer.append(tmp);
		}
		System.out.println(sbBuffer.toString());
		// // String
		ServiceRequestInfo serviceRequestInfo = new ServiceRequestInfo("",
				sbBuffer.toString(), "", "");
		//		
		Entry<String, ServiceBody>[] result = manager
				.getServices(serviceRequestInfo);
		for (int i = 0; i < result.length; ++i)
			System.out.println("result: " + result[i].getKey());

		logger.info("----------Query end--------------");

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
				profile = (ProfileImpl) profileList.getNthServiceProfile(0);
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

			// // get all process from Process
			// ProcessList processList = processModel.getProcessList();
			// if (processList == null) {
			// logger.error("failed to parse owls or get owls process info!");
			// return false;
			// }
			//			
			// // find the specified process in Process
			// process =
			// processList.getProcess(firstProcessFromProfile.getURI());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

		// return true;
	}

}
