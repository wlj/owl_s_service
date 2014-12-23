package edu.pku.sj.rscasd.reasoningmodule.main;
import java.net.URI;
import java.util.Set;

import org.junit.Test;
import org.mindswap.pellet.owlapi.PelletReasonerFactory;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerAdapter;
import org.semanticweb.owl.inference.OWLReasonerFactory;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;

import com.clarkparsia.owlapi.OWL;

public class PelletReasonerOwlapiv2Test {

	final static String file = "http://localhost:8080/juddiv3/owl-s/1.1/koala.owl";
	final static String ns = "http://localhost:8080/juddiv3/owl-s/1.1/koala.owl#";

	public static void main(String[] args) throws OWLException {
		try {
			// Create our ontology manager in the usual way.
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

			// Load a copy of the people+pets ontology. We'll load the ontology
			// from the web (it's acutally located
			// in the TONES ontology repository).
			URI docIRI = URI.create(file);
			// We load the ontology from a document - our IRI points to it
			// directly
			OWLOntology ont = manager.loadOntology(docIRI);

			// We need to create an instance of OWLReasoner. An OWLReasoner
			// provides the basic
			// query functionality that we need, for example the ability obtain
			// the subclasses
			// of a class etc. To do this we use a reasoner factory.

			// Create a reasoner factory. In this case, we will use HermiT, but
			// we could also
			// use FaCT++ (http://code.google.com/p/factplusplus/) or
			// Pellet(http://clarkparsia.com/pellet)
			// Note that (as of 03 Feb 2010) FaCT++ and Pellet OWL API 3.0.0
			// compatible libraries are
			// expected to be available in the near future).

			// For now, we'll use HermiT
			// HermiT can be downloaded from http://hermit-reasoner.com
			// Make sure you get the HermiT library and add it to your class
			// path. You can then
			// instantiate the HermiT reasoner factory:
			// Comment out the first line below and uncomment the second line
			// below to instantiate
			// the HermiT reasoner factory. You'll also need to import the
			// org.semanticweb.HermiT.Reasoner
			// package.
			// OWLReasonerFactory reasonerFactory = null;
			OWLReasonerFactory reasonerFactory = new PelletReasonerFactory();

			// We'll now create an instance of an OWLReasoner (the
			// implementation being provided by HermiT as
			// we're using the HermiT reasoner factory). The are two categories
			// of reasoner, Buffering and
			// NonBuffering. In our case, we'll create the buffering reasoner,
			// which is the default kind of reasoner.
			// We'll also attach a progress monitor to the reasoner. To do this
			// we set up a configuration that
			// knows about a progress monitor.

			OWLReasoner reasoner = reasonerFactory.createReasoner(manager);

			// Ask the reasoner to do all the necessary work now
			reasoner.classify();

			// We can determine if the ontology is actually consistent (in this
			// case, it should be).
			boolean consistent = reasoner.isConsistent(ont);
			System.out.println("Consistent: " + consistent);
			System.out.println("\n");

			// We can easily get a list of unsatisfiable classes. (A class is
			// unsatisfiable if it
			// can't possibly have any instances). Note that the
			// getUnsatisfiableClasses method
			// is really just a convenience method for obtaining the classes
			// that are equivalent
			// to owl:Nothing. In our case there should be just one
			// unsatisfiable class - "mad_cow"
			// We ask the reasoner for the unsatisfiable classes, which returns
			// the bottom node
			// in the class hierarchy (an unsatisfiable class is a subclass of
			// every class).
			Set<OWLClass> unsatisfiable = reasoner.getInconsistentClasses();
			// This node contains owl:Nothing and all the classes that are
			// equivalent to owl:Nothing -
			// i.e. the unsatisfiable classes.
			// We just want to print out the unsatisfiable classes excluding
			// owl:Nothing, and we can
			// used a convenience method on the node to get these
			for (OWLClass cls : unsatisfiable) {
				System.out.println("    " + cls);
			}
			System.out.println("\n");

			// Now we want to query the reasoner for all descendants of
			// vegetarian. Vegetarians are defined in the
			// ontology to be animals that don't eat animals or parts of
			// animals.
			OWLDataFactory fac = manager.getOWLDataFactory();
			// Get a reference to the vegetarian class so that we can as the
			// reasoner about it.
			// The full IRI of this class happens to be:
			// <http://owl.man.ac.uk/2005/07/sssw/people#vegetarian>
			OWLClass habitat = fac.getOWLClass(URI.create(ns + "Habitat"));

			// Now use the reasoner to obtain the subclasses of vegetarian.
			// We can ask for the direct subclasses of vegetarian or all of the
			// (proper) subclasses of vegetarian.
			// In this case we just want the direct ones (which we specify by
			// the "true" flag).
			Set<Set<OWLClass>> subClses = reasoner
					.getDescendantClasses(habitat);

			// The reasoner returns a NodeSet, which represents a set of Nodes.
			// Each node in the set represents a subclass of vegetarian pizza. A
			// node of classes contains classes,
			// where each class in the node is equivalent. For example, if we
			// asked for the
			// subclasses of some class A and got back a NodeSet containing two
			// nodes {B, C} and {D}, then A would have
			// two proper subclasses. One of these subclasses would be
			// equivalent to the class D, and the other would
			// be the class that is equivalent to class B and class C.

			// In this case, we don't particularly care about the equivalences,
			// so we will flatten this
			// set of sets and print the result
			Set<OWLClass> clses = OWLReasonerAdapter.flattenSetOfSets(subClses);
			clses.removeAll(unsatisfiable);
			System.out.println("Subclasses of Habitat: ");
			for (OWLClass cls : clses) {
				System.out.println("    " + cls);
			}
			System.out.println("\n");
			
			
			subClses = reasoner.getDescendantClasses(OWL.Nothing);
			clses = OWLReasonerAdapter.flattenSetOfSets(subClses);
			System.out.println("Subclasses of impossible: ");
			for (OWLClass cls : clses) {
				System.out.println("    " + cls);
			}
			System.out.println("\n");

			// In this case, we should find that the classes, cow, sheep and
			// giraffe are vegetarian. Note that in this
			// ontology only the class cow had been stated to be a subclass of
			// vegetarian. The fact that sheep and
			// giraffe are subclasses of vegetarian was implicit in the ontology
			// (through other things we had said)
			// and this illustrates why it is important to use a reasoner for
			// querying an ontology.

			// We can easily retrieve the instances of a class. In this example
			// we'll obtain the instances of
			// the class pet. This class has a full IRI of
			// <http://owl.man.ac.uk/2005/07/sssw/people#pet>

			// We need to obtain a reference to this class so that we can ask
			// the reasoner about it.
			OWLClass college = fac.getOWLClass(URI.create(ns + "College"));
			subClses = reasoner.getAncestorClasses(college);
			clses = OWLReasonerAdapter.flattenSetOfSets(subClses);
			System.out.println("Superclasses of college: ");
			for (OWLClass cls : clses) {
				System.out.println("    " + cls);
			}
			System.out.println("\n");

		} catch (UnsupportedOperationException exception) {
			System.out.println("Unsupported reasoner operation.");
		} catch (OWLOntologyCreationException e) {
			System.out.println("Could not load the pizza ontology: "
					+ e.getMessage());
		}

	}

}
