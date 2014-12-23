package cn.edu.pku.ss.matchmaker.reasoning.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owl.inference.OWLReasonerAdapter;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.clarkparsia.pellet.owlapiv3.Reasoner;

public class Utils {
	
	private static Logger logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.reasoning.impl.Utils.class);
	
	
	public static OWLEntity locateOWLEntity(IRI iri, OWLOntologyManager manager, Reasoner reasoner) {
		return locateOWLEntity(iri, manager, reasoner, true);
	}
	
	public static OWLEntity locateOWLEntity(IRI iri, OWLOntologyManager manager, Reasoner reasoner, boolean tryMoreIfOntologyNotExist) {
		if (iri == null) {
			logger.error("parameter is null or illegal");
			return null;
		}
		
		OWLDataFactory factory = manager.getOWLDataFactory();
		OWLClass owlClass = factory.getOWLClass(iri);
		OWLNamedIndividual owlIndividual = factory.getOWLNamedIndividual(iri);
		
		if (owlIndividual != null && reasoner.isDefined(owlIndividual))
			return owlIndividual;
		
		if (owlClass != null && reasoner.isDefined(owlClass))
			return owlClass;
		
		return null;
		
//		Set<OWLOntology> ontologies = manager.getOntologies();
//		
//		if (owlIndividual.getTypes(ontologies).isEmpty() == false)
//			return owlIndividual;
//		
//		if (owlClass.isDefined(ontologies))
//			return owlClass;
//		
//		if (tryMoreIfOntologyNotExist == true) {
//			String uri = iri.toString();
//			int pos = uri.indexOf("#");
//			if (pos > 0)
//				uri = uri.substring(0, pos);
//			
//			try {
//				OWLOntology ontology;
//				if ((ontology = manager.loadOntology(IRI.create(uri))) == null) {
//					logger.error("failed to load ontology : " + uri);
//					return null;
//				}
//				
//	
//				return locateOWLEntity(iri, manager, false);
//			} catch (OWLOntologyCreationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		return null;
	}
	
	
	public static Set<OWLEntity> getDirectIndividuals(Reasoner reasoner, Set<? extends OWLEntity> owlClasses) {
		Set<OWLEntity> individuals = new HashSet<OWLEntity>();
		if (reasoner == null || owlClasses == null || owlClasses.isEmpty()) {
			return individuals;
		}

		for (OWLEntity owlClz : owlClasses) {
			if (!owlClz.isOWLClass()) {
				continue;
			}

			individuals.addAll(reasoner.getIndividuals((OWLClass) owlClz, true));
		}

		return individuals;
	}

	public static Set<OWLEntity> getOWLClass(Reasoner reasoner, Set<OWLNamedIndividual> individuals) {
		Set<OWLEntity> owlClasses = new HashSet<OWLEntity>();
		if (reasoner == null || individuals == null || individuals.isEmpty()) {
			return owlClasses;
		}

		for (OWLNamedIndividual individual : individuals) {
			owlClasses.addAll(OWLReasonerAdapter.flattenSetOfSets(reasoner.getTypes(individual, true)));
		}

		return owlClasses;
	}
	

	public static Set<OWLClass> getDirectSubClasses(Reasoner reasoner, Set<? extends OWLEntity> owlClasses) {
		Set<OWLClass> subClasses = new HashSet<OWLClass>();
		if (reasoner == null || owlClasses == null || owlClasses.isEmpty()) {
			return subClasses;
		}

		for (OWLEntity owlClz : owlClasses) {
			if (!owlClz.isOWLClass()) {
				continue;
			}

			Set<OWLClass> equClasses = reasoner.getAllEquivalentClasses((OWLClass) owlClz);
			equClasses.add((OWLClass) owlClz);
			for (OWLClass tmpOwlClz : equClasses) {
				subClasses.addAll(OWLReasonerAdapter.flattenSetOfSets(reasoner.getSubClasses(tmpOwlClz)));
			}
		}

		return subClasses;
	}
	
	
	public static Set<OWLClass> getDirectSuperClasses(Reasoner reasoner, Set<? extends OWLEntity> owlClasses) {
		Set<OWLClass> superClasses = new HashSet<OWLClass>();
		if (reasoner == null || owlClasses == null || owlClasses.isEmpty()) {
			return superClasses;
		}

		for (OWLEntity owlClz : owlClasses) {
			if (!owlClz.isOWLClass()) {
				continue;
			}

			Set<OWLClass> equClasses = reasoner.getAllEquivalentClasses((OWLClass) owlClz);
			equClasses.add((OWLClass) owlClz);
			for (OWLClass tmpOwlClz : equClasses) {
				superClasses.addAll(OWLReasonerAdapter.flattenSetOfSets(reasoner.getSuperClasses(tmpOwlClz)));
			}
		}
		
		return superClasses;
	}
}

