package EDU.pku.sj.rscasd.owls1_1.utils;

import java.util.Iterator;
import java.util.Vector;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;

import edu.cmu.atlas.owl.utils.OWLUtil;

public class OWLIndividualUtils {

	private static interface PropertyIndividualFetcher<T> {
		T fetch(Individual instance, Property property) throws Exception;
	};

	public static <T> T fectInstanceByProperty(Individual instance, Property property,
			PropertyIndividualFetcher<T> fetcher) throws Exception {
		T result = null;
		while (result == null && instance != null) {
			result = (fetcher == null ? null : fetcher.fetch(instance, property));
			if (result == null) {
				OntResource ontResource = instance.getSameAs();
				instance = ((ontResource != null) ? ontResource.asIndividual() : null);
			}
		}
		return result;
	}

	public static Individual getInstanceFromFunctionalProperty(Individual instance, Property property) throws Exception {
		return fectInstanceByProperty(instance, property, new PropertyIndividualFetcher<Individual>() {
			public Individual fetch(Individual instance, Property property) throws Exception {
				return OWLUtil.getInstanceFromFunctionalProperty(instance, property);
			}
		});
	}

	public static Individual getInstanceFromProperty(Individual instance, Property property) throws Exception {
		return fectInstanceByProperty(instance, property, new PropertyIndividualFetcher<Individual>() {
			public Individual fetch(Individual instance, Property property) throws Exception {
				return OWLUtil.getInstanceFromProperty(instance, property);
			}
		});
	}

	public static Literal getLiteralFromFunctionalProperty(Individual instance, Property property) throws Exception {
		return OWLIndividualUtils.<Literal> fectInstanceByProperty(instance, property,
				new PropertyIndividualFetcher<Literal>() {
					public Literal fetch(Individual instance, Property property) throws Exception {
						return OWLUtil.getLiteralFromFunctionalProperty(instance, property);
					}
				});
	}

	public static Literal getLiteralFromProperty(Individual instance, Property property) throws Exception {
		return OWLIndividualUtils.<Literal> fectInstanceByProperty(instance, property,
				new PropertyIndividualFetcher<Literal>() {
					public Literal fetch(Individual instance, Property property) throws Exception {
						return OWLUtil.getLiteralFromProperty(instance, property);
					}
				});
	}

	public static RDFNode extractFunctionalPropertyValue(Individual instance, Property property) throws Exception {
		try {
			return OWLIndividualUtils.<RDFNode> fectInstanceByProperty(instance, property,
					new PropertyIndividualFetcher<RDFNode>() {
						public RDFNode fetch(Individual instance, Property property) throws Exception {
							return OWLUtil.extractFunctionalPropertyValue(instance, property);
						}
					});
		} catch (Exception e) {
			return null;
		}
	}

	public static RDFNode extractPropertyValue(Individual instance, Property property) {
		try {
			return OWLIndividualUtils.<RDFNode> fectInstanceByProperty(instance, property,
					new PropertyIndividualFetcher<RDFNode>() {
						public RDFNode fetch(Individual instance, Property property) throws Exception {
							return OWLUtil.extractPropertyValue(instance, property);
						}
					});
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static Vector<Literal> extractPropertyValues(Individual instance, Property property) {
		try {
			return OWLIndividualUtils.<Vector<Literal>> fectInstanceByProperty(instance, property,
					new PropertyIndividualFetcher<Vector<Literal>>() {
						public Vector<Literal> fetch(Individual instance, Property property) throws Exception {
							return OWLUtil.extractPropertyValues(instance, property);
						}
					});
		} catch (Exception e) {
			return new Vector<Literal>();
		}
	}

	@SuppressWarnings("unchecked")
	public static Iterator<Literal> extractPropertyValueS(Individual instance, Property property) throws Exception {
		try {
			return OWLIndividualUtils.<Iterator<Literal>> fectInstanceByProperty(instance, property,
					new PropertyIndividualFetcher<Iterator<Literal>>() {
						public Iterator<Literal> fetch(Individual instance, Property property) throws Exception {
							return OWLUtil.extractPropertyValueS(instance, property);
						}
					});
		} catch (Exception e) {
			return new Vector<Literal>().iterator();
		}
	}

}
