import java.util.Set;

import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.utils.ATermUtils;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;


import aterm.ATermAppl;

import com.clarkparsia.owlapiv3.OWL;
import com.clarkparsia.pellet.owlapiv3.Reasoner;



public class test2 {
	public static void main(String[] args) throws OWLOntologyCreationException {
		String aString = "aaa";
		String bString = "bbb";
		
//		OWLIndividual individual = OWL.Individual(aString);
//		OWLClass owlClass = OWL.Class(bString);
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
		
		
		KnowledgeBase kb = new KnowledgeBase();
		ATermAppl individual = ATermUtils.makeTermAppl("Person");
		ATermAppl classAppl = ATermUtils.makeTermAppl("Person");
		kb.addClass(classAppl);
		kb.addIndividual(individual);
		
		Reasoner reasoner = new Reasoner(manager, kb);
		OWLDataFactory factory = manager.getOWLDataFactory();
		OWLClass owlClass = factory.getOWLClass(IRI.create("Person"));
		OWLNamedIndividual individual2 = factory.getOWLNamedIndividual(IRI.create("Person"));
		
		System.out.println("class: " + owlClass);
		Set<OWLOntology> ontoloties = manager.getOntologies();
		
		Set<OWLClass> owlClassesSet = reasoner.getClasses();
		System.out.println("owlClass: " + owlClassesSet);
		Set<OWLIndividual> individualSet = reasoner.getIndividuals();
		System.out.println("individual: " + individualSet);
		System.out.println("ontologies: " + ontoloties);
		if (owlClass.isDefined(ontoloties))
			System.out.println("class");
		
		if (individual2.getTypes(ontoloties).isEmpty() == false)
			System.out.println("individual");
		
		
	
	
	}
	

}
