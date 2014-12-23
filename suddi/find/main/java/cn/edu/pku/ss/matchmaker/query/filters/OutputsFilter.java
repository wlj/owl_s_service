package cn.edu.pku.ss.matchmaker.query.filters;


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.process.Output;
import EDU.cmu.Atlas.owls1_1.process.OutputList;
import EDU.cmu.Atlas.owls1_1.process.Process;
import cn.edu.pku.ss.matchmaker.index.Indexer;
import cn.edu.pku.ss.matchmaker.index.impl.ComponentType;
import cn.edu.pku.ss.matchmaker.index.impl.DataModel;
import cn.edu.pku.ss.matchmaker.index.impl.ServiceBody;
import cn.edu.pku.ss.matchmaker.query.Query;
import cn.edu.pku.ss.matchmaker.query.QueryProcessScorer;
import cn.edu.pku.ss.matchmaker.query.QueryResults;

public class OutputsFilter extends AbstractQueryFilter {
	private Indexer indexer;
	private ComponentType type;
	
	private Logger logger;
	
	public OutputsFilter(Indexer indexer, ComponentType type) {
		super();
		this.indexer = indexer;
		this.type = type;
		
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.query.filters.impl.OutputsFilter.class);
	}

	public Hashtable<String, QueryResults> process(Query query,
			Hashtable<String, QueryResults> resultTable) {
		// TODO Auto-generated method stub
		OutputList outputList = query.getOutputList();
		
		if (getContainsOutputSerivces(outputList, type, resultTable) == false)
			logger.error("failed to get contain Output services");
		
		return sendToNextFilter(query, resultTable);
	}
	
	private boolean getContainsOutputSerivces(OutputList outputList,
			ComponentType type, 
			Hashtable<String, QueryResults> resultTable) {
		if (resultTable == null) {
			logger.error("parameter is null || is illegal!");
			return false;
		}
		
		if (type.equals(ComponentType.PROFILE)) {
			if (getProfileOutputServices(outputList, resultTable) == false) {
				logger.error("failed to get profile Output services");
				return false;
			}
		}
		
		if (type.equals(ComponentType.PROCESS)) {
			if (getProcessOutputServices(outputList, resultTable) == false) {
				logger.error("failed to get process output services");
				return false;
			}
		}
		
		return true;
	}
	
	private boolean getProfileOutputServices(OutputList outputList, Hashtable<String, QueryResults> resultTable) {
		if (outputList == null || resultTable == null) {
			logger.error("parameter is null");
			return false;
		}
		
		int outputSize = outputList.size();
		for (int i = 0; i < outputSize; ++i) {
			Output output = outputList.getNthOutput(i);
			Set<String> similarConcepts = indexer.getAllSimilarConcepts(output.getParameterType());
			if (getSpecifiedProfileOutputConceptServices(output.getParameterType(), outputSize, similarConcepts, resultTable) == false)
				logger.error("failed to get specified profile output concept services");
		}
		
		if (filterProfileNotEqualsParameterNumServices(resultTable, outputSize) == false) {
			logger.error("failed to filter services which parameter num is not equal");
			return false;
		}
			
		return true;
	}
	
	
	private boolean getProcessOutputServices(OutputList outputList, Hashtable<String, QueryResults> resultTable) {
		if (outputList == null || resultTable == null) {
			logger.error("parameter is null");
			return false;
		}
		
		int outputSize = outputList.size();
		for (int i = 0; i < outputSize; ++i) {
			Output output = outputList.getNthOutput(i);
			Set<String> similarConcepts = indexer.getAllSimilarConcepts(output.getParameterType());
			if (getSpecifiedProcessOutputConceptServices(output.getParameterType(), outputSize, similarConcepts, resultTable) == false)
				logger.error("failed to get specified process output concept services");
		}
		
		if (filterProcessNotEqualsParameterNumServices(resultTable, outputSize) == false) {
			logger.error("failed to filter services which parameter num is not equal");
			return false;
		}
			
		return true;
	}
	
	
	private boolean getSpecifiedProfileOutputConceptServices(String concept,
			int outputSize,
			Set<String> similarConcepts,
			Hashtable<String, QueryResults> resultTable) {
		if (concept == null || resultTable == null || outputSize < 0) {
			logger.error("parameter is null || is illegal!");
			return false;
		}
		
		Hashtable<String, QueryResults> queryResultTable = new Hashtable<String, QueryResults>();
		for (String conceptString : similarConcepts) {
			Vector<Object> services = indexer.getProfileOutputServices(conceptString);
			if (services == null)
				continue;
			
			for (int i = 0; i < services.size(); ++i) {
				ServiceBody serviceBody = (ServiceBody)services.get(i);
				String serviceKey = serviceBody.getServiceKey();
				int outputNum = serviceBody.getProfile().getOutputList().size();
				
				int maxOutputSize = outputSize > outputNum ? outputSize : outputNum;

				// TODO
				if (queryResultTable.containsKey(serviceKey) == false) {
					QueryResults queryResults = new QueryResults(serviceBody);
					double outputSimilarity = indexer.getSemanticMatchDegree(concept, conceptString) / maxOutputSize;
					
					queryResults.setOutputScore(outputSimilarity);
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
				globalResult.setOutputScore(globalResult.getOutputScore() + localResult.getOutputScore());
				globalResult.getParameterRecord().addAll(localResult.getParameterRecord());
			} else {
				resultTable.put(key, localResult);
			}
		}
		
		return true;
	}
	
	private boolean filterProfileNotEqualsParameterNumServices(Hashtable<String, QueryResults> resultTable, int outputSize) {
		if (resultTable == null || outputSize < 0) {
			logger.error("parameter is null or parameter is illegal!");
			return false;
		}
		
		Enumeration enumeration = resultTable.keys();
		while (enumeration.hasMoreElements()) {
			String serviceKey = (String) enumeration.nextElement();
			QueryResults queryResults = resultTable.get(serviceKey);
			//int inputParameterNum = queryResults.getProfile().getInputList().size();
			
			if (queryResults == null)
				continue;
			
			Vector<Object> parameterRecord = queryResults.getParameterRecord();
			if (parameterRecord == null)
				continue;
			
			if (parameterRecord.size() != outputSize) {
				resultTable.remove(serviceKey);
				queryResults.clear();
			}
			queryResults.clearParameterRecord();
		}
		
		return true;
	}
		

	
	
	// brief:  first process OWLS-PROCESS
	// in:     concept - an output parameter type
	//         outputSize - output paraeter num
	//         similarCocepts - get similar cocepts from pallet according parameter concept
	// out:    resultTable - Store QueryResults
	// return: true - succeed
	//         false - failed
	private boolean getSpecifiedProcessOutputConceptServices(String concept,
			int outputSize,
			Set<String> similarConcepts,
			Hashtable<String, QueryResults> resultTable) {
		if (concept == null || resultTable == null || outputSize < 0) {
			logger.error("parameter is null || is illegal!");
			return false;
		}
		
		Vector<String> allConcepts = new Vector<String>();
		allConcepts.add(concept);
		allConcepts.addAll(similarConcepts);
		
		Hashtable<String, QueryResults> queryResultTable = new Hashtable<String, QueryResults>();
		for (String conceptString : allConcepts) {			
			// get services according to concept
			Vector<Object> serviceVector = indexer.getProcessOutputServices(conceptString);
			
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
				
				// the processID which contains output concept
				QueryResults queryResults = new QueryResults(serviceBody); 
				Vector<Object> result = getProcessesContainProcessOutputConcept(serviceBody, conceptString);
				Hashtable<String, QueryProcessScorer> processScorerHashtable = queryResults.getQueryProcessScorerHashtable();
				for (int j = 0; j < result.size(); ++j) {
					Process tmpProcess = (Process)result.get(j);
					if (tmpProcess == null)
						continue;
					
					// TODO
					int outputNum = tmpProcess.getOutputList().size();
					if (outputNum <= 0)
						continue;
					
					int maxOutputSize = outputSize > outputNum ? outputSize : outputNum;
					double outputScore = indexer.getSemanticMatchDegree(concept, conceptString) / maxOutputSize;
					
					QueryProcessScorer queryProcessScorer = new QueryProcessScorer(tmpProcess);
					// record parameter which current service contains 
					queryProcessScorer.getParameterRecord().add(conceptString);
					queryProcessScorer.setOutputScore(outputScore);
					
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
	
	
	private Vector<Object> getProcessesContainProcessOutputConcept(ServiceBody serviceBody, String concept) {
		if (serviceBody == null)
			return null;
		
		Hashtable<String, DataModel> processIndexData = serviceBody.getProcessIndexData();
		if (processIndexData == null)
			return null;
		
		if (processIndexData.containsKey(concept)) {
			DataModel dataModel = processIndexData.get(concept);
			if (dataModel != null)
				return dataModel.getOutput();
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
				double outputScore = glocalScorer.getOutputScore() + localScorer.getOutputScore();
				glocalScorer.setOutputScore(outputScore);
				
				glocalScorer.getParameterRecord().addAll(localScorer.getParameterRecord());			
			} else 
				glocalProcessScorer.put(key, localScorer);
		}
		
		return true;
	}
	
	
	private boolean filterProcessNotEqualsParameterNumServices(Hashtable<String, QueryResults> resultTable, int outputSize) {
		if (resultTable == null || outputSize < 0) {
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
				
				//int inputParameterNum = queryProcessScorer.getInputList().size();
				
				// TODO
				if (parameterRecord.size() != outputSize)
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

