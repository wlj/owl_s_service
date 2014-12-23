package edu.pku.ly.semantic;

import java.util.Set;

public interface SemanticOpe {
	
	public int getSemanticDistance(String srcConcept, String destConcept);
	
	public Set<String> getConceptsSpecifiedMatchLevel(String concept, MatchLevel level);
	
	public MatchLevel getMatchLevel(String srcConcept, String destConcept);
}
