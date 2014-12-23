package cn.edu.pku.ss.matchmaker.query.filters;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.process.Input;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.Process;
import cn.edu.pku.ss.matchmaker.index.Indexer;
import cn.edu.pku.ss.matchmaker.index.impl.ComponentType;
import cn.edu.pku.ss.matchmaker.index.impl.DataModel;
import cn.edu.pku.ss.matchmaker.index.impl.ServiceBody;
import cn.edu.pku.ss.matchmaker.query.Query;
import cn.edu.pku.ss.matchmaker.query.QueryProcessScorer;
import cn.edu.pku.ss.matchmaker.query.QueryResults;

public class InputsFilter extends AbstractQueryFilter {
	private Indexer indexer;
	private ComponentType type;
	
	private Logger logger;
	
	public InputsFilter(Indexer indexer, ComponentType type) {
		super();
		this.indexer = indexer;
		this.type = type;
		
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.query.filters.impl.InputsFilter.class);
	}

	public Hashtable<String, QueryResults> process(Query query,
			Hashtable<String, QueryResults> resultTable) {
		// TODO Auto-generated method stub	
		InputList inputList = query.getInputList();
		
		if (getContainsInputSerivces(inputList, type, resultTable) == false)
			logger.error("failed to get contain input services");
		
		return sendToNextFilter(query, resultTable);
	}
	
	private boolean getContainsInputSerivces(InputList inputList,
			ComponentType type, 
			Hashtable<String, QueryResults> resultTable) {
		if (resultTable == null) {
			logger.error("parameter is null || is illegal!");
			return false;
		}
		
		if (type.equals(ComponentType.PROFILE)) {
			if (getProfileInputServices(inputList, resultTable) == false) {
				logger.error("failed to get profile input services");
				return false;
			}
		}
		
		if (type.equals(ComponentType.PROCESS)) {
			if (getProcessInputServices(inputList, resultTable) == false) {
				logger.error("failed to get process input services");
				return false;
			}
		}
		
		return true;
	}
	
	private boolean getProfileInputServices(InputList inputList, Hashtable<String, QueryResults> resultTable) {
		if (inputList == null || resultTable == null) {
			logger.error("parameter is null");
			return false;
		}
		
		int inputSize = inputList.size();
		for (int i = 0; i < inputSize; ++i) {
			Input input = inputList.getNthInput(i);
			Set<String> similarConcepts = indexer.getAllSimilarConcepts(input.getParameterType());
			
			if (getSpecifiedProfileInputConceptServices(input.getParameterType(), inputSize, similarConcepts, resultTable) == false)
				logger.error("failed to get specified profile input concept services");
		}
		
		if (filterProfileNotEqualsParameterNumServices(resultTable) == false) {
			logger.error("failed to filter services which parameter num is not equal");
			return false;
		}
			
		return true;
	}
	
	
	private boolean getProcessInputServices(InputList inputList, Hashtable<String, QueryResults> resultTable) {
		if (inputList == null || resultTable == null) {
			logger.error("parameter is null");
			return false;
		}
		
		int inputSize = inputList.size();
		for (int i = 0; i < inputSize; ++i) {
			Input input = inputList.getNthInput(i);
			Set<String> similarConcepts = indexer.getAllSimilarConcepts(input.getParameterType());
			if (getSpecifiedProcessInputConceptServices(input.getParameterType(), inputSize, similarConcepts, resultTable) == false)
				logger.error("failed to get specified process input concept services");
		}
		
		if (filterProcessNotEqualsParameterNumServices(resultTable) == false) {
			logger.error("failed to filter services which parameter num is not equal");
			return false;
		}
			
		
		return true;
	}
	
	
	private boolean getSpecifiedProfileInputConceptServices(String concept,
			int inputSize,
			Set<String> similarConcepts,
			Hashtable<String, QueryResults> resultTable) {
		if (concept == null || resultTable == null || inputSize < 0) {
			logger.error("parameter is null || is illegal!");
			return false;
		}
		
		Hashtable<String, QueryResults> queryResultTable = new Hashtable<String, QueryResults>();
		for (String conceptString : similarConcepts) {
			Vector<Object> services = indexer.getProfileInputServices(conceptString);
			if (services == null)
				continue;
			
			for (int i = 0; i < services.size(); ++i) {
				ServiceBody serviceBody = (ServiceBody)services.get(i);
				String serviceKey = serviceBody.getServiceKey();
				int inputNum = serviceBody.getProfile().getInputList().size();
				
				int maxInputSize = inputSize > inputNum ? inputSize : inputNum;		

				// TODO
				if (queryResultTable.containsKey(serviceKey) == false) {
					QueryResults queryResults = new QueryResults(serviceBody);
					double inputSimilarity = indexer.getSemanticMatchDegree(concept, conceptString) / maxInputSize;
					
					queryResults.setInputScore(inputSimilarity);
					queryResults.getParameterRecord().add(conceptString);
					
					queryResultTable.put(serviceKey, queryResults);
				}
			}
			
		}
		
		Enumeration enumeration = queryResultTable.keys();
		while (enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
			QueryResults localResult = queryResultTable.get(key);
			
			if (resultTable.containsKey(key)) {
				QueryResults globalResult = resultTable.get(key);
				globalResult.setInputScore(globalResult.getInputScore() + localResult.getInputScore());
				globalResult.getParameterRecord().addAll(localResult.getParameterRecord());
			} else {
				resultTable.put(key, localResult);
			}
		}
		
		return true;
	}
	
	private boolean filterProfileNotEqualsParameterNumServices(Hashtable<String, QueryResults> resultTable) {
		if (resultTable == null) {
			logger.error("parameter is null or parameter is illegal!");
			return false;
		}
		
		Enumeration enumeration = resultTable.keys();
		while (enumeration.hasMoreElements()) {
			String serviceKey = (String) enumeration.nextElement();
			QueryResults queryResults = resultTable.get(serviceKey);
			int inputParameterNum = queryResults.getProfile().getInputList().size();
			
			if (queryResults == null)
				continue;
			
			Vector<Object> parameterRecord = queryResults.getParameterRecord();
			if (parameterRecord == null)
				continue;
			
			if (parameterRecord.size() != inputParameterNum) {
				resultTable.remove(serviceKey);
				queryResults.clear();
			}
			queryResults.clearParameterRecord();
		}
		
		return true;
	}
		

	
	
	// brief:  first process OWLS-PROCESS
	// in:     concept - an input parameter type
	//         inputSize - input paraeter num
	//         similarCocepts - get similar cocepts from pallet according parameter concept
	// out:    resultTable - Store QueryResults
	// return: true - succeed
	//         false - failed
	private boolean getSpecifiedProcessInputConceptServices(String concept,
			int inputSize,
			Set<String> similarConcepts,
			Hashtable<String, QueryResults> resultTable) {
		if (concept == null || resultTable == null || inputSize < 0) {
			logger.error("parameter is null || is illegal!");
			return false;
		}
		Vector<String> allConcepts = new Vector<String>();
		allConcepts.add(concept);
		allConcepts.addAll(similarConcepts);
		
		Hashtable<String, QueryResults> queryResultTable = new Hashtable<String, QueryResults>();
		for (String conceptString : allConcepts) {			
			// get services according to concept
			Vector<Object> serviceVector = indexer.getProcessInputServices(conceptString);
			if (serviceVector == null)
				continue;		
			
			// put all services to resultTable			
			for (int i = 0; i < serviceVector.size(); ++i) {
				ServiceBody serviceBody = (ServiceBody)serviceVector.get(i);
				if (serviceBody == null)
					continue;
				
				String serviceKey = serviceBody.getServiceKey();
				if (queryResultTable.containsKey(serviceKey))
					continue;
				
				// the processID which contains input concept
				QueryResults queryResults = new QueryResults(serviceBody); 
				Vector<Object> result = getProcessesContainProcessInputConcept(serviceBody, conceptString);
				Hashtable<String, QueryProcessScorer> processScorerHashtable = queryResults.getQueryProcessScorerHashtable();
				for (int j = 0; j < result.size(); ++j) {
					Process tmpProcess = (Process)result.get(j);
					if (tmpProcess == null)
						continue;
					
					// TODO
					int inputNum = tmpProcess.getInputList().size();
					if (inputNum <= 0)
						continue;
					
					int maxInputSize = inputSize > inputNum ? inputSize : inputNum;
					double inputScore = indexer.getSemanticMatchDegree(concept, conceptString) / maxInputSize;
					
					QueryProcessScorer queryProcessScorer = new QueryProcessScorer(tmpProcess);
					// record parameter which current service contains 
					queryProcessScorer.getParameterRecord().add(conceptString);
					queryProcessScorer.setInputScore(inputScore);
					
					processScorerHashtable.put(tmpProcess.getURI(), queryProcessScorer);
				}				
				
				queryResultTable.put(serviceBody.getServiceKey(), queryResults);
			}
		}
		
		Enumeration enumeration = queryResultTable.keys();
		while (enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
			QueryResults localResult = queryResultTable.get(key);
			Hashtable<String, QueryProcessScorer> localProcessScorer = localResult.getQueryProcessScorerHashtable();
			if (resultTable.containsKey(key)) {
				QueryResults globalResult = resultTable.get(key);
				
				Hashtable<String, QueryProcessScorer> processScorerHashtable = globalResult.getQueryProcessScorerHashtable();
				if (processScorerHashtable == null)
					continue;
				
				computeCandidateProcessScore(localProcessScorer, processScorerHashtable);
				
			} else {
				resultTable.put(key, localResult);
			}
		}
		
		
		return true;
	}
	
	
	private Vector<Object> getProcessesContainProcessInputConcept(ServiceBody serviceBody, String concept) {
		if (serviceBody == null)
			return null;
		
		Hashtable<String, DataModel> processIndexData = serviceBody.getProcessIndexData();
		if (processIndexData == null)
			return null;
		
		if (processIndexData.containsKey(concept)) {
			DataModel dataModel = processIndexData.get(concept);
			if (dataModel != null)
				return dataModel.getInput();
		}
		
		return null;
	}
	
	private boolean computeCandidateProcessScore(Hashtable<String, QueryProcessScorer> localProcessScorer,
			Hashtable<String, QueryProcessScorer> glocalProcessScorer) {
		Enumeration enumeration = localProcessScorer.keys();
		while (enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
			QueryProcessScorer localScorer = localProcessScorer.get(key);
			
			if (glocalProcessScorer.containsKey(key)) {
				QueryProcessScorer glocalScorer = glocalProcessScorer.get(key);
				double inputScore = glocalScorer.getInputScore() + localScorer.getInputScore();
				glocalScorer.setInputScore(inputScore);
				
				glocalScorer.getParameterRecord().addAll(localScorer.getParameterRecord());			
			} else 
				glocalProcessScorer.put(key, localScorer);
		}
		
		return true;
	}
	
	
	private boolean filterProcessNotEqualsParameterNumServices(Hashtable<String, QueryResults> resultTable) {
		if (resultTable == null) {
			logger.error("parameter is null or parameter is illegal!");
			return false;
		}
		
		Enumeration enumeration = resultTable.keys();
		while (enumeration.hasMoreElements()) {
			String serviceKey = (String) enumeration.nextElement();
			QueryResults queryResults = resultTable.get(serviceKey);
			if (queryResults == null)
				continue;
			
			Hashtable<String, QueryProcessScorer> processScorerHashtable = queryResults.getQueryProcessScorerHashtable();
			if (processScorerHashtable == null)
				continue;
			
			// process OWL-S PROCESS
			boolean flag = false;
			Enumeration enumeration1 =  processScorerHashtable.keys();
			while (enumeration1.hasMoreElements()) {
				String processKey = (String) enumeration1.nextElement();
				QueryProcessScorer queryProcessScorer = processScorerHashtable.get(processKey);
				
				if (queryProcessScorer == null)
					continue;
				
				Vector<Object> parameterRecord = queryProcessScorer.getParameterRecord();
				if (parameterRecord == null)
					continue;
				
				int inputParameterNum = queryProcessScorer.getProcess().getInputList().size();
				
				if (parameterRecord.size() != inputParameterNum)
					processScorerHashtable.remove(processKey);
				else
					flag = true;
			
				queryProcessScorer.clear();
			}
			
			if (flag == false) {
				resultTable.remove(serviceKey);
				queryResults.clear();
			}
			
			queryResults.clearProcessScorertHashtable();
			
		}
		
		return true;
	}
	
}
