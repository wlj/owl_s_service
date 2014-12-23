package cn.edu.pku.ss.matchmaker.query.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import cn.edu.pku.ss.matchmaker.index.Indexer;
import cn.edu.pku.ss.matchmaker.index.impl.IndexerImpl;
import cn.edu.pku.ss.matchmaker.index.impl.ServiceBody;
import cn.edu.pku.ss.matchmaker.query.QueryManager;
import cn.edu.pku.ss.matchmaker.query.QueryResults;
import cn.edu.pku.ss.matchmaker.query.ServiceRequestInfo;

import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProfileException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfServiceException;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProcessParser;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProfileParser;
import EDU.cmu.Atlas.owls1_1.parser.OWLSServiceParser;
import EDU.cmu.Atlas.owls1_1.process.OWLSProcessModel;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.ProcessList;
import EDU.cmu.Atlas.owls1_1.profile.OWLSProfileModel;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileImpl;
import EDU.cmu.Atlas.owls1_1.service.OWLSServiceModel;
import EDU.cmu.Atlas.owls1_1.service.ServiceProfileList;

public class QueryManagerImpl implements QueryManager {	
//	private ProcessManager processManager;
	private ProfileQuery profileQuery;
	private ProcessQuery processQuery;
	private Ranker ranker;
	private Scorer scorer;
	
	OWLSProcessParser processParser;
	OWLSProfileParser profileParser;
	private Logger logger;
	
	public QueryManagerImpl(Indexer indexer) {
		// TODO Auto-generated constructor stub
	//	processManager = new ProcessManagerImpl();	
		scorer = new Scorer();
		processQuery = new ProcessQuery(indexer, scorer);
		profileQuery = new ProfileQuery(indexer, scorer);

		ranker = new Ranker();
		processParser = new OWLSProcessParser();
		profileParser = new OWLSProfileParser();
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.query.impl.QueryManagerImpl.class);
		
		if (scorer == null || profileQuery == null || processQuery == null || ranker == null || processParser == null || profileParser == null) {
			logger.error("failed in constructor");
		}
	}
	
	public Entry<String, ServiceBody>[] getServices(Profile profile, Process process) {
		if (profile == null && process == null) {
			logger.error("request info is null or illegal");
			return null;
		}
		
		clear();
		
        Hashtable<String, QueryResults> profileResultTable = new Hashtable<String, QueryResults>();
		
		if (profileQuery.process(profile, profileResultTable) == false)
			logger.error("failed to profile query!");
		
		if (process == null || (process != null && process.isAtomic() == true))
			return ranker.rankProfile(profileResultTable);
			
		// find service according to process info
		Hashtable<String, QueryResults> processResultTable = new Hashtable<String, QueryResults>();
		if (processQuery.process(process, processResultTable) == false)
			logger.error("failed to process query");
		
		// merge profileResultTable and processResultTable	
		profileResultTable = mergeProfileAndProcess(profileResultTable, processResultTable);
		
		
		// rank
		return ranker.rank(profileResultTable);
	}

	public Entry<String, ServiceBody>[] getServices(ServiceRequestInfo serviceRequestInfo) {
		// TODO Auto-generated method stub
		// parse owls, and get profile info and process info
		QueryData queryData;
		if ((queryData = getQueryData(serviceRequestInfo)) == null) {
			logger.error("failed to get profile and process info from owls!");
			return null;
		}
		
		
		clear();
		
		Profile profile = queryData.profile;
		Process process = queryData.process;
		// find serivice according to profile info
		Hashtable<String, QueryResults> profileResultTable = new Hashtable<String, QueryResults>();
		
		if (profileQuery.process(profile, profileResultTable) == false)
			logger.error("failed to profile query!");
		
		if (process == null || (process != null && process.isAtomic() == true))
			return ranker.rankProfile(profileResultTable);
			
		
		// find service according to process info
		Hashtable<String, QueryResults> processResultTable = new Hashtable<String, QueryResults>();
		if (processQuery.process(process, processResultTable) == false)
			logger.error("failed to process query");
		
		// merge profileResultTable and processResultTable	
		profileResultTable = mergeProfileAndProcess(profileResultTable, processResultTable);
		
		// rank
		return ranker.rank(profileResultTable);
	}
	

	// profile is prior
	private Hashtable<String, QueryResults> mergeProfileAndProcess(Hashtable<String, QueryResults> profileResultTable, 
			Hashtable<String, QueryResults> processResultTable) {
		if (profileResultTable == null || processResultTable == null) {
			logger.error("parameter is null!");
			return null;
		}
		
		Enumeration processEnumeration = processResultTable.keys();
		while (processEnumeration.hasMoreElements()) {
			String key = (String) processEnumeration.nextElement();
			QueryResults processQueryResults = processResultTable.get(key);
			
			if (profileResultTable.containsKey(key)) {
				QueryResults profileQueryResults = profileResultTable.get(key);
				
				profileQueryResults.setProcessScore(processQueryResults.getProcessScore());
				
				profileQueryResults.setTotalScore(profileQueryResults.getProfileScore() * Scorer.PROFILE_RATE + 
						profileQueryResults.getProcessScore() * Scorer.PROCESS_RATE);
			} else {
				processQueryResults.setTotalScore(processQueryResults.getProcessScore() * Scorer.PROCESS_RATE);
				profileResultTable.put(key, processQueryResults);
				
			}
		}
		
		return profileResultTable;
	}
	
	public QueryData getProfileAndProcess(String owlsPath) {
		if (owlsPath == null) {
			logger.error("owl-s path is not corrent!");
			return null;
		}
		
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
			
			// get all process from Process 
			ProcessList processList = processModel.getProcessList();
			if (processList == null) {
				logger.error("failed to parse owls or get owls process info!");
				return null;
			}
			
			// find the specified process in Process
			Process process = processList.getProcess(firstProcessFromProfile.getURI());		
			return new QueryData(profile, process);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private void clear() {
		Scorer.clear();
	}
	
	
	class QueryData {
		Profile profile;
		Process process;
		
		public QueryData(Profile profile, Process process) {
			// TODO Auto-generated constructor stub
			this.profile = profile;
			this.process = process;
		}
	}


	public boolean getProfileAndProcess(String owlsPath, Profile profile,
			Process process) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean getProfileAndProcess(OWLSProfileModel profileModel, 
			OWLSProcessModel processModel,
			Profile profile,
			Process process) {
		if (profileModel == null && processModel == null)
			return false;
		
		Process firstProcessFromProfile = null;
		ProfileList profileList = profileModel.getProfileList();
		if (profileList == null) {
			logger.error("failed to parse owls or get owls profile info!");
			return false;
		}
		// get the first Process from Profile
		if (profileList != null && profileList.size() > 0) {
			profile = (ProfileImpl)profileList.getNthServiceProfile(0);
			firstProcessFromProfile = profile.getHasProcess();
		}
		
		// get all process from Process 
		ProcessList processList = processModel.getProcessList();
		if (processList != null)
			process = processList.getProcess(firstProcessFromProfile.getURI());	
					
		return true;
	}
	
	public QueryData getQueryData(ServiceRequestInfo serviceRequestInfo) {
		if (serviceRequestInfo == null) {
			logger.error("query is null");
			return null;
		}
		OWLSProfileModel profileModel = null;
		OWLSProcessModel processModel = null;
		QueryData queryData = null;
		String owlsContent = serviceRequestInfo.getOwlsContent();
		if (owlsContent == null || owlsContent.isEmpty())
			logger.info("owls content is null");
		else {
			InputStream inputStream =  new ByteArrayInputStream(owlsContent.getBytes());
			Reader reader = new InputStreamReader(inputStream);
			try {
//				profileModel = profileParser.read(reader);
//				processModel = processParser.read(reader);
				OWLSServiceParser serviceParser = new OWLSServiceParser();
				OWLSServiceModel model = serviceParser.read(reader);
				ProfileList profileList = (ProfileList) model.getServiceList().getNthService(0).getPresents();
				Process process = (Process) model.getServiceList().getNthService(0).getDescribes();
				Profile profile = profileList.getNthProfile(0);
				process = profile.getHasProcess();
				queryData = new QueryData(profile, process);
				System.out.println("process: " + process.getInputList());
				System.out.println(profileList.getNthProfile(0).getInputList());
			} catch (NotInstanceOfServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return queryData;
//		Profile contentProfile = null;
//		Process contentProcess = null;
//		if (getProfileAndProcess(profileModel, processModel, contentProfile, contentProcess) == false)
//			logger.error("failed to get process and profile by owls content");
//		if (contentProfile != null && contentProcess != null)
//			return new QueryData(contentProfile, contentProcess);
//		
//		String owlsURI = serviceRequestInfo.getOwlsURI();
//		try {
//			profileModel = null;
//			processModel = null;
//			profileModel = profileParser.read(owlsURI);
//			processModel = processParser.read(owlsURI);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (NotInstanceOfProfileException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (NotInstanceOfProcessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//   
//		Profile uriProfile = null;
//		Process uriProcess = null;
//		if (getProfileAndProcess(profileModel, processModel, uriProfile, uriProcess) == false)
//			logger.error("failed to get process and profile by owls uri");
//		if (contentProfile != null && contentProcess != null)
//			return new QueryData(contentProfile, contentProcess);
//		
//		if (contentProfile != null && uriProcess != null)
//			return new QueryData(contentProfile, uriProcess);
//		
//		if (contentProcess != null && uriProfile != null)
//			return new QueryData(uriProfile, contentProcess);
//		
//		if (contentProfile != null)
//			return new QueryData(contentProfile, null);
//		
//		if (uriProfile != null)
//			return new QueryData(uriProfile, null);
//		
//		return null; 
	}
}

