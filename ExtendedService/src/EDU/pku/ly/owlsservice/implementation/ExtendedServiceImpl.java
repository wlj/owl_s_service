package EDU.pku.ly.owlsservice.implementation;

import java.util.List;

import EDU.pku.edu.ly.temporaryowls.QoS;
import EDU.pku.ly.owlsservice.ExtendedService;
import EDU.cmu.Atlas.owls1_1.service.ServiceGroundingList;
import EDU.cmu.Atlas.owls1_1.service.ServiceList;
import EDU.cmu.Atlas.owls1_1.service.ServiceModel;
import EDU.cmu.Atlas.owls1_1.service.ServiceProfileList;
import EDU.cmu.Atlas.owls1_1.service.implementation.ServiceImpl;
import EDU.cmu.Atlas.owls1_1.service.implementation.ServiceListImpl;

public class ExtendedServiceImpl implements ExtendedService {
	
	public ExtendedServiceImpl(){
		
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String service_key;
	
	private ServiceList service_lst;
	
	private String contribution_exd_path;
	
	private String wsdl_file_path;
	
	private String contextRule;
	
	private List<QoS> lst_qos;
	
	public String getServiceKey()
	{
		return service_key;
	}
	
	public String getContextRule()
	{
		return contextRule;
	}
	
	public void setContextRule(String contextRule)
	{
		this.contextRule = contextRule;
	}
	
	public List<QoS> getQoS()
	{
		return lst_qos;
	}
	
	public void setQoS(List<QoS> qos_lst)
	{
		lst_qos = qos_lst;
	}
	
	public void setServiceKey(String servicekey)
	{
		service_key = servicekey;
	}
	
	public ServiceList getServiceList()
	{
		return service_lst;
	}
	
	public void setServiceList(ServiceList servicelist)
	{
		service_lst = servicelist;
	}
	
	public String getContributionExdPath()
	{
		return contribution_exd_path;
	}
	
	public void setContributionPath(String path)
	{
		contribution_exd_path = path;
	}
	
	public String getWSDLFilePath()
	{
		return wsdl_file_path;
	}
	
	public void getGetWSDLFilePath(String path)
	{
		wsdl_file_path = path;
	}
	
	public ServiceProfileList getServiceProfileList()
	{
		return service_lst.getNthService(0).getPresents();
	}
	
	public ServiceModel getServiceModel()
	{
		return service_lst.getNthService(0).getDescribes();
	}
	
	public ServiceGroundingList getServiceGroundingList()
	{
		return service_lst.getNthService(0).getSupports();
	}
	
	public void setServiceProfileList(ServiceProfileList serviceProfileList)
	{
		if(service_lst == null)
			service_lst = new ServiceListImpl();

		if(service_lst.size() == 0)
			service_lst.addService(new ServiceImpl());
		
		service_lst.getNthService(0).setPresents(serviceProfileList);
	}
	
	public void setServiceModel(ServiceModel serviceModel)
	{
		if(service_lst == null)
			service_lst = new ServiceListImpl();

		if(service_lst.size() == 0)
			service_lst.addService(new ServiceImpl());
		
		service_lst.getNthService(0).setDescribes(serviceModel);
	}
	
	public void setServiceGroundingList(ServiceGroundingList serviceGroundingList)
	{
		if(service_lst == null)
			service_lst = new ServiceListImpl();

		if(service_lst.size() == 0)
			service_lst.addService(new ServiceImpl());
		
		service_lst.getNthService(0).setSupports(serviceGroundingList);
	}
}
