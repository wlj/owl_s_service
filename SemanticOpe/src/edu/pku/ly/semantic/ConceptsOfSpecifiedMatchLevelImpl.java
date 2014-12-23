package edu.pku.ly.semantic;

import java.util.HashSet;
import java.util.Set;

import org.mindswap.pellet.owlapi.Reasoner;
import org.semanticweb.owl.inference.OWLReasonerAdapter;
import org.semanticweb.owl.model.OWLClass;


public class ConceptsOfSpecifiedMatchLevelImpl implements ConceptsOfSpecifiedMatchLevel {
	
	public void GetExactMatchConcepts(
			Reasoner reasoner, 
			Set<OWLClass> unsatisfiable, 
			OWLClass owlclass, 
			Set<String> results) 
	{
		if(reasoner == null || owlclass == null)
		{
			return;
		}
		
		Set<OWLClass> equClasses = reasoner.getAllEquivalentClasses(owlclass);
		
		//semantically equal
		for(OWLClass tmpclass : equClasses)
		{
			if(!unsatisfiable.contains(tmpclass) 
					&& !tmpclass.isOWLNothing() 
					&& !tmpclass.isOWLThing()
					&& tmpclass.getURI().toString() != owlclass.getURI().toString())
			{
				results.add(tmpclass.getURI().toString());
			}
		}
		
		//direct super class
		Set<OWLClass> direct_superclasses = OWLReasonerAdapter.flattenSetOfSets(reasoner
				.getSuperClasses(owlclass));
		for(OWLClass superclass : direct_superclasses)
		{
			if(!unsatisfiable.contains(superclass) && !superclass.isOWLNothing() && !superclass.isOWLThing())
			{
				results.add(superclass.getURI().toString());
			}
		}
		
	}
	
	public void GetPluginMatchConceptsFirst(
			Reasoner reasoner, 
			Set<OWLClass> unsatisfiable,
			OWLClass owlclass, 
			Set<String> results) 
	{
		if(reasoner == null || owlclass == null)
		{
			return;
		}
		
		Set<OWLClass> direct_superclasses = OWLReasonerAdapter.flattenSetOfSets(reasoner
				.getSuperClasses(owlclass));
		
		for(OWLClass superclass : direct_superclasses)
		{
			if(!unsatisfiable.contains(superclass) && !superclass.isOWLNothing() && !superclass.isOWLThing())
			{
				results.add(superclass.getURI().toString());
				GetPluginMatchConceptsFirst(reasoner, unsatisfiable, superclass, results);
			}
		}
	}
	
	public void GetPluginMatchConceptsSecond(
			Reasoner reasoner, 
			OWLClass owlclass, 
			Set<String> results) 
	{
		if(reasoner == null || owlclass == null)
		{
			return;
		}
		
		Set<OWLClass> direct_superclasses = OWLReasonerAdapter.flattenSetOfSets(reasoner
				.getSuperClasses(owlclass));
		for(OWLClass superclass : direct_superclasses)
		{
			results.remove(superclass.getURI().toString());
		}		
	}

	public void GetSubsumeMatchConcepts(
			Reasoner reasoner, 
			Set<OWLClass> unsatisfiable, 
			OWLClass owlclass, 
			Set<String> results) 
	{
		if(reasoner == null || owlclass == null)
		{
			return;
		}
		
		Set<OWLClass> direct_subclasses = OWLReasonerAdapter.flattenSetOfSets(reasoner
				.getSubClasses(owlclass));
		
		for(OWLClass subclass : direct_subclasses)
		{
			if(!unsatisfiable.contains(subclass) && !subclass.isOWLNothing() && !subclass.isOWLThing())
			{
				results.add(subclass.getURI().toString());
				GetSubsumeMatchConcepts(reasoner, unsatisfiable, subclass, results);
			}
		}
	}
	
	public Set<String> GetNoMatchConcepts(
			Reasoner reasoner, 
			Set<OWLClass> unsatisfiable, 
			OWLClass owlclass,
			Set<String> results) 
	{
		if(reasoner == null || owlclass == null)
		{
			return new HashSet<String>();
		}
		
		Set<OWLClass> allclasses = reasoner.getClasses();
		Set<String> allstrings = new HashSet<String>();
		Set<String> new_results = new HashSet<String>();
		
		for(OWLClass tmpclass : allclasses)
		{
			allstrings.add(tmpclass.getURI().toString());
		}
		GetExactMatchConcepts(reasoner, unsatisfiable, owlclass, results);
		GetPluginMatchConceptsFirst(reasoner, unsatisfiable, owlclass, results);
		GetPluginMatchConceptsSecond(reasoner, owlclass, results);
		GetSubsumeMatchConcepts(reasoner, unsatisfiable, owlclass, results);
		
		for(String str : allstrings)
		{
			if(!results.contains(str))
			{
				new_results.add(str);
			}
		}
		
		return new_results;		
	}
	
	
}
