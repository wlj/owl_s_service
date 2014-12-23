package cn.edu.pku.ss.matchmaker.adapter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.expression.Condition;
import EDU.cmu.Atlas.owls1_1.expression.Expression;
import EDU.cmu.Atlas.owls1_1.expression.implementation.ConditionImpl;
import EDU.cmu.Atlas.owls1_1.expression.implementation.ExpressionImpl;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProcessParser;
import EDU.cmu.Atlas.owls1_1.process.EffectList;
import EDU.cmu.Atlas.owls1_1.process.Input;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.OWLSProcessModel;
import EDU.cmu.Atlas.owls1_1.process.Output;
import EDU.cmu.Atlas.owls1_1.process.OutputList;
import EDU.cmu.Atlas.owls1_1.process.PreConditionList;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.ProcessList;
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
import EDU.cmu.Atlas.owls1_1.profile.Actor;
import EDU.cmu.Atlas.owls1_1.profile.ActorsList;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategoriesList;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategory;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ActorImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ActorsListImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ServiceCategoriesListImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ServiceCategoryImpl;

public class ProfileAdapter {
	private static Logger logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.adapter.ProfileAdapter.class);
	private static OWLFileParser parser = new OWLFileParser();
	private static OWLSProcessParser processParser = new OWLSProcessParser();
	private static final String FIRST_PROCESS = "http://localhost:8080/juddiv3/owl-s/1.1/BravoAirProcess.owl#CompositeProcess_0";
	
	public static OWLSQueryData getProfileAndProcess(String owlsContent) {
		if (owlsContent == null) {
			logger.error("client request owls content is null");
			return null;
		}
		
		if (parser.parse(owlsContent) == false) {
			logger.error("failed to parse owls content");
			return null;
		}
		
		
		Profile profile = new ProfileImpl();
//		// service key
//		String serviceKey = parser.getServiceKey();
		
		String serviceName = null;
		List<String> serviceNameList = parser.getSerNames();
		
		if (serviceNameList != null && serviceNameList.size() > 1)
			serviceName = serviceNameList.get(0);
		if (serviceName != null)
			profile.setServiceName(serviceName);
		
		// description
		List<String> descriptionList = parser.getTextDescriptions();
		if (descriptionList != null && descriptionList.size() > 1) 
			profile.setTextDescription(descriptionList.get(0));
		
		// input
		List<IOInfo> inputs = parser.getInputValues();
		if (inputs != null) {
			InputList inputList = new InputListImpl();
			for (int i = 0; i < inputs.size(); ++i) {
				IOInfo inputInfo = inputs.get(i);
				if (inputInfo == null)
					continue;
				
				logger.info("input type: " + inputInfo.getParameterType());
				Input input = new InputImpl();
				input.setParameterType(inputInfo.getParameterType());
				input.setParameterValue(inputInfo.getParameterName());
					
				inputList.addInput(input);
			}
			
			profile.setInputList(inputList);
 		}
		
		
		// output
		List<IOInfo> outputs = parser.getOutputValues();
		logger.info("outputList: " + outputs);
		if (outputs != null) {
			OutputList outputList = new OutputListImpl();
			for (int i = 0; i < outputs.size(); ++i) {
				IOInfo outputInfo = outputs.get(i);
				if (outputInfo == null)
					continue;
				
				logger.info("output type: " + outputInfo.getParameterType());
				Output output = new OutputImpl();
				output.setParameterType(outputInfo.getParameterType());
				output.setParameterValue(outputInfo.getParameterName());
				
				outputList.addOutput(output);
			}
			
			profile.setOutputList(outputList);
		}
		
		// precondition
		List<String> precondList = parser.getPreconditionLists();
		if (precondList != null) {
			PreConditionList preConditionList = new PreConditionListImpl();
			for (int i = 0; i < precondList.size(); ++i) {
				String precondition = precondList.get(i);
				if (precondition == null)
					continue;
				
				Condition condition = new ConditionImpl();
				condition.setExpressionBody(precondition);
				
				preConditionList.addPreCondition(condition);
			}
			profile.setPreconditionList(preConditionList);
		}
		
		
		// effect
		List<String> effects = parser.getEffectLists();
		if (effects != null) {
			ResultList resultList = new ResultListImpl();
			Result result = new ResultImpl();
			EffectList effectList = new EffectListImpl();
			for (int i = 0; i < effects.size(); ++i) {
				String eff = effects.get(i);
				if (eff == null)
					continue;
				
				Expression expression = new ExpressionImpl();
				expression.setExpressionBody(eff);
				
				effectList.addExpression(expression);
			}
			result.setHasEffects(effectList);
			resultList.addResult(result);
			
			profile.setResultList(resultList);
		}
		
		// actor
		List<ActorInfo> actors = parser.getActorLists();
		if (actors != null) {
			ActorsList actorsList = new ActorsListImpl();
			for (int i = 0; i < actors.size(); ++i) {
				ActorInfo actorInfo = actors.get(i);
				if (actorInfo == null)
					continue;
				
				Actor actor = new ActorImpl();
				actor.setAddress(actorInfo.getPhysicalAddress());
				actor.setEmail(actorInfo.getEmail());
				actor.setFax(actorInfo.getFax());
				actor.setName(actorInfo.getName());
				actor.setPhone(actorInfo.getPhone());
				actor.setTitle(actorInfo.getTitle());
				actor.setWebURL(actorInfo.getWebURI());
				
				actorsList.addActor(actor);
			}
			profile.setContactInformation(actorsList);
		}
		
		// category
		List<CategoryInfo> categories = parser.getCategoryLists();
		if (categories != null) {
			ServiceCategoriesList serviceCategoriesList = new ServiceCategoriesListImpl();
			for (int i = 0; i < categories.size(); ++i) {
				CategoryInfo categoryInfo = categories.get(i);
				if (categoryInfo == null)
					continue;
				
				ServiceCategory serviceCategory = new ServiceCategoryImpl();
				serviceCategory.setCategoryName(categoryInfo.getCategoryName());
				serviceCategory.setCode(categoryInfo.getCode());
				serviceCategory.setTaxonomy(categoryInfo.getTaxonomy());
				serviceCategory.setValue(categoryInfo.getValue());
				
				serviceCategoriesList.addServiceCategory(serviceCategory);
			}
			
			profile.setServiceCategory(serviceCategoriesList);
		}
		
		
		// process
		Process process = null;
		String processContent = parser.getProcessContents();
		logger.info(processContent);
		if ((process = getFirstProcess(processContent)) == null)
			logger.error("failed to get owls process");
		
		System.out.println("process1: " + process);
		return new OWLSQueryData(profile, process);
	}
	
	private static Process getFirstProcess(String processContent)  {
		if (processContent == null || processContent.isEmpty()) {
			logger.error("process content is null or empty");
			return null;
		}
		
		InputStream inputStream = new ByteArrayInputStream(processContent.getBytes());
		Reader reader = new InputStreamReader(inputStream);
		OWLSProcessModel processModel = null;
		try {
			processModel = processParser.read(reader);
		} catch (NotInstanceOfProcessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (processModel == null) {
			logger.error("failed to parse owls process");
			return null;
		}
		
		ProcessList processList = processModel.getProcessList();
		
		if (processList == null)
			return null;
		
		Process firstProcess = null;
		for (int i = 0; i < processList.size(); ++i) {
			Process process = processList.getNthProcess(i);
			if (process == null)
				continue;
			
			//System.out.println(process);
			if (process.getName().equals(FIRST_PROCESS)) {
				firstProcess = process;
				break;
			}
		}
		
		return firstProcess;
	}
}
