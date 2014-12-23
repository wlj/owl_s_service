package edu.pku.ly.semantic;

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
	
	private CheckMatchLevelRelationImpl cml;
	private SemanticDistance sd;
	
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
		
		if(cml == null)
		{
			cml = new CheckMatchLevelRelationImpl();
		}
		
		Set<String> results = new HashSet<String>();
		OWLClass tmpclass = fac.getOWLClass(URI.create(concept));
		
		Set<OWLClass> unsatisfiable = reasoner.getInconsistentClasses();
		
		if(MatchLevel.EXACT == level)
		{
			cml.cosm.GetExactMatchConcepts(reasoner, unsatisfiable, tmpclass, results);
		}
		else if(MatchLevel.PLUGIN == level)
		{
			cml.cosm.GetPluginMatchConceptsFirst(reasoner, unsatisfiable, tmpclass, results);
			cml.cosm.GetPluginMatchConceptsSecond(reasoner, tmpclass, results);
		}
		else if(MatchLevel.SUBSUME == level)
		{
			cml.cosm.GetSubsumeMatchConcepts(reasoner, unsatisfiable, tmpclass, results);
		}
		else
		{
			return cml.cosm.GetNoMatchConcepts(reasoner, unsatisfiable, tmpclass, results);
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
		if(cml.CheckExactMatchRelation(reasoner, unsatisfiable, src, dest))
		{
			return MatchLevel.EXACT;
		}
		if(cml.CheckPluginMatchRelation(reasoner, unsatisfiable, src, dest))
		{
			return MatchLevel.PLUGIN;
		}
		if(cml.CheckSubsumeMatchRelation(reasoner, unsatisfiable, src, dest))
		{
			return MatchLevel.SUBSUME;
		}
		if(cml.CheckNoMatchRelation(reasoner, unsatisfiable, src, dest))
		{
			return MatchLevel.NOMATCH;
		}		
		
		return MatchLevel.NOMATCH;
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
		
		return sd.GetDistance(src_node, dest_node);
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
