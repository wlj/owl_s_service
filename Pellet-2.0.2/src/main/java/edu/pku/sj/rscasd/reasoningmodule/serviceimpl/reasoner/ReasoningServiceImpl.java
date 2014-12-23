package edu.pku.sj.rscasd.reasoningmodule.serviceimpl.reasoner;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.owlapi.Reasoner;
import org.semanticweb.owl.inference.OWLReasonerAdapter;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLNamedObject;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyURIMapper;
import org.semanticweb.owl.util.NonMappingOntologyURIMapper;

import uk.ac.manchester.cs.owl.OWLOntologyURIMapperImpl;
import edu.pku.sj.rscasd.reasoningmodule.constant.ResultItemType;
import edu.pku.sj.rscasd.reasoningmodule.exception.InvalidReasonerPreparedException;
import edu.pku.sj.rscasd.reasoningmodule.exception.ReasonerNotExistException;
import edu.pku.sj.rscasd.reasoningmodule.model.ReasoningResult;
import edu.pku.sj.rscasd.reasoningmodule.service.reasoner.ReasoningService;
import edu.pku.sj.rscasd.reasoningmodule.util.owlapi.OWLUtil;
import edu.pku.sj.rscasd.reasoningmodule.util.owlapi.ReasoningUtils;

public class ReasoningServiceImpl implements ReasoningService {

	@SuppressWarnings("unused")
	private final static Log logger = LogFactory.getLog(ReasoningServiceImpl.class);

	public final ThreadLocal<List<OWLOntologyURIMapper>> uriMappers = new ThreadLocal<List<OWLOntologyURIMapper>>() {
		@Override
		protected List<OWLOntologyURIMapper> initialValue() {
			List<OWLOntologyURIMapper> uriMapperObjs = new LinkedList<OWLOntologyURIMapper>();
			uriMapperObjs.add(new NonMappingOntologyURIMapper());
			uriMapperObjs.add(new OWLOntologyURIMapperImpl());
			return uriMapperObjs;
		}
	};

	private final ThreadLocal<Map<String, Reasoner>> reasonerCache = new ThreadLocal<Map<String, Reasoner>>() {
		@Override
		protected Map<String, Reasoner> initialValue() {
			return new HashMap<String, Reasoner>();
		}
	};

	private final ThreadLocal<Reasoner> reasonerContainer = new ThreadLocal<Reasoner>() {
		@Override
		protected Reasoner initialValue() {
			return OWLUtil.getPelletReasoner();
		}
	};

	public void clearURIMapper() {
		getURIMapper().clear();
	}

	private List<OWLOntologyURIMapper> getURIMapper() {
		List<OWLOntologyURIMapper> uriMapperObjs = uriMappers.get();
		if (uriMapperObjs == null) {
			uriMapperObjs = new LinkedList<OWLOntologyURIMapper>();
			uriMapperObjs.add(new NonMappingOntologyURIMapper());
			uriMapperObjs.add(new OWLOntologyURIMapperImpl());
			uriMappers.set(uriMapperObjs);
		}

		return uriMapperObjs;
	}

	public ReasoningServiceImpl() {
	}

	public ReasoningServiceImpl(OWLOntologyURIMapper uriMapper) {
		getURIMapper().add(0, (uriMapper == null ? new OWLOntologyURIMapperImpl() : uriMapper));
	}

	public void setURIMapper(OWLOntologyURIMapper uriMapper) {
		clearURIMapper();
		getURIMapper().add(uriMapper == null ? new OWLOntologyURIMapperImpl() : uriMapper);
	}

	public void addURIMapper(OWLOntologyURIMapper uriMapper) {
		getURIMapper().add(0, uriMapper == null ? new OWLOntologyURIMapperImpl() : uriMapper);
	}

	public void deInitAllReasonerConfigs() {
		// clear up current one.
		deInitCurrentReasoner();
		// clear up cached ones.
		Map<String, Reasoner> reasonerCache = getReasonerCache();
		for (Entry<String, Reasoner> entry : reasonerCache.entrySet()) {
			Reasoner reasoner = entry.getValue();
			if (reasoner == null) {
				continue;
			}
			reasoner.clearOntologies();
		}
		reasonerCache.clear();
	}

	public void deInitCurrentReasoner() {
		Reasoner currentReasoner = getCurrentReasoner();
		if (currentReasoner == null) {
			throw new InvalidReasonerPreparedException("No available reasoner setup in current context. ");
		}
		currentReasoner.clearOntologies();
	}

	public void deInitReasoner(String reasonerId) {
		if (reasonerId == null || (reasonerId = reasonerId.trim()).equals("")) {
			return;
		}

		Reasoner currentReasoner = getCurrentReasoner();
		String currentReasonerId = String.valueOf(System.identityHashCode(currentReasoner));

		// check whether it is the current one.
		if (reasonerId.equals(currentReasonerId)) {
			deInitCurrentReasoner();
			return;
		}

		Map<String, Reasoner> reasonerCache = getReasonerCache();
		Reasoner targetReasoner = reasonerCache.remove(reasonerId);
		if (targetReasoner != null) {
			targetReasoner.clearOntologies();
		}
	}

	/**
	 * Get current reasoner used.
	 * 
	 * @return Reasoner
	 */
	public Reasoner getCurrentReasoner() {
		return reasonerContainer.get();
	}

	public Set<ReasoningResult> getMatchings(URI srcUri, OntologyMatchingAndRenderingHandler handler) {
		Set<ReasoningResult> results = new HashSet<ReasoningResult>();
		if (srcUri == null) {
			return results;
		}

		Reasoner currentReasoner = getCurrentReasoner();
		Set<OWLOntology> involvedOntologies = new HashSet<OWLOntology>(currentReasoner.getLoadedOntologies());
		Set<OWLOntology> additionalOntologies = new HashSet<OWLOntology>();

		// check whether to add each time.
		additionalOntologies.addAll(OWLUtil.getOWLOntologyManager(getURIMapper()).getOntologies());
		additionalOntologies.addAll(involvedOntologies);

		Set<OWLEntity> entities = OWLUtil.locateOWLEntity(srcUri, additionalOntologies);
		Set<OWLOntology> relatedOntologies = OWLUtil.locateOntologies(entities);
		// entities = filterDefinedOWLEntities(entities);

		// check to involve the ones not yet.
		relatedOntologies.removeAll(involvedOntologies);
		currentReasoner.loadOntologies(relatedOntologies);
		// XXX whether to prepare after this each time.
		// prepare the reasoner.
		prepare();

		// Reasoning
		Set<OWLEntity> resultEntities = (handler == null ? Collections.<OWLEntity> emptySet() : handler
				.execute(entities));

		/*-
		 *  remove: equivalent Nothings. 
		 *  Result will consider: original Nothing & Thing
		 */
		Set<OWLClass> equNothings = currentReasoner.getInconsistentClasses();
		equNothings.remove(OWLUtil.getNothing());
		resultEntities.removeAll(equNothings);

		results.addAll(renderResultFromOWLEntity(resultEntities));
		resultEntities.clear();
		involvedOntologies.clear();
		additionalOntologies.clear();
		return results;
	}

	public Set<ReasoningResult> getFulfilledExactMatchings(String srcUriStr) {
		try {
			URI srcUri = URI.create(srcUriStr);
			return getFulfilledExactMatchings(srcUri);
		} catch (Exception e) {
			return Collections.<ReasoningResult> emptySet();
		}
	}

	public Set<ReasoningResult> getFulfilledExactMatchings(URI srcUri) {
		// All direct-subClass and equivalent classes.
		return getMatchings(srcUri, new OntologyMatchingAndRenderingHandler() {
			public Set<OWLEntity> execute(Set<? extends OWLEntity> entities) {
				// Reasoning
				Set<OWLEntity> resultEntities = new HashSet<OWLEntity>();
				Reasoner currentReasoner = getCurrentReasoner();
				for (OWLEntity entity : entities) {
					// all direct subClass
					/*-
					 *  how about the subclass of the equivalent class: can also be queried.
					 */
					if (entity.isOWLClass()) {
						// all equivalent first.
						Set<OWLClass> equOWLClasses = currentReasoner.getAllEquivalentClasses((OWLDescription) entity);
						resultEntities.add(entity);
						resultEntities.addAll(equOWLClasses);
						/*- all direct subClasses, include direct subclasses of equivalent ones. */
						Set<OWLClass> directClasses = OWLReasonerAdapter.flattenSetOfSets(currentReasoner
								.getSubClasses((OWLClass) entity));
						resultEntities.addAll(directClasses);
						directClasses.clear();
						equOWLClasses.clear();
					}
					if (entity.isOWLIndividual()) {
						OWLIndividual individual = (OWLIndividual) entity;
						Set<OWLIndividual> equIndividuals = currentReasoner.getSameAsIndividuals(individual);
						equIndividuals.add(individual);
						Set<OWLEntity> allDefinedClassesIncludeEquivalentOnes = OWLUtil.getOWLClass(currentReasoner,
								equIndividuals);
						// all equivalent first.
						resultEntities.addAll(equIndividuals);

						// all direct subClass
						/*- get all direct subclass for defined classes of individual */
						/*- get all direct subclass for defined classes of equivalent individuals */
						Set<OWLEntity> directSubClasses = execute(allDefinedClassesIncludeEquivalentOnes);
						resultEntities.addAll(directSubClasses);

						// clear the direct
						directSubClasses.clear();
						allDefinedClassesIncludeEquivalentOnes.clear();
						equIndividuals.clear();
					}
				}

				// Add related individuals
				resultEntities.addAll(ReasoningUtils.getDirectIndividuals(currentReasoner, resultEntities));
				return resultEntities;
			}
		});
	}

	public Set<ReasoningResult> getFulfilledPluginMatchings(String srcUriStr) {
		try {
			URI srcUri = URI.create(srcUriStr);
			return getFulfilledPluginMatchings(srcUri);
		} catch (Exception e) {
			return Collections.<ReasoningResult> emptySet();
		}
	}

	public Set<ReasoningResult> getFulfilledPluginMatchings(URI srcUri) {
		// All indirect-subClass.
		return getMatchings(srcUri, new OntologyMatchingAndRenderingHandler() {
			public Set<OWLEntity> execute(Set<? extends OWLEntity> entities) {
				// Reasoning
				Set<OWLEntity> resultEntities = new HashSet<OWLEntity>();
				Reasoner currentReasoner = getCurrentReasoner();
				for (OWLEntity entity : entities) {
					/*-
					 *  how about the subclass of the equivalent class: can also be queried.
					 */
					if (entity.isOWLClass()) {
						// direct subClass, include subclass of equivalent ones.
						Set<OWLClass> directSubclasses = OWLReasonerAdapter.flattenSetOfSets(currentReasoner
								.getSubClasses((OWLClass) entity));
						Set<OWLClass> allSubclasses = OWLReasonerAdapter.flattenSetOfSets(currentReasoner
								.getDescendantClasses((OWLClass) entity));
						allSubclasses.removeAll(directSubclasses);
						resultEntities.addAll(allSubclasses);
						allSubclasses.clear();
						directSubclasses.clear();
					}
					if (entity.isOWLIndividual()) {
						OWLIndividual individual = (OWLIndividual) entity;
						Set<OWLIndividual> equIndividuals = currentReasoner.getSameAsIndividuals(individual);
						equIndividuals.add(individual);
						Set<OWLEntity> allDefinedClassesIncludeEquivalentOnes = OWLUtil.getOWLClass(currentReasoner,
								equIndividuals);

						/*- all indirect subclass, also include those of equivalent ones.*/
						/*- get all indirect subclass for defined classes of individual */
						/*- get all indirect subclass for defined classes of equivalent individuals */
						Set<OWLEntity> indirectSubClasses = execute(allDefinedClassesIncludeEquivalentOnes);
						resultEntities.addAll(indirectSubClasses);
						indirectSubClasses.clear();
						allDefinedClassesIncludeEquivalentOnes.clear();
						equIndividuals.clear();
					}
				}

				// Add related individuals
				resultEntities.addAll(ReasoningUtils.getDirectIndividuals(currentReasoner, resultEntities));
				return resultEntities;
			}
		});
	}

	public Set<ReasoningResult> getFulfilledSubsumeMatchings(String srcUriStr) {
		try {
			URI srcUri = URI.create(srcUriStr);
			return getFulfilledSubsumeMatchings(srcUri);
		} catch (Exception e) {
			return Collections.<ReasoningResult> emptySet();
		}
	}

	public Set<ReasoningResult> getFulfilledSubsumeMatchings(URI srcUri) {
		// All parent
		return getMatchings(srcUri, new OntologyMatchingAndRenderingHandler() {
			public Set<OWLEntity> execute(Set<? extends OWLEntity> entities) {
				// Reasoning
				Set<OWLEntity> resultEntities = new HashSet<OWLEntity>();
				Reasoner currentReasoner = getCurrentReasoner();
				for (OWLEntity entity : entities) {
					/*-
					 *  how about the parent of the equivalent class: can also be queried.
					 */
					if (entity.isOWLClass()) {
						// all parent, include those of equivalent ones.
						Set<OWLClass> allSuperclasses = OWLReasonerAdapter.flattenSetOfSets(currentReasoner
								.getAncestorClasses((OWLClass) entity));
						resultEntities.addAll(allSuperclasses);
						allSuperclasses.clear();
					}
					if (entity.isOWLIndividual()) {
						OWLIndividual individual = (OWLIndividual) entity;
						Set<OWLIndividual> equIndividuals = currentReasoner.getSameAsIndividuals(individual);
						equIndividuals.add(individual);
						Set<OWLEntity> allDefinedClassesIncludeEquivalentOnes = OWLUtil.getOWLClass(currentReasoner,
								equIndividuals);

						/*- all parent, also include those of equivalent ones.*/
						/*- get all parent for defined classes of individual */
						/*- get all parent for equivalent individuals */
						Set<OWLEntity> superClasses = execute(allDefinedClassesIncludeEquivalentOnes);
						resultEntities.addAll(superClasses);
						superClasses.clear();
						allDefinedClassesIncludeEquivalentOnes.clear();
						equIndividuals.clear();
					}
				}

				// Add related individuals
				resultEntities.addAll(ReasoningUtils.getDirectIndividuals(currentReasoner, resultEntities));
				return resultEntities;
			}
		});
	}

	public Set<ReasoningResult> getFulfillingExactMatchings(String srcUriStr) {
		try {
			URI srcUri = URI.create(srcUriStr);
			return getFulfillingExactMatchings(srcUri);
		} catch (Exception e) {
			return Collections.<ReasoningResult> emptySet();
		}
	}

	public Set<ReasoningResult> getFulfillingExactMatchings(URI srcUri) {
		// All direct-parent and equivalent classes.
		return getMatchings(srcUri, new OntologyMatchingAndRenderingHandler() {
			public Set<OWLEntity> execute(Set<? extends OWLEntity> entities) {
				// Reasoning
				Set<OWLEntity> resultEntities = new HashSet<OWLEntity>();
				Reasoner currentReasoner = getCurrentReasoner();
				for (OWLEntity entity : entities) {
					// all direct parent
					/*-
					 *  how about the parent of the equivalent class: can also be queried.
					 */
					if (entity.isOWLClass()) {
						// all equivalent first.
						Set<OWLClass> equOWLClasses = currentReasoner.getAllEquivalentClasses((OWLDescription) entity);
						resultEntities.add(entity);
						resultEntities.addAll(equOWLClasses);
						/*- all direct parent, include direct parent of equivalent ones. */
						Set<OWLClass> directParents = OWLReasonerAdapter.flattenSetOfSets(currentReasoner
								.getSuperClasses((OWLClass) entity));
						resultEntities.addAll(directParents);
						directParents.clear();
						equOWLClasses.clear();
					}
					if (entity.isOWLIndividual()) {
						OWLIndividual individual = (OWLIndividual) entity;
						Set<OWLIndividual> equIndividuals = currentReasoner.getSameAsIndividuals(individual);
						equIndividuals.add(individual);
						Set<OWLEntity> allDefinedClassesIncludeEquivalentOnes = OWLUtil.getOWLClass(currentReasoner,
								equIndividuals);
						// all equivalent first.
						resultEntities.addAll(equIndividuals);

						// all direct parent
						/*- get all direct parent for defined classes of individual */
						/*- get all direct parent for defined classes of equivalent individuals */
						Set<OWLEntity> directParents = execute(allDefinedClassesIncludeEquivalentOnes);
						resultEntities.addAll(directParents);
						directParents.clear();
						allDefinedClassesIncludeEquivalentOnes.clear();
						equIndividuals.clear();
					}
				}

				// Add related individuals
				resultEntities.addAll(ReasoningUtils.getDirectIndividuals(currentReasoner, resultEntities));
				return resultEntities;
			}
		});
	}

	public Set<ReasoningResult> getFulfillingPluginMatchings(String srcUriStr) {
		try {
			URI srcUri = URI.create(srcUriStr);
			return getFulfillingPluginMatchings(srcUri);
		} catch (Exception e) {
			return Collections.<ReasoningResult> emptySet();
		}
	}

	public Set<ReasoningResult> getFulfillingPluginMatchings(URI srcUri) {
		// All indirect-parent.
		return getMatchings(srcUri, new OntologyMatchingAndRenderingHandler() {
			public Set<OWLEntity> execute(Set<? extends OWLEntity> entities) {
				// Reasoning
				Set<OWLEntity> resultEntities = new HashSet<OWLEntity>();
				Reasoner currentReasoner = getCurrentReasoner();
				for (OWLEntity entity : entities) {
					/*-
					 *  how about the parent of the equivalent class: can also be queried.
					 */
					if (entity.isOWLClass()) {
						/*- direct parent, include parent of equivalent ones. */
						Set<OWLClass> directParents = OWLReasonerAdapter.flattenSetOfSets(currentReasoner
								.getSuperClasses((OWLClass) entity));
						Set<OWLClass> allParents = OWLReasonerAdapter.flattenSetOfSets(currentReasoner
								.getAncestorClasses((OWLClass) entity));
						allParents.removeAll(directParents);
						resultEntities.addAll(allParents);
						allParents.clear();
						directParents.clear();
					}
					if (entity.isOWLIndividual()) {
						OWLIndividual individual = (OWLIndividual) entity;
						Set<OWLIndividual> equIndividuals = currentReasoner.getSameAsIndividuals(individual);
						equIndividuals.add(individual);
						Set<OWLEntity> allDefinedClassesIncludeEquivalentOnes = OWLUtil.getOWLClass(currentReasoner,
								equIndividuals);

						/*- all indirect parent, also include those of equivalent ones.*/
						/*- get all indirect parent for defined classes of individual */
						/*- get all indirect parent for defined classes of equivalent individuals */
						Set<OWLEntity> indirectParents = execute(allDefinedClassesIncludeEquivalentOnes);
						resultEntities.addAll(indirectParents);
						indirectParents.clear();
						allDefinedClassesIncludeEquivalentOnes.clear();
						equIndividuals.clear();
					}
				}

				// Add related individuals
				resultEntities.addAll(ReasoningUtils.getDirectIndividuals(currentReasoner, resultEntities));
				return resultEntities;
			}
		});
	}

	public Set<ReasoningResult> getFulfillingSubsumeMatchings(String srcUriStr) {
		try {
			URI srcUri = URI.create(srcUriStr);
			return getFulfillingSubsumeMatchings(srcUri);
		} catch (Exception e) {
			return Collections.<ReasoningResult> emptySet();
		}
	}

	public Set<ReasoningResult> getFulfillingSubsumeMatchings(URI srcUri) {
		// All subClass.
		return getMatchings(srcUri, new OntologyMatchingAndRenderingHandler() {
			public Set<OWLEntity> execute(Set<? extends OWLEntity> entities) {
				// Reasoning
				Set<OWLEntity> resultEntities = new HashSet<OWLEntity>();
				Reasoner currentReasoner = getCurrentReasoner();
				for (OWLEntity entity : entities) {
					/*-
					 *  how about the subclass of the equivalent class: can also be queried.
					 */
					if (entity.isOWLClass()) {
						/*- all parent, include those of equivalent ones. */
						Set<OWLClass> allSubclasses = OWLReasonerAdapter.flattenSetOfSets(currentReasoner
								.getDescendantClasses((OWLClass) entity));
						resultEntities.addAll(allSubclasses);
						allSubclasses.clear();
					}
					if (entity.isOWLIndividual()) {
						OWLIndividual individual = (OWLIndividual) entity;
						Set<OWLIndividual> equIndividuals = currentReasoner.getSameAsIndividuals(individual);
						equIndividuals.add(individual);
						Set<OWLEntity> allDefinedClassesIncludeEquivalentOnes = OWLUtil.getOWLClass(currentReasoner,
								equIndividuals);

						/*- all subClass, also include those of equivalent ones.*/
						/*- get all subclass for defined classes of individual */
						/*- get all subclass for equivalent individuals */
						Set<OWLEntity> subClasses = execute(allDefinedClassesIncludeEquivalentOnes);
						resultEntities.addAll(subClasses);
						subClasses.clear();
						allDefinedClassesIncludeEquivalentOnes.clear();
						equIndividuals.clear();
					}
				}

				// Add related individuals
				resultEntities.addAll(ReasoningUtils.getDirectIndividuals(currentReasoner, resultEntities));
				return resultEntities;
			}
		});
	}

	public Map<String, Reasoner> getReasonerCache() {
		return this.reasonerCache.get();
	}

	/**
	 * To check whether there exists. If yes, clear up first. Then create new
	 * one.
	 */
	public String initialize() {
		return initialize(true);
	}

	public String initialize(boolean loadOntologiesToReaonser) {
		return initialize(null, loadOntologiesToReaonser);
	}

	public String initialize(KnowledgeBase kb) {
		return initialize(kb, true);
	}

	public String initialize(KnowledgeBase kb, boolean loadOntologiesToReaonser) {
		return initialize(kb, loadOntologiesToReaonser, (Set<OWLOntology>) null);
	}

	public String initialize(KnowledgeBase kb, boolean loadOntologiesToReaonser, Set<OWLOntology> otherOntologies) {
		Reasoner reasoner = getCurrentReasoner();
		Set<OWLOntology> ontologiesToLoad = new HashSet<OWLOntology>();
		if (loadOntologiesToReaonser) {
			ontologiesToLoad.addAll(reasoner.getManager().getOntologies());
		}
		if (otherOntologies != null && !otherOntologies.isEmpty()) {
			ontologiesToLoad.addAll(otherOntologies);
		}

		if (reasoner != null) {
			reasoner.clearOntologies();
		} else {
			reasoner = OWLUtil.getPelletReasoner(kb);
		}

		OWLUtil.prepareReasoner(reasoner, ontologiesToLoad);
		String reasonerId = String.valueOf(System.identityHashCode(reasoner));
		getReasonerCache().put(reasonerId, reasoner);

		return reasonerId;
	}

	public OWLOntology involveManagedOntologies() {
		involveManagedOntologies(getCurrentReasoner());

		return null;
	}

	public Set<OWLOntology> involveManagedOntologies(Reasoner reasoner) {
		if (reasoner == null) {
			return new HashSet<OWLOntology>(0);
		}
		Set<OWLOntology> loadedOntologies = reasoner.getManager().getOntologies();
		reasoner.loadOntologies(loadedOntologies);
		return Collections.unmodifiableSet(loadedOntologies);
	}

	public OWLOntology loadAndInvolveOntology(OWLOntology ontology) {
		// load
		OWLOntology newOntology = OWLUtil.loadOntology(ontology);
		Reasoner currentReasoner = getCurrentReasoner();
		// involve
		if (newOntology != null) {
			currentReasoner.loadOntology(newOntology);
		}
		return newOntology;
	}

	public OWLOntology loadAndInvolveOntology(String ontologyUriStr) {
		// load
		OWLOntology newOntology = OWLUtil.loadOntology(ontologyUriStr);
		Reasoner currentReasoner = getCurrentReasoner();
		// involve
		if (newOntology != null) {
			currentReasoner.loadOntology(newOntology);
		}
		return newOntology;
	}

	public OWLOntology loadAndInvolveOntology(URI ontologyUri) {
		// load
		OWLOntology newOntology = OWLUtil.loadOntology(ontologyUri);
		Reasoner currentReasoner = getCurrentReasoner();
		// involve
		if (newOntology != null) {
			currentReasoner.loadOntology(newOntology);
		}
		return newOntology;
	}

	public OWLOntology loadOntology(OWLOntology ontology) {
		return OWLUtil.loadOntology(ontology);
	}

	public OWLOntology loadOntology(String ontologyUriStr) {
		return OWLUtil.loadOntology(ontologyUriStr);
	}

	public OWLOntology loadOntology(URI ontologyUri) {
		return OWLUtil.loadOntology(ontologyUri);
	}

	/**
	 * Merge the ontologies loaded within the Ontology Manager. And remove the
	 * merged one from the current reasoner.
	 * 
	 * @param ontologyURI
	 * @return
	 */
	public OWLOntology mergeInvolvedOntologies(URI ontologyURI) {
		return mergeInvolvedOntologies(ontologyURI, true);
	}

	public OWLOntology mergeInvolvedOntologies(URI ontologyURI, boolean removeAfterMerge) {
		Reasoner currentReasoner = getCurrentReasoner();
		Set<OWLOntology> setProvided = currentReasoner.getLoadedOntologies();
		return OWLUtil.mergeOntologies(ontologyURI, setProvided, removeAfterMerge);
	}

	/**
	 * Merge the ontologies loaded within the Ontology Manager. And remove the
	 * merged one from the ontology manager.
	 * 
	 * @param ontologyURI
	 * @return
	 */
	public OWLOntology mergeLoadedOntologies(URI ontologyURI) {
		return mergeLoadedOntologies(ontologyURI, true);
	}

	public OWLOntology mergeLoadedOntologies(URI ontologyURI, boolean removeAfterMerge) {
		Set<OWLOntology> setProvided = OWLUtil.getOWLOntologyManager(getURIMapper()).getOntologies();
		return OWLUtil.mergeOntologies(ontologyURI, setProvided, removeAfterMerge);
	}

	public void prepare() {
		OWLUtil.prepareReasoner(getCurrentReasoner(), (Set<OWLOntology>) null);
	}

	public void reInitService() {
		// clear up all configuration related to reasoners
		deInitAllReasonerConfigs();
		// also clear up the configuration for Ontology manager.
		OWLUtil.clearUpOntologies();
	}

	public void setCurrentReasoner(Reasoner reasoner) {
		Reasoner currentReasoner = reasonerContainer.get();
		if (currentReasoner != null) {
			currentReasoner.clearOntologies();
		}
	}

	/**
	 * Switch to the one in cache.
	 * 
	 * @param reasonerId
	 * @return
	 */
	public String switchReasoner(String reasonerId) {
		if (reasonerId != null && (reasonerId = reasonerId.trim()).equals("")) {
			Map<String, Reasoner> reasonerCache = getReasonerCache();
			Reasoner targetReasoner = reasonerCache.get(reasonerId);
			if (targetReasoner != null) {
				Reasoner currentReasoner = getCurrentReasoner();
				String curReasonerId = String.valueOf(System.identityHashCode(targetReasoner));
				reasonerCache.put(curReasonerId, currentReasoner);
				setCurrentReasoner(targetReasoner);
				return curReasonerId;
			}
		}
		throw new ReasonerNotExistException("Failed on switching the reasoner. Reasoner can not "
				+ "be located by given ID: " + reasonerId + ". Maybe for deinitialization or other reasons. ");
	}

	private Set<ReasoningResult> renderResultFromOWLEntity(Set<? extends OWLEntity> entities) {
		Set<ReasoningResult> results = new HashSet<ReasoningResult>();
		if (entities != null && (!entities.isEmpty())) {
			for (OWLEntity entity : entities) {
				URI uri = entity.getURI();
				String fragement = uri.getFragment();
				String uriStr = uri.toString();
				String namespace = uriStr;
				int fragIndex = uriStr.indexOf("#");
				if (fragIndex > 0) {
					namespace = uriStr.substring(0, fragIndex + 1);
				}

				ResultItemType resultItemType = ResultItemType.CLASS;
				if (entity.isOWLClass()) {
					resultItemType = ResultItemType.CLASS;
				} else if (entity.isOWLIndividual()) {
					resultItemType = ResultItemType.INDIVIDUAL;
				} else if (entity.isOWLDataProperty()) {
					resultItemType = ResultItemType.DATAPROPERTY;
				} else if (entity.isOWLObjectProperty()) {
					resultItemType = ResultItemType.OBJECTPROPERTY;
				} else if (entity.isOWLDataType()) {
					resultItemType = ResultItemType.DATAPROPERTY;
				} else {
					resultItemType = ResultItemType.UNKNOWN;
				}

				results.add(new ReasoningResult(fragement, uriStr, uriStr, resultItemType, namespace,
						(OWLNamedObject) entity));
			}
		}
		return results;
	}

	@SuppressWarnings("unused")
	private Set<OWLEntity> filterDefinedOWLEntities(Set<? extends OWLEntity> entities) {
		return filterDefinedOWLEntities(getCurrentReasoner(), entities);
	}

	private Set<OWLEntity> filterDefinedOWLEntities(Reasoner reasoner, Set<? extends OWLEntity> entities) {
		Set<OWLEntity> definedEntities = new HashSet<OWLEntity>();
		if (reasoner == null || entities == null) {
			return definedEntities;
		}

		for (OWLEntity entity : entities) {
			if (entity == null) {
				continue;
			}

			if ((entity.isOWLClass() && reasoner.isDefined((OWLClass) entity))
					|| (entity.isOWLIndividual() && reasoner.isDefined((OWLIndividual) entity))
					|| (entity.isOWLDataProperty() && reasoner.isDefined((OWLDataProperty) entity))
					|| (entity.isOWLObjectProperty() && reasoner.isDefined((OWLObjectProperty) entity))) {
				definedEntities.add(entity);
			}
		}
		return definedEntities;
	}

	public interface OntologyMatchingAndRenderingHandler {
		public Set<OWLEntity> execute(Set<? extends OWLEntity> entities);
	}
}
