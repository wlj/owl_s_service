package cn.edu.pku.ss.matchmaker.query.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import cn.edu.pku.ss.matchmaker.index.Indexer;
import cn.edu.pku.ss.matchmaker.index.impl.ComponentType;
import cn.edu.pku.ss.matchmaker.query.Query;
import cn.edu.pku.ss.matchmaker.query.QueryResults;
import cn.edu.pku.ss.matchmaker.query.filters.QueryFilter;
import cn.edu.pku.ss.matchmaker.query.filters.impl.InputsFilter;
import cn.edu.pku.ss.matchmaker.query.filters.impl.OutputsFilter;
import cn.edu.pku.ss.matchmaker.util.AtomicProcessExtractor;


import EDU.cmu.Atlas.owls1_1.process.Process;

public class ProcessQuery {
	private Indexer indexer;
	private Scorer scorer;
	QueryFilter filter;
	Logger logger;
	
	public ProcessQuery(Indexer indexer, Scorer scorer) {
		// TODO Auto-generated constructor stub
		this.indexer = indexer;
		this.scorer = scorer;
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.query.impl.ProcessQuery.class);
		
		init();
	}
	
	private void init() {
		// TODO
		// add precondition and effect
		filter = new InputsFilter(indexer, ComponentType.PROCESS);
		QueryFilter outputFilter = new OutputsFilter(indexer, ComponentType.PROCESS);
		filter.addNextFilter(outputFilter);
	}
	
	public boolean singleProcess(Process process, Hashtable<String, QueryResults> resultTable) {
		if (process == null || resultTable == null) {
			logger.error("parameter is empty or parameter is illegal!");
			return false;
		}
		
		Query query = new Query(process.getInputList(), process.getOutputList(), process.getPreConditionList(),
				process.getResultList(), null, null, null);
		
		filter.process(query, resultTable);
		if (scorer.computeAtomicProcessPairScore(process, resultTable) == false) {
			logger.error("failed to compute process score!");
			return false;
		}
		
		return true;
	}
	
	public boolean process(Process process, Hashtable<String, QueryResults> resultTable) {
		if (process == null || resultTable == null) {
			logger.error("parameter is empty or parameter is illegal!");
			return false;
		}
		
		List<Process> atomicList = new ArrayList<Process>();
		AtomicProcessExtractor.getAtomics(process, atomicList);
	
	
		List<Hashtable<String, QueryResults> > result = new ArrayList<Hashtable<String,QueryResults> > ();
		int atomicSize = atomicList.size();
		for (int i = 0; i < atomicList.size(); ++i) {
			Process tmpProcess = atomicList.get(i);
			if (tmpProcess == null)
				continue;
			
			// process single process	
			Hashtable<String,QueryResults> tmpResult = new Hashtable<String, QueryResults> ();	
			
			if (singleProcess(tmpProcess, tmpResult))
				result.add(tmpResult);	
		}
		
		// merge List<Hashtable<String, QueryResults> > to resultTable
		for (int i = 0; i < result.size(); ++i) {
			Hashtable<String, QueryResults> tmpResult = result.get(i);
			Enumeration enumeration = tmpResult.keys();		
			while(enumeration.hasMoreElements()) {	
				String key = (String) enumeration.nextElement();
				QueryResults queryResults = tmpResult.get(key);
				
				if (resultTable.containsKey(key) == false)
					resultTable.put(key, queryResults);
			}
		}
		
		
		if (scorer.computeProcessScore(process, resultTable) == false) {
			logger.error("failed to compute process score");
			return false;
		}
		
		return true;
	}	
}
