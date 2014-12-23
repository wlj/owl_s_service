package edu.pku.sj.rscasd.reasoningmodule.service.reasoner;

import java.net.URI;
import java.util.Set;

import org.mindswap.pellet.KnowledgeBase;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyURIMapper;

import edu.pku.sj.rscasd.reasoningmodule.model.ReasoningResult;

public interface ReasoningService {

	void deInitAllReasonerConfigs();

	void deInitCurrentReasoner();

	void deInitReasoner(String reasonerId);

	Set<ReasoningResult> getFulfilledExactMatchings(String srcUriStr);

	Set<ReasoningResult> getFulfilledExactMatchings(URI srcUri);

	Set<ReasoningResult> getFulfilledPluginMatchings(String srcUriStr);

	Set<ReasoningResult> getFulfilledPluginMatchings(URI srcUri);

	Set<ReasoningResult> getFulfilledSubsumeMatchings(String srcUriStr);

	Set<ReasoningResult> getFulfilledSubsumeMatchings(URI srcUri);

	Set<ReasoningResult> getFulfillingExactMatchings(String srcUriStr);

	Set<ReasoningResult> getFulfillingExactMatchings(URI srcUri);

	Set<ReasoningResult> getFulfillingPluginMatchings(String srcUriStr);

	Set<ReasoningResult> getFulfillingPluginMatchings(URI srcUri);

	Set<ReasoningResult> getFulfillingSubsumeMatchings(String srcUriStr);

	Set<ReasoningResult> getFulfillingSubsumeMatchings(URI srcUri);

	/**
	 * To check whether there exists. If yes, clear up first. Then create new
	 * one.
	 */
	String initialize();

	String initialize(boolean loadOntologiesToReaonser);

	String initialize(KnowledgeBase kb);

	String initialize(KnowledgeBase kb, boolean loadOntologiesToReaonser);

	String initialize(KnowledgeBase kb, boolean loadOntologiesToReaonser, Set<OWLOntology> otherOntologies);

	OWLOntology involveManagedOntologies();

	OWLOntology loadAndInvolveOntology(OWLOntology ontology);

	OWLOntology loadAndInvolveOntology(String ontologyUriStr);

	OWLOntology loadAndInvolveOntology(URI ontologyUri);

	OWLOntology loadOntology(OWLOntology ontology);

	OWLOntology loadOntology(String ontologyUriStr);

	OWLOntology loadOntology(URI ontologyUri);

	/**
	 * Merge the ontologies loaded within the Ontology Manager. And remove the
	 * merged one from the current reasoner.
	 * 
	 * @param ontologyURI
	 * @return
	 */
	OWLOntology mergeInvolvedOntologies(URI ontologyURI);

	OWLOntology mergeInvolvedOntologies(URI ontologyURI, boolean removeAfterMerge);

	/**
	 * Merge the ontologies loaded within the Ontology Manager. And remove the
	 * merged one from the ontology manager.
	 * 
	 * @param ontologyURI
	 * @return
	 */
	OWLOntology mergeLoadedOntologies(URI ontologyURI);

	OWLOntology mergeLoadedOntologies(URI ontologyURI, boolean removeAfterMerge);

	void prepare();

	void reInitService();

	/**
	 * Switch to the one in cache.
	 * 
	 * @param reasonerId
	 * @return
	 */
	String switchReasoner(String reasonerId);

	public void setURIMapper(OWLOntologyURIMapper uriMapper);
	
	public void addURIMapper(OWLOntologyURIMapper uriMapper);
	
	public void clearURIMapper();

}
