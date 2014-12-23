/**
 * 
 */
package EDU.cmu.Atlas.owls1_1.writer;

import java.io.OutputStream;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.exception.OWLSWriterException;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * @author Roman Vaculin
 *
 */
public class OWLSProfileWriterMultiparty extends OWLSProfileWriterDynamic {

	private static Logger logger = Logger.getLogger(OWLSProcessWriter.class);


	/**
	 * 
	 */
	public OWLSProfileWriterMultiparty() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected OntModel init(String base) {

    	logger.debug("XXXXXXXXXXXXXXX\nXXXXXXXXXXXXXXX\nXXXXXXXXXXXXXXX\nXXXXXXXXXXXXXXX\nINIT CALLED\n\n");
    	
    	// call parent
    	OntModel baseModel = super.init(base);
    	OntModel model = ModelFactory.createOntologyModel();
        
        OntDocumentManager docMgr = model.getDocumentManager();
        // FIXME: fix the local address address 
        docMgr.addAltEntry("http://ns.vsmie.cz/~vaculin/ontologies/agents/owls_multiparty.owl", "file:/usr0/rvaculin/cmu/ontologies/agents/owls_multiparty.owl");

        model.setNsPrefix("multiparty", "http://ns.vsmie.cz/~vaculin/ontologies/agents/owls_multiparty.owl#");

        model.read("http://ns.vsmie.cz/~vaculin/ontologies/agents/owls_multiparty.owl");
        baseModel.addSubModel(model);
    	
    	Ontology ont = baseModel.getOntology(base);
        ont.addImport(model.getOntology("http://ns.vsmie.cz/~vaculin/ontologies/agents/owls_multiparty.owl"));
    	return baseModel;
    }
	
	
	
	
	public OntModel writeModel(ProfileList profileList, String base, String[] imports, OntModel submodel, OutputStream out)
	 		throws IndexOutOfBoundsException, OWLSWriterException {
    	
    	logger.debug("XXXXXXXXXXXXXXX\nXXXXXXXXXXXXXXX\nXXXXXXXXXXXXXXX\nXXXXXXXXXXXXXXX\n");
    	return super.writeModel(profileList, base, imports, submodel, out);
    }
	
	/* FIXME: This has to be modified: message types + dependences
	 * @see EDU.cmu.Atlas.owls1_1.writer.OWLSProfileWriterDynamic#writeProfile(EDU.cmu.Atlas.owls1_1.profile.Profile, com.hp.hpl.jena.ontology.OntModel)
	 */
	public Individual writeProfile(Profile profile, OntModel ontModel) throws OWLSWriterException {
		return super.writeProfile(profile, ontModel);
	}
	
}
