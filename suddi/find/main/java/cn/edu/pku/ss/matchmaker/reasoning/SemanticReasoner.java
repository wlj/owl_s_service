package cn.edu.pku.ss.matchmaker.reasoning;

import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import cn.edu.pku.ss.matchmaker.index.impl.SemanticDataModel;

public interface SemanticReasoner {
	public boolean loadontologies(String filePath);
	
	public boolean loadOntologies(List<String> uriList);
	
	public boolean loadOntology(String uri);
	
	public Set<String> getAllConcepts();
	
	public Set<OWLEntity> getAllEntities();
	
	public SemanticDataModel getSemanticDataModel(String srcConcept, String destConcept);
	
	public int getSemanticDistance(String srcConcept, String destConcept);
	
	public double getSemanticMatchDegree(String srcConcept, String destConcept);
	
	public MatchLevel getMatchLevel(String srcConcept, String destConcept);
	
	public Set<SemanticDataModel> getConceptsSpecifiedMatchLevel(String concept, MatchLevel level);
	
	public OWLClass getCorrespondingOWLClasses(OWLEntity entity);
	
	public double getSemanticMatchDegree(MatchLevel level, int distance);
	
	public Set<SemanticDataModel> getExactMatch(IRI iri);
	
	public Set<SemanticDataModel> getPluginMatch(IRI iri);
	
	public Set<SemanticDataModel> getSubsumeMatch(IRI iri);
}
