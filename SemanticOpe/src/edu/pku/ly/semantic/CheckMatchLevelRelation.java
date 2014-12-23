package edu.pku.ly.semantic;

import java.util.Set;

import org.mindswap.pellet.owlapi.Reasoner;
import org.semanticweb.owl.model.OWLClass;

public interface CheckMatchLevelRelation {
	
	public boolean CheckExactMatchRelation(
			Reasoner reasoner, 
			Set<OWLClass> unsatisfiable, 
			OWLClass src, 
			OWLClass dest);
	
	public boolean CheckPluginMatchRelation(
			Reasoner reasoner, 
			Set<OWLClass> unsatisfiable, 
			OWLClass src,
			OWLClass dest);
	
	public boolean CheckSubsumeMatchRelation(
			Reasoner reasoner, 
			Set<OWLClass> unsatisfiable, 
			OWLClass src,
			OWLClass dest);
	
	public boolean CheckNoMatchRelation(
			Reasoner reasoner, 
			Set<OWLClass> unsatisfiable, 
			OWLClass src,
			OWLClass dest);

}
