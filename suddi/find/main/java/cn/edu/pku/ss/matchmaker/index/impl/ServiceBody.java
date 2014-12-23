package cn.edu.pku.ss.matchmaker.index.impl;

import java.util.Hashtable;
import java.util.List;

import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.service.ServiceGrounding;
import EDU.pku.edu.ly.temporaryowls.QoS;
public class ServiceBody {
	String serviceKey;
    Profile profile;
    Process process;
    ServiceGrounding grounding;
    String wsdlURI;
	String wsdlContent;
	List<QoS> qosList;
    String contextRule;
    Hashtable<String, DataModel>  processIndexData;
    
	public ServiceBody(String serviceKey,
			Profile profile,
			Process process,
			ServiceGrounding grounding,
			String wsdlURI,
			String wsdlContent,
			List<QoS> qosList,
			String contextRule,
			Hashtable<String, DataModel> processIndexData) {
		this.serviceKey = serviceKey;
		this.profile = profile;
		this.process = process;
		this.grounding = grounding;
		this.wsdlURI = wsdlURI;
		this.wsdlContent = wsdlContent;
		this.qosList = qosList;
		this.contextRule = contextRule;
		this.processIndexData = processIndexData;
	}

	public ServiceBody(ServiceBody serviceBody) {
		this.serviceKey = serviceBody.getServiceKey();
		this.profile = serviceBody.getProfile();
		this.process = serviceBody.getProcess();
		this.grounding = serviceBody.getGrounding();
		this.wsdlURI = serviceBody.getWsdlURI();
		this.wsdlContent = serviceBody.getWsdlContent();
		this.qosList = serviceBody.getQosList();
		this.contextRule = serviceBody.getContextRule();
		this.processIndexData = serviceBody.getProcessIndexData();
	}
	
    public Hashtable<String, DataModel> getProcessIndexData() {
		return processIndexData;
	}

	public void setProcessIndexData(Hashtable<String, DataModel> processIndexData) {
		this.processIndexData = processIndexData;
	}


	public ServiceGrounding getGrounding() {
		return grounding;
	}

	public void setGrounding(ServiceGrounding grounding) {
		this.grounding = grounding;
	}

	public String getWsdlURI() {
		return wsdlURI;
	}

	public void setWsdlURI(String wsdlURI) {
		this.wsdlURI = wsdlURI;
	}

	public String getWsdlContent() {
		return wsdlContent;
	}

	public void setWsdlContent(String wsdlContent) {
		this.wsdlContent = wsdlContent;
	}

	public String getServiceKey() {
		return serviceKey;
	}

	public void setServiceKey(String serviceKey) {
		this.serviceKey = serviceKey;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public List<QoS> getQosList() {
		return qosList;
	}

	public void setQosList(List<QoS> qosList) {
		this.qosList = qosList;
	}

	public String getContextRule() {
		return contextRule;
	}

	public void setContextRule(String contextRule) {
		this.contextRule = contextRule;
	}
	
}
