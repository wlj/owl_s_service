package edu.pku.sj.rscasd.reasoningmodule.main;

import java.net.URI;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.mindswap.pellet.ABox;
import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.owlapi.PelletLoader;
import org.mindswap.pellet.owlapi.PelletReasonerFactory;
import org.mindswap.pellet.owlapi.Reasoner;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.inference.OWLReasonerAdapter;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;

import edu.pku.sj.rscasd.reasoningmodule.util.owlapi.ReasoningUtils;

public class PelletReasonerOwlapiv2IndividualMergeTest {

	private final static Log logger = LogFactory.getLog(PelletReasonerOwlapiv2IndividualMergeTest.class);

	final String file1 = "file:data/koala.owl";
	final String ns1 = "http://protege.stanford.edu/plugins/owl/owl-library/koala.owl#";
	final String file2 = "file:data/koala2.owl";
	final String ns2 = "http://protege.stanford.edu/plugins/owl/owl-library/koala2.owl#";

	@Test
	public void tryReasoning() {
		try {
			// Create our ontology manager in the usual way.
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

			// Load a copy of the people+pets ontology. We'll load the ontology
			// from the web (it's acutally located
			// in the TONES ontology repository).
			URI docIRI1 = URI.create(file1);
			URI docIRI2 = URI.create(file2);
			// We load the ontology from a document - our IRI points to it
			// directly
			OWLOntology ont2 = manager.loadOntology(docIRI2);
			System.out.println("Loaded " + ont2.getURI());
			OWLOntology ont1 = manager.loadOntology(docIRI1);
			System.out.println("Loaded " + ont1.getURI());

			PelletReasonerFactory reasonerFactory = new PelletReasonerFactory();
			Reasoner reasoner = reasonerFactory.createReasoner(manager);
			reasoner.loadOntology(ont1);
			reasoner.loadOntology(ont2);

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
			OWLClass university = fac.getOWLClass(URI.create(ns1 + "University"));
			OWLClass habitat = fac.getOWLClass(URI.create(ns1 + "Habitat"));
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
			Set<OWLOntology> ontologies = manager.getOntologies();
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
			OWLIndividual onScInd4 = fac.getOWLIndividual(URI.create(ns2 + "SuperSchoolWithCorrectProp2"));
			System.out.println("INDIVIDUAL[Super School with correct prop 2]: " + onScInd4);
			System.out.println("INDIVIDUAL[Super School with correct prop 2]: Defined classes: "
					+ onScInd4.getTypes(ontologies));
			System.out.println("INDIVIDUAL[Super School with correct prop 2]: Inferred classes: "
					+ reasoner.getTypes(onScInd4));
			OWLIndividual onScInd4_1 = fac.getOWLIndividual(URI.create(ns2 + "SuperSchoolWithCorrectProp3"));
			System.out.println("INDIVIDUAL[Super School with correct prop 3]: " + onScInd4_1);
			System.out.println("INDIVIDUAL[Super School with correct prop 3]: Defined classes: "
					+ onScInd4_1.getTypes(ontologies));
			System.out.println("INDIVIDUAL[Super School with correct prop 3]: Inferred classes: "
					+ reasoner.getTypes(onScInd4_1));
			OWLIndividual onNuInd1 = fac.getOWLIndividual(URI.create(ns2 + "NormalUniversityWithCorrectProp"));
			System.out.println("INDIVIDUAL[Normal University with super size]: " + onNuInd1);
			System.out.println("INDIVIDUAL[Normal University with super size]: Defined classes: "
					+ onNuInd1.getTypes(ontologies));
			System.out.println("INDIVIDUAL[Normal University with super size]: Inferred classes: "
					+ reasoner.getTypes(onNuInd1));
			OWLIndividual onNuInd2 = fac.getOWLIndividual(URI.create(ns2 + "NormalUniversityWithAnotherCorrectProp"));
			System.out.println("INDIVIDUAL[Normal University with Another super size]: " + onNuInd2);
			System.out.println("INDIVIDUAL[Normal University with Another super size]: Defined classes: "
					+ onNuInd2.getTypes(ontologies));
			System.out.println("INDIVIDUAL[Normal University with Another super size]: Inferred classes: "
					+ reasoner.getTypes(onNuInd2));
			boolean isSame = reasoner.isSameAs(onScInd3, onNuInd1);
			isSame = reasoner.isSameAs(onScInd3, onScInd4);
			isSame = reasoner.isSameAs(onScInd4, onScInd4_1);
			PelletLoader loader = reasoner.getLoader();
			KnowledgeBase kb = reasoner.getKB();
			ABox abox = kb.getABox();
			isSame = abox.isSameAs(loader.term(onScInd3), loader.term(onNuInd1));
			logger.info("SC3 - NU1: isSame: " + isSame);
			isSame = abox.isSameAs(loader.term(onNuInd1), loader.term(onScInd3));
			logger.info("NU1 - SC3: isSame: " + isSame);
			isSame = abox.isSameAs(loader.term(onScInd3), loader.term(onScInd4));
			logger.info("SC3 - SC4: isSame: " + isSame);
			isSame = abox.isSameAs(loader.term(onScInd4), loader.term(onScInd4_1));
			logger.info("SC4 - SC4_1: isSame: " + isSame);

			boolean isSimilar = ReasoningUtils.isSimilarIndividual(reasoner, onScInd3, onNuInd1);
			isSimilar = ReasoningUtils.isSimilarIndividual(reasoner, onNuInd1, onScInd3);
			logger.info("NU1 - SC3: isSimilar: " + isSimilar);
			isSimilar = ReasoningUtils.isSimilarIndividual(reasoner, onNuInd1, onNuInd2);
			logger.info("NU1 - NU2: isSimilar: " + isSimilar);
			isSimilar = ReasoningUtils.isSimilarIndividual(reasoner, onScInd3, onScInd4);
			logger.info("SC3 - SC4: isSimilar: " + isSimilar);
			isSimilar = ReasoningUtils.isSimilarIndividual(reasoner, onScInd4, onScInd4_1);
			logger.info("SC4 - SC4_1: isSimilar: " + isSimilar);

			boolean belongTo = ReasoningUtils.belongTo(reasoner, onScInd3, onNuInd1); // true
			logger.info("SC3 - NU1: BelongTo: " + belongTo);
			belongTo = ReasoningUtils.belongTo(reasoner, onNuInd1, onScInd3); // true 
			logger.info("NU1 - SC3: BelongTo: " + belongTo);
			belongTo = ReasoningUtils.belongTo(reasoner, onNuInd1, onNuInd2); // false 
			logger.info("NU1 - NU2: BelongTo: " + belongTo);
			belongTo = ReasoningUtils.belongTo(reasoner, onScInd3, onScInd4); // true
			logger.info("SC3 - SC4: BelongTo: " + belongTo);
			belongTo = ReasoningUtils.belongTo(reasoner, onScInd4, onScInd3); // true
			logger.info("SC4 - SC3: BelongTo: " + belongTo);
			belongTo = ReasoningUtils.belongTo(reasoner, onScInd3, onScInd2); // false
			logger.info("SC3 - SC2: BelongTo: " + belongTo);
			belongTo = ReasoningUtils.belongTo(reasoner, onScInd4, onNuInd1); // true
			logger.info("SC4 - NU1: BelongTo: " + belongTo);
			///
			belongTo = ReasoningUtils.belongTo(reasoner, onScInd4, onScInd4_1); // false 
			logger.info("SC4 - SC4_1: BelongTo: " + belongTo);
			belongTo = ReasoningUtils.belongTo(reasoner, onScInd4_1, onScInd4); // true
			logger.info("SC4_1 - SC4: BelongTo: " + belongTo);
			belongTo = ReasoningUtils.belongTo(reasoner, onNuInd1, onScInd4); // true 
			logger.info("NU1 - SC4: BelongTo: " + belongTo);
			belongTo = ReasoningUtils.belongTo(reasoner, onScInd4, onNuInd1); // true
			logger.info("SC4 - NU1: BelongTo: " + belongTo);
			///
			belongTo = ReasoningUtils.belongTo(reasoner, onScInd4_1, onNuInd1); // true
			logger.info("SC4_1 - NU1: BelongTo: " + belongTo);
			belongTo = ReasoningUtils.belongTo(reasoner, onNuInd1, onScInd4_1); // false
			logger.info("NU1 - SC4_1: BelongTo: " + belongTo);

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

			OWLIndividual onScInd5 = fac.getOWLIndividual(URI.create(ns2 + "NormalUniversityWithCorrectProp"));
			System.out.println("INDIVIDUAL[Normal University with correct prop]: " + onScInd5);
			System.out.println("INDIVIDUAL[Normal University with correct prop]: Defined classes: "
					+ onScInd5.getTypes(ontologies));
			Set<OWLIndividual> onSameAsNormalWithCorPropScInd5 = reasoner.getSameAsIndividuals(onScInd5);
			System.out.println("\n");
			for (OWLIndividual oni : onSameAsNormalWithCorPropScInd5) {
				System.out.println("SAME - INDIVIDUAL[Normal University with correct prop]: " + oni);
				System.out.println("SAME - INDIVIDUAL[Normal University with correct prop]: Defined classes: "
						+ oni.getTypes(ontologies));
			}

			// consistency check
			Collection<OWLIndividual> individuals = new LinkedList<OWLIndividual>();
			individuals.add(onScInd1);
			individuals.add(onScInd2);
			individuals.add(onScInd3);
			individuals.add(onScInd4);
			individuals.add(onScInd5);
			boolean isConsistent = ReasoningUtils.checkConsistent(reasoner, individuals, university, false);
			logger.info("University Consistency Sampling Result: " + isConsistent);

			OWLClass thirdSuperSchool = fac.getOWLClass(URI.create(ns2 + "ThirdSuperSchool"));
			OWLIndividual thirdSuperSchoolExample = fac.getOWLIndividual(URI.create(ns2 + "ThirdSuperSchoolExample"));
			Assert.assertTrue(reasoner.isDefined(thirdSuperSchoolExample));
			individuals.clear();
			individuals.add(thirdSuperSchoolExample);
			isConsistent = ReasoningUtils.checkConsistent(reasoner, individuals, thirdSuperSchool, false);
			logger.info("ThirdSuperSchool Consistency Sampling Result: " + isConsistent);
			isConsistent = ReasoningUtils.checkConsistent(reasoner, individuals, university, false);
			reasoner.getKB().getTBox().getAxioms(
					reasoner.getLoader().term(fac.getOWLClass(URI.create(ns2 + "SuperSchool"))));
			logger.info("ThirdSuperSchool Against University Consistency Sampling Result: " + isConsistent);
			reasoner.getLoader().term(thirdSuperSchool);

			OWLClass superSchoolSize = fac.getOWLClass(URI.create(ns2 + "SuperSchoolSize"));
			OWLIndividual justTestingIncorrectSchoolSize = fac.getOWLIndividual(URI.create(ns2
					+ "JustTestingIncorrectSchoolSize"));
			Assert.assertTrue(reasoner.isDefined(justTestingIncorrectSchoolSize));
			individuals.clear();
			individuals.add(justTestingIncorrectSchoolSize);
			isConsistent = ReasoningUtils.checkConsistent(reasoner, individuals, superSchoolSize, false);

		} catch (UnsupportedOperationException exception) {
			System.out.println("Unsupported reasoner operation.");
		} catch (OWLOntologyCreationException e) {
			System.out.println("Could not load the pizza ontology: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
