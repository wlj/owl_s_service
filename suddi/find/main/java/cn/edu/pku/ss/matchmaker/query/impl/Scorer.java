package cn.edu.pku.ss.matchmaker.query.impl;


import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.process.Process;
import cn.edu.pku.ss.matchmaker.query.QueryProcessScorer;
import cn.edu.pku.ss.matchmaker.query.QueryResults;
import cn.edu.pku.ss.matchmaker.similarity.process.ProcessSimilarity;
import cn.edu.pku.ss.matchmaker.similarity.process.impl.ProcessSimilarityImpl;



public class Scorer {
	// profile
	public final static double PROFILE_INPUT_RATE = 0.3;
	public final static double PROFILE_OUTPUT_RATE = 0.3;
	public final static double PROFILE_PRECONDITION_RATE = 0.1;
	public final static double PROFILE_EFFECT_RATE = 0.1;
	public final static double PROFILE_CLASSIFICATION_RATE = 0.1;
	public final static double PROFILE_ACTOR_RATE = 0.05;
	public final static double PROFILE_PRODUCT_RATE = 0.05;
	
	// atomic process
	public final static double PROCESS_INPUT_RATE = 0.3;
	public final static double PROCESS_OUTPUT_RATE =0.3;
	public final static double PROCESS_PRECONDITION_RATE = 0.2;
	public final static double PROCESS_EFFECT_RATE = 0.2;
	
	// Service
	public final static double PROCESS_RATE = 0.4;
	public final static double PROFILE_RATE = 0.4;
	public final static double GROUNDING_RATE = 0.2;
	
	// Process
	public final static double PROCESS_STRUCTURE_RATE = 0.4;
	public final static double PROCESS_ATOMIC_RATE = 0.6;
	
	private ProcessSimilarity processSimilarity;
	private static Hashtable<Process, Hashtable<Process, Double>> atomicProcessPairSimilarity;
	Logger logger;
	
	public Scorer() {
		// TODO Auto-generated constructor stub
		processSimilarity = new ProcessSimilarityImpl();
		
		atomicProcessPairSimilarity = new Hashtable<Process, Hashtable<Process,Double>>();
		
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.query.impl.Scorer.class);
	}
	
	public static Hashtable<Process, Hashtable<Process, Double>> getAtomicProcessPairSimilarity() {
		return atomicProcessPairSimilarity;
	}
	
	public boolean computeProfileScore(Hashtable<String, QueryResults> resultTable) {
		if (resultTable == null) {
			logger.error("parameter is null!");
			return false;
		}
		
		Enumeration enumeration = resultTable.keys();
		while (enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
			QueryResults queryResults = resultTable.get(key);
			
			double inputScore = queryResults.getInputScore();
			double outputScore = queryResults.getOutputScore();
			double preconditionScore = queryResults.getPreconditionScore();
			double effectScore = queryResults.getEffectScore();
			double classificationScore = queryResults.getClassificationScore();
			double actorScore = queryResults.getActorScore();
			double productScore = queryResults.getProductScore();
			
			double score = inputScore * PROFILE_INPUT_RATE + outputScore * PROFILE_OUTPUT_RATE 
			               + preconditionScore * PROFILE_PRECONDITION_RATE + effectScore * PROFILE_EFFECT_RATE
			               + classificationScore * PROFILE_CLASSIFICATION_RATE + actorScore * PROFILE_ACTOR_RATE
			               + productScore * PROFILE_PRODUCT_RATE;
			
			queryResults.setProfileScore(score);
		}
		
		return true;
	}
	
	public boolean computeProcessScore(Process srcProcess, Hashtable<String, QueryResults> resultTable) {
		if (resultTable == null) {
			logger.error("parameter is null!");
			return false;
		}
		if (resultTable == null) {
			logger.error("parameter is null!");
			return false;
		}
		
		Enumeration enumeration = resultTable.keys();
		while (enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
			QueryResults queryResults = resultTable.get(key);
			
			Process destProcess = queryResults.getProcess();
			double similarity = processSimilarity.getSimilarity(srcProcess, destProcess);
			queryResults.setProcessScore(similarity);
		}
		
		return true;
		
	}
	
	public boolean computeAtomicProcessPairScore(Process srcProcess, Hashtable<String, QueryResults> resultTable) {
		if (resultTable == null) {
			logger.error("parameter is null!");
			return false;
		}
	
		
		Enumeration enumeration = resultTable.keys();
		while (enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
			QueryResults queryResults = resultTable.get(key);
		
			Hashtable<String, QueryProcessScorer> processScore = queryResults.getQueryProcessScorerHashtable();
			if (processScore == null)
				continue;
			
			Enumeration enumeration1 = processScore.keys();
			
			while (enumeration1.hasMoreElements()) {
				String ProcessKey = (String) enumeration1.nextElement();
				QueryProcessScorer queryProcessScorer = processScore.get(ProcessKey);
				
				
				double inputScore = queryProcessScorer.getInputScore();
				double outputScore = queryProcessScorer.getOutputScore();
				double preconditionScore = queryProcessScorer.getPreconditionScore();
				double effectScore = queryProcessScorer.getEffectScore();
				double score = inputScore * PROCESS_INPUT_RATE + outputScore * PROCESS_OUTPUT_RATE 
				        + preconditionScore * PROCESS_PRECONDITION_RATE + effectScore * PROCESS_EFFECT_RATE;
				
				if (atomicProcessPairSimilarity.containsKey(srcProcess)) {
					Hashtable<Process, Double> processMatchDegree = atomicProcessPairSimilarity.get(srcProcess);
					processMatchDegree.put(queryProcessScorer.getProcess(), score);
				} else {
					Hashtable<Process, Double> processMatchDegree  = new Hashtable<Process, Double>();
					processMatchDegree.put(queryProcessScorer.getProcess(), score);
					atomicProcessPairSimilarity.put(srcProcess, processMatchDegree);
				}
			}
		}		
		
		return true;
	}


	// call clear() after each query 
	public static void clear() {
		 Collection<Hashtable<Process, Double>> valuesCollection = atomicProcessPairSimilarity.values();
		 for (Hashtable<Process, Double> item : valuesCollection)
			 item.clear();
		 
		 atomicProcessPairSimilarity.clear();
	}
	
	
	public boolean getGroundingScore(Hashtable<String, QueryProcessScorer> processScore) {
		
		return true;
	}
}

