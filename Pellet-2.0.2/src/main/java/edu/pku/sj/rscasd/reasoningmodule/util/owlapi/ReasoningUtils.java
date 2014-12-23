package edu.pku.sj.rscasd.reasoningmodule.util.owlapi;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mindswap.pellet.ABox;
import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.owlapi.PelletLoader;
import org.mindswap.pellet.owlapi.Reasoner;
import org.mindswap.pellet.taxonomy.Taxonomy;
import org.mindswap.pellet.utils.ATermUtils;
import org.mindswap.pellet.utils.Bool;
import org.semanticweb.owl.inference.OWLReasonerAdapter;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLIndividual;

import aterm.ATerm;
import aterm.ATermAppl;

public class ReasoningUtils {

	private static final Log logger = LogFactory.getLog(ReasoningUtils.class);

	public static boolean isSimilarIndividual(OWLDataFactory factory, Reasoner reasoner, String firstIndividualUri,
			String secondIndividualUri) {
		return belongTo(factory, reasoner, firstIndividualUri, secondIndividualUri)
				|| belongTo(factory, reasoner, secondIndividualUri, firstIndividualUri);
	}

	public static boolean isSimilarIndividual(Reasoner reasoner, OWLIndividual firstIndividual,
			OWLIndividual secondIndividual) {
		return belongTo(reasoner, firstIndividual, secondIndividual)
				|| belongTo(reasoner, secondIndividual, firstIndividual);
	}

	public static boolean belongTo(OWLDataFactory factory, Reasoner reasoner, String firstIndividualUri,
			String secondIndividualUri) {
		if (factory == null) {
			return false;
		}

		OWLIndividual firstIndividual = factory.getOWLIndividual(URI.create(firstIndividualUri));
		OWLIndividual secondIndividual = factory.getOWLIndividual(URI.create(secondIndividualUri));
		return belongTo(reasoner, firstIndividual, secondIndividual);
	}

	/**
	 * Detail properties in First, as super class (down-cast) or sub class
	 * (up-cast)
	 */
	public static boolean belongTo(Reasoner reasoner, OWLIndividual firstIndividual, OWLIndividual secondIndividual) {
		if (reasoner == null) {
			return false;
		}

		if (firstIndividual == null && secondIndividual == null) {
			return true;
		}

		if (firstIndividual == null || secondIndividual == null) {
			return false;
		}

		KnowledgeBase kb = reasoner.getKB();
		PelletLoader loader = reasoner.getLoader();
		ABox abox = kb.getABox();
		Taxonomy<ATermAppl> taxonomy = kb.getTaxonomy();

		ATermAppl firstATerm = loader.term(firstIndividual);
		ATermAppl secondATerm = loader.term(secondIndividual);
		Set<OWLDescription> firstOwlTypes = firstIndividual.getTypes(reasoner.getLoadedOntologies());
		OWLClass firstOwlType = (firstOwlTypes.isEmpty() ? reasoner.getType(firstIndividual)
				: (OWLClass) (firstOwlTypes.iterator().next()));
		Set<OWLDescription> secondOwlTypes = secondIndividual.getTypes(reasoner.getLoadedOntologies());
		OWLClass secondOwlType = (secondOwlTypes.isEmpty() ? reasoner.getType(secondIndividual)
				: (OWLClass) (secondOwlTypes.iterator().next()));
		ATermAppl firstType = loader.term(firstOwlType);
		ATermAppl secondType = loader.term(secondOwlType);

		ATerm notTopATerm = ATermUtils.makeNot(ATermUtils.TOP);
		Set<ATermAppl> unsatisfiable = kb.getUnsatisfiableClasses();
		Set<ATermAppl> firstTypeSubs = taxonomy.getFlattenedSubs(firstType, false);
		Set<ATermAppl> secondTypeSubs = taxonomy.getFlattenedSubs(secondType, false);
		firstTypeSubs.add(firstType);
		firstTypeSubs.removeAll(unsatisfiable);
		firstTypeSubs.remove(notTopATerm);
		secondTypeSubs.add(secondType);
		secondTypeSubs.removeAll(unsatisfiable);
		secondTypeSubs.remove(notTopATerm);

		/*-
		 * All props in Second all within the First one. 
		 * First one is more detail. Will belong to the Second. 
		 */
		Bool isProp1stBelongTo2nd = abox.isKnownSubClassOf(ATermUtils.makeValue(firstATerm), ATermUtils
				.makeValue(secondATerm));
		/*- if first is super, down-cast: super(with more attribute)*/
		boolean isSuperDownCasted = (firstTypeSubs.contains(secondType));
		/*- if first is sub, up-cast: super(with less attribute) and check the type*/
		boolean isSubUpCasted = abox.getIndividual(firstATerm).hasObviousType(secondTypeSubs);
		// 
		return ((isProp1stBelongTo2nd.isTrue() || isProp1stBelongTo2nd.isUnknown()) && (isSuperDownCasted || isSubUpCasted));
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

	/**
	 * 
	 * @param individuals
	 * @param c
	 * @param cacheModel
	 * @return
	 * 
	 * @see org.mindswap.pellet.ABox#isConsistent(Collection<ATermAppl>,
	 *      ATermAppl, boolean)
	 */
	public static boolean checkConsistent(Reasoner reasoner, Collection<OWLIndividual> individuals, OWLClass owlClz,
			boolean cacheModel) {
		KnowledgeBase kb = reasoner.getKB();
		ABox abox = kb.getABox();
		PelletLoader loader = reasoner.getLoader();
		Object result = Boolean.FALSE;

		Collection<ATermAppl> indATerms = null;
		if (individuals != null) {
			indATerms = new ArrayList<ATermAppl>(individuals.size());
			for (OWLIndividual individual : individuals) {
				indATerms.add(loader.term(individual));
			}
		}

		try {
			Method method = ABox.class.getDeclaredMethod("isConsistent", new Class<?>[] { Collection.class,
					ATermAppl.class, boolean.class });
			method.setAccessible(true);
			result = method.invoke(abox, new Object[] { indATerms, loader.term(owlClz), cacheModel });
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return ((result instanceof Boolean) ? ((Boolean) result) : false);
	}

	// Individual to which class
}
