package cn.edu.pku.ss.matchmaker.index.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.profile.Actor;
import EDU.cmu.Atlas.owls1_1.profile.ActorsList;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategoriesList;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategory;
import EDU.cmu.Atlas.owls1_1.process.CompositeProcess;
import EDU.cmu.Atlas.owls1_1.process.ControlConstruct;
import EDU.cmu.Atlas.owls1_1.process.ControlConstructList;
import EDU.cmu.Atlas.owls1_1.process.Input;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.Output;
import EDU.cmu.Atlas.owls1_1.process.OutputList;
import EDU.cmu.Atlas.owls1_1.process.Perform;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.implementation.IfThenElseImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.RepeatWhileImpl;
import EDU.cmu.Atlas.owls1_1.service.ServiceGrounding;
import EDU.pku.edu.ly.temporaryowls.QoS;
import EDU.pku.ly.owlsservice.ExtendedService;


import cn.edu.pku.ss.matchmaker.index.Indexer;
import cn.edu.pku.ss.matchmaker.index.impl.ComponentType;
import cn.edu.pku.ss.matchmaker.index.impl.DataModel;
import cn.edu.pku.ss.matchmaker.index.impl.SemanticDataModel;
import cn.edu.pku.ss.matchmaker.index.impl.ServiceBody;
import cn.edu.pku.ss.matchmaker.index.impl.ServiceDataModel;
import cn.edu.pku.ss.matchmaker.reasoning.MatchLevel;
import cn.edu.pku.ss.matchmaker.reasoning.SemanticReasoner;
import cn.edu.pku.ss.matchmaker.reasoning.impl.SemanticReasonerImpl;

public class IndexerImpl implements Indexer {
	private SemanticReasoner semanticReasoner;
	private Hashtable<String, ServiceDataModel> serviceIndexer;
	private Hashtable<String, Hashtable<String, SemanticDataModel>> semanticIndexer;
	private Hashtable<String, Hashtable<MatchLevel, Set<String>> > matchLevelIndexer;
	private List<ExtendedService> extendedServiceList;
	private Hashtable<Process, List<Process>> atomicProcesses;
	
	private Logger logger;
	
	public IndexerImpl(SemanticReasoner semanticReasoner) {
		// TODO Auto-generated constructor stub
		this.semanticReasoner = semanticReasoner;
		serviceIndexer = new Hashtable<String, ServiceDataModel>();
		semanticIndexer = new Hashtable<String, Hashtable<String,SemanticDataModel>>();
		matchLevelIndexer = new Hashtable<String, Hashtable<MatchLevel,Set<String>>>();
		atomicProcesses = new Hashtable<Process, List<Process>>();
		
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.index.impl.IndexerImpl.class);
		
	}
	
	// need to invoke init() when use this constructor
	public IndexerImpl() {
		// TODO Auto-generated constructor stub
		semanticReasoner = new SemanticReasonerImpl();
		serviceIndexer = new Hashtable<String, ServiceDataModel>();
		semanticIndexer = new Hashtable<String, Hashtable<String,SemanticDataModel>>();
		matchLevelIndexer = new Hashtable<String, Hashtable<MatchLevel,Set<String>>>();
		atomicProcesses = new Hashtable<Process, List<Process>>();
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.index.impl.IndexerImpl.class);
	}
	
	public boolean init(String filePath) {
		// TODO Auto-generated method stub
		if (semanticReasoner.loadontologies(filePath) == false) {
			logger.error("faile to load ontologies");
			return false;
		}
		
		return true;
	}
	
	public  boolean process() {
		boolean serviceFlag = processServices();
		boolean semanticFlag = processSemanticRelation();
		
		return serviceFlag && semanticFlag;
	}
	
	// for test
	public boolean processForTest(List<ExtendedService> serviceList) {
		boolean serviceFlag = processServicesForTest(serviceList);
		boolean semanticFlag = processSemanticRelation();
		
		return serviceFlag && semanticFlag;
	}
	
	public boolean processServices() {
		// TODO Auto-generated method stub
		List<ServiceBody> bodyList = new ArrayList<ServiceBody> ();
		// process data from ExtendedService to ServiceBody
		if (preprocess(extendedServiceList, bodyList) == false)
			return false;
		
		for (int i = 0; i < bodyList.size(); ++i) {
			ServiceBody serviceBody = bodyList.get(i);
			
			// process profile info
			addServiceToProfileContainer(serviceBody.getProfile(), serviceBody);
			
			// process process info
			if (serviceBody.getProcess() == null || atomicProcesses.containsKey(serviceBody.getProcess()) == false)
				continue;
			
			List<Process> processList = atomicProcesses.get(serviceBody.getProcess());
			for (int j = 0; j < processList.size(); ++j)
				addServiceToProcessContainer(processList.get(j), serviceBody);
			
			// process grounding info
		}
		
		return true;
	}

	// for test
	public boolean processServicesForTest(List<ExtendedService> serviceList) {
		List<ServiceBody> bodyList = new ArrayList<ServiceBody> ();
		// process data from ExtendedService to ServiceBody
		if (preprocess(serviceList, bodyList) == false)
			return false;
		
		for (int i = 0; i < bodyList.size(); ++i) {
			ServiceBody serviceBody = bodyList.get(i);
			
			// process profile info
			addServiceToProfileContainer(serviceBody.getProfile(), serviceBody);
			
			// process process info
			if (atomicProcesses.containsKey(serviceBody.getProcess()) == false)
				continue;
			
			List<Process> processList = atomicProcesses.get(serviceBody.getProcess());
			for (int j = 0; j < processList.size(); ++j)
				addServiceToProcessContainer(processList.get(j), serviceBody);
			
			// process grounding info
		}
		
		return true;
		
	}

	public boolean processSemanticRelation() {
		// TODO Auto-generated method stub
		Set<String> concepts = semanticReasoner.getAllConcepts();
		for (String srcConcept : concepts) {
			for (String destConcept : concepts) {
				SemanticDataModel model = semanticReasoner.getSemanticDataModel(srcConcept, destConcept);
				if (model == null) {
					logger.error("failed to get semantic data model: \n" + srcConcept + " and " + destConcept);
				    continue;
				}
				addSemanticDataModel(srcConcept, destConcept, model);
			}
		}
		
		
		for (String concept : concepts) {
			// exact
			Set<SemanticDataModel> exacts = semanticReasoner.getConceptsSpecifiedMatchLevel(concept, MatchLevel.EXACT);
			if (exacts == null || exacts.isEmpty())
				logger.error("failed to get result for exact match");
			else
				addSpecifiedMatchConcepts(concept, MatchLevel.EXACT, getConceptsFromSemanticDataModel(exacts));
			
			// plugin
			Set<SemanticDataModel> plugins = semanticReasoner.getConceptsSpecifiedMatchLevel(concept, MatchLevel.PLUGIN);
			if (plugins == null ||  plugins.isEmpty())
				logger.error("failed to get result for plugin match");
			else 
				addSpecifiedMatchConcepts(concept, MatchLevel.PLUGIN, getConceptsFromSemanticDataModel(plugins));
			
			// subsume
			Set<SemanticDataModel> subsumes = semanticReasoner.getConceptsSpecifiedMatchLevel(concept, MatchLevel.SUBSUME);
			if (subsumes == null || subsumes.isEmpty())
				logger.error("failed to get result for subsume match");
			else 
				addSpecifiedMatchConcepts(concept, MatchLevel.SUBSUME, getConceptsFromSemanticDataModel(subsumes));
		}
		
		return true;
	}
	
	
	// brief:  preprocess data from ExtendedService to ServiceBody
	// in:     List<ExtendedService>
	// out:    List<ServiceBody>
	// return: true  - preprocess succeed
	//         false - preprocess failed
	private boolean preprocess(List<ExtendedService> serviceList, List<ServiceBody> bodyList) {
		if (serviceList == null || bodyList == null) {
			logger.error("parameter is null or illegal!");
			return false;
		}
		
		for (int i = 0; i < serviceList.size(); ++i) {
			ExtendedService extendedService = serviceList.get(i);
			if (extendedService == null)
				continue;
			
			String serviceKey = extendedService.GetServiceKey();
			Profile profile = null;
			if (extendedService.GetServiceProfileList() != null && extendedService.GetServiceProfileList().size() > 0) {
				profile = (Profile)extendedService.GetServiceProfileList().getNthServiceProfile(0);
			}
			
			
			Process process = (Process)extendedService.GetServiceModel();
			
			List<QoS> qosList = extendedService.GetQoS();
			ServiceGrounding serviceGrounding = null;
			if (extendedService.GetServiceGroundingList() != null && extendedService.GetServiceGroundingList().size() > 1)
				serviceGrounding = extendedService.GetServiceGroundingList().getNthServiceGrounding(0);
			
			String wsdlURI = extendedService.GetWSDLFilePath();
			String contextRule = extendedService.GetContextRule();
			
			// index process's input and output and in order to query
			Hashtable<String, DataModel> processTable = null;
			if (process != null) {
				processTable = new Hashtable<String, DataModel>();
				List<Process> atomics = new ArrayList<Process>();
				
				getAtomics(process, atomics);
				atomicProcesses.put(process, atomics);
				
				for (int j = 0; j < atomics.size(); ++j) {
					addProcessToInputContainer(atomics.get(j), processTable);
					addProcessToOutputContainer(atomics.get(j), processTable);
				}
			}
			
			ServiceBody serviceBody = new ServiceBody(serviceKey, profile, process, serviceGrounding, wsdlURI, null, qosList, contextRule, processTable);
			bodyList.add(serviceBody);
		}
		
		return true;
	}
	
	
	// profile
	private boolean addServiceToProfileContainer(Profile profile, ServiceBody serviceBody) {
		if (profile == null)
			return false;
		
		
		// profile InputList
		if (profile.getInputList() != null) 
			addServiceToInputContainer(profile.getInputList(), ComponentType.PROFILE, serviceBody);
		
		// profile outputList
		if (profile.getOutputList() != null)
			addServiceToOutputContainer(profile.getOutputList(), ComponentType.PROFILE, serviceBody);
		
		// category
		if (profile.getServiceCategory() != null)
			addServiceToClassificationContainer(profile.getServiceCategory(), ComponentType.PROFILE, serviceBody);
		
		// actor
        if (profile.getContactInformation() != null)
        	addServiceToActorContainer(profile.getContactInformation(), ComponentType.PROFILE, serviceBody);
        
        
        
		return true;
	}
	
	// process
	private boolean addServiceToProcessContainer(Process process, ServiceBody serviceBody) {
		if (process == null)
			return false;
		
		// process inputList
		if (process != null && process.getInputList() != null)
			addServiceToInputContainer(process.getInputList(), ComponentType.PROCESS, serviceBody);
		
		
		// process outputList
		if (process != null && process.getOutputList() != null)
			addServiceToOutputContainer(process.getOutputList(), ComponentType.PROCESS, serviceBody);
		
		return true;
	}
	
	// input
	private boolean addServiceToInputContainer(InputList inputList, ComponentType type, ServiceBody serviceBody) {
		if (inputList == null) {
			logger.error("InputList is null");
			return false;
		}
		
		for (int i = 0; i < inputList.size(); ++i) {
			Input input = inputList.getNthInput(i);
			if (input == null)
				continue;
			
			String parameterType = input.getParameterType();
			if (serviceIndexer.containsKey(parameterType)) {
				if (type.equals(ComponentType.PROFILE))
					serviceIndexer.get(parameterType).getProfileDataModel().getInput().add(serviceBody);
				else if (type.equals(ComponentType.PROCESS))
					serviceIndexer.get(parameterType).getProcessDataModel().getInput().add(serviceBody);
					//indexData.get(parameterType).getProcessDataModel().getInput().get(i).add(serviceKey);
			} else {
				ServiceDataModel serviceDataModel = new ServiceDataModel();
				if (type.equals(ComponentType.PROFILE))
					serviceDataModel.getProfileDataModel().getInput().add(serviceBody);
				else if (type.equals(ComponentType.PROCESS))
					serviceDataModel.getProcessDataModel().getInput().add(serviceBody);
				
				serviceIndexer.put(parameterType, serviceDataModel);
			}
		}
		
		return true;
	}
	
	// output
	private boolean addServiceToOutputContainer(OutputList outputList, ComponentType type, ServiceBody serviceBody) {
		if (outputList == null) {
			logger.error("OutputList is null");
			return false;
		}
		
		for (int i = 0; i < outputList.size(); ++i) {
			Output output = outputList.getNthOutput(i);
			if (output == null)
				continue;
			
			String parameterType = output.getParameterType();
			if (serviceIndexer.containsKey(parameterType)) {
				if (type.equals(ComponentType.PROFILE))
					serviceIndexer.get(parameterType).getProfileDataModel().getOutput().add(serviceBody);
				else if (type.equals(ComponentType.PROCESS))
					serviceIndexer.get(parameterType).getProcessDataModel().getOutput().add(serviceBody);
					//indexData.get(parameterType).getProcessDataModel().getInput().get(i).add(serviceKey);
			} else {
				ServiceDataModel serviceDataModel = new ServiceDataModel();
				if (type.equals(ComponentType.PROFILE))
					serviceDataModel.getProfileDataModel().getOutput().add(serviceBody);
				else if (type.equals(ComponentType.PROCESS))
					serviceDataModel.getProcessDataModel().getOutput().add(serviceBody);
				
				serviceIndexer.put(parameterType, serviceDataModel);
			}
		}
		
		return true;
	}
	
	// classification
	private boolean addServiceToClassificationContainer(ServiceCategoriesList categoriesList, ComponentType type, ServiceBody serviceBody) {
		if (categoriesList == null) {
		    logger.error("parameter is null");
			return false;
		}
		
		
		for (int i = 0; i < categoriesList.size(); ++i) {
			ServiceCategory category = categoriesList.getServiceCategoryAt(i);
			if (category == null)
				continue;
			
			String value = category.getValue();
			if (serviceIndexer.containsKey(value)) {
				if (type.equals(ComponentType.PROFILE)) {
					serviceIndexer.get(value).getProfileDataModel().getClassification().add(serviceBody);
				} else if (type.equals(ComponentType.PROCESS)) {
					serviceIndexer.get(value).getProcessDataModel().getClassification().add(serviceBody);
				}
			} else {
				ServiceDataModel serviceDataModel = new ServiceDataModel();
				if (type.equals(ComponentType.PROFILE))
					serviceDataModel.getProfileDataModel().getClassification().add(serviceBody);
				else if (type.equals(ComponentType.PROCESS))
					serviceDataModel.getProcessDataModel().getClassification().add(serviceBody);
				
				serviceIndexer.put(value, serviceDataModel);
			}
		}
		
		return true;
	}
	
	// actor
	private boolean addServiceToActorContainer(ActorsList actorsList, ComponentType type, ServiceBody serviceBody) {
		if (actorsList == null) {
			 logger.error("parameter is null");
			 return false;
		}
		
		for (int i = 0; i < actorsList.size(); ++i) {
			Actor actor = actorsList.getNthActor(i);
			if (actor == null)
				continue;
			
			String name = actor.getName();
			if (serviceIndexer.containsKey(name)) {
				if (type.equals(ComponentType.PROFILE)) {
					serviceIndexer.get(name).getProfileDataModel().getActor().add(serviceBody);
				} else if (type.equals(ComponentType.PROCESS)) {
					serviceIndexer.get(name).getProcessDataModel().getActor().add(serviceBody);
				}
			} else {
				ServiceDataModel serviceDataModel = new ServiceDataModel();
				if (type.equals(ComponentType.PROFILE))
					serviceDataModel.getProfileDataModel().getActor().add(serviceBody);
				else if (type.equals(ComponentType.PROCESS))
					serviceDataModel.getProcessDataModel().getActor().add(serviceBody);
				
				serviceIndexer.put(name, serviceDataModel);
			}
		}
		
		return true;
	}
	
	
	private List<Process> getAtomics(Process process, List<Process> atomics) {
		if (process == null) {
			logger.error("parameter is null");
			return null;
		}
		
		
		if (process.isAtomic() || process.isSimple()) {
			atomics.add(process);
			return atomics;
		}
		
	//	logger.info(process);
		CompositeProcess compositeProcess = (CompositeProcess) process;
		ControlConstruct comp = compositeProcess.getComposedOf();
		if (comp == null) {
		    logger.info(process);
		    return null;
		}
		    
//			return null;
		
		if (comp instanceof IfThenElseImpl) {
			ControlConstruct  thenConstruct = ((IfThenElseImpl)comp).getThen();
			ControlConstruct elseConstruct = ((IfThenElseImpl)comp).getElse();
			if(thenConstruct != null) {
				Process tmpProcess = ((Perform)thenConstruct).getProcess();
				getAtomics(tmpProcess, atomics);
			}
			
			if (elseConstruct != null) {
				Process tmpProcess = ((Perform)elseConstruct).getProcess();
				getAtomics(tmpProcess, atomics);
			}
			
		} else if (comp instanceof RepeatWhileImpl) {
			ControlConstruct whileConstruct = ((RepeatWhileImpl)comp).getWhileProcess();
			if (whileConstruct != null) {
				Process tmpProcess = ((Perform)whileConstruct).getProcess();
				getAtomics(tmpProcess, atomics);
			}
			
		} else {
			ControlConstructList components = (ControlConstructList)comp.getComponents();
			while(components != null) {
				Process tmpProcess = ((Perform)components.getFirst()).getProcess();
				getAtomics(tmpProcess, atomics);
				components = (ControlConstructList) components.getRest();
			}
		}
		
		return atomics;
	}
	
	private boolean addProcessToInputContainer(Process process, Hashtable<String, DataModel> processIndexData) {
		if (process == null || processIndexData == null) {
		    logger.error("parameter is null or illegal");
			return false;
		}
		
		InputList inputList = process.getInputList();
		if (inputList == null) {
		    logger.info("InputList is null");
			return false;
		}
		
		for (int i = 0; i < inputList.size(); ++i) {
			Input input = inputList.getNthInput(i);
			if (input == null)
				continue;
			
			String parameterTypeString = input.getParameterType();
			if (processIndexData.containsKey(parameterTypeString)) {
				processIndexData.get(parameterTypeString).getInput().add(process);
			} else {
				DataModel dataModel = new DataModel();
				dataModel.getInput().add(process);
				
				processIndexData.put(parameterTypeString, dataModel);
			}
		}
		
		return true;
	}
	
	// add process to output container
	private boolean addProcessToOutputContainer(Process process, Hashtable<String, DataModel> processIndexData) {
		if (process == null || processIndexData == null) {
		    logger.error("parameter is null or illegal");
			return false;
		}
		
		OutputList outputList = process.getOutputList();
		if (outputList == null) {
		    logger.info("OutputList is null");
			return false;
		}
		
		for (int i = 0; i < outputList.size(); ++i) {
			Output output = outputList.getNthOutput(i);
			if (output == null)
				continue;
			
			String parameterTypeString = output.getParameterType();
			if (processIndexData.containsKey(parameterTypeString)) {
				processIndexData.get(parameterTypeString).getOutput().add(process);
			} else {
				DataModel dataModel = new DataModel();
				dataModel.getOutput().add(process);
				
				processIndexData.put(parameterTypeString, dataModel);
			}
		}
		
		return true;
	}
	
	

	public Hashtable<String, ServiceDataModel> getServiceIndexer() {
		// TODO Auto-generated method stub
		return serviceIndexer;
	}

	public Hashtable<String, Hashtable<MatchLevel, Set<String>>> getmatchLevelIndexer() {
		// TODO Auto-generated method stub
		return matchLevelIndexer;
	}

	public Hashtable<String, Hashtable<String, SemanticDataModel>> getsemanticIndexer() {
		// TODO Auto-generated method stub
		return semanticIndexer;
	}
	
	public Vector<Object> getActorServices(String concept) {
		// TODO Auto-generated method stub
		if (serviceIndexer.containsKey(concept))
			return serviceIndexer.get(concept).getProfileDataModel().getActor();
		
		return null;
	}

	
	public Vector<Object> getClassificationServices(String concept) {
		// TODO Auto-generated method stub
		if (serviceIndexer.containsKey(concept))
			return serviceIndexer.get(concept).getProfileDataModel().getClassification();
		
		return null;
	}

	public Vector<Object> getServiceProductServices(String concept) {
		// TODO Auto-generated method stub
		if (serviceIndexer.containsKey(concept))
			return serviceIndexer.get(concept).getProfileDataModel().getServiceProduct();
		
		return null;
	}
	

	public Vector<Object> getProcessInputServices(String concept) {
		// TODO Auto-generated method stub
		if (serviceIndexer.containsKey(concept))
			return serviceIndexer.get(concept).getProcessDataModel().getInput();
		
		return null;
	}

	public Vector<Object> getProcessOutputServices(String concept) {
		// TODO Auto-generated method stub
		if (serviceIndexer.containsKey(concept))
			return serviceIndexer.get(concept).getProcessDataModel().getOutput();
		
		return null;
	}

	public Vector<Object> getProfileInputServices(String concept) {
		// TODO Auto-generated method stub
		if (serviceIndexer.containsKey(concept))
			return serviceIndexer.get(concept).getProfileDataModel().getInput();
		
		return null;
	}

	public Vector<Object> getProfileOutputServices(String concept) {
		// TODO Auto-generated method stub
		if (serviceIndexer.containsKey(concept))
			return serviceIndexer.get(concept).getProfileDataModel().getOutput();
		
		return null;
	}


	public SemanticDataModel getReasonDataModel(String srcConcept,
			String destConcept) {
		// TODO Auto-generated method stub
		if (semanticIndexer.containsKey(srcConcept) == false)
			return null;
		
		Hashtable<String, SemanticDataModel> tmp = semanticIndexer.get(srcConcept);
		
		if (tmp == null || tmp.containsKey(destConcept) == false)
			return null;
		
		
		return tmp.get(destConcept);
	}
	
	
	public int getSemanticDistance(String srcConcept, String destConcept) {
		// TODO Auto-generated method stub
		SemanticDataModel reasonDataModel = getReasonDataModel(srcConcept, destConcept);
		if (reasonDataModel == null)
			return 0;
		
		return reasonDataModel.getDistance();
	}

	public double getSemanticMatchDegree(String srcConcept, String destConcept) {
		// TODO Auto-generated method stub
		if (srcConcept == destConcept || srcConcept.equals(destConcept))
			return 1;
		
		SemanticDataModel reasonDataModel = getReasonDataModel(srcConcept, destConcept);
		if (reasonDataModel == null)
			return 0;
		
		return reasonDataModel.getMatchDegree();
	}

	public MatchLevel getSemanticMatchLevel(String srcConcept,
			String destConcept) {
		// TODO Auto-generated method stub
		SemanticDataModel reasonDataModel = getReasonDataModel(srcConcept, destConcept);
		if (reasonDataModel == null)
			return null;
		
		return reasonDataModel.getMatchLevel();
	}

	

	public Set<String> getSpecifiedMatchLevelConcept(String concept,
			MatchLevel level) {
		// TODO Auto-generated method stub
		if (matchLevelIndexer.containsKey(concept) == false)
			return Collections.emptySet();
		
		Hashtable<MatchLevel, Set<String> > tmp = matchLevelIndexer.get(concept);
		
		if (tmp == null || tmp.containsKey(level) == false)
			return Collections.<String> emptySet();
		
		return tmp.get(level);
	}
	
	public Set<String> getAllSimilarConcepts(String concept) {
		// TODO Auto-generated method stub
		Set<String> similarConceptsSet =  new HashSet<String> ();
		similarConceptsSet.add(concept);
		similarConceptsSet.addAll(getSpecifiedMatchLevelConcept(concept, MatchLevel.EXACT));
		similarConceptsSet.addAll(getSpecifiedMatchLevelConcept(concept, MatchLevel.PLUGIN));
		similarConceptsSet.addAll(getSpecifiedMatchLevelConcept(concept, MatchLevel.SUBSUME));
				
		return similarConceptsSet;
	}
	
	private void addSemanticDataModel(String srcConcept, String destConcept, SemanticDataModel model) {
		if (semanticIndexer.containsKey(srcConcept)) {
			Hashtable<String, SemanticDataModel> indexerHashtable = semanticIndexer.get(srcConcept);
			if (indexerHashtable.containsKey(destConcept) == false)
				indexerHashtable.put(destConcept, model);
		} else {
			Hashtable<String, SemanticDataModel> indexerHashtable = new Hashtable<String, SemanticDataModel>();
			indexerHashtable.put(destConcept, model);
			semanticIndexer.put(srcConcept, indexerHashtable);
		}
	}
	
	private Set<String> getConceptsFromSemanticDataModel(Set<SemanticDataModel> models) {
		Set<String> results = new HashSet<String>();
		
		for (SemanticDataModel model : models)
			results.add(model.getDestConcept());
		
		return results;
	}
	
	private void addSpecifiedMatchConcepts(String concept, MatchLevel level, Set<String> concepts) {
		if (matchLevelIndexer.containsKey(concept)) {
			Hashtable<MatchLevel, Set<String>> matchLevelHashtable = matchLevelIndexer.get(concept);
			if (matchLevelHashtable.containsKey(level) == false)
				matchLevelHashtable.put(level, concepts);
			else 
				matchLevelHashtable.get(level).addAll(concepts);
		} else {
			Hashtable<MatchLevel, Set<String>> matchLevelHashtable = new Hashtable<MatchLevel, Set<String>>();
			matchLevelHashtable.put(level, concepts);
			matchLevelIndexer.put(concept, matchLevelHashtable);
		}
		
	}

	public SemanticReasoner getSemanticReasoner() {
		return semanticReasoner;
	}

	public void setSemanticReasoner(SemanticReasoner semanticReasoner) {
		this.semanticReasoner = semanticReasoner;
	}

	public void setSemanticIndexer(
			Hashtable<String, Hashtable<String, SemanticDataModel>> semanticIndexer) {
		this.semanticIndexer = semanticIndexer;
	}


	public void setMatchLevelIndexer(
			Hashtable<String, Hashtable<MatchLevel, Set<String>>> matchLevelIndexer) {
		this.matchLevelIndexer = matchLevelIndexer;
	}

	public Hashtable<Process, List<Process>> getAtomicProcesses() {
		return atomicProcesses;
	}

	public void setAtomicProcesses(Hashtable<Process, List<Process>> atomicProcesses) {
		this.atomicProcesses = atomicProcesses;
	}

	public void setServiceIndexer(Hashtable<String, ServiceDataModel> serviceIndexer) {
		this.serviceIndexer = serviceIndexer;
	}

	public List<ExtendedService> getExtendedServiceList() {
		// TODO Auto-generated method stub
		return extendedServiceList;
	}

	public void setExtendedServiceList(List<ExtendedService> extendedServiceList) {
		// TODO Auto-generated method stub
		this.extendedServiceList = extendedServiceList;
		
	}
}
