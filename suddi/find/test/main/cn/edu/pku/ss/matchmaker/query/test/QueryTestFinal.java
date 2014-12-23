package cn.edu.pku.ss.matchmaker.query.test;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.parser.OWLSProcessParser;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProfileParser;
import EDU.cmu.Atlas.owls1_1.process.Input;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.OWLSProcessModel;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.profile.OWLSProfileModel;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileListImpl;
import EDU.pku.ly.owlsservice.ExtendedService;
import EDU.pku.ly.owlsservice.OwlsServiceMain;
import EDU.pku.ly.owlsservice.implementation.ExtendedServiceImpl;
import EDU.pku.ly.owlsservice.implementation.OwlsServiceMainImpl;
import cn.edu.pku.ss.matchmaker.index.Indexer;
import cn.edu.pku.ss.matchmaker.index.impl.IndexerImpl;
import cn.edu.pku.ss.matchmaker.index.impl.ServiceBody;
import cn.edu.pku.ss.matchmaker.query.QueryManager;
import cn.edu.pku.ss.matchmaker.query.ServiceRequestInfo;
import cn.edu.pku.ss.matchmaker.query.impl.QueryManagerImpl;
import cn.edu.pku.ss.matchmaker.reasoning.SemanticReasoner;
import cn.edu.pku.ss.matchmaker.reasoning.impl.SemanticReasonerImpl;
import cn.edu.pku.ss.matchmaker.util.ServiceInfoExtractor;

public class QueryTestFinal {

	/**
	 * @param args
	 */
	private static Logger logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.query.test.QueryTest.class);
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		SemanticReasoner reasoner = new SemanticReasonerImpl();
		String filePath = "C:\\Users\\tanli\\Desktop\\ontologies.txt";
		if (reasoner.loadontologies(filePath) == false) {
			logger.error("failed to load ontologies file");
			return;
		}
		
		String owlsPath = "http://localhost:8080/juddiv3/owl-s/1.1/BravoAirProcess.owl";
		Indexer indexer = new IndexerImpl(reasoner);
		
//		ExtendedService service;
//		if ((service = ServiceInfoExtractor.getProcess(owlsPath, "11111111", "http://localhost:8080/juddiv3/owl-s/1.1/BravoAirProcess.owl#CompositeProcess_0")) == null) {
//			logger.error("failed to get profile and process");
//			return;
//		}
//		
//		
//		List<ExtendedService> serviceList = new ArrayList<ExtendedService>();
//		serviceList.add(service);
		
		OwlsServiceMain owlsservicemain = new OwlsServiceMainImpl();
		List<ExtendedService> lst_services = owlsservicemain.OwlsServiceInquiryEntry();
//		for (ExtendedService service : lst_services)
//			System.out.println(service.GetServiceProfileList().getNthServiceProfile(0));
		indexer.setExtendedServiceList(lst_services);
		if (indexer.process() == false) {
			logger.error("failed to process service info");
			return ;
		}
//		
//		if (indexer.processForTest(serviceList) == false) {
//			logger.debug("failed to process service info");
//			return ;
//		}
		
		logger.info("----------Query begin--------------");
		QueryManager manager = new QueryManagerImpl(indexer);
		
		
//		String owls = "H:\\juddi\\suddi\\suddi\\data\\BravoAirService.owl";
//		FileReader fileInputStream = new FileReader(owls);
//		BufferedReader in = new BufferedReader(fileInputStream);
//		String tmp;
//		StringBuffer sbBuffer = new StringBuffer();
//		while ((tmp = in.readLine()) != null) {
//			sbBuffer.append(tmp);
//		}
//		System.out.println(sbBuffer.toString());
//		// // String
//		ServiceRequestInfo serviceRequestInfo = new ServiceRequestInfo("",
//				sbBuffer.toString(), "", "");
		
		ExtendedService queryService = ServiceInfoExtractor.getProcess(owlsPath, "11111111", "http://localhost:8080/juddiv3/owl-s/1.1/BravoAirProcess.owl#CompositeProcess_0");
		Entry<String, ServiceBody>[] result = manager.getServices(null, (Process)queryService.GetServiceModel());
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
//			OWLSProcessModel processModel = processParser.read(owlsPath);
			
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
