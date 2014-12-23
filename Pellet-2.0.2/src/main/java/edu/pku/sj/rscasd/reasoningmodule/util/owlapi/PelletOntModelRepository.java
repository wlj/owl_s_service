package edu.pku.sj.rscasd.reasoningmodule.util.owlapi;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class PelletOntModelRepository {

	private static OntModel ontModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);;

	static {
		ontModel.setDynamicImports(true);
	}

	public static synchronized OntModel getOntModel() {
		return ontModel;
	}
	
	public static synchronized OntModel getNewOntModel() {
		if(ontModel == null) {
			ontModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
		}
		else {
			ontModel.reset();
			ontModel.prepare();
		}
		
		return ontModel;
	}
}
