package EDU.pku.ly.owlsservice.implementation;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
//8-2¸ü¸Ä
//import com.ibm.xtq.bcel.generic.RETURN;



import edu.pku.ly.SqlOpe.SQLHelper;
import edu.pku.ly.XmlOpe.DomOpe;
import EDU.cmu.Atlas.owls1_1.grounding.OWLSGroundingModel;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGroundingList;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlGroundingImpl;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlGroundingListImpl;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.implementation.ProcessImpl;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileListImpl;
import EDU.cmu.Atlas.owls1_1.service.OWLSServiceModel;
import EDU.cmu.Atlas.owls1_1.service.Service;
import EDU.cmu.Atlas.owls1_1.service.ServiceGroundingList;
import EDU.cmu.Atlas.owls1_1.service.ServiceList;
import EDU.cmu.Atlas.owls1_1.service.ServiceModel;
import EDU.cmu.Atlas.owls1_1.service.implementation.ServiceImpl;
import EDU.cmu.Atlas.owls1_1.service.implementation.ServiceListImpl;
import EDU.pku.edu.ly.temporaryowls.PointOfReturn;
import EDU.pku.edu.ly.temporaryowls.QoS;
import EDU.pku.ly.Grounding.GroundingMain;
import EDU.pku.ly.Grounding.Implementation.GroundingMainImpl;
import EDU.pku.ly.Grounding.util.GroundingResource;
import EDU.pku.ly.Process.ProcessMain;
import EDU.pku.ly.Process.Implementation.ProcessMainImpl;
import EDU.pku.ly.Profile.ProfileMain;
import EDU.pku.ly.Profile.Implementation.ProfileMainImpl;
import EDU.pku.ly.owlsservice.util.ServiceResource;
import EDU.pku.ly.owlsservice.ExtendedService;
import EDU.pku.ly.owlsservice.OwlsServiceMain;

import EDU.pku.ly.owlsservice.util.OwlsServiceSql;

public class OwlsServiceMainImpl implements OwlsServiceMain {

	public List<ExtendedService> OwlsServiceInquiryEntry() {
		// TODO Auto-generated method stub
		
		List<ExtendedService> extendedService_lst = new ArrayList<ExtendedService>();
		
		String sql = OwlsServiceSql.sql_query_all_owlsservice;
		Object[] params = new Object[]{};
		ResultSet rs_owlsservice = SQLHelper.ExecuteQueryRtnSet(sql);
		
		ProcessMain processmain = new ProcessMainImpl();
		ProfileMain profilemain = new ProfileMainImpl();
		GroundingMain groundingmain = new GroundingMainImpl();
		
		try {
			ExtendedService extendedService = null;	
			String uri = "";
			String version = "";
			
			ServiceList service_lst = null;
			Service service = null;
			
			ProfileList profile_lst = null;
			ServiceModel serviceModel = null;
			WsdlGroundingList grounding_lst = null; 
			while(rs_owlsservice.next())
			{
				int owlsservice_id = Integer.parseInt(rs_owlsservice.getString("id"));
				uri = rs_owlsservice.getString("uri");
				version = rs_owlsservice.getString("version");
				
				String context_rule = rs_owlsservice.getString("context_rule");
				String qos = rs_owlsservice.getString("qos");
				
				extendedService = new ExtendedServiceImpl();
				extendedService.setServiceKey((uri.endsWith("#") ? uri.replace('#', '_') : (uri + "_")) + "v" + version);
				
				service_lst = new ServiceListImpl();
				
				sql = OwlsServiceSql.sql_query_service;
				params = new Object[]{ owlsservice_id };
				ResultSet rs_service = SQLHelper.ExecuteQueryRtnSet(sql, params);
				while(rs_service.next())
				{
					int service_id = Integer.parseInt(rs_service.getString("id"));
					
					service = new ServiceImpl();
					
					//process
					serviceModel = processmain.ProcessInquiryEntry(service_id);
					service.setDescribes(serviceModel);
					
					//profilelist
					profile_lst = profilemain.ProfileInquiryEntry(service_id);
					service.setPresents(profile_lst);
					
					grounding_lst = groundingmain.GroundingInquiryEntry(service_id);
					service.setSupports(grounding_lst);
					
					service_lst.addService(service);
				}
				extendedService.setServiceList(service_lst);
				
				extendedService.setContextRule(context_rule);
				extendedService.setQoS(GetQOS(qos));
				
				extendedService_lst.add(extendedService);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return extendedService_lst;
	}

	private List<QoS> GetQOS(String qos_str) {
		// TODO Auto-generated method stub
		
		if(qos_str == null || qos_str == "")
			return new ArrayList<QoS>();
		
    	String[] res_arr = qos_str.split("\\^");
    	List<QoS> qos_lst = new ArrayList<QoS>();
    	QoS qos = null;
    	
    	PointOfReturn pointOfReturn = null;
    	for(int i = 0; i < res_arr.length; i++)
    	{
    		qos = new QoS();
    		
    		String refine = res_arr[i];
    		if(refine.startsWith("("))
    			refine = refine.substring(1);
    		if(refine.endsWith(")"))
    			refine = refine.substring(0, refine.length() - 1);
    		
    		String[] refine_arr = refine.split(",");
    		
    		for(int j = 0; j < refine_arr.length; j++)
    		{
    			String tmp = refine_arr[j].trim();
    			if(tmp.startsWith("Name"))
    			{
    				String name = tmp.split(":")[1].trim();
    				qos.Name = name;
    			}
    			else if(tmp.startsWith("Weight"))
    			{
    				String weight = tmp.split(":")[1].trim();
    				qos.weight = weight;
    			}
    			else if(tmp.startsWith("Type"))
    			{
    				String type = tmp.split(":")[1].trim();
    				qos.Type = type;
    			}
    			else if(tmp.startsWith("PointOfSupply"))
    			{
    				String pointOfSupply = tmp.split(":")[1].trim();
    				qos.PointOfSupply = pointOfSupply;
    			}
    			else if(tmp.startsWith("PointOfValuation"))
    			{
    				String pointOfValuation = tmp.split(":")[1].trim();
    				qos.PointOfValuation = pointOfValuation;
    			}
    			else if(tmp.startsWith("PointOfReturn"))
    			{
    				pointOfReturn = new PointOfReturn();
    				
    				int startidx = tmp.indexOf("<");
    				int endidx = tmp.indexOf(">");
    				String pointOfReturnStr = tmp.substring(startidx + 1, endidx);
    				
    				String[] pointOfReturn_Arr = pointOfReturnStr.split(";");

    				for(int k = 0; k < pointOfReturn_Arr.length; k++)
    				{
    					String tmp_pointOfReturn = pointOfReturn_Arr[k].trim();
    					if(tmp_pointOfReturn.startsWith("hasTotalPoint"))
    					{
    						String hasTotalPoint = tmp_pointOfReturn.split(":")[1].trim();
    						pointOfReturn.hasTotalPoint = hasTotalPoint;
    					}
    					else if(tmp_pointOfReturn.startsWith("hasTimes"))
    					{
    						String hasTimes = tmp_pointOfReturn.split(":")[1].trim();
    						pointOfReturn.hasTimes = hasTimes;
    					}
    					else {
    						String hasCurrentPoint = tmp_pointOfReturn.split(":")[1].trim();
    						pointOfReturn.hasCurrentPoint = hasCurrentPoint;
						}
    				}
    				qos.pointOfReturn = pointOfReturn;
				}
    		}
    		qos_lst.add(qos);
    	}
		
		return qos_lst;
	}

	public void OwlsServicePublishEntry(String service_uri) {
		// TODO Auto-generated method stub
		
		//get version number
		String new_version = DomOpe.GetVersionNum(service_uri);
		String default_ns = DomOpe.GetDefaultNS(service_uri);
		
		//check if the service above has been registered already
		String sql = OwlsServiceSql.sql_query_owlsservice;
		Object[] params = new Object[]{ service_uri };
		
		ResultSet rs_owlsservice = SQLHelper.ExecuteQueryRtnSet(sql, params);
		
		ProcessMain processmain = new ProcessMainImpl();
		ProfileMain profilemain = new ProfileMainImpl();
		GroundingMain groundingmain = new GroundingMainImpl();
		
		try {
			if(rs_owlsservice.next())
			{
				String version = rs_owlsservice.getString("version");
				if(new_version != version)
				{
					//get old owlsservice id
					int old_servicemodel_id = Integer.parseInt(rs_owlsservice.getString("id"));
					
					//delete old owlsservice
					sql = OwlsServiceSql.sql_delete_owlsservice;
					params = new Object[]{ old_servicemodel_id };
					SQLHelper.ExecuteNoneQuery(sql, params);
					
					//iterate to delete service
					sql = OwlsServiceSql.sql_query_service;
					params = new Object[]{ old_servicemodel_id };
					ResultSet rs_service = SQLHelper.ExecuteQueryRtnSet(sql, params);
					while(rs_service.next())
					{
						int old_service_id = Integer.parseInt(rs_service.getString("id"));
						
						//delete service
						DeleteServiceById(old_service_id);
						
						//delete process, profile and grounding
						DeleteProcessById(processmain, old_service_id);
						DeleteProfileById(profilemain, old_service_id);
						DeleteGroundingById(groundingmain, old_service_id);
					}
					
					//publish new owlsservcie
					PublishNewServiceModel(processmain, profilemain, groundingmain, 
							service_uri, default_ns, new_version);
				}
				
				return;
			}
			
			PublishNewServiceModel(processmain, profilemain, groundingmain, 
					service_uri, default_ns, new_version);
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void PublishNewServiceModel(ProcessMain processmain, ProfileMain profilemain, 
			GroundingMain groundingmain, String service_uri, String default_ns, String new_version) {

		//new owlsservice
		String sql = OwlsServiceSql.sql_insert_owlsservice;
		Object[] params = new Object[]{ 0, default_ns, new_version };
		ExecuteInsertOpe(sql, params);	
		
		int new_servicemodel_id = SQLHelper.GetLastInsertID();
		
		OWLSServiceModel model = ServiceResource.GetOWLSServiceModel(service_uri);
		ServiceList services = model.getServiceList();

		Service service = null;
		String uri = "";
		for(int i = 0; i < services.size(); i++)
		{
			service = (ServiceImpl)services.getNthService(i);
			uri = service.getURI();
			
			//new service
			sql = OwlsServiceSql.sql_insert_service;
			params = new Object[]{ 0, uri, new_servicemodel_id, "" };
			ExecuteInsertOpe(sql, params);
			
			int new_service_id = SQLHelper.GetLastInsertID();
			
			//publish profile, process and grounding
			ProcessPublishOpe(processmain, service, new_service_id);
			ProfilePublishOpe(profilemain, service, new_service_id);
			GroundingPublishOpe(groundingmain, service, new_service_id);
		}
	}

	private void DeleteProfileById(ProfileMain profilemain, int service_id) {
		
		profilemain.ProfileDeleteEntry(service_id);
	}

	private void ProfilePublishOpe(ProfileMain profilemain, Service service, int service_id) {

		ProfileList profile_lst = (ProfileList)service.getPresents();
		for(int i = 0; i < profile_lst.size(); i++)
		{
			Profile profile = profile_lst.getNthProfile(i);
			profilemain.ProfilePublishEntry(profile, service_id);
		}
	}

	private void ProcessPublishOpe(ProcessMain processmain, Service service, int service_id) {

		Process process = (ProcessImpl)service.getDescribes();
		processmain.ProcessPublishEntry(process, service_id);
	}

	private void GroundingPublishOpe(GroundingMain groundingmain, Service service, int service_id) {
		// TODO Auto-generated method stub
		
		String filepath = "C:\\luoshan\\SourceCode\\juddi\\running\\juddi-portal-bundle-3.0.2\\webapps\\juddiv3\\owl-s\\1.1\\BravoAirService.owl";
		
		OWLSGroundingModel groundingmode = GroundingResource.GetOWLSGroundingModel(filepath);
		
		WsdlGroundingList grounding_lst = groundingmode.getWsdlGroundingList();
		
		//WsdlGroundingList grounding_lst = (WsdlGroundingListImpl) service.getSupports();
		for(int i = 0; i < grounding_lst.size(); i++)
		{
			WsdlGrounding grounding = grounding_lst.getNthWsdlGrounding(i);
			groundingmain.GroundingPublishEntry(grounding, service_id);
		}
	}

	private void DeleteGroundingById(GroundingMain groundingmain, int service_id) {
		// TODO Auto-generated method stub
		
		groundingmain.GroundingDeleteEntry(service_id);
	}

	private void DeleteProcessById(ProcessMain processmain, int service_id) {
		// TODO Auto-generated method stub
		
		processmain.ProcessDeleteEntry(service_id);
	}

	private void DeleteServiceById(int service_id) {
		// TODO Auto-generated method stub
		
		String sql_delete_by_id = OwlsServiceSql.sql_delete_service_by_id;
		Object[] params = new Object[]{ service_id };
		try {
			SQLHelper.ExecuteNoneQuery(sql_delete_by_id, params);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void ExecuteInsertOpe(String sql, Object[] params) {
		// TODO Auto-generated method stub
		try {
			SQLHelper.ExecuteNoneQuery(sql, params);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
