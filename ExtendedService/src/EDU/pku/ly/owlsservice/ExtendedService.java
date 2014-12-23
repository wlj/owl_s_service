package EDU.pku.ly.owlsservice;

import java.io.Serializable;
import java.util.List;

import EDU.cmu.Atlas.owls1_1.service.ServiceGroundingList;
import EDU.cmu.Atlas.owls1_1.service.ServiceList;
import EDU.cmu.Atlas.owls1_1.service.ServiceModel;
import EDU.cmu.Atlas.owls1_1.service.ServiceProfileList;
import EDU.pku.edu.ly.temporaryowls.QoS;

public interface ExtendedService extends Serializable{
	
	public String getContextRule();
	
	public void setContextRule(String contextRule);
	
	public List<QoS> getQoS();
	
	public void setQoS(List<QoS> qos_lst);
	
	public String getServiceKey();
	
	public void setServiceKey(String servicekey);
	
	public ServiceList getServiceList();
	
	public void setServiceList(ServiceList servicelist);
	
	public String getContributionExdPath();
	
	public void setContributionPath(String path);
	
	public String getWSDLFilePath();
	
	public void getGetWSDLFilePath(String path);
	
	public ServiceProfileList getServiceProfileList();
	
	public ServiceModel getServiceModel();
	
	public ServiceGroundingList getServiceGroundingList();
	
	public void setServiceProfileList(ServiceProfileList serviceProfileList);
	
	public void setServiceModel(ServiceModel serviceModel);
	
	public void setServiceGroundingList(ServiceGroundingList serviceGroundingList);	
}
