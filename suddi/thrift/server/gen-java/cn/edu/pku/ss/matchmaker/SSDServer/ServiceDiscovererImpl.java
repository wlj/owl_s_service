package cn.edu.pku.ss.matchmaker.SSDServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;

import EDU.cmu.Atlas.owls1_1.expression.Condition;
import EDU.cmu.Atlas.owls1_1.expression.Expression;
import EDU.cmu.Atlas.owls1_1.expression.implementation.ConditionImpl;
import EDU.cmu.Atlas.owls1_1.expression.implementation.ExpressionImpl;
import EDU.cmu.Atlas.owls1_1.process.EffectList;
import EDU.cmu.Atlas.owls1_1.process.Input;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.Output;
import EDU.cmu.Atlas.owls1_1.process.OutputList;
import EDU.cmu.Atlas.owls1_1.process.PreConditionList;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.Result;
import EDU.cmu.Atlas.owls1_1.process.ResultList;
import EDU.cmu.Atlas.owls1_1.process.implementation.EffectListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.InputImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.InputListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.OutputImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.OutputListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.PreConditionListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ResultImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ResultListImpl;
import EDU.cmu.Atlas.owls1_1.profile.ActorsList;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategoriesList;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategory;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ActorImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ActorsListImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileListImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ServiceCategoriesListImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ServiceCategoryImpl;
import EDU.cmu.Atlas.owls1_1.service.ServiceGrounding;
import EDU.pku.edu.ly.temporaryowls.QoS;
import EDU.pku.ly.owlsservice.ExtendedService;
import EDU.pku.ly.owlsservice.OwlsServiceMain;
import EDU.pku.ly.owlsservice.implementation.ExtendedServiceImpl;
import EDU.pku.ly.owlsservice.implementation.OwlsServiceMainImpl;

import cn.edu.pku.ss.matchmaker.adapter.OWLSQueryData;
import cn.edu.pku.ss.matchmaker.adapter.ProfileAdapter;
import cn.edu.pku.ss.matchmaker.index.Indexer;
import cn.edu.pku.ss.matchmaker.index.impl.IndexerImpl;
import cn.edu.pku.ss.matchmaker.index.impl.ServiceBody;
import cn.edu.pku.ss.matchmaker.query.QueryManager;
import cn.edu.pku.ss.matchmaker.query.impl.QueryManagerImpl;
import cn.edu.pku.ss.matchmaker.reasoning.SemanticReasoner;
import cn.edu.pku.ss.matchmaker.reasoning.impl.SemanticReasonerImpl;
import cn.edu.pku.ss.matchmaker.thrift.Actor;
import cn.edu.pku.ss.matchmaker.thrift.Category;
import cn.edu.pku.ss.matchmaker.thrift.IOModel;
import cn.edu.pku.ss.matchmaker.thrift.PECRModel;
import cn.edu.pku.ss.matchmaker.thrift.ProfileInfo;
import cn.edu.pku.ss.matchmaker.thrift.RequestInfo;
import cn.edu.pku.ss.matchmaker.thrift.ResponseInfo;
import cn.edu.pku.ss.matchmaker.thrift.ReturnPoint;
import cn.edu.pku.ss.matchmaker.thrift.TaskInfo;
import cn.edu.pku.ss.matchmaker.thrift.ServiceDiscoverer.Iface;
import cn.edu.pku.ss.matchmaker.util.ServiceInfoExtractor;

public class ServiceDiscovererImpl implements Iface {
	private final int FAIL_REQUEST_INFO = 1;
	private final int FAIL_QUERY_RESULT = 2;
	private final int SUCCESS_QUERY = 0;
	
	private QueryManager manager;
	private Logger logger;
	
	public ServiceDiscovererImpl(String ontologiesFileList) {
		// TODO Auto-generated constructor stub
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.SSDServer.ServiceDiscovererImpl.class);
		
		if (init(ontologiesFileList) == false) {
			logger.error("failed to init server");
			return ;
		}
		logger.info("successed to init");
	}
	
	private boolean init(String ontologiesFileList) {
		SemanticReasoner reasoner = new SemanticReasonerImpl();
		if (reasoner.loadontologies(ontologiesFileList) == false) {
			logger.debug("failed to load ontologies file");
			return false;
		}
		
//	
//		List<ExtendedService> serviceList = new ArrayList<ExtendedService>();
//		String owlsPath = "http://localhost:8080/juddiv3/owl-s/1.1/BravoAirProcess.owl";
//		ExtendedService service;
//		
//		
//		if ((service = ServiceInfoExtractor.getProcess(owlsPath, "11111111", "http://localhost:8080/juddiv3/owl-s/1.1/BravoAirProcess.owl#CompositeProcess_0")) == null) {
//			logger.debug("failed to get profile and process");
//			return false;
//		}
//
//		service.SetServiceKey("111111111");
//		Profile profile = new ProfileImpl();
//		profile.setServiceName("Book Flight ticket");
//		profile.setTextDescription("BuyAirTicketService");
//		
//		InputList inputList = new InputListImpl();
//		Input input = new InputImpl();
//		input.setParameterType("input");
//		inputList.addInput(input);
//		profile.setInputList(inputList);
//		
//		OutputList outputList = new OutputListImpl();
//		Output output = new OutputImpl();
//		output.setParameterType("output");
//		outputList.addOutput(output);
//		profile.setOutputList(outputList);
//		
//		ProfileList profileList = new ProfileListImpl();
//		profileList.addProfile(profile);
//		service.SetServiceProfileList(profileList);
//		serviceList.add(service);
		
		
		OwlsServiceMain owlsservicemain = new OwlsServiceMainImpl();
		List<ExtendedService> serviceList = owlsservicemain.OwlsServiceInquiryEntry();
		Indexer indexer = new IndexerImpl(reasoner);
		indexer.setExtendedServiceList(serviceList);
		if (indexer.process() == false) {
			logger.debug("failed to process service info");
			return false;
		}
		
		manager = new QueryManagerImpl(indexer);
		if (manager == null) {
			logger.error("failed to create QueryManager object");
			return false;
		}
		
		return true;
	}
	
	
	// 1: request info is null
	// 2: failed to get query result
	// 0: succeed
	public ResponseInfo getServices(RequestInfo requestInfo)
			throws TException {
		// TODO Auto-generated method stub
		System.out.println("request info----:" + requestInfo);
		ResponseInfo responseInfo = new ResponseInfo();
		if (requestInfo == null) {
			logger.error("client request info is null");
			responseInfo.setStatusIsSet(true);
			responseInfo.setStatus(FAIL_REQUEST_INFO); // set status = 1
			return responseInfo;
		}
		
		List<TaskInfo> taskInfoList = requestInfo.getTaskInfoList();
		if (taskInfoList == null) {
			logger.error("client task info is null");
			responseInfo.setStatusIsSet(true);
			responseInfo.setStatus(FAIL_REQUEST_INFO);
			return responseInfo;
		}
		
		Map<String, List<ProfileInfo>> resultsMap = new HashMap<String, List<ProfileInfo>>();
		for (int j = 0; j < taskInfoList.size(); ++j) {			
			TaskInfo taskInfo = taskInfoList.get(j);
			if (taskInfo == null)
				continue;
			
			// generate query profile according client request info
			// from request task info
			Profile profile = null;
			Process process = null;
			if (taskInfo.isSetProfileInfo() == true) {
				profile = getProfile(taskInfo.getProfileInfo());
				if (profile == null)
					logger.warn("failed to get query profile from request task info");
			}
			
			// or from request owls content
			if (taskInfo.isSetOwlsContent() == true) {
				OWLSQueryData owlsQueryData = null;
				logger.info("query content: " + taskInfo.getOwlsContent());
				if ((owlsQueryData = ProfileAdapter.getProfileAndProcess(taskInfo.getOwlsContent())) == null) {
					logger.error("failed to get profile and process");
				}
				
				if (owlsQueryData.getProfile() != null && profile == null)
					profile = owlsQueryData.getProfile();
				
				process = owlsQueryData.getProcess();
				if (process == null)
					logger.warn("failed to get query process from request owls content");
				
			}
			
			if (profile == null && process == null)
				continue;
			
			System.out.println("process: " + process);
			System.out.println("profile: " + profile);
			Entry<String, ServiceBody>[] results = manager.getServices(profile, process);
			if (results == null) {
				logger.error("failed to get service which you queried");
				continue;
			}
			
			logger.info("**************result info*************");
			String taskID = taskInfo.getTaskID();
			List<ProfileInfo> profileInfoList = new ArrayList<ProfileInfo>();
			for (int k = 0; k < results.length; ++k) {
				Entry<String, ServiceBody> serviceEntry = results[k];
				if (serviceEntry == null) {
					logger.warn("service entry is null");
					continue;
				}
				
				ServiceBody serviceBody = serviceEntry.getValue();
				ProfileInfo profileInfo = getProfileInfo(serviceBody);
				if (profileInfo == null) {
					logger.warn("failed to get query result");
					continue;
				}
				logger.info("profileInfo out\n: " + profileInfo);
				profileInfoList.add(profileInfo);
			}
			
			if (resultsMap.containsKey(taskID)) {
				List<ProfileInfo> profileInfoResult = resultsMap.get(taskID);
				if (profileInfoResult != null)
					profileInfoResult.addAll(profileInfoList);
				else 
					profileInfoResult = profileInfoList;
			} else {
				resultsMap.put(taskID, profileInfoList);
			}
		}
		
		if (resultsMap == null) {
			responseInfo.setStatusIsSet(true);
			responseInfo.setStatus(FAIL_QUERY_RESULT);
			return responseInfo;
		}
		
		responseInfo.setStatusIsSet(true);
		responseInfo.setStatus(SUCCESS_QUERY);
		responseInfo.setTaskServices(resultsMap);
			
		return responseInfo;
	}
	

	
	private  Profile getProfile(ProfileInfo profileInfo) {
		if (profileInfo == null)
			return null;
		
		Profile profile = new ProfileImpl();
		
		// service name;
		profile.setServiceName(profileInfo.getServiceName());
		
		// service description
		profile.setTextDescription(profileInfo.getDescription());
		
		// service input
		InputList inputList = getInputList(profileInfo.getInputList());
		if (inputList != null)
			profile.setInputList(inputList);
		
		// service output
		OutputList outputList = getOutputList(profileInfo.getOutputList());
		if (outputList != null)
			profile.setOutputList(outputList);
		
		// service precondition
	    PreConditionList preconditionList = getPreConditionList(profileInfo.getPreconditionList());
	    if (preconditionList != null)
	    	profile.setPreconditionList(preconditionList);
	    
	    // service effect
	    EffectList effectList = geteffEffectList(profileInfo.getEffectList());
	    if (effectList != null) {
	    	ResultList resultList = new ResultListImpl();
	    	Result result = new ResultImpl();
	    	result.setHasEffects(effectList);
	    	
	    	resultList.addResult(result);
	    	
	    	profile.setResultList(resultList);
	    }
	    
	    // service actor
	    ActorsList actorsList = getActorsList(profileInfo.getActorList());
	    if (actorsList != null)
	    	profile.setContactInformation(actorsList);
	    
	    // service category
	    ServiceCategoriesList serviceCategoriesList = getServiceCategoriesList(profileInfo.getCategoryList());
	    if (serviceCategoriesList != null)
	    	profile.setServiceCategory(serviceCategoriesList);
	    
		return profile;
	}
	
	private  InputList getInputList(List<IOModel> inputs) {
		if (inputs == null)
			return null;
		
		InputList inputList = new InputListImpl();
		for (int i = 0; i < inputs.size(); ++i) {
			IOModel ioModel = inputs.get(i);
			if (ioModel == null)
				continue;
			
			Input input = new InputImpl();		
			input.setParameterType(ioModel.getParameterType());
			input.setParameterValue(ioModel.getParameterValue());
			inputList.addInput(input);
		}

		return inputList;
	}
	
	
	private OutputList getOutputList(List<IOModel> outputs) {
		if (outputs == null)
			return null;
		
		OutputList outputList = new OutputListImpl();
		for (int i = 0; i < outputs.size(); ++i) {
			IOModel ioModel = outputs.get(i);
			if (ioModel == null)
				continue;
			
			Output output = new OutputImpl();			
			output.setParameterType(ioModel.getParameterType());
			output.setParameterValue(ioModel.getParameterValue());
			outputList.addOutput(output);
		}

		return outputList;
	}
	
	private PreConditionList getPreConditionList(List<PECRModel> precondList) {
		if (precondList == null) {
		    logger.warn("profile precondition list is null");
			return null;
		}
		
		PreConditionList preConditionList = new PreConditionListImpl();

		for (int i = 0; i < precondList.size(); ++i) {
			PECRModel precondition = precondList.get(i);
			if (precondition == null)
				continue;
			
			Condition condition = new ConditionImpl();
			condition.setExpressionBody(precondition.getExpressionBody());
			preConditionList.addPreCondition(condition);
		}
		
		return preConditionList;
	}
	
	
	private EffectList geteffEffectList(List<PECRModel> effList) {
		if (effList == null) {
		    logger.warn("profile effect list is null");
			return null;
		}
		
		EffectList effectList = new EffectListImpl();
		for (int i = 0; i < effList.size(); ++i) {
			PECRModel effect = effList.get(i);
			if (effect == null)
				continue;
			
			Expression expression = new ExpressionImpl();
			expression.setExpressionBody(effect.getExpressionBody());
			effectList.addExpression(expression);
		}
		
		return effectList;
	}
	
	private ActorsList getActorsList(List<Actor> actors) {
		if (actors == null) {
			logger.warn("profile actor list is null");
			return null;
		}
		
		ActorsList actorsList = new ActorsListImpl();
		for (int i = 0; i < actors.size(); ++i) {
			Actor actor = actors.get(i);
			if (actor == null)
				continue;
			
			EDU.cmu.Atlas.owls1_1.profile.Actor profileActor = new ActorImpl();
			profileActor.setName(actor.getName());
			profileActor.setAddress(actor.getPhysicalAddress());
			profileActor.setEmail(actor.getEmail());
			profileActor.setFax(actor.getFax());
			profileActor.setPhone(actor.getPhone());
			profileActor.setTitle(actor.getTitle());
			profileActor.setWebURL(actor.getWebURI());
			actorsList.addActor(profileActor);
		}
		
		return actorsList;
	}
	
	private ServiceCategoriesList getServiceCategoriesList(List<Category> categories) {
		if (categories == null) {
			logger.warn("profile category list is null");
			return null;
		}
		
		ServiceCategoriesList serviceCategoriesList = new ServiceCategoriesListImpl();
		for (int i = 0; i < categories.size(); ++i) {
			Category category = categories.get(i);
			if (category == null) 
				continue;
			
			ServiceCategory serviceCategory = new ServiceCategoryImpl();
			serviceCategory.setCategoryName(category.getCategoryName());
			serviceCategory.setCode(category.getCode());
			serviceCategory.setTaxonomy(category.getTaxonomy());
			serviceCategory.setValue(category.getValue());
			
			serviceCategoriesList.addServiceCategory(serviceCategory);
		}
		
		return serviceCategoriesList;
	}
	
	
	// fill info
	private ProfileInfo getProfileInfo(ServiceBody serviceBody) {
		if (serviceBody == null)
			return null;
		
		ProfileInfo profileInfo = new ProfileInfo();
		
		// service key
		profileInfo.setServiceKey(serviceBody.getServiceKey());
		
		Profile profile = serviceBody.getProfile();
		if (profile == null)
			return null;
		
		// service name
		profileInfo.setServiceName(serviceBody.getProfile().getServiceName());
		
		// service description
		profileInfo.setDescription(profile.getTextDescription());
		
		// input
		profileInfo.setInputList(getInputList(profile.getInputList()));
		
		// output
		profileInfo.setOutputList(getOutputList(profile.getOutputList()));
		
		// precontion
		profileInfo.setPreconditionList(getPreconditionList(profile.getPreconditionList()));
		
		// effect
		ResultList resultList = profile.getResultList();
		if (resultList != null) {
			Result result = resultList.getNthResult(0);
			if (result != null) {
				EffectList effectList =result.getHasEffects();
				profileInfo.setEffectList(getEffectList(effectList));
			}
		}
		
		// TODO
		// context, rule and QoS
		// rule
		profileInfo.setRule(getContextRuleList(Collections.singletonList(serviceBody.getContextRule())));
		
		// qos
		profileInfo.setQosList(getQoSList(serviceBody.getQosList()));
		
		// actor
		profileInfo.setActorList(getActorList(profile.getContactInformation()));
		
		// category
		profileInfo.setCategoryList(getCategoryList(profile.getServiceCategory()));
		
		// profile name
		profileInfo.setProfileName(profile.getURI());
		
		// process name
		Process process = serviceBody.getProcess();
		if (process != null)
			profileInfo.setProcessName(process.getName());
		
		ServiceGrounding serviceGrounding = serviceBody.getGrounding();
		if (serviceGrounding != null)
			profileInfo.setGroundingName(serviceGrounding.getURI());
		
		profileInfo.setWsdlURI(serviceBody.getWsdlURI());
		
		return profileInfo;
	}
	
	
	// input list
	private List<IOModel> getInputList(InputList inputs) {
		if (inputs == null)
			return Collections.emptyList();
		
		List<IOModel> inputList = new ArrayList<IOModel>();
		for (int i = 0; i < inputs.size(); ++i) {
			Input input = inputs.getNthInput(i);
			if (input == null)
				continue;
			
			IOModel iModel = new IOModel();
			iModel.setParameterType(input.getParameterType());
			iModel.setParameterValue(input.getParameterValue());
			iModel.setURI(input.getURI());
			
			inputList.add(iModel);
		}
		
		return inputList;
	}
	
	// output list
	private List<IOModel> getOutputList(OutputList outputs) {
		if (outputs == null)
			return Collections.emptyList();
		
		List<IOModel> outputList = new ArrayList<IOModel>();;
		for (int i = 0; i < outputs.size(); ++i) {
			Output output = outputs.getNthOutput(i);
			if (output == null)
				continue;
			
			IOModel oModel = new IOModel();
			oModel.setParameterType(output.getParameterType());
			oModel.setParameterValue(output.getParameterValue());
			oModel.setURI(output.getURI());
			
			outputList.add(oModel);
		}
		
		return outputList;
	}
	
	// precondition list
	private List<PECRModel> getPreconditionList(PreConditionList precondList) {
		if (precondList == null)
			return Collections.emptyList();
		
		List<PECRModel> preconditionList = new ArrayList<PECRModel>();
		for (int i = 0; i < precondList.size(); ++i) {
			Condition condition = precondList.getNthPreCondition(i);
			if (condition == null)
				continue;
			
			PECRModel pModel = new PECRModel();
			pModel.setExpressionBody(condition.getExpressionBody());
			pModel.setURI(condition.getURI());
			
			preconditionList.add(pModel);
		}
		
		return preconditionList;
	}
	
	// effect list
	private List<PECRModel> getEffectList(EffectList effList) {
		if (effList == null)
			return Collections.emptyList();
		
		List<PECRModel> effectList = new ArrayList<PECRModel>();
		for (int i = 0; i < effList.size(); ++i) {
			Expression expression = effList.getNthEffect(i);
			if (expression == null)
				continue;
			
			PECRModel eModel = new PECRModel();
			eModel.setExpressionBody(expression.getExpressionBody());
			eModel.setURI(expression.getURI());
			
			effectList.add(eModel);
		}
		
		return effectList;
	}
	
	// rule
	private List<PECRModel> getContextRuleList(List<String> ruleList) {
		if (ruleList == null)
			return Collections.emptyList();
		
		List<PECRModel> contextRuleList = new ArrayList<PECRModel>();
		for (int i = 0; i < ruleList.size(); ++i) {
			String rule = ruleList.get(i);
			if (rule == null)
				continue;
			
			PECRModel ruleModel = new PECRModel();
			ruleModel.setExpressionBody(rule);
			contextRuleList.add(ruleModel);
		}
		
		return contextRuleList; 
	}
	
	// QoS
	private List<cn.edu.pku.ss.matchmaker.thrift.QoS> getQoSList(List<QoS> qoses) {
		if (qoses == null)
			return Collections.emptyList();
		
		List<cn.edu.pku.ss.matchmaker.thrift.QoS> qosList = new ArrayList<cn.edu.pku.ss.matchmaker.thrift.QoS>();
		for (int i = 0; i < qoses.size(); ++i) {
			QoS qos = qoses.get(i);
			if (qos == null)
				continue;
			
			cn.edu.pku.ss.matchmaker.thrift.QoS qosModel = new cn.edu.pku.ss.matchmaker.thrift.QoS();
//			qosModel.setName(qos.Name);
//			if (qos.pointAll != null)
//				qosModel.setPointAll(Double.valueOf(qos.pointAll));
//			qosModel.setPointOfReturn(Double.valueOf(qos.pointReturn));
//			qosModel.setPointOfSupply(Double.valueOf(qos.PointOfSupply));
//			qosModel.setPointOfValuation(Double.valueOf(qos.PointOfValuation));
//			qosModel.setType(Boolean.valueOf(qos.Type));
//			qosModel.setWeight(Double.valueOf(qos.weight));
//			
//			ReturnPoint returnPoint = new ReturnPoint();
//			returnPoint.setCurrentPoint(Double.valueOf(qos.pointOfReturn.hasCurrentPoint));
//			returnPoint.setTimes(Integer.valueOf(qos.pointOfReturn.hasTimes));
//			returnPoint.setTotalPoint(Double.valueOf(qos.pointOfReturn.hasTotalPoint));
//			
//			qosModel.setReturnPoint(returnPoint);
//			
//			qosList.add(qosModel);
		}
		
		return qosList;
	}
	
	// actor
	private List<Actor> getActorList(ActorsList actors) {
		if (actors == null)
			return Collections.emptyList();
		
		List<Actor> actorList = new ArrayList<Actor>();
		for (int i = 0; i < actors.size(); ++i) {
			EDU.cmu.Atlas.owls1_1.profile.Actor actor = actors.getNthActor(i);
			if (actor == null)
				continue;
			
			Actor act = new Actor();
			act.setEmail(actor.getEmail());
			act.setFax(actor.getFax());
			act.setName(actor.getName());
			act.setPhone(actor.getPhone());
			act.setPhysicalAddress(actor.getAddress());
			act.setTitle(actor.getTitle());
			act.setURI(actor.getURI());
			act.setWebURI(actor.getWebURL());
			
			actorList.add(act);
		}
		
		return actorList;	
	}
	
	// category
	private List<Category> getCategoryList(ServiceCategoriesList serviceCategoriesList) {
		if (serviceCategoriesList == null)
			return Collections.emptyList();
		
		List<Category> categoriesList = new ArrayList<Category>();
		for (int i = 0; i < serviceCategoriesList.size(); ++i) {
			ServiceCategory serviceCategory = serviceCategoriesList.getServiceCategoryAt(i);
			if (serviceCategory == null)
				continue;
			
			Category category = new Category();
			category.setCategoryName(serviceCategory.getCategoryName());
			category.setCode(serviceCategory.getCode());
			category.setTaxonomy(serviceCategory.getTaxonomy());
			category.setURI(serviceCategory.getURI());
			category.setValue(serviceCategory.getValue());
			
			categoriesList.add(category);
		}
		
		return categoriesList;
	}
}
