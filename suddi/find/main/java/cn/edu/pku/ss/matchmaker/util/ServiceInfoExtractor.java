package cn.edu.pku.ss.matchmaker.util;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProcessParser;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProfileParser;
import EDU.cmu.Atlas.owls1_1.process.OWLSProcessModel;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.ProcessList;
import EDU.cmu.Atlas.owls1_1.profile.OWLSProfileModel;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileListImpl;
import EDU.pku.ly.owlsservice.ExtendedService;
import EDU.pku.ly.owlsservice.implementation.ExtendedServiceImpl;

public class ServiceInfoExtractor {
	private static Logger logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.util.ServiceInfoExtractor.class);
	public static ExtendedService getProfileAndProcess(String owlsPath, String key) {
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
			service.SetServiceKey(key);
			service.SetServiceProfileList(list);
			service.SetServiceModel(process);
			
			return service;		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ExtendedService getProcess(String owlsPath, String key, String processID) {
		if (owlsPath == null || key == null)
			return null;
		
		OWLSProcessParser processParser = new OWLSProcessParser();
		try {
			OWLSProcessModel processModel = processParser.read(owlsPath);
			ProcessList processList = processModel.getProcessList();
			
			if (processList == null || processList.size() < 1)
				return null;
			
			Process process = null;
			for (int i = 0; i < processList.size(); ++i) {
				Process tmpProcess = processList.getNthProcess(i);
				if (tmpProcess.getName().equals(processID)) {
					process = tmpProcess;
					break;
				}
				
			}
			
			ExtendedService extendedService = new ExtendedServiceImpl();
			extendedService.SetServiceKey(key);
			extendedService.SetServiceModel(process);
			
			return extendedService;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotInstanceOfProcessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
