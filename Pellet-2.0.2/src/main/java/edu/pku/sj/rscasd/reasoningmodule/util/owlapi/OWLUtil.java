package edu.pku.sj.rscasd.reasoningmodule.util.owlapi;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.owlapi.PelletReasonerFactory;
import org.mindswap.pellet.owlapi.Reasoner;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerAdapter;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.io.OWLOntologyInputSource;
import org.semanticweb.owl.io.StreamInputSource;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLOntologyURIMapper;

import uk.ac.manchester.cs.owl.OWLClassAssertionAxiomImpl;
import uk.ac.manchester.cs.owl.OWLDeclarationAxiomImpl;
import uk.ac.manchester.cs.owl.OWLDifferentIndividualsAxiomImpl;
import uk.ac.manchester.cs.owl.OWLDisjointClassesAxiomImpl;
import uk.ac.manchester.cs.owl.OWLDisjointUnionAxiomImpl;
import uk.ac.manchester.cs.owl.OWLEquivalentClassesAxiomImpl;
import uk.ac.manchester.cs.owl.OWLSameIndividualsAxiomImpl;
import uk.ac.manchester.cs.owl.OWLSubClassAxiomImpl;
import edu.pku.sj.rscasd.reasoningmodule.constant.PropertyConstant.OntologyPreloadConfig;
import edu.pku.sj.rscasd.utils.properties.PropertiesUtil;

public class OWLUtil {

	private final static Log logger = LogFactory.getLog(OWLUtil.class);

	/**
	 * Not used for reasoning, just for storing data and handling temporary
	 * data.
	 */
	private final static OWLOntologyManager globalHandler = OWLManager.createOWLOntologyManager();

	private final static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

	private final static List<OWLOntologyURIMapper> uriMappers = new ArrayList<OWLOntologyURIMapper>();

	private static boolean loadingInitiated = false;

	private static void initLoading() {
		loadingInitiated = true;
		OWLOntologyManager currentManager = getCurrentOWLOntologyManager();
		addRSCASDUriMapper(currentManager);

		/*-
		 *  TODO check whether to load the ontologied configured 
		 *       in the properties file. 
		 */
		String propFilePathStr = PropertiesUtil.getProperty(OntologyPreloadConfig.LOAD_LIST_PATH);
		if (PropertiesUtil.getBoolean(OntologyPreloadConfig.ENABLED)) {
			try {
				URL proFilePathUrl = OWLUtil.class.getResource(propFilePathStr);
				if (proFilePathUrl == null) {
					throw new IllegalArgumentException("Incorrect file path set: " + propFilePathStr);
				}
				PropertiesConfiguration preloadOntologies = new PropertiesConfiguration(proFilePathUrl);
				for (Object value : preloadOntologies.getList(OntologyPreloadConfig.LOAD_LIST_CONFIG_ITEM_NAME)) {
					String strUri = (String) value;
					if (strUri != null && !strUri.trim().equals("")) {
						try {
							OWLOntology ontology = currentManager.loadOntology(URI.create(strUri));
							
							if(ontology.getURI().toString()=="http://protege.stanford.edu/plugins/owl/owl-library/koala2.owl")
							{
								if (logger.isDebugEnabled()) {
									logger.debug("Ontology loaded as required by configuration: [" + ontology.getURI()
											+ "]");
								}
							}
							
							/*if (logger.isDebugEnabled()) {
								logger.debug("Ontology loaded as required by configuration: [" + ontology.getURI()
										+ "]");
							}*/
						} catch (Exception e) {
							logger.error("Ontology failed to load: [" + strUri + "]. With message: " + e.getMessage());
						}
					}
				}
			} catch (ConfigurationException e) {
				logger.error(e);
			}
		}
	}

	private static void addRSCASDUriMapper(OWLOntologyManager localManager) {
		if (localManager == null) {
			return;
		}

		try {
			Class<?> uriMapperClz = Thread.currentThread().getContextClassLoader().loadClass(
					"edu.pku.sj.rscasd.utils.ontology.RSCASDOntologyURIMapper");
			Object obj = uriMapperClz.newInstance();
			if (obj instanceof OWLOntologyURIMapper) {
				localManager.addURIMapper((OWLOntologyURIMapper) obj);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private static class OWLOntologyManagerInvocationHandler implements InvocationHandler {
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String methodName = method.getName();
			if ("addURIMapper".equals(methodName) || "clearURIMappers".equals(methodName)
					|| "removeURIMapper".equals(methodName)) {

				// maintain local URI mappers
				synchronized (uriMappers) {
					if ("addURIMapper".equals(methodName)) {
						Object obj = (args.length > 0 ? args[0] : null);
						OWLOntologyURIMapper uriMapper = ((obj instanceof OWLOntologyURIMapper) ? (OWLOntologyURIMapper) obj
								: null);
						uriMappers.add(uriMapper);
					} else if ("clearURIMappers".equals(methodName)) {
						uriMappers.clear();
					} else if ("removeURIMapper".equals(methodName)) {
						Object obj = (args.length > 0 ? args[0] : null);
						OWLOntologyURIMapper uriMapper = ((obj instanceof OWLOntologyURIMapper) ? (OWLOntologyURIMapper) obj
								: null);
						uriMappers.remove(uriMapper);
					}
				}

				method.invoke(globalHandler, args);
				return method.invoke(manager, args);
			} else if ("loadOntology".equals(methodName)) {
				Object obj = method.invoke(manager, args);
				Object ontoLoc = args[0];
				Object url = ontoLoc;
				if (ontoLoc != null) {
					if (ontoLoc instanceof OWLOntologyInputSource) {
						url = "(from physical URI) " + ((OWLOntologyInputSource) ontoLoc).getPhysicalURI();
					} else if (ontoLoc instanceof URL) {
						url = "(URL)" + ontoLoc.toString();
					} else if (ontoLoc instanceof URI) {
						url = "(URI)" + ontoLoc.toString();
					}
				}
				logger.debug("Ontology loaded: [" + url + "]");
				return obj;
			}
			return method.invoke(manager, args);
		}
	}

	public static OWLOntologyManager getOWLOntologyManager() {
		return getCurrentOWLOntologyManager();
	}

	public static OWLOntologyManager getOWLOntologyManager(OWLOntologyURIMapper mapper) {
		return getOWLOntologyManager(mapper == null ? (List<OWLOntologyURIMapper>) null : Collections
				.singletonList(mapper));
	}

	public static OWLOntologyManager getOWLOntologyManager(List<OWLOntologyURIMapper> mappers) {
		return getOWLOntologyManager(mappers, false);
	}

	public static OWLOntologyManager getOWLOntologyManager(List<OWLOntologyURIMapper> mappers, boolean reInitLoading) {
		OWLOntologyManager proxiedManager = (OWLOntologyManager) Proxy.newProxyInstance(OWLUtil.class.getClassLoader(),
				new Class<?>[] { OWLOntologyManager.class }, new OWLOntologyManagerInvocationHandler());

		if (uriMappers != mappers) {
			proxiedManager.clearURIMappers();
			if (mappers != null) {
				for (OWLOntologyURIMapper mapper : mappers) {
					proxiedManager.addURIMapper(mapper);
				}
			}
		}

		if (reInitLoading || !loadingInitiated) {
			initLoading();
		}

		return proxiedManager;
	}

	public static OWLOntologyManager getCurrentOWLOntologyManager() {
		return getCurrentOWLOntologyManager(uriMappers);
	}

	public static OWLOntologyManager getCurrentOWLOntologyManager(List<OWLOntologyURIMapper> mappers) {
		return getOWLOntologyManager(mappers, false);
	}

	public static OWLOntologyManager getNewOWLOntologyManager(List<OWLOntologyURIMapper> mappers,
			boolean isToInitLoading) {
		final OWLOntologyManager localManager = OWLManager.createOWLOntologyManager();
		OWLOntologyManager proxiedManager = (OWLOntologyManager) Proxy.newProxyInstance(OWLUtil.class.getClassLoader(),
				new Class<?>[] { OWLOntologyManager.class }, new InvocationHandler() {
					public Object invoke(Object target, Method method, Object[] args) throws Throwable {
						String methodName = method.getName();
						if ("addURIMapper".equals(methodName) || "clearURIMappers".equals(methodName)
								|| "removeURIMapper".equals(methodName)) {
							return method.invoke(localManager, args);
						} else if ("loadOntology".equals(methodName)) {
							Object obj = method.invoke(localManager, args);
							Object ontoLoc = args[0];
							Object url = ontoLoc;
							if (ontoLoc != null) {
								if (ontoLoc instanceof OWLOntologyInputSource) {
									url = "(from physical URI) " + ((OWLOntologyInputSource) ontoLoc).getPhysicalURI();
								} else if (ontoLoc instanceof URL) {
									url = "(URL)" + ontoLoc.toString();
								} else if (ontoLoc instanceof URI) {
									url = "(URI)" + ontoLoc.toString();
								}
							}
							logger.debug("Ontology loaded: [" + url + "]");
							return obj;
						}
						return method.invoke(localManager, args);
					}
				});

		// not affect the global one, not need to check "equal"
		proxiedManager.clearURIMappers();
		if (mappers != null) {
			for (OWLOntologyURIMapper mapper : mappers) {
				proxiedManager.addURIMapper(mapper);
			}
		}

		if (isToInitLoading) {
			initLoading();
		} else if (uriMappers.size() <= 0) {
			addRSCASDUriMapper(proxiedManager);
		}

		return proxiedManager;
	}

	public static void clearUpOntologies() {
		synchronized (manager) {
			OWLOntologyManager currentManager = getCurrentOWLOntologyManager();
			Set<OWLOntology> ontologies = currentManager.getOntologies();
			for (OWLOntology ontology : ontologies) {
				URI ontologyURI = ontology.getURI();
				if (currentManager.contains(ontologyURI)) {
					currentManager.removeOntology(ontologyURI);
				}
			}
		}
	}

	public static OWLClass getNothing() {
		synchronized (manager) {
			OWLOntologyManager currentManager = getCurrentOWLOntologyManager();
			return currentManager.getOWLDataFactory().getOWLNothing();
		}
	}

	public static OWLClass getThing() {
		synchronized (manager) {
			OWLOntologyManager currentManager = getCurrentOWLOntologyManager();
			return currentManager.getOWLDataFactory().getOWLThing();
		}
	}

	public static OWLOntology loadOntology(URI ontologyURI) {
		if (ontologyURI == null) {
			return null;
		}
		try {
			synchronized (manager) {
				OWLOntologyManager currentManager = getCurrentOWLOntologyManager();
				if (!currentManager.contains(ontologyURI)) {
					return currentManager.loadOntology(ontologyURI);
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	public static OWLOntology loadOntology(String ontologyURIStr) {
		if (ontologyURIStr == null || ontologyURIStr.trim().equals("")) {
			return null;
		}
		try {
			URI uri = URI.create(ontologyURIStr);
			return loadOntology(uri);
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	public static OWLOntology loadOntology(OWLOntology ontology) {
		return loadOntology(getCurrentOWLOntologyManager(), ontology);
	}

	private static OWLOntology loadOntology(OWLOntologyManager manager, OWLOntology ontology) {
		if (ontology == null || manager == null) {
			return null;
		}
		return mergeOntologies(ontology.getURI(), Collections.singleton(ontology), false);
	}

	public static Reasoner getPelletReasoner() {
		return getPelletReasoner(uriMappers);
	}

	public static Reasoner getPelletReasoner(List<OWLOntologyURIMapper> mappers) {
		return getPelletReasoner(null, mappers);
	}

	public static Reasoner getPelletReasoner(KnowledgeBase kb) {
		return getPelletReasoner(kb, uriMappers);
	}

	public static Reasoner getPelletReasoner(KnowledgeBase kb, List<OWLOntologyURIMapper> mappers) {
		PelletReasonerFactory reasonerFactory = new PelletReasonerFactory();
		Reasoner reasoner = null;
		synchronized (manager) {
			OWLOntologyManager currentManager = getCurrentOWLOntologyManager(mappers);
			if (kb != null) {
				reasoner = new Reasoner(currentManager, kb);
			} else {
				reasoner = reasonerFactory.createReasoner(currentManager);
			}
		}
		return reasoner;
	}

	public static void prepareReasoner(OWLReasoner reasoner, String[] ontUris) {
		if (reasoner == null) {
			return;
		}

		Set<URI> uris = new HashSet<URI>();
		if (ontUris != null && ontUris.length > 0) {
			for (String ontUri : ontUris) {
				if (ontUri == null || ontUri.trim().equals("")) {
					continue;
				}
				try {
					URI uri = URI.create(ontUri);
					uris.add(uri);
				} catch (Exception e) {
					logger.error(e);
					continue;
				}
			}
		}

		prepareReasoner(reasoner, uris.toArray(new URI[uris.size()]));
	}

	public static void prepareReasoner(OWLReasoner reasoner, URI[] ontUris) {
		if (reasoner == null) {
			return;
		}

		Set<OWLOntology> onts = new HashSet<OWLOntology>();
		if (ontUris != null && ontUris.length > 0) {
			for (URI uri : ontUris) {
				try {
					synchronized (manager) {
						OWLOntologyManager currentManager = getCurrentOWLOntologyManager();
						OWLOntology ont = currentManager.loadOntology(uri);
						onts.add(ont);
						if (logger.isDebugEnabled()) {
							logger.debug("Ontology loaded: " + ont.getURI());
						}
					}
				} catch (Exception e) {
					logger.error(e);
					continue;
				}
			}
		}

		prepareReasoner(reasoner, onts);
	}

	public static void prepareReasoner(OWLReasoner reasoner, OWLOntology[] onts) {
		Set<OWLOntology> ontSet = new HashSet<OWLOntology>();
		Collections.addAll(ontSet, onts);
		prepareReasoner(reasoner, onts);
	}

	public static void prepareReasoner(OWLReasoner reasoner, Set<OWLOntology> onts) {
		if (reasoner == null) {
			return;
		}

		try {
			if (onts != null && (!onts.isEmpty())) {
				reasoner.loadOntologies(onts);
			}
			reasoner.classify();
		} catch (OWLReasonerException e) {
			logger.error(e);
		}
	}

	/**
	 * Merge the given set of ontologies included within ontologiesToMerge under
	 * the given URI.
	 * 
	 * @param ontologyURI
	 * @param ontologiesToMerge
	 * @param removeAfterMerge
	 * @return
	 */
	public static OWLOntology mergeOntologies(URI ontologyURI, Set<OWLOntology> ontologiesToMerge,
			boolean removeAfterMerge) {
		return mergeOntologies(getCurrentOWLOntologyManager(), ontologyURI, ontologiesToMerge, removeAfterMerge);
	}

	private static OWLOntology mergeOntologies(OWLOntologyManager manager, URI ontologyURI,
			Set<OWLOntology> ontologiesToMerge, boolean removeAfterMerge) {
		synchronized (manager) {
			OWLOntology ontology = null;
			try {
				if (ontologyURI != null) {
					if (manager.contains(ontologyURI)) {
						ontologyURI = getAnonymousURI();
					}
				} else {
					ontologyURI = getAnonymousURI();
				}

				if (ontologiesToMerge == null) {
					ontologiesToMerge = Collections.<OWLOntology> emptySet();
				}
				ontology = manager.createOntology(ontologyURI, ontologiesToMerge);
				if (removeAfterMerge) {
					manager.removeOntology(ontologyURI);
				}
			} catch (OWLOntologyCreationException e) {
				logger.error(e);
			} catch (OWLOntologyChangeException e) {
				logger.error(e);
			}
			return ontology;
		}
	}

	/**
	 * Locate ontologies which define the given entity.
	 * 
	 * @param entity
	 * @return
	 */
	public static Set<OWLOntology> locateOntologies(OWLEntity entity) {
		OWLOntologyManager currentManager = getCurrentOWLOntologyManager();
		Set<OWLOntology> ontologies = currentManager.getOntologies();
		return locateOntologies(ontologies, entity);
	}

	public static Set<OWLOntology> locateOntologies(Set<OWLEntity> entities) {
		if (entities == null || entities.isEmpty()) {
			return Collections.emptySet();
		} else {
			Set<OWLOntology> ontologies = new HashSet<OWLOntology>();
			for (OWLEntity entity : entities) {
				ontologies.addAll(locateOntologies(entity));
			}
			return ontologies;
		}
	}

	public static Set<OWLOntology> locateOntologies(Set<OWLOntology> ontologies, OWLEntity entity) {
		if (entity == null || ontologies == null || ontologies.isEmpty()) {
			return Collections.emptySet();
		}

		Set<OWLOntology> ontologiesLocated = new HashSet<OWLOntology>();
		for (OWLOntology ontology : ontologies) {
			if (entity.isOWLClass()) {
				if (((OWLClass) entity).isDefined(ontology)) {
					ontologiesLocated.add(ontology);
				}
			} else if (entity.isOWLIndividual()) {
				OWLIndividual individual = (OWLIndividual) entity;
				Set<OWLDescription> types = individual.getTypes(ontology);
				if (types != null && (!types.isEmpty())) {
					ontologiesLocated.add(ontology);
				}
			}
		}

		return ontologiesLocated;
	}

	public static Set<OWLClass> locateOWLClass(URI uri, Set<OWLOntology> additionalOntologies) {
		Set<OWLClass> classes = new HashSet<OWLClass>();
		Set<OWLEntity> entities = locateOWLEntity(uri, additionalOntologies);
		for (OWLEntity entity : entities) {
			if (!entity.isOWLClass()) {
				continue;
			}
			classes.add((OWLClass) entity);
		}
		return classes;
	}

	public static Set<OWLIndividual> locateOWLIndividual(URI uri, Set<OWLOntology> additionalOntologies) {
		Set<OWLIndividual> individuals = new HashSet<OWLIndividual>();
		Set<OWLEntity> entities = locateOWLEntity(uri, additionalOntologies);
		for (OWLEntity entity : entities) {
			if (!entity.isOWLIndividual()) {
				continue;
			}
			individuals.add((OWLIndividual) entity);
		}
		return individuals;
	}

	public static Set<OWLEntity> locateOWLEntity(URI uri, Set<OWLOntology> additionalOntologies) {
		return locateOWLEntity(uri, additionalOntologies, true);
	}

	public static Set<OWLEntity> locateOWLEntity(URI uri, Set<OWLOntology> additionalOntologies,
			boolean tryMoreIfOntologyNotExist) {
		if (uri == null) {
			return Collections.emptySet();
		}

		Set<OWLEntity> entities = new HashSet<OWLEntity>();
		synchronized (globalHandler) {
			// load ontologies
			if (additionalOntologies != null) {
				additionalOntologies.removeAll(globalHandler.getOntologies());
				for (OWLOntology ontology : additionalOntologies) {
					if (ontology == null) {
						continue;
					}
					mergeOntologies(globalHandler, ontology.getURI(), Collections.singleton(ontology), false);
				}
			}

			OWLDataFactory factory = globalHandler.getOWLDataFactory();
			OWLClass owlClass = factory.getOWLClass(uri);
			OWLIndividual individual = factory.getOWLIndividual(uri);
			Set<OWLOntology> managedOntologies = globalHandler.getOntologies();
			if (owlClass.isDefined(managedOntologies)) {
				entities.add(owlClass);
			}
			if (!individual.getTypes(managedOntologies).isEmpty()) {
				entities.add(individual);
			}

			// XXX not consider the property now
			// OWLDataProperty dataProp = factory.getOWLDataProperty(uri);
			// OWLObjectProperty objProp = factory.getOWLObjectProperty(uri);
			// entities.add(dataProp);
			// entities.add(objProp);

			if (entities.isEmpty() && tryMoreIfOntologyNotExist) {
				// try to check and load/register the ontology first.
				// and try to locate once more.
				String srcUriStr = uri.toString();
				int paramSepInd = srcUriStr.indexOf("#");
				if (paramSepInd > 0) {
					srcUriStr = srcUriStr.substring(0, paramSepInd);
				}
				OWLOntology ontology = OWLUtil.loadOntology(srcUriStr);

				additionalOntologies.add(ontology);
				entities = locateOWLEntity(uri, additionalOntologies, false);
			}

			return entities;
		}
	}

	public static Set<OWLOntology> checkNotReasonerInvolved(Reasoner reasoner, Set<OWLOntology> ontologiesToCheck) {
		if (reasoner == null || ontologiesToCheck == null || ontologiesToCheck.isEmpty()) {
			return Collections.emptySet();
		}

		Set<OWLOntology> ontologiesChecked = new HashSet<OWLOntology>(ontologiesToCheck);
		Set<OWLOntology> ontologiesInvolved = reasoner.getLoadedOntologies();
		ontologiesChecked.removeAll(ontologiesInvolved);
		return ontologiesChecked;
	}

	public static Set<OWLEntity> getOWLClass(Reasoner reasoner, Set<OWLIndividual> individuals) {
		Set<OWLEntity> owlClasses = new HashSet<OWLEntity>();
		if (reasoner == null || individuals == null || individuals.isEmpty()) {
			return owlClasses;
		}

		for (OWLIndividual individual : individuals) {
			owlClasses.addAll(OWLReasonerAdapter.flattenSetOfSets(reasoner.getTypes(individual)));
		}

		return owlClasses;
	}

	private static URI getAnonymousURI() {
		try {
			return new URI(ReasoningModuleConstants.ANONYMOUS_URI_PREFIX + System.currentTimeMillis() + ":"
					+ System.identityHashCode(new Object()));
		} catch (URISyntaxException e) {
			logger.error(e);
			return null;
		}
	}

	public static OWLOntology parseOWLOntologyByUrl(String url) {
		if (StringUtils.isEmpty(url)) {
			return null;
		}
		OWLOntologyManager newOntoManager = getNewOWLOntologyManager(uriMappers, false);
		try {
			URI uri = URI.create(url);
			return newOntoManager.loadOntology(uri);
		} catch (OWLOntologyCreationException e) {
			return null;
		}
	}

	public static OWLOntology parseOWLOntologyByPlainText(String plainText) {
		if (StringUtils.isEmpty(plainText)) {
			return null;
		}
		OWLOntologyManager newOntoManager = getNewOWLOntologyManager(uriMappers, false);
		ByteArrayInputStream bais = new ByteArrayInputStream(plainText.getBytes());

		try {
			return newOntoManager.loadOntology(new StreamInputSource(bais));
		} catch (OWLOntologyCreationException e) {
			return null;
		}
	}

	public static List<OWLEntity> parseOWLEntityByUrl(String url) {
		OWLOntology ontology = parseOWLOntologyByUrl(url);
		return parseOWLEntity(ontology);
	}

	public static List<OWLEntity> parseOWLEntityByPlainText(String plainText) {
		OWLOntology ontology = parseOWLOntologyByPlainText(plainText);
		return parseOWLEntity(ontology);
	}

	public static List<OWLEntity> parseOWLEntity(OWLOntology ontology) {
		if (ontology == null) {
			return Collections.emptyList();
		}

		List<OWLEntity> entities = new LinkedList<OWLEntity>();
		Set<OWLAxiom> axioms = ontology.getAxioms();
		for (OWLAxiom axiom : axioms) {
			/*-
			if (axiom instanceof OWLClassAxiom) {
				OWLClassAxiom clzAxiom = (OWLClassAxiom) axiom;
				OWLClass subject = clzAxiom.getClassesInSignature().iterator().next();
				entities.add(subject);
				logger.debug(subject);
			} else if (axiom instanceof OWLIndividualAxiom) {
				OWLIndividualAxiom indAxiom = (OWLIndividualAxiom) axiom;
				OWLIndividual subject = indAxiom.getIndividualsInSignature().iterator().next();
				entities.add(subject);
				logger.debug(subject);
			}
			 */
			OWLEntity subject = getOWLEntity(axiom, ontology);
			if (subject != null) {
				entities.add(subject);
			}
		}

		entities.remove(getThing());
		return entities;
	}

	private static OWLEntity getOWLEntity(OWLAxiom axiom, OWLOntology ontology) {
		OWLEntity result = null;
		if (axiom instanceof OWLSubClassAxiomImpl) {
			OWLSubClassAxiomImpl subclass = (OWLSubClassAxiomImpl) axiom;
			result = (OWLEntity) subclass.getSubClass();
		} else if (axiom instanceof OWLEquivalentClassesAxiomImpl) {
			OWLEquivalentClassesAxiomImpl equClass = (OWLEquivalentClassesAxiomImpl) axiom;
			result = (OWLEntity) equClass.getDescriptions().iterator().next();
		} else if (axiom instanceof OWLDisjointClassesAxiomImpl) {
			OWLDisjointClassesAxiomImpl disjointClz = (OWLDisjointClassesAxiomImpl) axiom;
			result = (OWLEntity) disjointClz.getDescriptions().iterator().next();
		} else if (axiom instanceof OWLDisjointUnionAxiomImpl) {
			OWLDisjointUnionAxiomImpl disjointUnionClz = (OWLDisjointUnionAxiomImpl) axiom;
			result = (OWLEntity) disjointUnionClz.getOWLClass();
		} else if (axiom instanceof OWLDifferentIndividualsAxiomImpl) {
			OWLDifferentIndividualsAxiomImpl diffInd = (OWLDifferentIndividualsAxiomImpl) axiom;
			result = (OWLEntity) diffInd.getIndividuals().iterator().next();
		} else if (axiom instanceof OWLClassAssertionAxiomImpl) {
			OWLClassAssertionAxiomImpl clzAssert = (OWLClassAssertionAxiomImpl) axiom;
			result = clzAssert.getIndividual();
		} else if (axiom instanceof OWLSameIndividualsAxiomImpl) {
			OWLSameIndividualsAxiomImpl sameAsInd = (OWLSameIndividualsAxiomImpl) axiom;
			result = sameAsInd.getIndividuals().iterator().next();
		} else if (axiom instanceof OWLDeclarationAxiomImpl) {
			OWLDeclarationAxiomImpl declare = (OWLDeclarationAxiomImpl) axiom;
			result = declare.getEntity();
		} else {
			result = null;
		}

		if (result != null && result.getURI().equals(getThing().getURI())) {
			result = null;
		}
		return result;
	}
}
