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
import edu.pku.sj.rscasd.reasoningmodule.constant.MatchLevel;
import edu.pku.sj.rscasd.reasoningmodule.constant.ResultItemType;
import edu.pku.sj.rscasd.reasoningmodule.exception.InvalidReasonerPreparedException;
import edu.pku.sj.rscasd.reasoningmodule.exception.ReasonerNotExistException;
import edu.pku.sj.rscasd.reasoningmodule.model.ReasoningResult;
import edu.pku.sj.rscasd.reasoningmodule.service.reasoner.ReasoningService;
import edu.pku.sj.rscasd.reasoningmodule.util.owlapi.OWLUtil;
import edu.pku.sj.rscasd.reasoningmodule.util.owlapi.ReasoningUtils;

public class WeightedReasoningServiceImpl implements ReasoningService {

	@SuppressWarnings("unused")
	private final static Log logger = LogFactory.getLog(WeightedReasoningServiceImpl.class);

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
			return OWLUtil.getPelletReasoner(getURIMapper());
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

	public WeightedReasoningServiceImpl() {
	}

	public WeightedReasoningServiceImpl(OWLOntologyURIMapper uriMapper) {
		if (uriMapper != null) {
			getURIMapper().add(0, uriMapper);
		}
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

	public Set<ReasoningResult> getMatchings(URI srcUri, MatchLevel level, Integer nextDistance,
			OntologyMatchingHandler handler) {
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

		/*-
		 *  remove: equivalent Nothings. 
		 *  Result will consider: original Nothing & Thing
		 */
		Set<OWLClass> exclusiveResultEntities = currentReasoner.getInconsistentClasses();
		exclusiveResultEntities.remove(OWLUtil.getNothing());

		// Reasoning
		results = (handler == null ? Collections.<ReasoningResult> emptySet() : handler.execute(entities, level,
				nextDistance, exclusiveResultEntities));

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

	public Set<ReasoningResult> getFulfilledExactAlmostMatchings(URI srcUri) {
		// All direct-subClass and equivalent classes.
		return getMatchings(srcUri, MatchLevel.EXACT_ALMOST, 1, new OntologyMatchingHandler() {
			public Set<ReasoningResult> execute(Set<? extends OWLEntity> entities, MatchLevel level,
					Integer nextDistance, Set<? extends OWLEntity> exclusiveResultEntities) {
				// Reasoning
				Set<ReasoningResult> reasoningResults = new HashSet<ReasoningResult>();
				Set<OWLEntity> resultEntities = new HashSet<OWLEntity>();
				Reasoner currentReasoner = getCurrentReasoner();
				Integer currentDistance = nextDistance;
				for (OWLEntity entity : entities) {
					// all direct subClass
					/*-
					 *  how about the subclass of the equivalent class: can also be queried.
					 */
					if (entity.isOWLClass()) {
						resultEntities.clear();
						/*- all direct subClasses, include direct subclasses of equivalent ones. */
						Set<OWLClass> directSubClasses = OWLReasonerAdapter.flattenSetOfSets(currentReasoner
								.getSubClasses((OWLClass) entity));
						resultEntities.addAll(directSubClasses);
						resultEntities.removeAll(exclusiveResultEntities);
						directSubClasses.clear();

						// Add related individuals
						Set<OWLEntity> directIndividuals = ReasoningUtils.getDirectIndividuals(currentReasoner,
								resultEntities);
						resultEntities.addAll(directIndividuals);
						directIndividuals.clear();

						reasoningResults.addAll(renderResultFromOWLEntity(resultEntities, level, currentDistance));
						resultEntities.clear();
					}

					if (entity.isOWLIndividual()) {
						OWLIndividual individual = (OWLIndividual) entity;
						Set<OWLIndividual> equIndividuals = currentReasoner.getSameAsIndividuals(individual);
						equIndividuals.add(individual);

						Set<OWLEntity> allDefinedClassesForIndividual = OWLUtil.getOWLClass(currentReasoner,
								equIndividuals);
						equIndividuals.clear();

						// reasoning results for all direct subClass
						/*- get reasoning results for all direct subclass for defined classes of individual */
						/*- get reasoning results for all direct subclass for defined classes of equivalent individuals */
						Set<ReasoningResult> reasoningResultForDirectSubClasses = execute(
								allDefinedClassesForIndividual, level, currentDistance, exclusiveResultEntities);
						reasoningResults.addAll(reasoningResultForDirectSubClasses);

						// clear the direct
						reasoningResultForDirectSubClasses.clear();
						allDefinedClassesForIndividual.clear();
					}
				}

				return reasoningResults;
			}
		});
	}

	public Set<ReasoningResult> getFulfilledExactMatchings(URI srcUri) {
		Set<ReasoningResult> reasoningResults = new HashSet<ReasoningResult>();
		Set<ReasoningResult> reasoningResultsExactDirectly = getFulfilledExactDirectlyMatchings(srcUri);
		Set<ReasoningResult> reasoningResultsExactAlmost = getFulfilledExactAlmostMatchings(srcUri);
		reasoningResults.addAll(reasoningResultsExactDirectly);
		reasoningResults.addAll(reasoningResultsExactAlmost);
		reasoningResultsExactDirectly.clear();
		reasoningResultsExactAlmost.clear();
		return reasoningResults;
	}

	public Set<ReasoningResult> getFulfilledExactDirectlyMatchings(URI srcUri) {
		// All direct-subClass and equivalent classes.
		return getMatchings(srcUri, MatchLevel.EXACT, 0, new OntologyMatchingHandler() {
			public Set<ReasoningResult> execute(Set<? extends OWLEntity> entities, MatchLevel level,
					Integer nextDistance, Set<? extends OWLEntity> exclusiveResultEntities) {
				// Reasoning
				Set<ReasoningResult> reasoningResults = new HashSet<ReasoningResult>();
				Set<OWLEntity> resultEntities = new HashSet<OWLEntity>();
				Reasoner currentReasoner = getCurrentReasoner();
				Integer currentDistance = nextDistance;
				for (OWLEntity entity : entities) {
					// all direct subClass
					/*-
					 *  how about the subclass of the equivalent class: can also be queried.
					 */
					if (entity.isOWLClass()) {
						// all equivalent classes first.
						resultEntities.clear();
						Set<OWLClass> equOWLClasses = currentReasoner.getAllEquivalentClasses((OWLDescription) entity);
						resultEntities.add(entity);
						resultEntities.addAll(equOWLClasses);
						equOWLClasses.clear();
						resultEntities.removeAll(exclusiveResultEntities);

						// Add related individuals
						Set<OWLEntity> equIndividuals = ReasoningUtils.getDirectIndividuals(currentReasoner,
								resultEntities);
						resultEntities.addAll(equIndividuals);
						reasoningResults.addAll(renderResultFromOWLEntity(resultEntities, level, currentDistance));
						resultEntities.clear();
					}

					if (entity.isOWLIndividual()) {
						OWLIndividual individual = (OWLIndividual) entity;
						Set<OWLIndividual> equIndividuals = currentReasoner.getSameAsIndividuals(individual);
						equIndividuals.add(individual);

						Set<OWLEntity> allDefinedClassesIncludeEquivalentOnes = OWLUtil.getOWLClass(currentReasoner,
								equIndividuals);
						equIndividuals.clear();
						// Not involve current "equIndividuals"
						resultEntities.addAll(allDefinedClassesIncludeEquivalentOnes);
						allDefinedClassesIncludeEquivalentOnes.clear();

						// equivalent individuals will be added within this
						// execution
						// no need to add explicitly here.
						// reasoning result for equivalent individuals.
						reasoningResults
								.addAll(execute(resultEntities, level, currentDistance, exclusiveResultEntities));
						resultEntities.clear();
					}
				}

				return reasoningResults;
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
		return getMatchings(srcUri, MatchLevel.PLUGIN, 2, new OntologyMatchingHandler() {
			public Set<ReasoningResult> execute(Set<? extends OWLEntity> entities, MatchLevel level,
					Integer nextDistance, Set<? extends OWLEntity> exclusiveResultEntities) {
				// Reasoning
				Set<ReasoningResult> reasoningResults = new HashSet<ReasoningResult>();
				Reasoner currentReasoner = getCurrentReasoner();
				Integer currentDistance = nextDistance;
				for (OWLEntity entity : entities) {
					/*-
					 *  how about the subclass of the equivalent class: can also be queried.
					 */
					if (entity.isOWLClass()) {
						/*- direct subclass, include subclasses of equivalent ones. */
						// Not include the direct subclass
						Set<OWLClass> directChildren = ReasoningUtils.getDirectSubClasses(currentReasoner, Collections
								.singleton(entity));
						directChildren.removeAll(exclusiveResultEntities);
						// Not include the direct subclass
						/*-
						reasoningResults.addAll(renderResultFromOWLEntity(directChildren, level, currentDistance));
						 */
						// only consider direct individual.
						// Not for other classes for such direct individuals.
						Set<OWLClass> secondLevelChildren = ReasoningUtils.getDirectSubClasses(currentReasoner,
								directChildren);
						secondLevelChildren.removeAll(exclusiveResultEntities);
						reasoningResults.addAll(renderResultFromOWLEntity(secondLevelChildren, level, currentDistance));
						Set<OWLEntity> secondLevelChildrenIndividuals = ReasoningUtils.getDirectIndividuals(
								currentReasoner, secondLevelChildren);
						reasoningResults.addAll(renderResultFromOWLEntity(secondLevelChildrenIndividuals, level,
								currentDistance));
						secondLevelChildrenIndividuals.clear();

						Set<ReasoningResult> reasoningResultsFromThirdLevelChildren = execute(directChildren, level,
								(currentDistance + 1), exclusiveResultEntities);
						reasoningResults.addAll(reasoningResultsFromThirdLevelChildren);
						reasoningResultsFromThirdLevelChildren.clear();
						directChildren.clear();
					}

					if (entity.isOWLIndividual()) {
						OWLIndividual individual = (OWLIndividual) entity;
						Set<OWLIndividual> equIndividuals = currentReasoner.getSameAsIndividuals(individual);
						equIndividuals.add(individual);
						Set<OWLEntity> allDefinedClassesIncludeEquivalentOnes = OWLUtil.getOWLClass(currentReasoner,
								equIndividuals);
						equIndividuals.clear();

						/*- reasoning results for all indirect subclasses, also include those of equivalent ones.*/
						/*- get reasoning results for all indirect subclasses for defined classes of individual */
						/*- get reasoning results for all indirect subclasses for defined classes of equivalent individuals */
						Set<ReasoningResult> reasoningResultsForIndirectSubClasses = execute(
								allDefinedClassesIncludeEquivalentOnes, level, currentDistance, exclusiveResultEntities);
						reasoningResults.addAll(reasoningResultsForIndirectSubClasses);
						allDefinedClassesIncludeEquivalentOnes.clear();
					}
				}

				return reasoningResults;
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
		return getMatchings(srcUri, MatchLevel.SUBSUME, 1, new OntologyMatchingHandler() {
			public Set<ReasoningResult> execute(Set<? extends OWLEntity> entities, MatchLevel level,
					Integer nextDistance, Set<? extends OWLEntity> exclusiveResultEntities) {
				// Reasoning
				Set<ReasoningResult> reasoningResults = new HashSet<ReasoningResult>();
				Reasoner currentReasoner = getCurrentReasoner();
				Integer currentDistance = nextDistance;
				for (OWLEntity entity : entities) {
					/*-
					 *  how about the parent of the equivalent class: can also be queried.
					 */
					if (entity.isOWLClass()) {
						/*- all parents, include those of equivalent ones. */
						Set<OWLClass> directParents = ReasoningUtils.getDirectSuperClasses(currentReasoner, Collections
								.singleton(entity));
						directParents.removeAll(exclusiveResultEntities);
						// Include the direct parents
						reasoningResults.addAll(renderResultFromOWLEntity(directParents, level, currentDistance));
						// direct individual.
						Set<OWLEntity> directParentIndividuals = ReasoningUtils.getDirectIndividuals(currentReasoner,
								directParents);
						reasoningResults.addAll(renderResultFromOWLEntity(directParentIndividuals, level,
								currentDistance));
						directParentIndividuals.clear();

						// next level parents
						Set<ReasoningResult> reasoningResultsFromSecondLevelParents = execute(directParents, level,
								(currentDistance + 1), exclusiveResultEntities);
						reasoningResults.addAll(reasoningResultsFromSecondLevelParents);
						reasoningResultsFromSecondLevelParents.clear();
						directParents.clear();
					}

					if (entity.isOWLIndividual()) {
						OWLIndividual individual = (OWLIndividual) entity;
						Set<OWLIndividual> equIndividuals = currentReasoner.getSameAsIndividuals(individual);
						equIndividuals.add(individual);
						Set<OWLEntity> allDefinedClassesIncludeEquivalentOnes = OWLUtil.getOWLClass(currentReasoner,
								equIndividuals);
						equIndividuals.clear();

						/*- reasoning results for all parent, also include those of equivalent ones.*/
						/*- get reasoning results for all parent for defined classes of individual */
						/*- get reasoning results for all parent for equivalent individuals */
						Set<ReasoningResult> reasoningResultsForParents = execute(
								allDefinedClassesIncludeEquivalentOnes, level, currentDistance, exclusiveResultEntities);
						reasoningResults.addAll(reasoningResultsForParents);
						allDefinedClassesIncludeEquivalentOnes.clear();
					}
				}

				return reasoningResults;
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
		Set<ReasoningResult> reasoningResults = new HashSet<ReasoningResult>();
		Set<ReasoningResult> reasoningResultsExactDirectly = getFulfillingExactDirectlyMatchings(srcUri);
		Set<ReasoningResult> reasoningResultsExactAlmost = getFulfillingExactAlmostMatchings(srcUri);
		reasoningResults.addAll(reasoningResultsExactDirectly);
		reasoningResults.addAll(reasoningResultsExactAlmost);
		reasoningResultsExactDirectly.clear();
		reasoningResultsExactAlmost.clear();
		return reasoningResults;
	}

	public Set<ReasoningResult> getFulfillingExactDirectlyMatchings(URI srcUri) {
		// All direct-parent and equivalent classes.
		return getMatchings(srcUri, MatchLevel.EXACT, 0, new OntologyMatchingHandler() {
			public Set<ReasoningResult> execute(Set<? extends OWLEntity> entities, MatchLevel level,
					Integer nextDistance, Set<? extends OWLEntity> exclusiveResultEntities) {
				// Reasoning
				Set<ReasoningResult> reasoningResults = new HashSet<ReasoningResult>();
				Set<OWLEntity> resultEntities = new HashSet<OWLEntity>();
				Reasoner currentReasoner = getCurrentReasoner();
				Integer currentDistance = nextDistance;
				for (OWLEntity entity : entities) {
					if (entity.isOWLClass()) {
						resultEntities.clear();
						// all equivalent classes first.
						Set<OWLClass> equOWLClasses = currentReasoner.getAllEquivalentClasses((OWLDescription) entity);
						resultEntities.add(entity);
						resultEntities.addAll(equOWLClasses);
						resultEntities.removeAll(exclusiveResultEntities);

						// Add related individuals
						Set<OWLEntity> equIndividuals = ReasoningUtils.getDirectIndividuals(currentReasoner,
								resultEntities);
						resultEntities.addAll(equIndividuals);
						reasoningResults.addAll(renderResultFromOWLEntity(resultEntities, level, currentDistance));
						resultEntities.clear();
					}

					if (entity.isOWLIndividual()) {
						OWLIndividual individual = (OWLIndividual) entity;
						Set<OWLIndividual> equIndividuals = currentReasoner.getSameAsIndividuals(individual);
						equIndividuals.add(individual);

						Set<OWLEntity> allDefinedClassesIncludeEquivalentOnes = OWLUtil.getOWLClass(currentReasoner,
								equIndividuals);
						equIndividuals.clear();
						// Not involve current "equIndividuals"
						resultEntities.addAll(allDefinedClassesIncludeEquivalentOnes);
						allDefinedClassesIncludeEquivalentOnes.clear();

						// equivalent individuals will be added within this
						// execution
						// no need to add explicitly here.
						// reasoning result for equivalent individuals.
						reasoningResults
								.addAll(execute(resultEntities, level, currentDistance, exclusiveResultEntities));
						resultEntities.clear();
					}
				}

				return reasoningResults;
			}
		});
	}

	public Set<ReasoningResult> getFulfillingExactAlmostMatchings(URI srcUri) {
		// All direct-parent and equivalent classes.
		return getMatchings(srcUri, MatchLevel.EXACT_ALMOST, 1, new OntologyMatchingHandler() {
			public Set<ReasoningResult> execute(Set<? extends OWLEntity> entities, MatchLevel level,
					Integer nextDistance, Set<? extends OWLEntity> exclusiveResultEntities) {
				// Reasoning
				Set<ReasoningResult> reasoningResults = new HashSet<ReasoningResult>();
				Set<OWLEntity> resultEntities = new HashSet<OWLEntity>();
				Reasoner currentReasoner = getCurrentReasoner();
				Integer currentDistance = nextDistance;
				for (OWLEntity entity : entities) {
					// all direct parent
					/*-
					 *  how about the parent of the equivalent class: can also be queried.
					 */
					if (entity.isOWLClass()) {
						resultEntities.clear();
						/*- all direct parent, include direct parent of equivalent ones. */
						Set<OWLClass> directParents = OWLReasonerAdapter.flattenSetOfSets(currentReasoner
								.getSuperClasses((OWLClass) entity));
						resultEntities.addAll(directParents);
						resultEntities.removeAll(exclusiveResultEntities);
						directParents.clear();

						// Add related individuals
						Set<OWLEntity> directIndividuals = ReasoningUtils.getDirectIndividuals(currentReasoner,
								resultEntities);
						resultEntities.addAll(directIndividuals);
						directIndividuals.clear();

						reasoningResults.addAll(renderResultFromOWLEntity(resultEntities, level, currentDistance));
						resultEntities.clear();
					}

					if (entity.isOWLIndividual()) {
						OWLIndividual individual = (OWLIndividual) entity;
						Set<OWLIndividual> equIndividuals = currentReasoner.getSameAsIndividuals(individual);
						equIndividuals.add(individual);

						Set<OWLEntity> allDefinedClassesForIndividual = OWLUtil.getOWLClass(currentReasoner,
								equIndividuals);
						equIndividuals.clear();

						// reasoning results for all direct parent
						/*- get reasoning results for all direct parent for defined classes of individual */
						/*- get reasoning results for all direct parent for defined classes of equivalent individuals */
						Set<ReasoningResult> reasoningResultForDirectParentClasses = execute(
								allDefinedClassesForIndividual, level, currentDistance, exclusiveResultEntities);
						reasoningResults.addAll(reasoningResultForDirectParentClasses);

						// clear the direct
						reasoningResultForDirectParentClasses.clear();
						allDefinedClassesForIndividual.clear();
					}
				}
				return reasoningResults;
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
		return getMatchings(srcUri, MatchLevel.PLUGIN, 2, new OntologyMatchingHandler() {
			public Set<ReasoningResult> execute(Set<? extends OWLEntity> entities, MatchLevel level,
					Integer nextDistance, Set<? extends OWLEntity> exclusiveResultEntities) {
				// Reasoning
				Set<ReasoningResult> reasoningResults = new HashSet<ReasoningResult>();
				Reasoner currentReasoner = getCurrentReasoner();
				Integer currentDistance = nextDistance;
				for (OWLEntity entity : entities) {
					/*-
					 *  how about the parent of the equivalent class: can also be queried.
					 */
					if (entity.isOWLClass()) {
						/*- direct parent, include parent of equivalent ones. */
						Set<OWLClass> directParents = ReasoningUtils.getDirectSuperClasses(currentReasoner, Collections
								.singleton(entity));
						directParents.removeAll(exclusiveResultEntities);
						// Not include the direct parents
						/*-
						reasoningResults.addAll(renderResultFromOWLEntity(directParents, level, currentDistance));
						 */
						// only consider direct individual.
						// Not for other classes for such direct individuals.
						Set<OWLClass> secondLevelParents = ReasoningUtils.getDirectSuperClasses(currentReasoner,
								directParents);
						secondLevelParents.removeAll(exclusiveResultEntities);
						reasoningResults.addAll(renderResultFromOWLEntity(secondLevelParents, level, currentDistance));
						Set<OWLEntity> secondLevelParentIndividuals = ReasoningUtils.getDirectIndividuals(
								currentReasoner, secondLevelParents);
						reasoningResults.addAll(renderResultFromOWLEntity(secondLevelParentIndividuals, level,
								currentDistance));
						secondLevelParentIndividuals.clear();

						Set<ReasoningResult> reasoningResultsFromThirdLevelParents = execute(directParents, level,
								(currentDistance + 1), exclusiveResultEntities);
						reasoningResults.addAll(reasoningResultsFromThirdLevelParents);
						reasoningResultsFromThirdLevelParents.clear();
						directParents.clear();
					}

					if (entity.isOWLIndividual()) {
						OWLIndividual individual = (OWLIndividual) entity;
						Set<OWLIndividual> equIndividuals = currentReasoner.getSameAsIndividuals(individual);
						equIndividuals.add(individual);
						Set<OWLEntity> allDefinedClassesIncludeEquivalentOnes = OWLUtil.getOWLClass(currentReasoner,
								equIndividuals);
						equIndividuals.clear();

						/*- reasoning results for all indirect parent, also include those of equivalent ones.*/
						/*- get reasoning results for all indirect parent for defined classes of individual */
						/*- get reasoning results for all indirect parent for defined classes of equivalent individuals */
						Set<ReasoningResult> reasoningResultsForIndirectParents = execute(
								allDefinedClassesIncludeEquivalentOnes, level, currentDistance, exclusiveResultEntities);
						reasoningResults.addAll(reasoningResultsForIndirectParents);
						allDefinedClassesIncludeEquivalentOnes.clear();
					}
				}

				return reasoningResults;
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
		return getMatchings(srcUri, MatchLevel.SUBSUME, 1, new OntologyMatchingHandler() {
			public Set<ReasoningResult> execute(Set<? extends OWLEntity> entities, MatchLevel level,
					Integer nextDistance, Set<? extends OWLEntity> exclusiveResultEntities) {
				// Reasoning
				Set<ReasoningResult> reasoningResults = new HashSet<ReasoningResult>();
				Reasoner currentReasoner = getCurrentReasoner();
				Integer currentDistance = nextDistance;
				for (OWLEntity entity : entities) {
					/*-
					 *  how about the subclass of the equivalent class: can also be queried.
					 */
					if (entity.isOWLClass()) {
						/*- all subclasses, include those of equivalent ones. */
						Set<OWLClass> directChildren = ReasoningUtils.getDirectSubClasses(currentReasoner, Collections
								.singleton(entity));
						directChildren.removeAll(exclusiveResultEntities);
						// Include the direct children
						reasoningResults.addAll(renderResultFromOWLEntity(directChildren, level, currentDistance));
						// direct individual.
						Set<OWLEntity> directChildrenIndividuals = ReasoningUtils.getDirectIndividuals(currentReasoner,
								directChildren);
						reasoningResults.addAll(renderResultFromOWLEntity(directChildrenIndividuals, level,
								currentDistance));
						directChildrenIndividuals.clear();

						// next level children
						Set<ReasoningResult> reasoningResultsFromSecondLevelChildren = execute(directChildren, level,
								(currentDistance + 1), exclusiveResultEntities);
						reasoningResults.addAll(reasoningResultsFromSecondLevelChildren);
						reasoningResultsFromSecondLevelChildren.clear();
						directChildren.clear();

					}
					if (entity.isOWLIndividual()) {
						OWLIndividual individual = (OWLIndividual) entity;
						Set<OWLIndividual> equIndividuals = currentReasoner.getSameAsIndividuals(individual);
						equIndividuals.add(individual);
						Set<OWLEntity> allDefinedClassesIncludeEquivalentOnes = OWLUtil.getOWLClass(currentReasoner,
								equIndividuals);
						equIndividuals.clear();

						/*- reasoning results for all subClass, also include those of equivalent ones.*/
						/*- get reasoning results for all subclass for defined classes of individual */
						/*- get reasoning results for all subclass for equivalent individuals */
						Set<ReasoningResult> reasoningResultsForChildren = execute(
								allDefinedClassesIncludeEquivalentOnes, level, currentDistance, exclusiveResultEntities);
						reasoningResults.addAll(reasoningResultsForChildren);
						allDefinedClassesIncludeEquivalentOnes.clear();
					}
				}

				return reasoningResults;
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
			reasoner = OWLUtil.getPelletReasoner(kb, getURIMapper());
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
		reasonerContainer.set(reasoner);
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

	private Set<ReasoningResult> renderResultFromOWLEntity(Set<? extends OWLEntity> entities, MatchLevel level,
			Integer currentDistance) {
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
						(OWLNamedObject) entity, level, currentDistance));
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

	public interface OntologyMatchingHandler {
		public Set<ReasoningResult> execute(Set<? extends OWLEntity> entities, MatchLevel level, Integer nextDistance,
				Set<? extends OWLEntity> exclusiveResultEntities);
	}
}
