package cn.edu.pku.ss.matchmaker.query;


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import cn.edu.pku.ss.matchmaker.index.impl.ServiceBody;

public class QueryResults extends ServiceBody{
	private double profileScore;
	private double processScore;
	private double groundingScore;
	private double inputScore;
	private double outputScore;
	

	private double preconditionScore;
	private double effectScore;
	
	private double classificationScore;
	private double actorScore;
	private double productScore;
	
	private double totalScore;
	
	private double contextScore;
	private Vector<Object> parameterRecord;
	//private Hashtable<String, QueryProcessScorer> queryProcessScorer;
	private Hashtable<String, QueryProcessScorer> processScorertHashtable;
   // private Vector<QueryProcessScorer> queryProcessScorer;
    
//	public QueryResults(ServiceBody serviceBody, double score) {
//		super(serviceBody);
//		
//		this.score = score;
//	}

	
	public QueryResults(ServiceBody serviceBody) {
		super(serviceBody);
		profileScore = 0;
		processScore = 0;
		groundingScore = 0;
		inputScore = 0;
		outputScore =0;
		preconditionScore = 0;
		effectScore = 0;
		
		classificationScore = 0;
		actorScore = 0;
		productScore = 0;
		totalScore = 0;
		contextScore = 0;
		parameterRecord = new Vector<Object> ();
		processScorertHashtable = new Hashtable<String, QueryProcessScorer> ();
	}
	public double getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}
	public double getProfileScore() {
		return profileScore;
	}
	public void setProfileScore(double profileScore) {
		this.profileScore = profileScore;
	}
	public void clear() {
		clearParameterRecord();
		clearProcessScorertHashtable();
	}
	
	public void clearParameterRecord() {
		parameterRecord.clear();
	}
	
	public void clearProcessScorertHashtable() {
		Enumeration enumeration = processScorertHashtable.keys();
		while(enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
			if (processScorertHashtable.containsKey(key)) {
				QueryProcessScorer queryResults = processScorertHashtable.get(key);
				queryResults.clear();
			}
		}
	}
	
	
	public double getInputScore() {
		return inputScore;
	}
	
	public void setInputScore(double inputScore) {
		this.inputScore = inputScore;
	}
	
	public double getOutputScore() {
		return outputScore;
	}
	
	public void setOutputScore(double outputScore) {
		this.outputScore = outputScore;
	}
	
	public double getPreconditionScore() {
		return preconditionScore;
	}
	
	public void setPreconditionScore(double preconditionScore) {
		this.preconditionScore = preconditionScore;
	}
	
	public double getEffectScore() {
		return effectScore;
	}
	
	public void setEffectScore(double effectScore) {
		this.effectScore = effectScore;
	}
	
	public double getClassificationScore() {
		return classificationScore;
	}
	public void setClassificationScore(double classificationScore) {
		this.classificationScore = classificationScore;
	}
	
	public double getActorScore() {
		return actorScore;
	}
	
	public void setActorScore(double actorScore) {
		this.actorScore = actorScore;
	}
	public double getProductScore() {
		return productScore;
	}
	
	public void setProductScore(double productScore) {
		this.productScore = productScore;
	}

	
	public double getContextScore() {
		return contextScore;
	}
	public void setContextScore(double contextScore) {
		this.contextScore = contextScore;
	}
	public Vector<Object> getParameterRecord() {
		return parameterRecord;
	}



	public void setParameterRecord(Vector<Object> parameterRecord) {
		this.parameterRecord = parameterRecord;
	}



	public Hashtable<String, QueryProcessScorer> getQueryProcessScorerHashtable() {
		return processScorertHashtable;
	}



	public void setQueryProcessScorerHashtable(Hashtable<String, QueryProcessScorer> processScorertHashtable) {
		this.processScorertHashtable = processScorertHashtable;
	}
	
//	public double getProfileScore() {
//		return profileScore;
//	}
//
//	public void setProfileScore(double profileScore) {
//		this.profileScore = profileScore;
//	}

	public double getProcessScore() {
		return processScore;
	}

	public void setProcessScore(double processScore) {
		this.processScore = processScore;
	}

	public double getGroundingScore() {
		return groundingScore;
	}

	public void setGroundingScore(double groundingScore) {
		this.groundingScore = groundingScore;
	}
}
