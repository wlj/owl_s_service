package edu.pku.ly.semantic;

import java.util.Set;

import org.mindswap.pellet.owlapi.Reasoner;
import org.semanticweb.owl.model.OWLClass;

public interface ConceptsOfSpecifiedMatchLevel {
	
	public void GetExactMatchConcepts(
			Reasoner reasoner, 
			Set<OWLClass> unsatisfiable, 
			OWLClass owlclass, 
			Set<String> results);
	
	public void GetPluginMatchConceptsFirst(
			Reasoner reasoner, 
			Set<OWLClass> unsatisfiable,
			OWLClass owlclass, 
			Set<String> results);
	
	public void GetPluginMatchConceptsSecond(
			Reasoner reasoner, 
			OWLClass owlclass, 
			Set<String> results);

	public void GetSubsumeMatchConcepts(
			Reasoner reasoner, 
			Set<OWLClass> unsatisfiable, 
			OWLClass owlclass, 
			Set<String> results);
	
	public Set<String> GetNoMatchConcepts(
			Reasoner reasoner, 
			Set<OWLClass> unsatisfiable, 
			OWLClass owlclass,
			Set<String> results);
}
