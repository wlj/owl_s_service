package cn.edu.pku.ss.matchmaker.index;

import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import EDU.pku.ly.owlsservice.ExtendedService;


import cn.edu.pku.ss.matchmaker.index.impl.SemanticDataModel;
import cn.edu.pku.ss.matchmaker.index.impl.ServiceDataModel;
import cn.edu.pku.ss.matchmaker.reasoning.MatchLevel;

public interface Indexer {
	// brief: before using other methods, please call init()
	// return: ture - succeed
	//         false - failed
	public abstract boolean init(String filePath);
	
	public abstract boolean process();
	public boolean processForTest(List<ExtendedService> serviceList);
	
	public abstract List<ExtendedService> getExtendedServiceList();
	public abstract void setExtendedServiceList(List<ExtendedService> extendedServiceList);
	
	// brief: finish indexing data
	// return: true - succeed
	//         false - failed
	public abstract boolean processServices();
	
	public abstract boolean processSemanticRelation();
	
//	public abstract boolean processFotTest();
	
	public abstract Hashtable<String, ServiceDataModel> getServiceIndexer();
	
	public abstract Hashtable<String, Hashtable<String, SemanticDataModel> > getsemanticIndexer();
	
	public abstract Hashtable<String, Hashtable<MatchLevel, Set<String>> > getmatchLevelIndexer();
	
	// to profile interface
	public abstract Vector<Object> getClassificationServices(String concept);
	
	public abstract Vector<Object> getActorServices(String concept);
	
	public abstract Vector<Object> getProfileInputServices(String concept);
	
	public abstract Vector<Object> getProfileOutputServices(String concept);
	
	public abstract Vector<Object> getServiceProductServices(String concept);
	
	
	// to process interface
    public abstract Vector<Object> getProcessInputServices(String concept);
	
	public abstract Vector<Object> getProcessOutputServices(String concept);
	
	// to reasoner
	public abstract SemanticDataModel getReasonDataModel(String srcConcept, String destConcept);
	
	public abstract int getSemanticDistance(String srcConcept, String destConcept);
	
	public abstract double getSemanticMatchDegree(String srcConcept, String destConcept);
	
	public abstract MatchLevel getSemanticMatchLevel(String srcConcept, String destConcept);
	
	public abstract Set<String> getSpecifiedMatchLevelConcept(String concept, MatchLevel level);
	
	// main include EXACT, EXACT-ALMOST, PLUGIN and SUBSUME
	public abstract Set<String> getAllSimilarConcepts(String concept);
}
