package cn.edu.pku.ss.matchmaker.index.impl;

import cn.edu.pku.ss.matchmaker.reasoning.MatchLevel;

public class SemanticDataModel {
	String srcConcpet;
	String destConcept;
	MatchLevel matchLevel;
	int distance;
	double matchDegree;
	
	public SemanticDataModel(String srcConcpet, String destConcept, MatchLevel matchLevel,
			int distance, double matchDegree) {
		this.srcConcpet = srcConcpet;
		this.destConcept = destConcept;
		this.matchLevel = matchLevel;
		this.distance = distance;
		this.matchDegree = matchDegree;
	}
	
	public SemanticDataModel() {
		// TODO Auto-generated constructor stub
	}
	
	public MatchLevel getMatchLevel() {
		return matchLevel;
	}

	public void setMatchLevel(MatchLevel matchLevel) {
		this.matchLevel = matchLevel;
	}

	public String getSrcConcpet() {
		return srcConcpet;
	}
	
	public void setSrcConcpet(String srcConcpet) {
		this.srcConcpet = srcConcpet;
	}
	
	public String getDestConcept() {
		return destConcept;
	}
	
	public void setDestConcept(String destConcept) {
		this.destConcept = destConcept;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public double getMatchDegree() {
		return matchDegree;
	}
	
	public void setMatchDegree(double matchDegree) {
		this.matchDegree = matchDegree;
	}
}

