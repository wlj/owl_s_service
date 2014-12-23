//package EDU.pku.ly.owlsservice.implementation;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStreamReader;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityTransaction;
//
//import org.apache.juddi.api.impl.UDDIInquiryImpl;
//import org.apache.juddi.config.PersistenceManager;
//import org.uddi.api_v3.AuthToken;
//import org.uddi.api_v3.BusinessDetail;
//import org.uddi.api_v3.BusinessEntity;
//import org.uddi.api_v3.GetAuthToken;
//import org.uddi.api_v3.GetBusinessDetail;
//import org.uddi.v3_service.DispositionReportFaultMessage;
//
//import com.hp.hpl.jena.ontology.OntClass;
//import com.hp.hpl.jena.ontology.OntModel;
//import com.hp.hpl.jena.util.iterator.ExtendedIterator;
//
//import edu.pku.sj.rscasd.utils.RSCASDConfigConstants;
//import edu.pku.sj.rscasd.utils.RSCASDConfigProperties;
//import edu.pku.sj.rscasd.utils.UDDISecurityUtil;
//import edu.pku.sj.rscasd.utils.encoding.BASE64Util;
//import EDU.pku.ly.owlsservice.ServiceRestore;
//
//import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfServiceException;
//import EDU.cmu.Atlas.owls1_1.parser.OWLSServiceParser;
//import EDU.cmu.Atlas.owls1_1.service.OWLSServiceModel;
//import EDU.cmu.Atlas.owls1_1.service.Service;
//import EDU.cmu.Atlas.owls1_1.service.ServiceList;
//import EDU.cmu.Atlas.owls1_1.service.ServiceProfileList;
//import EDU.cmu.Atlas.owls1_1.service.implementation.ServiceImpl;
//import edu.pku.ly.SqlOpe.SQLHelper;
//import EDU.pku.sj.rscasd.owls1_1.utils.OntModelRepository;
//
//public class ServiceRestoreImpl implements ServiceRestore {
//
//	public ServiceList GetAllService() {
//		
//		ServiceList servicelist = null;
//		
//		Service service = new ServiceImpl();
//		
//		ServiceProfileList profile_lst = GetAllProfile();
//		for(int i = 0; i < profile_lst.size(); i++)
//		{
//			service.getPresents().add(profile_lst.getNthServiceProfile(i));
//		}
//		
//		return servicelist;
//	}
//	
//	private ServiceProfileList GetAllProfile() {
//		// TODO Auto-generated method stub
//		
//		EntityManager em = PersistenceManager.getEntityManager();
//		EntityTransaction tx = em.getTransaction();
//		a
//		GetBusinessDetail detail = new GetBusinessDetail();
//		
//		GetAuthToken getAuthToken = new GetAuthToken();
//		getAuthToken.setUserID(RSCASDConfigProperties.get(RSCASDConfigConstants.RSCASD_AUTH_ROOT_USER));
//		getAuthToken.setCred(RSCASDConfigProperties.get(RSCASDConfigConstants.RSCASD_AUTH_ROOT_USER));			
//		
//		AuthToken authToken = null;
//		try {
//			authToken = UDDISecurityUtil.getAuthToken(em, getAuthToken);
//		} catch (DispositionReportFaultMessage e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		detail.setAuthInfo(authToken.getAuthInfo());		
//		detail.getBusinessKey().addAll(GetAllBusinessKey());
//		
//		UDDIInquiryImpl inquiry = new UDDIInquiryImpl();
//		BusinessDetail businessdetail = null;
//		try {
//			businessdetail = inquiry.getBusinessDetail(detail);
//		} catch (DispositionReportFaultMessage e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		ServiceProfileList profile_list = UDDIToOWLSProfileList(businessdetail);
//		
//		return profile_list;
//	}
//
//	private ServiceProfileList UDDIToOWLSProfileList(BusinessDetail businessdetail) {
//		// TODO Auto-generated method stub
//		
//		List<String> business_names = new ArrayList<String>();
//		List<BusinessEntity> businessEntities = businessdetail.getBusinessEntity();
//		List<BusinessEntity> refined_business_entities = new ArrayList<BusinessEntity>();
//		
//		for(int i = 0; i < businessEntities.size(); i++)
//		{
//			String entity_name = businessEntities.get(i).getName().get(0).getValue();
//			if(!business_names.contains(entity_name))
//			{
//				refined_business_entities.add(businessEntities.get(i));
//				
//				business_names.add(entity_name);
//			}
//		}
//		
//		return null;
//	}
//
//	private List<String> GetAllBusinessKey() {
//		// TODO Auto-generated method stub
//		
//		List<String> res = new ArrayList<String>();
//		
//		String sql = "select * from j3_business_entity;"; 
//			
//		ResultSet set = SQLHelper.ExecuteQueryRtnSet(sql);
//		try 
//		{
//			while(set.next())
//			{
//				res.add(set.getString("entity_key"));
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return res;
//	}
//
//	public static void main(String[] args)
//	{
//		/*ServiceRestoreImpl instance = new ServiceRestoreImpl();
//		instance.GetAllService();*/
//	}
//	
//	public static void testgetmodel()
//	{
//		String originalUrl = "C:\\luoshan\\SourceCode\\juddi\\running\\juddi-portal-bundle-3.0.2\\webapps\\juddiv3\\owl-s\\1.1\\BravoAirService.owl";
//		
//		OWLSServiceModel model = null;
//		
//		String encodedFileContent = BASE64Util.encodeFile(originalUrl);
//		String decodedPlainOwls = BASE64Util.decodePlainText(encodedFileContent);
//		
//		OWLSServiceParser owlsServiceParser = new OWLSServiceParser(OntModelRepository.getNewOntModel());
//		java.io.InputStream sbis = new ByteArrayInputStream(decodedPlainOwls.getBytes());
//		InputStreamReader reader = new InputStreamReader(sbis);
//		try 
//		{
//			model = owlsServiceParser.read(reader);
//		} catch (NotInstanceOfServiceException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		OntModel ont = model.getOntModel();
//		Map<String, String> map = ont.getNsPrefixMap();
//		//Iterator<String> ite = map.keySet().iterator();
//		for(Iterator<String> ite = map.keySet().iterator(); ite.hasNext();)
//		{
//			String item = ite.next();
//			System.out.println(item + "=" + map.get(item));
//		}
//		
//		System.out.println("---------------------------------------------------------");
//		
//		ExtendedIterator<OntClass> lstclass = ont.listClasses();
//		while(lstclass.hasNext())
//		{
//			System.out.println("*******");
//			OntClass cla = lstclass.next();
//			System.out.println("NameSpace:" + cla.getNameSpace());
//			System.out.println("URI:" + cla.getURI());
//			System.out.println("LocalName:" + cla.getLocalName());
//			System.out.println("toString" + cla.toString());
//			//System.out.println("URI" + cla.getId());
//			//System.out.println("OntModel.toString" + cla.getOntModel().toString());
//			//System.out.println("Model.toString:" + cla.getModel().toString());
//			System.out.println("Profile.NAMESPACE:" + cla.getProfile().NAMESPACE());
//			System.out.println("RDFType:" + cla.getRDFType().toString());
//		}
//	}
//}
