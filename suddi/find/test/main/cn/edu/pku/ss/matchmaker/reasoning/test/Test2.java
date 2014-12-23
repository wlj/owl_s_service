package cn.edu.pku.ss.matchmaker.reasoning.test;

import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.clarkparsia.pellet.owlapiv3.Reasoner;

public class Test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String base = "http://localhost:8080/juddiv3/owl-s/1.1/Concepts.owl#";
		OWLOntologyManager manager =  OWLManager.createOWLOntologyManager();
		Reasoner reasoner = new Reasoner(manager);
		try {
			OWLOntology ontology = manager.loadOntology(IRI.create(base));
			reasoner.loadOntology(ontology);
			
			reasoner.getKB().realize();
			reasoner.getKB().prepare();
			reasoner.classify();
			reasoner.getKB().printClassTree();
			
			Set<OWLClass> owlclasses = reasoner.getClasses();
			System.out.println("*****************************");
			for (OWLClass cs : owlclasses) {
				System.out.println("owlclass : " + cs.getIRI().toString());
				if (reasoner.isDefined(cs)) {
					System.out.println("define: " + cs.getIRI());
				}
			}
			Set<OWLIndividual> individuals = reasoner.getIndividuals();
			for(OWLIndividual individual : individuals) {
				System.out.println("individual: " + individual.asOWLNamedIndividual().getIRI().toString());
				if (reasoner.isDefined(individual.asOWLNamedIndividual()))
					System.out.println("define: " + individual.asOWLNamedIndividual().getIRI().toString());
			}
			System.out.println("------------------------------");
			
			
			String aa = "http://localhost:8080/juddiv3/owl-s/1.1/Concepts.owl#Airport";
			OWLClass owlclz = manager.getOWLDataFactory().getOWLClass(IRI.create(aa));
			System.out.println("##: " + owlclz.getIRI().toString());
			if (reasoner.isDefined(owlclz))
				System.out.println("@@: " + owlclz.getIRI().toString());
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
