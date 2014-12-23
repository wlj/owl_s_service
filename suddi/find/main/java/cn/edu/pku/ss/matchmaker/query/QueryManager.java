package cn.edu.pku.ss.matchmaker.query;

import java.util.Map.Entry;


import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.profile.Profile;

import cn.edu.pku.ss.matchmaker.index.impl.ServiceBody;

public interface QueryManager {
	
	public abstract Entry<String, ServiceBody>[] getServices(ServiceRequestInfo serviceRequestInfo); 
	
	public boolean getProfileAndProcess(String owlsPath, Profile profile, Process process);
	
	public abstract Entry<String, ServiceBody>[] getServices(Profile profile, Process process);
}
