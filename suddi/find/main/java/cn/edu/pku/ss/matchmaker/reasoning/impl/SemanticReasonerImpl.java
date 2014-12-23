package cn.edu.pku.ss.matchmaker.reasoning.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owl.inference.OWLReasonerAdapter;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.clarkparsia.pellet.owlapiv3.Reasoner;

import cn.edu.pku.ss.matchmaker.index.impl.SemanticDataModel;
import cn.edu.pku.ss.matchmaker.reasoning.MatchLevel;
import cn.edu.pku.ss.matchmaker.reasoning.SemanticReasoner;

public class SemanticReasonerImpl implements SemanticReasoner {
	private OWLOntologyManager manager;
	private Reasoner reasoner;
	private Set<OWLOntology> owlontologies;
	
	private Logger logger;

	
	public SemanticReasonerImpl() {
		// TODO Auto-generated constructor stub
		manager = OWLManager.createOWLOntologyManager();
		reasoner = new Reasoner(manager);
		owlontologies = new HashSet<OWLOntology>();
		
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.reasoning.impl.SemanticReasonerImpl.class);
	}
	

	
	public boolean loadontologies(String filePath) {
		if (filePath == null) {
			logger.error("file path is null");
			return false;
		}
		
		File file = new File(filePath);
		if (file.isFile() == false) {
			logger.error("file: " + filePath + " is not a file");
			return false;
		}
		
		try {
			List<String> uriList = new ArrayList<String>();
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String linesString;
			
			while ((linesString = reader.readLine()) != null)
				uriList.add(linesString);
			loadOntologies(uriList);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	public boolean loadOntologies(List<String> uriList) {
		if(uriList == null) {
			logger.error("ontoloy uri list is null");
			return false;
		}
		
		for (int i = 0; i < uriList.size(); ++i) {
			if (loadOntology(uriList.get(i)) == false)
				logger.info("failed to load ontoloty uri: " + uriList.get(i));
			
		}
		
		reasoner.loadOntologies(owlontologies);
		
		//reasoner.getKB().prepare();
		reasoner.getKB().realize();
		reasoner.getKB().prepare();
		reasoner.classify();
		reasoner.getKB().printClassTree();
		
		System.out.println("inconsist: " + reasoner.getInconsistentClasses());
		return true;
	}
	
	
	
	public boolean loadOntology(String uri) {
		// TODO Auto-generated method stub
		File file = new File(uri);
		OWLOntology ontology;
		try {
			ontology = manager.loadOntology(IRI.create(uri));
			if (ontology == null) {
				logger.error("failed to load ontology");
				return false;
			}
			
			if (owlontologies.add(ontology) == false)
				return false;

		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}

	
	public Set<String> getAllConcepts() {
		// TODO Auto-generated method stub
		Set<OWLClass> owlClasses = reasoner.getClasses();
		Set<String> conceptSet = new HashSet<String>();
		
		for (OWLClass owlClass : owlClasses)
			conceptSet.add(owlClass.getIRI().toString());
		
		Set<OWLIndividual> owlIndividuals = reasoner.getIndividuals();
		
		for (OWLIndividual individual : owlIndividuals) {
			Set<OWLNamedIndividual> owlNameIndividuals = individual.getIndividualsInSignature();
			for (OWLNamedIndividual namedIndividual : owlNameIndividuals)
				conceptSet.add(namedIndividual.getIRI().toString());
		}

			
		logger.info("concepts: " + conceptSet);
		return conceptSet;
	}
	
	
	public Set<OWLEntity> getAllEntities() {
		Set<OWLEntity> entities = new HashSet<OWLEntity>();
		Set<OWLClass> owlClasses = reasoner.getClasses();
		entities.addAll(owlClasses);
		
		Set<OWLIndividual> owlIndividuals = reasoner.getIndividuals();
		
		for (OWLIndividual individual : owlIndividuals)	
			entities.addAll(individual.getIndividualsInSignature());
		
		return entities;
	}
	
	public SemanticDataModel getSemanticDataModel(String srcConcept, String destConcept) {
		if (srcConcept == null || destConcept == null) {
			logger.error("parameter is null or illegal");
			return null;
		}
		
		OWLEntity srcEntity = Utils.locateOWLEntity(IRI.create(srcConcept), manager, reasoner);
		OWLEntity destEntity = Utils.locateOWLEntity(IRI.create(destConcept), manager, reasoner);
		if (srcEntity == null || destEntity == null) {
			logger.error("failed to locate owl entity");
			return null;
		}
		
		if (srcEntity.isOWLNamedIndividual()
				&& destEntity.isOWLNamedIndividual()
                && isSameIndividual(srcEntity.asOWLNamedIndividual(), destEntity.asOWLNamedIndividual())) {
			double matchDegree = getSemanticMatchDegree(MatchLevel.EXACT, 0);
			return new SemanticDataModel(srcConcept, destConcept, MatchLevel.EXACT, 0, matchDegree);
		}
		
		
		OWLClass srcOwlClass = getCorrespondingOWLClasses(srcEntity);
		OWLClass destOwlClass = getCorrespondingOWLClasses(destEntity);
		
		int distance = getDirectedSemanticDistance(srcOwlClass, destOwlClass, 0);
		int reverseDistance = getDirectedSemanticDistance(destOwlClass, srcOwlClass, 0);
			
		// get matchlevel
		MatchLevel level = null;
		if (distance == 0 || distance == 1)
			level = MatchLevel.EXACT;
		if (distance > 1)
			level = MatchLevel.PLUGIN;

		if (level == null) {
			if (reverseDistance == 0)
				level = MatchLevel.EXACT;
			else if (reverseDistance > 0)
				level = MatchLevel.SUBSUME;
			else 
				level = MatchLevel.NOMATCH;
		}
		
		// get semantic distance
		int semanticDistance = -2;
		if (distance != -1)
			semanticDistance = distance;
		else if (reverseDistance != -1)
			semanticDistance = reverseDistance;
		else 
			semanticDistance = getNoMatchSemanticDistance(srcOwlClass, destOwlClass);
		
		// get match degree
		double matchDegree =  getSemanticMatchDegree(level, semanticDistance);
		
		return new SemanticDataModel(srcConcept, destConcept, level, semanticDistance, matchDegree);
	}

	
	public Set<SemanticDataModel> getConceptsSpecifiedMatchLevel(String concept,
			MatchLevel level) {
		// TODO Auto-generated method stub
		if (concept == null)
			return Collections.emptySet();
		
		IRI iri = IRI.create(concept);
		OWLEntity owlEntity =  Utils.locateOWLEntity(iri, manager, reasoner);
		if (owlEntity == null) {
			logger.error("failed to locate owl entity");
			return Collections.emptySet();
		}
		
		if (level.equals(MatchLevel.EXACT))
			return getExactMatch(iri);
		else if (level.equals(MatchLevel.PLUGIN))
			return getPluginMatch(iri);
		else if (level.equals(MatchLevel.SUBSUME))
			return getSubsumeMatch(iri);
		
		return Collections.emptySet();
	}

	
	public MatchLevel getMatchLevel(String srcConcept, String destConcept) {
		// TODO Auto-generated method stub
		if (srcConcept == null || destConcept == null) {
			logger.error("parameter is null or is illegal!");
			return null;
		}
		
		OWLEntity srcEntity = Utils.locateOWLEntity(IRI.create(srcConcept), manager, reasoner);
		OWLEntity destEntity = Utils.locateOWLEntity(IRI.create(destConcept), manager, reasoner);
		if (srcEntity == null || destEntity == null) {
			logger.error("failed to locate owl entity");
			return null;
		}
		
		if (srcEntity.isOWLNamedIndividual()
				&& destEntity.isOWLNamedIndividual()
                && isSameIndividual(srcEntity.asOWLNamedIndividual(), destEntity.asOWLNamedIndividual()))
			return MatchLevel.EXACT;
		
		
		OWLClass srcOwlClass = getCorrespondingOWLClasses(srcEntity);
		OWLClass destOwlClass = getCorrespondingOWLClasses(destEntity);
		
        int distance = getDirectedSemanticDistance(srcOwlClass, destOwlClass, 0);
		
		if (distance == 0 || distance == 1)
			return MatchLevel.EXACT;
		
		if (distance > 1)
			return MatchLevel.PLUGIN;
		
		distance = getDirectedSemanticDistance(destOwlClass, srcOwlClass, 0);
		if (distance == 0)
			return MatchLevel.EXACT;
		
		if (distance > 0)
			return MatchLevel.SUBSUME;
		
		
		return MatchLevel.NOMATCH;
	}

	
	public int getSemanticDistance(String srcConcept, String destConcept) {
		// TODO Auto-generated method stub
		if (srcConcept == null || destConcept == null) {
			logger.error("parameter is null or is illegal!");
			return -2;
		}
		
		OWLEntity srcEntity = Utils.locateOWLEntity(IRI.create(srcConcept), manager, reasoner);
		OWLEntity destEntity = Utils.locateOWLEntity(IRI.create(destConcept), manager, reasoner);
		if (srcEntity == null || destEntity == null) {
			logger.error("failed to locate owl entity");
			return -2;
		}
			
		// individual
		if (srcEntity.isOWLNamedIndividual()
				&& destEntity.isOWLNamedIndividual()
                && isSameIndividual(srcEntity.asOWLNamedIndividual(), destEntity.asOWLNamedIndividual()))
			return 0;
		
		// class to class or class to individual or individual to class
		OWLClass srcClass = getCorrespondingOWLClasses(srcEntity);
		OWLClass destClass = getCorrespondingOWLClasses(destEntity);
		
		int distance = getMatchSemanticDistance(srcClass, destClass);
		if (distance != -1)
			return distance;
		
		return getNoMatchSemanticDistance(srcClass, destClass);	
	}
	
	
	public OWLClass getCorrespondingOWLClasses(OWLEntity entity) {
		if (entity == null) {
			logger.error("parameter is null or illegal");
			return null;
		}
		
		if (entity.isOWLClass())
			return entity.asOWLClass();
		
		if (entity.isOWLNamedIndividual()) {
			Object[] tmpArray = OWLReasonerAdapter.flattenSetOfSets(reasoner
						.getTypes(entity.asOWLNamedIndividual(), true)).toArray();
			
			if (tmpArray.length > 0)
				return (OWLClass) tmpArray[0];
		}
		
		return null;
	}
	
	public int getMatchSemanticDistance(OWLClass srcClass, OWLClass destClass) {
		if (srcClass == null || destClass == null)
			return -1;
		
		// srcOwlClass to destOwlClass
		int distance = getDirectedSemanticDistance(srcClass, destClass, 0);
		if (distance != -1)
			return distance;
		
		// destOwlClass to srcOwlClass
		return getDirectedSemanticDistance(destClass, srcClass, 0);
	}
	
	public int getDirectedSemanticDistance(OWLClass srcClass, OWLClass destClass, Integer distance) {
		if (isEquivalentClass(srcClass, destClass))
			return distance;
		
		Set<Set<OWLClass>> subClasses = reasoner.getSubClasses(destClass);
		Set<OWLClass> subClassesSet = OWLReasonerAdapter.flattenSetOfSets(subClasses);
		++distance;
		for (OWLClass owlClass : subClassesSet) {		
			int dis = getDirectedSemanticDistance(srcClass, owlClass, distance);
			if (dis >= 0)
				return dis;
		}
		
		return -1;
	}
	
	private int getNoMatchSemanticDistance(OWLClass srcOwlClass, OWLClass destOwlClass) {
		OWLClass minCommonAncestor = getMinCommonAncestor(srcOwlClass, destOwlClass);
		int srcDistance  = getMatchSemanticDistance(srcOwlClass, minCommonAncestor);
		int destDistance = getMatchSemanticDistance(destOwlClass, minCommonAncestor);
		
		return (srcDistance * srcDistance + destDistance * destDistance) / 2;

	}

	private OWLClass getMinCommonAncestor(OWLClass srcOwlClass, OWLClass destOwlClass) {
		Set<Set<OWLClass>> superClasses = reasoner.getSuperClasses(srcOwlClass);
		Set<OWLClass> superClassesSet = OWLReasonerAdapter.flattenSetOfSets(superClasses);
		
		Queue<OWLClass> queue = new LinkedList<OWLClass>();
		queue.addAll(superClassesSet);
		while (queue.isEmpty()) {
			OWLClass tmp = queue.poll();
			Set<Set<OWLClass>> descendants = reasoner.getDescendantClasses(tmp);
			Set<OWLClass> descendantsSet = OWLReasonerAdapter.flattenSetOfSets(descendants);
			if (descendantsSet.contains(destOwlClass.asOWLClass()))
				return tmp;
			
			Set<Set<OWLClass>> superClasses1 = reasoner.getSuperClasses(tmp);
			Set<OWLClass> superClassesSet1 = OWLReasonerAdapter.flattenSetOfSets(superClasses1);
			queue.addAll(superClassesSet1);
		}	
		
		return null;
	}
	
	
	public double getSemanticMatchDegree(String srcConcept, String destConcept) {
		// TODO Auto-generated method stub
        MatchLevel level = getMatchLevel(srcConcept, destConcept);
		
		if (level.equals(MatchLevel.EXACT))
			return 1;
		
		int distance = getSemanticDistance(srcConcept, destConcept);
		
		if (level.equals(MatchLevel.PLUGIN))
			return 1/2 + 1 / Math.pow(Math.E, distance -1);
		
		if (level.equals(MatchLevel.SUBSUME))
			return 1 / (2 * Math.pow(Math.E, distance -1));
			
		return 0;
	}
	
	
	private boolean isEquivalentClass(OWLClass srcClass, OWLClass destClass) {
		if (srcClass == null || destClass == null) {
			logger .error("parameter is null or illegal");
			return false;
		}
		
		if (srcClass.equals(destClass) || srcClass == destClass)
			return true;
		
		if (reasoner.isEquivalentClass(srcClass, destClass ))
			return true;
		
		return false;
	}
	
	private boolean isSameIndividual(OWLIndividual srcIndividual, OWLIndividual destIndividual) {
		if (srcIndividual == null || destIndividual == null) {
			logger .error("parameter is null or illegal");
			return false;
		}
		
		if (srcIndividual.equals(destIndividual) || srcIndividual == destIndividual)
			return true;
		
		if (reasoner.isSameAs(srcIndividual, destIndividual))
			return true;
		
		return false;
	}
	
	public double getSemanticMatchDegree(MatchLevel level, int distance) {
		if (level.equals(MatchLevel.EXACT))
		return 1;
	
		if (level.equals(MatchLevel.PLUGIN))
			return 1/2 + 1 / Math.pow(Math.E, distance -1);
		
		if (level.equals(MatchLevel.SUBSUME))
			return 1 / (2 * Math.pow(Math.E, distance -1));
			
		return 0;
	}
	
	public Set<SemanticDataModel> getExactMatch(IRI iri) {
		if (iri == null) {
			logger.error("concept uri is null or illegal");
			return Collections.emptySet();
		}
		
		OWLEntity entity = Utils.locateOWLEntity(iri, manager, reasoner);
		Set<SemanticDataModel> completedExactMatchResults = 
			getCompletedExactMatch(entity, Collections.singleton(entity), MatchLevel.EXACT, 0);
		Set<SemanticDataModel> almostExactMatchResults = 
			getAlmostExactMatch(entity, Collections.singleton(entity), MatchLevel.EXACT, 1);
		 
		Set<SemanticDataModel> exactResults = new HashSet<SemanticDataModel>();
		exactResults.addAll(completedExactMatchResults);
		exactResults.addAll(almostExactMatchResults);
		
		return exactResults;
	}
	
	public Set<SemanticDataModel> getPluginMatch(IRI iri) {
		if (iri == null) {
			logger.error("concept uri is null or illegal");
			return Collections.emptySet();
		}
		
		OWLEntity entity = Utils.locateOWLEntity(iri, manager, reasoner);
		return getPluginMatch(entity, Collections.singleton(entity), MatchLevel.PLUGIN, 2);
	}
	
	public Set<SemanticDataModel> getSubsumeMatch(IRI iri) {
		if (iri == null) {
			logger.error("concept uri is null or illegal");
			return Collections.emptySet();
		}
		
		OWLEntity entity = Utils.locateOWLEntity(iri, manager, reasoner);
		return getSubsumeMatch(entity, Collections.singleton(entity), MatchLevel.SUBSUME, 1);
	}
	
	public Set<SemanticDataModel> getCompletedExactMatch (
			OWLEntity srcEntity,
			Set<? extends OWLEntity> entities,
			MatchLevel level,
			Integer distance) {	
		if (entities == null || level == null || distance < 0) {
			logger.error("parameter is null or illegal");
			return Collections.emptySet();
		}
		
		Set<SemanticDataModel> results = new HashSet<SemanticDataModel>();
		Set<OWLEntity> resultEntities = new HashSet<OWLEntity>();
		for (OWLEntity entity : entities) {
			if (entity.isOWLClass()) {
				resultEntities.clear();

				// add equivalent classes
				Set<OWLClass> equOWLClasses = reasoner.getAllEquivalentClasses(entity.asOWLClass());
				resultEntities.add(entity);
				resultEntities.addAll(equOWLClasses);
				
				// add related individuals
				Set<OWLEntity> equIndividuals = Utils.getDirectIndividuals(reasoner, resultEntities);
				resultEntities.addAll(equIndividuals);
				
				results.addAll(createSemanticDataModels(srcEntity, level, distance, resultEntities));
			}
			
			if (entity.isOWLNamedIndividual()) {
				resultEntities.clear();
				Set<OWLIndividual> individuals = reasoner.getSameAsIndividuals(entity.asOWLNamedIndividual());
				
				Set<OWLNamedIndividual> namedIndividuals = new HashSet<OWLNamedIndividual>();
				for (OWLIndividual individual : individuals)
					namedIndividuals.add((OWLNamedIndividual) individual);
				namedIndividuals.add((OWLNamedIndividual) entity);
				
				Set<OWLEntity> allDefinedClassesIncludeEquivalentOnes = Utils.getOWLClass(reasoner, namedIndividuals);
				
				resultEntities.addAll(allDefinedClassesIncludeEquivalentOnes);
				
				results.addAll(getCompletedExactMatch(srcEntity, resultEntities, level, distance));
			}
		}
		
		return results;
	}
	
	
	public Set<SemanticDataModel> getAlmostExactMatch(
			OWLEntity srcEntity,
			Set<? extends OWLEntity> entities,
			MatchLevel level,
			Integer distance) {	
		if (entities == null || level == null || distance < 0) {
			logger.error("parameter is null or illegal");
			return Collections.emptySet();
		}
		
		Set<SemanticDataModel> results = new HashSet<SemanticDataModel>();
		Set<OWLEntity> resultEntities = new HashSet<OWLEntity>();
		for (OWLEntity entity : entities) {
			if (entity.isOWLClass()) {
				// add equivalent classes
				resultEntities.clear();
				Set<OWLClass> subOWLClasses = OWLReasonerAdapter.flattenSetOfSets(reasoner
						.getSubClasses(entity.asOWLClass()));
				
				resultEntities.addAll(subOWLClasses);
				
				// add related individuals
				Set<OWLEntity> directIndividuals = Utils.getDirectIndividuals(reasoner, resultEntities);
				resultEntities.addAll(directIndividuals);
				
				results.addAll(createSemanticDataModels(srcEntity, level, distance, resultEntities));
			}
			
			if (entity.isOWLNamedIndividual()) {
				resultEntities.clear();
				Set<OWLIndividual> individuals = reasoner.getSameAsIndividuals(entity.asOWLNamedIndividual());
				
				Set<OWLNamedIndividual> namedIndividuals = new HashSet<OWLNamedIndividual>();
				for (OWLIndividual individual : individuals)
					namedIndividuals.add((OWLNamedIndividual) individual);
				namedIndividuals.add((OWLNamedIndividual) entity);
				
				Set<OWLEntity> allDefinedClassesForIndividuals = Utils.getOWLClass(reasoner, namedIndividuals);
				
				resultEntities.addAll(allDefinedClassesForIndividuals);
				
				
				results.addAll(getAlmostExactMatch(srcEntity, resultEntities, level, distance));
			}
		}
		
		return results;
	}
	
	public Set<SemanticDataModel> getPluginMatch(
			OWLEntity srcEntity,
			Set<? extends OWLEntity> entities,
			MatchLevel level,
			Integer distance) {	
		if (entities == null || level == null || distance < 0) {
			logger.error("parameter is null or illegal");
			return Collections.emptySet();
		}
		
		Set<SemanticDataModel> results = new HashSet<SemanticDataModel>();
		Set<OWLEntity> resultEntities = new HashSet<OWLEntity>();
		for (OWLEntity entity : entities) {
			if (entity.isOWLClass()) {
				Set<OWLClass> directChildren = Utils.getDirectSubClasses(reasoner, Collections
						.singleton(entity));
				// class
				Set<OWLClass> secondLevelChildren = Utils.getDirectSubClasses(reasoner, directChildren);				
				results.addAll(createSemanticDataModels(srcEntity, level,
						distance, secondLevelChildren));
				
				// individual
				Set<OWLEntity> secondLevelChildrenIndividuals = Utils.getDirectIndividuals(
						reasoner, secondLevelChildren);
				results.addAll(createSemanticDataModels(srcEntity, level,
						distance, secondLevelChildrenIndividuals));
							
				// process distance + 1
				results.addAll(getPluginMatch(srcEntity, directChildren, level, (distance + 1)));
			}
			
			if (entity.isOWLNamedIndividual()) {
				resultEntities.clear();
				Set<OWLIndividual> individuals = reasoner.getSameAsIndividuals(entity.asOWLNamedIndividual());
				
				Set<OWLNamedIndividual> namedIndividuals = new HashSet<OWLNamedIndividual>();
				for (OWLIndividual individual : individuals)
					namedIndividuals.add((OWLNamedIndividual) individual);
				namedIndividuals.add((OWLNamedIndividual) entity);
				
				// get classes for all individuals
				Set<OWLEntity> allDefinedClassesForIndividuals = Utils.getOWLClass(reasoner, namedIndividuals);
				resultEntities.addAll(allDefinedClassesForIndividuals);
				
				// recursive invoke
				results.addAll(getPluginMatch(srcEntity, resultEntities, level, distance));
				
			}
		}
		
	    return results;
	}
	
	

	public Set<SemanticDataModel> getSubsumeMatch(
			OWLEntity srcEntity,
			Set<? extends OWLEntity> entities,
			MatchLevel level,
			Integer distance) {	
		if (entities == null || level == null || distance < 0) {
			logger.error("parameter is null or illegal");
			return Collections.emptySet();
		}
		
		Set<SemanticDataModel> results = new HashSet<SemanticDataModel>();
		Set<OWLEntity> resultEntities = new HashSet<OWLEntity>();
		for (OWLEntity entity : entities) {
			if (entity.isOWLClass()) {
				// classes
				Set<OWLClass> directParents = Utils.getDirectSuperClasses(reasoner, Collections
						.singleton(entity));
				results.addAll(createSemanticDataModels(srcEntity, level, distance, directParents));
				
				// individuals
				Set<OWLEntity> directParentIndividuals = Utils.getDirectIndividuals(reasoner,
						directParents);				
				results.addAll(createSemanticDataModels(srcEntity, level, distance, directParentIndividuals));
				
				// process super parents
				results.addAll(getSubsumeMatch(srcEntity, directParents, level, (distance + 1)));
				
			}
			
			if (entity.isOWLNamedIndividual()) {
				resultEntities.clear();
				Set<OWLIndividual> individuals = reasoner.getSameAsIndividuals(entity.asOWLNamedIndividual());
				
				Set<OWLNamedIndividual> namedIndividuals = new HashSet<OWLNamedIndividual>();
				for (OWLIndividual individual : individuals)
					namedIndividuals.add((OWLNamedIndividual) individual);
				namedIndividuals.add((OWLNamedIndividual) entity);
				
				// get classes for all individuals
				Set<OWLEntity> allDefinedClassesForIndividuals = Utils.getOWLClass(reasoner, namedIndividuals);
				resultEntities.addAll(allDefinedClassesForIndividuals);
				
				// recursive invoke
				results.addAll(getSubsumeMatch(srcEntity, resultEntities, level, distance));
			}
		}
		
		return results;
	}
	
	private Set<SemanticDataModel> createSemanticDataModels(
			OWLEntity entity,
			MatchLevel level,
			Integer distance,
			Set<? extends OWLEntity> entities) {
		if (entity == null || entities == null)
			return Collections.emptySet();
		
		Set<SemanticDataModel> results = new HashSet<SemanticDataModel>();
		
		for (OWLEntity e : entities) {
			double matchDegree = getSemanticMatchDegree(level, distance);
			SemanticDataModel model = new SemanticDataModel(entity.getIRI().toString(), e.getIRI().toString(), level, distance, matchDegree);
			results.add(model);
		}
		
		return results;
	}
	
}
