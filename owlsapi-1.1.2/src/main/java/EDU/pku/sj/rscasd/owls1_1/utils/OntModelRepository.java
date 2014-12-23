package EDU.pku.sj.rscasd.owls1_1.utils;

import EDU.cmu.Atlas.owls1_1.writer.OWLSWriterUtils;

import com.hp.hpl.jena.ontology.OntModel;

public class OntModelRepository {

	private static OntModel ontModel = OWLSWriterUtils.getNewOntModel();

	static {
		ontModel.setDynamicImports(true);
		// TODO file manager
	}

	public static synchronized OntModel getOntModel() {
		return ontModel;
	}
	
	public static synchronized OntModel getNewOntModel() {
		if(ontModel == null) {
			ontModel = OWLSWriterUtils.getNewOntModel();
		}
		else {
			ontModel.reset();
			ontModel.prepare();
		}
		
		return ontModel;
	}
}
