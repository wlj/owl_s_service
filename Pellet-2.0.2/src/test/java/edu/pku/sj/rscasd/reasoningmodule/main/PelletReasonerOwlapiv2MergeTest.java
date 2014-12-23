package edu.pku.sj.rscasd.reasoningmodule.main;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.owlapi.PelletReasonerFactory;
import org.mindswap.pellet.owlapi.Reasoner;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.inference.OWLReasonerAdapter;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;

import com.clarkparsia.pellet.sparqldl.engine.QueryEngine;
import com.clarkparsia.pellet.sparqldl.model.Query;
import com.clarkparsia.pellet.sparqldl.model.QueryResult;
import com.clarkparsia.pellet.sparqldl.model.ResultBinding;
import com.clarkparsia.pellet.sparqldl.parser.QueryParser;

import edu.pku.ly.semantic.SemanticNode;
import edu.pku.sj.rscasd.reasoningmodule.util.owlapi.ReasoningUtils;

public class PelletReasonerOwlapiv2MergeTest {

	private final static Log logger = LogFactory.getLog(PelletReasonerOwlapiv2MergeTest.class);

	final static String file1 = "http://localhost:8080/juddiv3/owl-s/1.1/koala.owl";//file:data/koala.owl
	final static String ns1 = "http://localhost:8080/juddiv3/owl-s/1.1/koala.owl#";
	final static String file2 = "http://localhost:8080/juddiv3/owl-s/1.1/koala2.owl";
	final static String ns2 = "http://localhost:8080/juddiv3/owl-s/1.1/koala2.owl#";
	final static String file3 = "http://localhost:8080/juddiv3/owl-s/1.1/family.owl";
	final static String ns3 = "http://localhost:8080/juddiv3/owl-s/1.1/family#";
	final static String stadns = "http://www.w3.org/2002/07/owl#";
	
	public static void main(String[] args) {
		try {
			// Create our ontology manager in the usual way.
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

			// Load a copy of the people+pets ontology. We'll load the ontology
			// from the web (it's acutally located
			// in the TONES ontology repository).
			URI docIRI1 = URI.create(file1);
			URI docIRI2 = URI.create(file2);
			URI docIRI3 = URI.create(file3);
			// We load the ontology from a document - our IRI points to it
			// directly
			OWLOntology ont3 = manager.loadOntology(docIRI3);
			System.out.println("Loaded " + ont3.getURI());
			OWLOntology ont2 = manager.loadOntology(docIRI2);
			System.out.println("Loaded " + ont2.getURI());
			OWLOntology ont1 = manager.loadOntology(docIRI1);
			System.out.println("Loaded " + ont1.getURI());

			PelletReasonerFactory reasonerFactory = new PelletReasonerFactory();
			Reasoner reasoner = reasonerFactory.createReasoner(manager);
			reasoner.loadOntology(ont1);
			reasoner.loadOntology(ont2);
			reasoner.loadOntology(ont3);

			reasoner.classify();
			
			boolean consistent = reasoner.isConsistent();
			System.out.println("Consistent: " + consistent);
			System.out.println("\n");
			Set<OWLClass> unsatisfiable = reasoner.getInconsistentClasses();
			if (!unsatisfiable.isEmpty()) {
				System.out.println("The following classes are unsatisfiable: ");
				for (OWLClass cls : unsatisfiable) {
					System.out.println("    " + cls);
				}
			} else {
				System.out.println("There are no unsatisfiable classes");
			}
			System.out.println("\n");

			OWLDataFactory fac = manager.getOWLDataFactory();
			OWLClass habitat = fac.getOWLClass(URI.create(ns1 + "Habitat"));
			
			System.out.println("super class of habitat:");
			Set<OWLClass> parents = getDirectSuperClasses(reasoner, habitat);
			for (OWLClass cls : parents) {
				System.out.println("    " + cls);
				
				System.out.println(cls.getURI());
			}
			
			RenderSemanticTreeEntry(reasoner);
			
			//get subclass relation
			Set<OWLClass> direct_subClses = getDirectSubClasses(unsatisfiable, reasoner, habitat);
			System.out.println("direct subclasses of Habitat: ");
			for (OWLClass cls : direct_subClses) {
				System.out.println("    " + cls);
			}
			System.out.println("\n");
			
			Set<Set<OWLClass>> subClses = reasoner.getDescendantClasses(habitat);
			Set<OWLClass> clses = OWLReasonerAdapter.flattenSetOfSets(subClses);
			System.out.println("Subclasses of Habitat: ");
			for (OWLClass cls : clses) {
				System.out.println("    " + cls);
			}
			System.out.println("\n");
			OWLClass college = fac.getOWLClass(URI.create(ns1 + "College"));
			subClses = reasoner.getAncestorClasses(college);
			clses = OWLReasonerAdapter.flattenSetOfSets(subClses);
			System.out.println("Superclasses of college: ");
			for (OWLClass cls : clses) {
				System.out.println("    " + cls);
			}
			System.out.println("\n");

			// MERGED

			// OWLOntologyMerger merger = new OWLOntologyMerger(manager);
			// OWLOntology merged = merger.createMergedOntology(manager, null);
			// for (OWLAxiom ax : merged.getAxioms()) {
			// System.out.println("MERGED: +++ " + ax);
			// }

			Set<OWLOntology> ontologies = manager.getOntologies();
			// INDIVIDUAL
			OWLIndividual onInd1 = fac.getOWLIndividual(URI.create(ns2 + "BigABCSchoolInstance"));
			System.out.println("INDIVIDUAL: " + onInd1);
			System.out.println("INDIVIDUAL: Defined classes: " + onInd1.getTypes(ontologies));
			OWLIndividual onInd2 = fac.getOWLIndividual(URI.create(ns2 + "BigABCSchoolInstance-notexists"));
			System.out.println("INDIVIDUAL: " + onInd2);
			System.out.println("INDIVIDUAL: Defined classes: " + onInd2.getTypes(ontologies));
			OWLIndividual onInd3 = fac.getOWLIndividual(URI.create("BigABCSchoolInstance"));
			System.out.println("INDIVIDUAL: SHORT: " + onInd3);
			System.out.println("INDIVIDUAL: SHORT - Defined classes: " + onInd3.getTypes(ontologies));

			OWLClass university = fac.getOWLClass(URI.create(ns1 + "University"));
			Set<OWLIndividual> individuals = reasoner.getIndividuals(university, true);
			System.out.println("Instances of University[Direct]: ");
			for (OWLIndividual ind : individuals) {
				System.out.println("    " + ind);
			}
			System.out.println("\n");
			individuals = reasoner.getIndividuals(university, false);
			System.out.println("Instances of University[Indirect]: ");
			for (OWLIndividual ind : individuals) {
				System.out.println("    " + ind);
			}
			System.out.println("\n");

			// reasoner.getTopClassNode().
			KnowledgeBase kb = reasoner.getKB();
			String prefix = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n"
					+ "PREFIX koala: <http://protege.stanford.edu/plugins/owl/owl-library/koala.owl#>\r\n"
					+ "PREFIX koala2: <http://protege.stanford.edu/plugins/owl/owl-library/koala2.owl#>\r\n"
					+ "SELECT * { ";
			String suffix = " }";
			QueryParser parser = QueryEngine.getParser();
			String queryStr = " ?x rdf:type koala:University";
			Query query = parser.parse(prefix + queryStr + suffix, kb);
			QueryResult result = QueryEngine.exec(query);
			for (ResultBinding rbnd : result) {
				System.out.println("Query Instance Result:    " + rbnd);
			}
			System.out.println("\n");
			queryStr = "koala2:BigABCSchoolInstance rdf:type ?x";
			query = parser.parse(prefix + queryStr + suffix, kb);
			result = QueryEngine.exec(query);
			for (ResultBinding rbnd : result) {
				System.out.println("Query Belonging-Classes Result:    " + rbnd);
			}
			System.out.println("\n");
			queryStr = " ?x koala2:hasSchoolSize _:a. _:a rdf:type koala2:SuperSchoolSize. ";
			query = parser.parse(prefix + queryStr + suffix, kb);
			result = QueryEngine.exec(query);
			for (ResultBinding rbnd : result) {
				System.out.println("Query Instance Result [With given property]:    " + rbnd);
			}
			System.out.println("\n");

			OWLIndividual onScInd1 = fac.getOWLIndividual(URI.create(ns2 + "SuperSchoolWithoutProp"));
			System.out.println("INDIVIDUAL[Super School without prop]: " + onScInd1);
			System.out.println("INDIVIDUAL[Super School without prop]: Defined classes: "
					+ onScInd1.getTypes(ontologies));
			System.out.println("INDIVIDUAL[Super School without prop]: Inferred classes: "
					+ reasoner.getTypes(onScInd1));
			OWLIndividual onScInd2 = fac.getOWLIndividual(URI.create(ns2 + "SuperSchoolWithIncorrectProp"));
			System.out.println("INDIVIDUAL[Super School with incorrect prop]: " + onScInd2);
			System.out.println("INDIVIDUAL[Super School with incorrect prop]: Defined classes: "
					+ onScInd2.getTypes(ontologies));
			System.out.println("INDIVIDUAL[Super School with incorrect prop]: Inferred classes: "
					+ reasoner.getTypes(onScInd2));
			OWLIndividual onScInd3 = fac.getOWLIndividual(URI.create(ns2 + "SuperSchoolWithCorrectProp"));
			System.out.println("INDIVIDUAL[Super School with correct prop]: " + onScInd3);
			System.out.println("INDIVIDUAL[Super School with correct prop]: Defined classes: "
					+ onScInd3.getTypes(ontologies));
			System.out.println("INDIVIDUAL[Super School with correct prop]: Inferred classes: "
					+ reasoner.getTypes(onScInd3));

			// Get same ones
			Set<OWLIndividual> onSameAsCorPropScInd3 = reasoner.getSameAsIndividuals(onScInd3);
			Set<OWLIndividual> onSameAsIncorPropScInd2 = reasoner.getSameAsIndividuals(onScInd2);
			Set<OWLIndividual> onSameAsNoPropScInd1 = reasoner.getSameAsIndividuals(onScInd1);
			System.out.println("\n");
			for (OWLIndividual oni : onSameAsNoPropScInd1) {
				System.out.println("SAME - INDIVIDUAL[Super School without prop]: " + oni);
				System.out.println("SAME - INDIVIDUAL[Super School without prop]: Defined classes: "
						+ oni.getTypes(ontologies));
			}
			System.out.println("\n");
			for (OWLIndividual oni : onSameAsIncorPropScInd2) {
				System.out.println("SAME - INDIVIDUAL[Super School with incorrect prop]: " + oni);
				System.out.println("SAME - INDIVIDUAL[Super School with incorrect prop]: Defined classes: "
						+ oni.getTypes(ontologies));
				oni.getDataPropertiesInSignature();
			}
			System.out.println("\n");
			for (OWLIndividual oni : onSameAsCorPropScInd3) {
				System.out.println("SAME - INDIVIDUAL[Super School with correct prop]: " + oni);
				System.out.println("SAME - INDIVIDUAL[Super School with correct prop]: Defined classes: "
						+ oni.getTypes(ontologies));
			}

			OWLIndividual onScInd4 = fac.getOWLIndividual(URI.create(ns2 + "NormalUniversityWithCorrectProp"));
			System.out.println("INDIVIDUAL[Normal University with correct prop]: " + onScInd4);
			System.out.println("INDIVIDUAL[Normal University with correct prop]: Defined classes: "
					+ onScInd4.getTypes(ontologies));
			Set<OWLIndividual> onSameAsNormalWithCorPropScInd4 = reasoner.getSameAsIndividuals(onScInd1);
			System.out.println("\n");
			for (OWLIndividual oni : onSameAsNormalWithCorPropScInd4) {
				System.out.println("SAME - INDIVIDUAL[Normal University with correct prop]: " + oni);
				System.out.println("SAME - INDIVIDUAL[Normal University with correct prop]: Defined classes: "
						+ oni.getTypes(ontologies));
			}

			OWLClass superSchool = fac.getOWLClass(URI.create(ns2 + "SuperSchool"));
			OWLClass thirdSuperSchool = fac.getOWLClass(URI.create(ns2 + "ThirdSuperSchool"));
			OWLClass anotherSuperSchool = fac.getOWLClass(URI.create(ns2 + "AnotherSuperSchool"));
			OWLClass superSchoolWithTwoParents = fac.getOWLClass(URI.create(ns2 + "SuperSchoolWithTwoParents"));
			OWLClass anotherSuperSchoolSubClass = fac.getOWLClass(URI.create(ns2 + "AnotherSuperSchoolSubClass"));
			Set<OWLIndividual> thirdSuperSchoolInds = reasoner.getIndividuals(thirdSuperSchool, false);
			OWLIndividual thirdSuperSchoolExample = fac.getOWLIndividual(URI.create(ns2 + "ThirdSuperSchoolExample"));
			OWLIndividual anotherSuperSchoolExample = fac.getOWLIndividual(URI
					.create(ns2 + "AnotherSuperSchoolExample"));
			System.out.println("\n");
			System.out.println("All equivalent for super school: " + reasoner.getAllEquivalentClasses(superSchool));
			System.out.println("All subclass for super school: " + reasoner.getSubClasses(superSchool));
			System.out.println("All parent for super school: " + reasoner.getSuperClasses(superSchool));
			System.out.println("All decendent class for super school: " + reasoner.getDescendantClasses(superSchool));
			System.out.println("All ancestor class for super school: " + reasoner.getAncestorClasses(superSchool));
			System.out.println("\n");
			System.out.println("All subclass for [AnotherSuperSchool]: " + reasoner.getSubClasses(anotherSuperSchool));
			System.out.println("All parent for [AnotherSuperSchool]: " + reasoner.getSuperClasses(anotherSuperSchool));
			System.out.println("All decendent class for [AnotherSuperSchool]: "
					+ reasoner.getDescendantClasses(anotherSuperSchool));
			System.out.println("All ancestor class for [AnotherSuperSchool]: "
					+ reasoner.getAncestorClasses(anotherSuperSchool));
			System.out.println("\n");
			System.out.println("All parent for [SuperSchoolWithTwoParents]: "
					+ reasoner.getSuperClasses(superSchoolWithTwoParents));
			System.out.println("All ancestor class for [SuperSchoolWithTwoParents]: "
					+ reasoner.getAncestorClasses(superSchoolWithTwoParents));
			System.out.println("\n");
			System.out.println("All parent for [AnotherSuperSchoolSubClass]: "
					+ reasoner.getSuperClasses(anotherSuperSchoolSubClass));
			System.out.println("All ancestor class for [AnotherSuperSchoolSubClass]: "
					+ reasoner.getAncestorClasses(anotherSuperSchoolSubClass));
			System.out.println("\n");
			logger.debug("Inferred type[Another Super School Example - 1]: "
					+ reasoner.getTypes(anotherSuperSchoolExample));
			logger.debug("Inferred [Third Super School] Individuals: " + thirdSuperSchoolInds);
			logger.debug("Check type[Third Super School Example - 1]: "
					+ reasoner.hasType(thirdSuperSchoolExample, superSchool));
			logger.debug("Check type[Third Super School Example - 2]: "
					+ reasoner.hasType(thirdSuperSchoolExample, thirdSuperSchool));
			logger.debug("Inferred type[Third Super School Example - 3]: "
					+ reasoner.getTypes(thirdSuperSchoolExample, false));
			logger.debug("Inferred type[Third Super School Example - 4]: "
					+ reasoner.getType(thirdSuperSchoolExample).getURI());
		} catch (UnsupportedOperationException exception) {
			System.out.println("Unsupported reasoner operation.");
		} catch (OWLOntologyCreationException e) {
			System.out.println("Could not load the pizza ontology: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private static void RenderSemanticTreeEntry(Reasoner reasoner)
	{
		OWLOntologyManager manager = reasoner.getManager();
		OWLDataFactory fac = manager.getOWLDataFactory();
		
		Set<OWLClass> unsatisfiable = reasoner.getInconsistentClasses();
		OWLClass thing = fac.getOWLClass(URI.create(stadns + "Thing"));
		OWLClass nothing = fac.getOWLClass(URI.create(stadns + "NoThing"));
		
		SemanticNode root = new SemanticNode(thing, null);
		List<SemanticNode> lst_SemanticNode = new ArrayList<SemanticNode>();
		
		Set<OWLClass> direct_subclasses = GetFirstLevelSubClassOfThing(unsatisfiable, reasoner, thing);
		
		SemanticNode node = null;
		for(OWLClass owlclass : direct_subclasses)
		{
			node = new SemanticNode(owlclass, null);
			lst_SemanticNode.add(node);
		}
		root.setSubNodes(lst_SemanticNode);
		
		RenderSemanticTree(unsatisfiable, reasoner, root, 0);
	}
	
	private static Set<OWLClass> GetFirstLevelSubClassOfThing(Set<OWLClass> unsatisfiable, Reasoner reasoner, OWLClass thing) {
		// TODO Auto-generated method stub
		
		Set<OWLClass> firstlevelofthing = new HashSet<OWLClass>();
		
		Set<OWLClass> allsubclassofthing = getDirectSubClasses(unsatisfiable, reasoner, thing);
		for(OWLClass owlclass : allsubclassofthing)
		{
			System.out.println("---" + owlclass.toString());
		}
		
		for(OWLClass owlclass : allsubclassofthing)
		{
			if(IsDirectSubClassOfThing(reasoner, owlclass, thing))
			{
				firstlevelofthing.add(owlclass);
			}
		}
		
		for(OWLClass owlclass : firstlevelofthing)
		{
			System.out.println("+++" + owlclass.toString());
		}
		
		return firstlevelofthing;
	}

	private static boolean IsDirectSubClassOfThing(Reasoner reasoner, OWLClass owlclass, OWLClass thing) {
		// TODO Auto-generated method stub
		
		Set<OWLClass> owlclasses = getDirectSuperClasses(reasoner, owlclass);
		for(OWLClass tmpowlclass : owlclasses)
		{
			if(tmpowlclass.toString() == thing.toString())
			{
				return false;
			}
		}
		
		return true;
	}

	private static void RenderSemanticTree(Set<OWLClass> unsatisfiable, Reasoner reasoner, SemanticNode root, int level) 
	{
		if(root.getSubNodes() == null)
		{
			return;
		}
		
		level++;
		
		List<SemanticNode> nodes = root.getSubNodes();
		for(SemanticNode node : nodes)
		{
			System.out.println(GetBlank(level) + node.toString());
			
			//get subclass relation
			Set<OWLClass> direct_subClses = getDirectSubClasses(unsatisfiable, reasoner, node.getCurr());
			
			List<SemanticNode> tmp_nodes = new ArrayList<SemanticNode>();
			SemanticNode tmp_node = null;
			for (OWLClass cls : direct_subClses) 
			{
				tmp_node = new SemanticNode(cls, null);
				tmp_nodes.add(tmp_node);
				
				//print
				System.out.println(GetBlank(level) + cls);
			}
			if(tmp_nodes.size() > 0)
			{
				node.setSubNodes(tmp_nodes);
				
				RenderSemanticTree(unsatisfiable, reasoner, node, level);
			}
		}
	}

	private static String GetBlank(int level) {
		// TODO Auto-generated method stub
		
		String blank = "";
		
		for(int i = 0; i < level; i++)
		{
			blank += "\t";
		}
		
		return blank;
	}

	public static Set<OWLClass> getDirectSubClasses(Set<OWLClass> unsatisfiable, Reasoner reasoner, OWLClass owlclass) {
		Set<OWLClass> subClasses = new HashSet<OWLClass>();
		if (reasoner == null || owlclass == null) {
			return subClasses;
		}
		
		Set<OWLClass> equClasses = reasoner.getAllEquivalentClasses(owlclass);
		equClasses.add(owlclass);
		for (OWLClass tmpOwlClz : equClasses)
		{
			Set<OWLClass> tmpclasses = OWLReasonerAdapter.flattenSetOfSets(reasoner.getSubClasses(tmpOwlClz));
			for(OWLClass tmpclass : tmpclasses)
			{
				if(!unsatisfiable.contains(tmpclass) && tmpclass.toString() != "Nothing")
				{
					subClasses.add(tmpclass);
				}
			}
		}

		return subClasses;
	}
	
	public static Set<OWLClass> getDirectSuperClasses(Reasoner reasoner, OWLClass owlClass) {
		Set<OWLClass> superClasses = new HashSet<OWLClass>();
		if (reasoner == null || owlClass == null) {
			return superClasses;
		}

		Set<OWLClass> equClasses = reasoner.getAllEquivalentClasses(owlClass);
		equClasses.add(owlClass);
		for (OWLClass tmpOwlClz : equClasses) {
			superClasses.addAll(OWLReasonerAdapter.flattenSetOfSets(reasoner.getSuperClasses(tmpOwlClz)));
		}	
		
		return superClasses;
	}
}
