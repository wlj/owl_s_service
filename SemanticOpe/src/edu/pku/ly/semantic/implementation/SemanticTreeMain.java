package edu.pku.ly.semantic.implementation;
//package edu.pku.ly.semantic;
//
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.mindswap.pellet.owlapi.PelletReasonerFactory;
//import org.mindswap.pellet.owlapi.Reasoner;
//import org.semanticweb.owl.apibinding.OWLManager;
//import org.semanticweb.owl.inference.OWLReasonerAdapter;
//import org.semanticweb.owl.model.OWLClass;
//import org.semanticweb.owl.model.OWLDataFactory;
//import org.semanticweb.owl.model.OWLOntology;
//import org.semanticweb.owl.model.OWLOntologyCreationException;
//import org.semanticweb.owl.model.OWLOntologyManager;
//
//import edu.pku.ly.semantic.implementation.SemanticOpeImpl;
//
//public class SemanticTreeMain {
//
//	final static String file1 = "http://localhost:8080/juddiv3/owl-s/1.1/koala.owl";//file:data/koala.owl
//	final static String ns1 = "http://localhost:8080/juddiv3/owl-s/1.1/koala.owl#";
//	final static String file2 = "http://localhost:8080/juddiv3/owl-s/1.1/koala2.owl";
//	final static String ns2 = "http://localhost:8080/juddiv3/owl-s/1.1/koala2.owl#";
//	final static String file3 = "http://localhost:8080/juddiv3/owl-s/1.1/family.owl";
//	final static String ns3 = "http://localhost:8080/juddiv3/owl-s/1.1/family#";
//	final static String stadns = "http://www.w3.org/2002/07/owl#";
//	
//	final static String services = "http://localhost:8080/juddiv3/owl-s/1.1/BravoAirService.owl";
//	final static String concepts = "http://localhost:8080/juddiv3/owl-s/1.1/Concepts.owl";
//	
//	public static void main(String[] args) {
//		
//		SemanticOpe ope = new SemanticOpeImpl();
//		//1
//		System.out.println("(1)getMatchLevel interface test:");
//		MatchLevel level = ope.getMatchLevel( 
//				ns2 + "SuperSchoolWithTwoParents",
//				ns1 + "University"
//				);
//		System.out.println(level);
//		
//		//2
//		level = ope.getMatchLevel(
//				ns1 + "University",
//				ns2 + "SuperSchool"
//				);
//		System.out.println(level);
//		
//		//3
//		level = ope.getMatchLevel(
//				ns1 + "DryEucalyptForest",
//				ns1 + "University"
//				);
//		System.out.println(level);
//		
//		//4
//		level = ope.getMatchLevel(
//				ns2 + "SuperSchool",
//				ns1 + "University"
//				);
//		System.out.println(level);
//		
//		//5
//		level = ope.getMatchLevel(
//				ns1 + "University",
//				ns2 + "SuperSchool"
//				);
//		System.out.println(level);
//		
//		System.out.println("(2)getConceptsSpecifiedMatchLevel interface test:");
//		String university = ns1 + "University";
//		//1
//		Set<String> exact_results = ope.getConceptsSpecifiedMatchLevel(university, MatchLevel.EXACT);
//		System.out.println("****EXACT match results:");
//		for(String str : exact_results)
//		{
//			System.out.println(str);
//		}
//		//2
//		exact_results = ope.getConceptsSpecifiedMatchLevel(university, MatchLevel.PLUGIN);
//		System.out.println("****PLUGIN match results:");
//		for(String str : exact_results)
//		{
//			System.out.println(str);
//		}
//		
//		//3
//		exact_results = ope.getConceptsSpecifiedMatchLevel(university, MatchLevel.SUBSUME);
//		System.out.println("****SUBSUME match results:");
//		for(String str : exact_results)
//		{
//			System.out.println(str);
//		}
//		
//		//4
//		exact_results = ope.getConceptsSpecifiedMatchLevel(university, MatchLevel.NOMATCH);
//		System.out.println("****NOMATCH match results:");
//		for(String str : exact_results)
//		{
//			System.out.println(str);
//		}
//		
//		//[3]
//		System.out.println("(3)getSemanticDistance interface test:");
//		
//		String _person = ns1 + "Person";
//		String _thirdsuperschool = ns2 + "ThirdSuperSchool";
//		
//		String _forthsuperschool = ns2 + "ForthSuperSchool";
//		String _anothersuperschoolsubclass2lvl = ns2 + "AnotherSuperSchoolSubClass2Lvl";
//		String _superschool = ns2 + "SuperSchool";
//		
//		System.out.println(ope.getSemanticDistance(_person, _thirdsuperschool));
//		System.out.println(ope.getSemanticDistance(_forthsuperschool, _anothersuperschoolsubclass2lvl));
//		System.out.println(ope.getSemanticDistance(_forthsuperschool, _superschool));
//		
//		System.out.println('\n');
//		
//		try {
//			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
//
//			URI docIRI1 = URI.create(file1);
//			URI docIRI2 = URI.create(file2);
//			URI docIRI3 = URI.create(file3);
//			
//			URI service = URI.create(services);
//
//			OWLOntology ont3 = manager.loadOntology(docIRI3);
//			System.out.println("Loaded " + ont3.getURI());
//			OWLOntology ont2 = manager.loadOntology(docIRI2);
//			System.out.println("Loaded " + ont2.getURI());
//			OWLOntology ont1 = manager.loadOntology(docIRI1);
//			System.out.println("Loaded " + ont1.getURI());
//
//			PelletReasonerFactory reasonerFactory = new PelletReasonerFactory();
//			Reasoner reasoner = reasonerFactory.createReasoner(manager);
//			reasoner.loadOntology(ont1);
//			reasoner.loadOntology(ont2);
//			reasoner.loadOntology(ont3);
//			
//			/*OWLOntology ontservice = manager.loadOntology(service);
//			System.out.println("Loaded " + ontservice.getURI());
//			reasoner.loadOntology(ontservice);*/
//
//			reasoner.classify();
//			
//			//Consistent
//			boolean consistent = reasoner.isConsistent();
//			System.out.println("Consistent: " + consistent);
//			System.out.println("\n");
//			Set<OWLClass> unsatisfiable = reasoner.getInconsistentClasses();
//			if (!unsatisfiable.isEmpty()) {
//				System.out.println("The following classes are unsatisfiable: ");
//				for (OWLClass cls : unsatisfiable) {
//					System.out.println("    " + cls);
//				}
//			} else {
//				System.out.println("There are no unsatisfiable classes");
//			}
//			System.out.println("\n");
//
//			OWLDataFactory fac = manager.getOWLDataFactory();
//			OWLClass habitat = fac.getOWLClass(URI.create(ns1 + "Habitat"));
//			OWLClass bigschool = fac.getOWLClass(URI.create(ns2 + "BigSchool"));
//			
//			//test
//			System.out.println("super class of habitat:");
//			Set<OWLClass> parents = getDirectSuperClasses(reasoner, habitat);
//			for (OWLClass cls : parents) {
//				System.out.println("    " + cls);
//				
//				System.out.println(cls.getURI());
//			}
//			
//			//generate a tree by parsing the ontology file
//			SemanticNode root = RenderSemanticTreeEntry(reasoner);
//			
//			//1
//			MatchLevel matchlevel = getMatchLevel(reasoner, fac, 
//					"http://localhost:8080/juddiv3/owl-s/1.1/koala2.owl#SuperSchoolWithTwoParents",
//					"http://localhost:8080/juddiv3/owl-s/1.1/koala.owl#University");
//			System.out.println(level);
//			
//			//2
//			matchlevel = getMatchLevel(reasoner, fac, 
//					"http://localhost:8080/juddiv3/owl-s/1.1/koala.owl#University",
//					"http://localhost:8080/juddiv3/owl-s/1.1/koala2.owl#SuperSchoolWithTwoParents"
//					);
//			System.out.println(level);
//			
//			//3
//			matchlevel = getMatchLevel(reasoner, fac, 
//					"http://localhost:8080/juddiv3/owl-s/1.1/koala.owl#DryEucalyptForest",
//					"http://localhost:8080/juddiv3/owl-s/1.1/koala.owl#University");
//			System.out.println(level);
//			
//			//4
//			matchlevel = getMatchLevel(reasoner, fac, 
//					"http://localhost:8080/juddiv3/owl-s/1.1/koala2.owl#SuperSchool",
//					"http://localhost:8080/juddiv3/owl-s/1.1/koala.owl#University");
//			System.out.println(level);
//			
//			//5
//			matchlevel = getMatchLevel(reasoner, fac, 
//					"http://localhost:8080/juddiv3/owl-s/1.1/koala.owl#University",
//					"http://localhost:8080/juddiv3/owl-s/1.1/koala2.owl#SuperSchool"
//					);
//			System.out.println(level);
//			
//			//compute 
//			Set<String> childs = getConceptsSpecifiedMatchLevel(reasoner, fac, unsatisfiable, bigschool.getURI().toString(), MatchLevel.PLUGIN);
//			for (String cls : childs) {
//				System.out.println("    " + cls);
//			}
//			
//			//compute semantic distance
//			OWLClass person = fac.getOWLClass(URI.create(ns1 + "Person"));// in koala
//			OWLClass thirdsuperschool = fac.getOWLClass(URI.create(ns2 + "ThirdSuperSchool"));
//			
//			OWLClass forthsuperschool = fac.getOWLClass(URI.create(ns2 + "ForthSuperSchool"));
//			OWLClass anothersuperschoolsubclass2lvl = fac.getOWLClass(URI.create(ns2 + "AnotherSuperSchoolSubClass2Lvl"));
//			OWLClass superschool = fac.getOWLClass(URI.create(ns2 + "SuperSchool"));
//			
//			SemanticNode node1 = new SemanticNode(person, null);
//			SemanticNode node2 = new SemanticNode(thirdsuperschool, null);
//			System.out.println("//1");
//			GetCommonNodeAndDistance(root, node1, node2);//1
//			
//			node1 = new SemanticNode(forthsuperschool, null);
//			node2 = new SemanticNode(anothersuperschoolsubclass2lvl, null);
//			System.out.println("//2");
//			GetCommonNodeAndDistance(root, node1, node2);//2
//			
//			node2 = new SemanticNode(superschool, null);
//			System.out.println("//3");
//			GetCommonNodeAndDistance(root, node1, node2);//3
//
//		} catch (UnsupportedOperationException exception) {
//			System.out.println("Unsupported reasoner operation.");
//		} catch (OWLOntologyCreationException e) {
//			System.out.println("Could not load the pizza ontology: " + e.getMessage());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public static MatchLevel getMatchLevel(Reasoner reasoner, OWLDataFactory fac, String srcConcept, String destConcept)
//	{
//		if(reasoner == null || fac == null || srcConcept == null || destConcept == null)
//		{
//			return MatchLevel.NOMATCH;
//		}
//		
//		Set<OWLClass> unsatisfiable = reasoner.getInconsistentClasses();
//		
//		OWLClass src = fac.getOWLClass(URI.create(srcConcept));
//		OWLClass dest = fac.getOWLClass(URI.create(destConcept));
//		
//		//exact
//		if(CheckExactMatchRelation(reasoner, unsatisfiable, src, dest))
//		{
//			return MatchLevel.EXACT;
//		}
//		if(CheckPluginMatchRelation(reasoner, unsatisfiable, src, dest))
//		{
//			return MatchLevel.PLUGIN;
//		}
//		if(CheckSubsumeMatchRelation(reasoner, unsatisfiable, src, dest))
//		{
//			return MatchLevel.SUBSUME;
//		}
//		if(CheckNoMatchRelation(reasoner, unsatisfiable, src, dest))
//		{
//			return MatchLevel.NOMATCH;
//		}		
//		
//		return MatchLevel.NOMATCH;
//	}
//	
//	private static boolean CheckNoMatchRelation(
//			Reasoner reasoner, 
//			Set<OWLClass> unsatisfiable, 
//			OWLClass src,
//			OWLClass dest) 
//	{
//		if(reasoner == null || src == null || dest == null)
//		{
//			return false;
//		}
//		
//		Set<String> results_str = new HashSet<String>();
//		Set<String> results = GetNoMatchConcepts(reasoner, unsatisfiable, src, results_str);
//		
//		if(results.contains(dest.getURI().toString()))
//		{
//			return true;
//		}
//		
//		return false;
//	}
//
//	private static boolean CheckSubsumeMatchRelation(
//			Reasoner reasoner, 
//			Set<OWLClass> unsatisfiable, 
//			OWLClass src,
//			OWLClass dest) 
//	{
//		if(reasoner == null || src == null || dest == null)
//		{
//			return false;
//		}
//		
//		Set<String> results_str = new HashSet<String>();
//		GetSubsumeMatchConcepts(reasoner, unsatisfiable, src, results_str);
//		
//		if(results_str.contains(dest.getURI().toString()))
//		{
//			return true;
//		}
//		
//		return false;
//	}
//
//	private static boolean CheckPluginMatchRelation(
//			Reasoner reasoner, 
//			Set<OWLClass> unsatisfiable, 
//			OWLClass src,
//			OWLClass dest) 
//	{
//		if(reasoner == null || src == null || dest == null)
//		{
//			return false;
//		}
//		
//		Set<String> results_str = new HashSet<String>();
//		
//		GetPluginMatchConceptsFirst(reasoner, unsatisfiable, src, results_str);
//		GetPluginMatchConceptsSecond(reasoner, src, results_str);
//		
//		if(results_str.contains(dest.getURI().toString()))
//		{
//			return true;
//		}
//		
//		return false;
//	}
//
//	private static boolean CheckExactMatchRelation(
//			Reasoner reasoner, 
//			Set<OWLClass> unsatisfiable, 
//			OWLClass src, 
//			OWLClass dest) 
//	{
//		if(reasoner == null || src == null || dest == null)
//		{
//			return false;
//		}
//		
//		Set<String> results_str = new HashSet<String>();
//		GetExactMatchConcepts(reasoner, unsatisfiable, src, results_str);
//		
//		if(results_str.contains(dest.getURI().toString()))
//		{
//			return true;
//		}
//		
//		return false;
//	}
//
//	public static Set<String> getConceptsSpecifiedMatchLevel(
//			Reasoner reasoner, 
//			OWLDataFactory fac, 
//			Set<OWLClass> unsatisfiable, 
//			String concept, 
//			MatchLevel level)
//	{
//		if(reasoner == null || fac == null || concept == null || 
//				concept == "" || level == null)
//		{
//			return new HashSet<String>();
//		}
//		
//		Set<String> results = new HashSet<String>();
//		OWLClass tmpclass = fac.getOWLClass(URI.create(concept));
//		
//		if(MatchLevel.EXACT == level)
//		{
//			GetExactMatchConcepts(reasoner, unsatisfiable, tmpclass, results);
//		}
//		else if(MatchLevel.PLUGIN == level)
//		{
//			GetPluginMatchConceptsFirst(reasoner, unsatisfiable, tmpclass, results);
//			GetPluginMatchConceptsSecond(reasoner, tmpclass, results);
//		}
//		else if(MatchLevel.SUBSUME == level)
//		{
//			GetSubsumeMatchConcepts(reasoner, unsatisfiable, tmpclass, results);
//		}
//		else
//		{
//			return GetNoMatchConcepts(reasoner, unsatisfiable, tmpclass, results);
//		}
//		
//		return results;
//	}
//
//	private static void GetPluginMatchConceptsSecond(Reasoner reasoner, OWLClass tmpclass, Set<String> results) {
//		
//		Set<OWLClass> direct_superclasses = OWLReasonerAdapter.flattenSetOfSets(reasoner
//				.getSuperClasses(tmpclass));
//		for(OWLClass owlclass : direct_superclasses)
//		{
//			results.remove(owlclass.getURI().toString());
//		}		
//	}
//
//	private static Set<String> GetNoMatchConcepts(
//			Reasoner reasoner, 
//			Set<OWLClass> unsatisfiable, 
//			OWLClass owlclass,
//			Set<String> results) 
//	{
//		if(reasoner == null || owlclass == null)
//		{
//			return new HashSet<String>();
//		}
//		
//		Set<OWLClass> allclasses = reasoner.getClasses();
//		Set<String> allstrings = new HashSet<String>();
//		Set<String> new_results = new HashSet<String>();
//		
//		for(OWLClass tmpclass : allclasses)
//		{
//			allstrings.add(tmpclass.getURI().toString());
//		}
//		GetExactMatchConcepts(reasoner, unsatisfiable, owlclass, results);
//		GetPluginMatchConceptsFirst(reasoner, unsatisfiable, owlclass, results);
//		GetPluginMatchConceptsSecond(reasoner, owlclass, results);
//		GetSubsumeMatchConcepts(reasoner, unsatisfiable, owlclass, results);
//		
//		for(String str : allstrings)
//		{
//			if(!results.contains(str))
//			{
//				new_results.add(str);
//			}
//		}
//		
//		return new_results;		
//	}
//
//	private static void GetExactMatchConcepts(
//			Reasoner reasoner, 
//			Set<OWLClass> unsatisfiable, 
//			OWLClass owlclass, 
//			Set<String> results) 
//	{
//		if(reasoner == null || owlclass == null)
//		{
//			return;
//		}
//		
//		Set<OWLClass> equClasses = reasoner.getAllEquivalentClasses(owlclass);
//		
//		//semantically equal
//		for(OWLClass tmpclass : equClasses)
//		{
//			if(!unsatisfiable.contains(tmpclass) && !tmpclass.isOWLNothing() && !tmpclass.isOWLThing())
//			{
//				results.add(tmpclass.getURI().toString());
//			}
//		}
//		
//		//direct super class
//		Set<OWLClass> direct_superclasses = OWLReasonerAdapter.flattenSetOfSets(reasoner
//				.getSuperClasses(owlclass));
//		for(OWLClass superclass : direct_superclasses)
//		{
//			if(!unsatisfiable.contains(superclass) && !superclass.isOWLNothing() && !superclass.isOWLThing())
//			{
//				results.add(superclass.getURI().toString());
//			}
//		}
//		
//	}
//
//	private static void GetPluginMatchConceptsFirst(
//			Reasoner reasoner, 
//			Set<OWLClass> unsatisfiable,
//			OWLClass owlclass, 
//			Set<String> results) 
//	{
//		if(reasoner == null || owlclass == null)
//		{
//			return;
//		}
//		
//		Set<OWLClass> direct_superclasses = OWLReasonerAdapter.flattenSetOfSets(reasoner
//				.getSuperClasses(owlclass));
//		
//		for(OWLClass superclass : direct_superclasses)
//		{
//			if(!unsatisfiable.contains(superclass) && !superclass.isOWLNothing() && !superclass.isOWLThing())
//			{
//				results.add(superclass.getURI().toString());
//				GetPluginMatchConceptsFirst(reasoner, unsatisfiable, superclass, results);
//			}
//		}
//	}
//
//	private static void GetSubsumeMatchConcepts(
//			Reasoner reasoner, 
//			Set<OWLClass> unsatisfiable, 
//			OWLClass owlclass, 
//			Set<String> results) 
//	{
//		if(reasoner == null || owlclass == null)
//		{
//			return;
//		}
//		
//		Set<OWLClass> direct_subclasses = OWLReasonerAdapter.flattenSetOfSets(reasoner
//				.getSubClasses(owlclass));
//		
//		for(OWLClass subclass : direct_subclasses)
//		{
//			if(!unsatisfiable.contains(subclass) && !subclass.isOWLNothing() && !subclass.isOWLThing())
//			{
//				results.add(subclass.getURI().toString());
//				GetSubsumeMatchConcepts(reasoner, unsatisfiable, subclass, results);
//			}
//		}
//	}
//
//	private static void GetCommonNodeAndDistance(SemanticNode root, SemanticNode node1, SemanticNode node2) {
//		
//		List<SemanticNode> path1 = new ArrayList<SemanticNode>();
//		GetNodePath(root, node1, path1);
//		path1.add(node1);
//		
//		List<SemanticNode> path2 = new ArrayList<SemanticNode>();
//		GetNodePath(root, node2, path2);
//		path2.add(node2);
//		
//		System.out.print("The last common node of " + node1.getCurr().toString() 
//				+ " and " + node2.getCurr().toString() + " is: ");
//		System.out.println(GetLastCommonNode(path1, path2));
//		
//		System.out.print("The semantic distance between " + node1.getCurr().toString() 
//				+ " and " + node2.getCurr().toString() + " is: ");
//		System.out.println(GetSemanticDistance(path1, path2));
//	}
//
//	private static SemanticNode GetLastCommonNode(List<SemanticNode> path1, List<SemanticNode> path2) {
//		// TODO Auto-generated method stub
//		
//		SemanticNode parent = null;
//		
//		int len1 = path1.size();
//		int len2 = path2.size();
//		
//		int i = 0, j = 0;
//		
//		while(i < len1 && j < len2)
//		{
//			if(path1.get(i).getCurr().getURI().toString()
//				== path2.get(j).getCurr().getURI().toString()) 
//			{
//				parent = path1.get(i);
//			}			
//			i++;
//			j++;
//		}
//		
//		return parent;
//	}
//
//	private static boolean GetNodePath(SemanticNode root, SemanticNode node, List<SemanticNode> path)
//	{
//		if(root.getCurr().getURI().toString() == node.getCurr().getURI().toString())
//		{
//			return true;
//		}
//		
//		path.add(root);
//		boolean found = false;
//		
//		List<SemanticNode> nodes = root.getSubNodes();
//		int i = 0, len = nodes.size();
//		while(!found && i < len)
//		{
//			found = GetNodePath(nodes.get(i), node, path);
//			i++;
//		}
//		
//		if(!found)
//			path.remove(path.size() - 1);
//		
//		return found;
//	}
//	
//	private static int GetSemanticDistance(List<SemanticNode> path1, List<SemanticNode> path2)
//	{
//		int len1 = path1.size();
//		int len2 = path2.size();
//		
//		if(IsSubsume(path1, path2))
//		{
//			return Math.abs(len1 - len2);
//		}
//		
//		SemanticNode lastcommonnode = GetLastCommonNode(path1, path2);
//		int lastcommonnode_index = 0;
//		if(lastcommonnode != null)
//		{
//			for(int i = 0; i < path1.size(); i++)
//			{
//				if(path1.get(i).getCurr().getURI().toString() 
//						== lastcommonnode.getCurr().getURI().toString())
//				{
//					lastcommonnode_index = i;
//				}
//			}
//		}
//		
//		return len1 + len2 - 2 * lastcommonnode_index - 2;
//	}
//	
//	private static boolean IsSubsume(List<SemanticNode> path1, List<SemanticNode> path2) {
//		// TODO Auto-generated method stub
//		
//		int len1 = path1.size();
//		int len2 = path2.size();
//		
//		if(len1 > len2 && path1.get(len2 - 1).getCurr().getURI().toString() == 
//			path2.get(len2 - 1).getCurr().getURI().toString())
//		{
//			return true;
//		}
//		
//		if(len1 < len2 && path1.get(len1 - 1).getCurr().getURI().toString() == 
//			path2.get(len1 - 1).getCurr().getURI().toString())
//		{
//			return true;
//		}
//		
//		return false;
//	}
//	
//	private static SemanticNode RenderSemanticTreeEntry(Reasoner reasoner)
//	{
//		OWLOntologyManager manager = reasoner.getManager();
//		OWLDataFactory fac = manager.getOWLDataFactory();
//		
//		Set<OWLClass> unsatisfiable = reasoner.getInconsistentClasses();
//		OWLClass thing = fac.getOWLClass(URI.create(stadns + "Thing"));
//		
//		SemanticNode root = new SemanticNode(thing, null);
//		List<SemanticNode> lst_SemanticNode = new ArrayList<SemanticNode>();
//		
//		Set<OWLClass> direct_subclasses = GetFirstLevelSubClassOfThing(unsatisfiable, reasoner, thing);
//		
//		SemanticNode node = null;
//		for(OWLClass owlclass : direct_subclasses)
//		{
//			node = new SemanticNode(owlclass, null);
//			lst_SemanticNode.add(node);
//		}
//		root.setSubNodes(lst_SemanticNode);
//		
//		RenderSemanticTree(unsatisfiable, reasoner, root, 0);
//		
//		return root;
//	}
//	
//	private static void RenderSemanticTree(Set<OWLClass> unsatisfiable, Reasoner reasoner, SemanticNode root, int level) 
//	{
//		if(root.getSubNodes() == null)
//		{
//			return;
//		}
//		
//		level++;
//		
//		List<SemanticNode> nodes = root.getSubNodes();
//		for(SemanticNode node : nodes)
//		{
//			System.out.println(GetBlank(level) + node.toString());
//			
//			//get subclass relation
//			Set<OWLClass> direct_subClses = getDirectSubClasses(unsatisfiable, reasoner, node.getCurr());
//			
//			List<SemanticNode> tmp_nodes = new ArrayList<SemanticNode>();
//			SemanticNode tmp_node = null;
//			for (OWLClass cls : direct_subClses) 
//			{
//				tmp_node = new SemanticNode(cls, null);
//				tmp_nodes.add(tmp_node);
//				
//				//print
//				//System.out.println(GetBlank(level) + cls);
//			}
//			node.setSubNodes(tmp_nodes);
//
//			RenderSemanticTree(unsatisfiable, reasoner, node, level);
//		}
//	}
//	
//	private static Set<OWLClass> GetFirstLevelSubClassOfThing(Set<OWLClass> unsatisfiable, Reasoner reasoner, OWLClass thing) {
//		// TODO Auto-generated method stub
//		
//		Set<OWLClass> firstlevelofthing = new HashSet<OWLClass>();
//		
//		Set<OWLClass> allsubclassofthing = getDirectSubClasses(unsatisfiable, reasoner, thing);
//		for(OWLClass owlclass : allsubclassofthing)
//		{
//			System.out.println("---" + owlclass.toString());
//		}
//		
//		for(OWLClass owlclass : allsubclassofthing)
//		{
//			if(IsDirectSubClassOfThing(reasoner, owlclass, thing))
//			{
//				firstlevelofthing.add(owlclass);
//			}
//		}
//		
//		for(OWLClass owlclass : firstlevelofthing)
//		{
//			System.out.println("+++" + owlclass.toString());
//		}
//		
//		return firstlevelofthing;
//	}
//
//	private static boolean IsDirectSubClassOfThing(Reasoner reasoner, OWLClass owlclass, OWLClass thing) {
//		// TODO Auto-generated method stub
//		
//		Set<OWLClass> owlclasses = getDirectSuperClasses(reasoner, owlclass);
//		for(OWLClass tmpowlclass : owlclasses)
//		{
//			if(tmpowlclass.toString() == thing.toString())
//			{
//				return false;
//			}
//		}
//		
//		return true;
//	}
//
//	private static String GetBlank(int level) {
//		// TODO Auto-generated method stub
//		
//		String blank = "";
//		
//		for(int i = 0; i < level; i++)
//		{
//			blank += "\t";
//		}
//		
//		return blank;
//	}
//
//	public static Set<OWLClass> getDirectSubClasses(Set<OWLClass> unsatisfiable, Reasoner reasoner, OWLClass owlclass) {
//		Set<OWLClass> subClasses = new HashSet<OWLClass>();
//		if (reasoner == null || owlclass == null) {
//			return subClasses;
//		}
//		
//		Set<OWLClass> equClasses = reasoner.getAllEquivalentClasses(owlclass);
//		equClasses.add(owlclass);
//		for (OWLClass tmpOwlClz : equClasses)
//		{
//			Set<OWLClass> tmpclasses = OWLReasonerAdapter.flattenSetOfSets(reasoner.getSubClasses(tmpOwlClz));
//			for(OWLClass tmpclass : tmpclasses)
//			{
//				if(!unsatisfiable.contains(tmpclass) && !tmpclass.isOWLNothing())
//				{
//					subClasses.add(tmpclass);
//				}
//			}
//		}
//
//		return subClasses;
//	}
//	
//	public static Set<OWLClass> getDirectSuperClasses(Reasoner reasoner, OWLClass owlClass) {
//		Set<OWLClass> superClasses = new HashSet<OWLClass>();
//		if (reasoner == null || owlClass == null) {
//			return superClasses;
//		}
//
//		Set<OWLClass> equClasses = reasoner.getAllEquivalentClasses(owlClass);
//		equClasses.add(owlClass);
//		for (OWLClass tmpOwlClz : equClasses) {
//			superClasses.addAll(OWLReasonerAdapter.flattenSetOfSets(reasoner.getSuperClasses(tmpOwlClz)));
//		}	
//		
//		return superClasses;
//	}
//	
//}
