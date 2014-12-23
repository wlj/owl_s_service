package edu.pku.ly.semantic;

import java.util.HashSet;
import java.util.Set;

import org.mindswap.pellet.owlapi.Reasoner;
import org.semanticweb.owl.model.OWLClass;


public class CheckMatchLevelRelationImpl implements CheckMatchLevelRelation{
	
	public ConceptsOfSpecifiedMatchLevel cosm = new ConceptsOfSpecifiedMatchLevelImpl();
	
	public boolean CheckExactMatchRelation(
			Reasoner reasoner, 
			Set<OWLClass> unsatisfiable, 
			OWLClass src, 
			OWLClass dest) 
	{
		if(reasoner == null || src == null || dest == null)
		{
			return false;
		}
		
		Set<String> results_str = new HashSet<String>();
		cosm.GetExactMatchConcepts(reasoner, unsatisfiable, src, results_str);
		
		if(results_str.contains(dest.getURI().toString()))
		{
			return true;
		}
		
		return false;
	}
	
	public boolean CheckPluginMatchRelation(
			Reasoner reasoner, 
			Set<OWLClass> unsatisfiable, 
			OWLClass src,
			OWLClass dest) 
	{
		if(reasoner == null || src == null || dest == null)
		{
			return false;
		}
		
		Set<String> results_str = new HashSet<String>();
		
		cosm.GetPluginMatchConceptsFirst(reasoner, unsatisfiable, src, results_str);
		cosm.GetPluginMatchConceptsSecond(reasoner, src, results_str);
		
		if(results_str.contains(dest.getURI().toString()))
		{
			return true;
		}
		
		return false;
	}
	
	public boolean CheckSubsumeMatchRelation(
			Reasoner reasoner, 
			Set<OWLClass> unsatisfiable, 
			OWLClass src,
			OWLClass dest) 
	{
		if(reasoner == null || src == null || dest == null)
		{
			return false;
		}
		
		Set<String> results_str = new HashSet<String>();
		cosm.GetSubsumeMatchConcepts(reasoner, unsatisfiable, src, results_str);
		
		if(results_str.contains(dest.getURI().toString()))
		{
			return true;
		}
		
		return false;
	}
	
	public boolean CheckNoMatchRelation(
			Reasoner reasoner, 
			Set<OWLClass> unsatisfiable, 
			OWLClass src,
			OWLClass dest) 
	{
		if(reasoner == null || src == null || dest == null)
		{
			return false;
		}
		
		Set<String> results_str = new HashSet<String>();
		Set<String> results = cosm.GetNoMatchConcepts(reasoner, unsatisfiable, src, results_str);
		
		if(results.contains(dest.getURI().toString()))
		{
			return true;
		}
		
		return false;
	}

}
