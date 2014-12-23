package edu.pku.ly.semantic.implementation;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mindswap.pellet.owlapi.PelletReasonerFactory;
import org.mindswap.pellet.owlapi.Reasoner;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.inference.OWLReasonerAdapter;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;

import edu.pku.ly.semantic.MatchLevel;
import edu.pku.ly.semantic.SemanticNode;
import edu.pku.ly.semantic.SemanticOpe;

public class SemanticOpeImpl implements SemanticOpe {
	
	final String file1 = "http://localhost:8080/juddiv3/owl-s/1.1/koala.owl";//file:data/koala.owl
	final String ns1 = "http://localhost:8080/juddiv3/owl-s/1.1/koala.owl#";
	final String file2 = "http://localhost:8080/juddiv3/owl-s/1.1/koala2.owl";
	final String ns2 = "http://localhost:8080/juddiv3/owl-s/1.1/koala2.owl#";
	final String file3 = "http://localhost:8080/juddiv3/owl-s/1.1/family.owl";
	final String ns3 = "http://localhost:8080/juddiv3/owl-s/1.1/family#";
	final String stadns = "http://www.w3.org/2002/07/owl#";
	
	final String services = "http://localhost:8080/juddiv3/owl-s/1.1/BravoAirService.owl";
	final String concepts = "http://localhost:8080/juddiv3/owl-s/1.1/Concepts.owl";
	
	private Reasoner reasoner;
	private OWLDataFactory fac;
	private SemanticNode rootnode;
	
	public SemanticOpeImpl()
	{
		try {
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

			URI docIRI1 = URI.create(file1);
			URI docIRI2 = URI.create(file2);
			URI docIRI3 = URI.create(file3);
			URI service = URI.create(services);

			OWLOntology ont3 = manager.loadOntology(docIRI3);
			OWLOntology ont2 = manager.loadOntology(docIRI2);
			OWLOntology ont1 = manager.loadOntology(docIRI1);

			PelletReasonerFactory reasonerFactory = new PelletReasonerFactory();
			reasoner = reasonerFactory.createReasoner(manager);
			reasoner.loadOntology(ont1);
			reasoner.loadOntology(ont2);
			reasoner.loadOntology(ont3);
			
			/*OWLOntology ontservice = manager.loadOntology(service);
			reasoner.loadOntology(ontservice);*/

			reasoner.classify();
			
			fac = manager.getOWLDataFactory();
			
			rootnode = RenderSemanticTreeEntry();
			
		} catch (UnsupportedOperationException exception) {
			System.out.println("Unsupported reasoner operation.");
		} catch (OWLOntologyCreationException e) {
			System.out.println("Could not load the pizza ontology: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Set<String> getConceptsSpecifiedMatchLevel(
			String concept, 
			MatchLevel level)
	{
		if(reasoner == null || fac == null)
			return null;
		
		if(concept == null || concept == "" || level == null)
		{
			return null;
		}
		
		Set<String> results = new HashSet<String>();
		OWLClass tmpclass = fac.getOWLClass(URI.create(concept));
		
		Set<OWLClass> unsatisfiable = reasoner.getInconsistentClasses();
		
		if(MatchLevel.EXACT == level)
		{
			GetExactMatchConcepts(reasoner, unsatisfiable, tmpclass, results);
		}
		else if(MatchLevel.PLUGIN == level)
		{
			GetPluginMatchConceptsFirst(reasoner, unsatisfiable, tmpclass, results);
			GetPluginMatchConceptsSecond(reasoner, tmpclass, results);
		}
		else if(MatchLevel.SUBSUME == level)
		{
			GetSubsumeMatchConcepts(reasoner, unsatisfiable, tmpclass, results);
		}
		else
		{
			return GetNoMatchConcepts(reasoner, unsatisfiable, tmpclass, results);
		}
		
		return results;
	}
	
	public MatchLevel getMatchLevel(String srcConcept, String destConcept)
	{
		if(reasoner == null || fac == null)
			return null;
		
		if(srcConcept == null || destConcept == null)
		{
			return MatchLevel.NOMATCH;
		}
		
		Set<OWLClass> unsatisfiable = reasoner.getInconsistentClasses();
		
		OWLClass src = fac.getOWLClass(URI.create(srcConcept));
		OWLClass dest = fac.getOWLClass(URI.create(destConcept));
		
		//exact
		if(CheckExactMatchRelation(reasoner, unsatisfiable, src, dest))
		{
			return MatchLevel.EXACT;
		}
		if(CheckPluginMatchRelation(reasoner, unsatisfiable, src, dest))
		{
			return MatchLevel.PLUGIN;
		}
		if(CheckSubsumeMatchRelation(reasoner, unsatisfiable, src, dest))
		{
			return MatchLevel.SUBSUME;
		}
		if(CheckNoMatchRelation(reasoner, unsatisfiable, src, dest))
		{
			return MatchLevel.NOMATCH;
		}		
		
		return MatchLevel.NOMATCH;
	}
	
	private boolean CheckExactMatchRelation(
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
		GetExactMatchConcepts(reasoner, unsatisfiable, src, results_str);
		
		if(results_str.contains(dest.getURI().toString()))
		{
			return true;
		}
		
		return false;
	}
	
	private boolean CheckPluginMatchRelation(
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
		
		GetPluginMatchConceptsFirst(reasoner, unsatisfiable, src, results_str);
		GetPluginMatchConceptsSecond(reasoner, src, results_str);
		
		if(results_str.contains(dest.getURI().toString()))
		{
			return true;
		}
		
		return false;
	}
	
	private boolean CheckSubsumeMatchRelation(
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
		GetSubsumeMatchConcepts(reasoner, unsatisfiable, src, results_str);
		
		if(results_str.contains(dest.getURI().toString()))
		{
			return true;
		}
		
		return false;
	}
	
	private boolean CheckNoMatchRelation(
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
		Set<String> results = GetNoMatchConcepts(reasoner, unsatisfiable, src, results_str);
		
		if(results.contains(dest.getURI().toString()))
		{
			return true;
		}
		
		return false;
	}

	private void GetExactMatchConcepts(
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
	
	private void GetPluginMatchConceptsFirst(
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
	
	private void GetPluginMatchConceptsSecond(
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

	private void GetSubsumeMatchConcepts(
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
	
	private Set<String> GetNoMatchConcepts(
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
	
	/*
	 * 
	 * */
	public int getSemanticDistance(
			String srcConcept, 
			String destConcept) 
	{
		if(reasoner == null || fac == null)
			return -1;
		
		if(srcConcept == null || srcConcept == "" || destConcept == null
				|| destConcept == "")
		{
			return -1;
		}
		
		OWLClass src_class = fac.getOWLClass(URI.create(srcConcept));
		OWLClass dest_class = fac.getOWLClass(URI.create(destConcept));
		
		SemanticNode src_node = new SemanticNode(src_class, null);
		SemanticNode dest_node = new SemanticNode(dest_class, null);
		
		return GetDistance(src_node, dest_node);
	}

	private int GetDistance(SemanticNode node1, SemanticNode node2) {
		
		List<SemanticNode> path1 = new ArrayList<SemanticNode>();
		GetNodePath(rootnode, node1, path1);
		path1.add(node1);
		
		List<SemanticNode> path2 = new ArrayList<SemanticNode>();
		GetNodePath(rootnode, node2, path2);
		path2.add(node2);

		return GetSemanticDistance(path1, path2);
	}
	
	private int GetSemanticDistance(List<SemanticNode> path1, List<SemanticNode> path2)
	{
		int len1 = path1.size();
		int len2 = path2.size();
		
		if(IsSubsume(path1, path2))
		{
			return Math.abs(len1 - len2);
		}
		
		SemanticNode lastcommonnode = GetLastCommonNode(path1, path2);
		int lastcommonnode_index = 0;
		if(lastcommonnode != null)
		{
			for(int i = 0; i < path1.size(); i++)
			{
				if(path1.get(i).getCurr().getURI().toString() 
						== lastcommonnode.getCurr().getURI().toString())
				{
					lastcommonnode_index = i;
				}
			}
		}
		
		return len1 + len2 - 2 * lastcommonnode_index - 2;
	}
	
	private boolean IsSubsume(List<SemanticNode> path1, List<SemanticNode> path2) {
		// TODO Auto-generated method stub
		
		int len1 = path1.size();
		int len2 = path2.size();
		
		if(len1 > len2 && path1.get(len2 - 1).getCurr().getURI().toString() == 
			path2.get(len2 - 1).getCurr().getURI().toString())
		{
			return true;
		}
		
		if(len1 < len2 && path1.get(len1 - 1).getCurr().getURI().toString() == 
			path2.get(len1 - 1).getCurr().getURI().toString())
		{
			return true;
		}
		
		return false;
	}
	
	private SemanticNode GetLastCommonNode(List<SemanticNode> path1, List<SemanticNode> path2) {
		// TODO Auto-generated method stub
		
		SemanticNode parent = null;
		
		int len1 = path1.size();
		int len2 = path2.size();
		
		int i = 0, j = 0;
		
		while(i < len1 && j < len2)
		{
			if(path1.get(i).getCurr().getURI().toString()
				== path2.get(j).getCurr().getURI().toString()) 
			{
				parent = path1.get(i);
			}			
			i++;
			j++;
		}
		
		return parent;
	}

	private boolean GetNodePath(
			SemanticNode tmproot, 
			SemanticNode node, 
			List<SemanticNode> path)
	{
		if(tmproot.getCurr().getURI().toString() == node.getCurr().getURI().toString())
		{
			return true;
		}
		
		path.add(tmproot);
		boolean found = false;
		
		List<SemanticNode> nodes = tmproot.getSubNodes();
		int i = 0, len = nodes.size();
		while(!found && i < len)
		{
			found = GetNodePath(nodes.get(i), node, path);
			i++;
		}
		
		if(!found)
			path.remove(path.size() - 1);
		
		return found;
	}
	
	
	/*
	 * 
	 */
	private SemanticNode RenderSemanticTreeEntry()
	{
		Set<OWLClass> unsatisfiable = reasoner.getInconsistentClasses();
		OWLClass thing = fac.getOWLClass(URI.create(stadns + "Thing"));
		
		SemanticNode tmproot = new SemanticNode(thing, null);
		List<SemanticNode> lst_SemanticNode = new ArrayList<SemanticNode>();
		
		Set<OWLClass> direct_subclasses = GetFirstLevelSubClassOfThing(unsatisfiable, thing);
		
		SemanticNode node = null;
		for(OWLClass owlclass : direct_subclasses)
		{
			node = new SemanticNode(owlclass, null);
			lst_SemanticNode.add(node);
		}
		tmproot.setSubNodes(lst_SemanticNode);
		
		RenderSemanticTree(unsatisfiable, reasoner, tmproot, 0);
		
		return tmproot;
	}
	
	private void RenderSemanticTree(
			Set<OWLClass> unsatisfiable, 
			Reasoner reasoner, 
			SemanticNode tmproot, 
			int level) 
	{
		if(reasoner == null || fac == null || tmproot == null || tmproot.getSubNodes() == null)
		{
			return;
		}
		
		level++;
		
		List<SemanticNode> nodes = tmproot.getSubNodes();
		for(SemanticNode node : nodes)
		{
			//get subclass relation
			Set<OWLClass> direct_subClses = getDirectSubClasses(unsatisfiable, node.getCurr());
			
			List<SemanticNode> tmp_nodes = new ArrayList<SemanticNode>();
			SemanticNode tmp_node = null;
			for (OWLClass cls : direct_subClses) 
			{
				tmp_node = new SemanticNode(cls, null);
				tmp_nodes.add(tmp_node);
			}
			node.setSubNodes(tmp_nodes);

			RenderSemanticTree(unsatisfiable, reasoner, node, level);
		}
	}
	
	private Set<OWLClass> GetFirstLevelSubClassOfThing(
			Set<OWLClass> unsatisfiable, 
			OWLClass thing) 
	{
		if(thing == null)
		{
			return null;
		}
		
		Set<OWLClass> firstlevelofthing = new HashSet<OWLClass>();
		
		Set<OWLClass> allsubclassofthing = getDirectSubClasses(unsatisfiable, thing);
		
		for(OWLClass owlclass : allsubclassofthing)
		{
			if(IsDirectSubClassOfThing(owlclass))
			{
				firstlevelofthing.add(owlclass);
			}
		}
		
		return firstlevelofthing;
	}
	
	private boolean IsDirectSubClassOfThing( 
			OWLClass owlclass) 
	{
		if(owlclass == null)
		{
			return false;
		}
		
		Set<OWLClass> owlclasses = getDirectSuperClasses(owlclass);
		for(OWLClass tmpowlclass : owlclasses)
		{
			if(tmpowlclass.isOWLThing())
			{
				return true;
			}
		}
		
		return false;
	}
	
	private Set<OWLClass> getDirectSubClasses(
			Set<OWLClass> unsatisfiable, 
			OWLClass owlclass) 
	{
		if(owlclass == null)
		{
			return null;
		}
		
		Set<OWLClass> subClasses = new HashSet<OWLClass>();
		
		Set<OWLClass> equClasses = reasoner.getAllEquivalentClasses(owlclass);
		equClasses.add(owlclass);
		for (OWLClass tmpOwlClz : equClasses)
		{
			Set<OWLClass> tmpclasses = OWLReasonerAdapter.flattenSetOfSets(reasoner.getSubClasses(tmpOwlClz));
			for(OWLClass tmpclass : tmpclasses)
			{
				if(!unsatisfiable.contains(tmpclass) && !tmpclass.isOWLNothing())
				{
					subClasses.add(tmpclass);
				}
			}
		}

		return subClasses;
	}
	
	private Set<OWLClass> getDirectSuperClasses(
			OWLClass owlClass) 
	{
		if(owlClass == null)
		{
			return null;
		}
		
		Set<OWLClass> superClasses = new HashSet<OWLClass>();

		Set<OWLClass> equClasses = reasoner.getAllEquivalentClasses(owlClass);
		equClasses.add(owlClass);
		for (OWLClass tmpOwlClz : equClasses) {
			superClasses.addAll(OWLReasonerAdapter.flattenSetOfSets(reasoner.getSuperClasses(tmpOwlClz)));
		}	
		
		return superClasses;
	}
}
