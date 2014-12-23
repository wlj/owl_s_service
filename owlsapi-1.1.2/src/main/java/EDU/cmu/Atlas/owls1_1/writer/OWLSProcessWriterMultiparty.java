package EDU.cmu.Atlas.owls1_1.writer;

import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import EDU.cmu.Atlas.owls1_1.exception.OWLSWriterException;
import EDU.cmu.Atlas.owls1_1.process.AtomicProcessMultiparty;
import EDU.cmu.Atlas.owls1_1.process.MessageType;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.ProcessList;
import EDU.cmu.Atlas.owls1_1.process.Result;
import EDU.cmu.Atlas.owls1_1.process.ResultMultiparty;

/**
 * @author Roman Vaculin
 *
 */
public class OWLSProcessWriterMultiparty extends OWLSProcessWriterDynamic {

    private static Logger logger = Logger.getLogger(OWLSProcessWriter.class);
    
    public ObjectProperty inMessageTypeProperty;
    public ObjectProperty outMessageTypeProperty;
    	
	public OWLSProcessWriterMultiparty() {
		super();
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
        model.read("http://ns.vsmie.cz/~vaculin/ontologies/agents/messages.owl");
        model.read("http://ns.vsmie.cz/~vaculin/ontologies/agents/bang_parameters_types.owl");
        baseModel.addSubModel(model);
    	
    	Ontology ont = baseModel.getOntology(base);
        ont.addImport(model.getOntology("http://ns.vsmie.cz/~vaculin/ontologies/agents/owls_multiparty.owl"));
        ont.addImport(model.getOntology("http://ns.vsmie.cz/~vaculin/ontologies/agents/bang_parameters_types.owl"));
//        ont.addImport(model.getOntology("http://ns.vsmie.cz/~vaculin/ontologies/agents/messages.owl"));
        ont.addImport(model.getOntology("http://ns.vsmie.cz/~vaculin/ontologies/agents/bang_parameters_types.owl"));
        
        inMessageTypeProperty  = baseModel.createObjectProperty("http://ns.vsmie.cz/~vaculin/ontologies/agents/owls_multiparty.owl#inMessageType");
        outMessageTypeProperty = baseModel.createObjectProperty("http://ns.vsmie.cz/~vaculin/ontologies/agents/owls_multiparty.owl#outMessageType");
        
    	return baseModel;
    }
	
	
	
	
    public OntModel writeModel(ProcessList processList, String base, String[] imports, OntModel submodel, OutputStream out)
    throws IndexOutOfBoundsException, OWLSWriterException {
    	
    	logger.debug("XXXXXXXXXXXXXXX\nXXXXXXXXXXXXXXX\nXXXXXXXXXXXXXXX\nXXXXXXXXXXXXXXX\n");
    	
    	return super.writeModel(processList, base, imports, submodel, out);
    }
    
    
    /* FIXME: This has to be modified: message types + dependences 
     * @see EDU.cmu.Atlas.owls1_1.writer.OWLSProcessWriterDynamic#writeProcess(EDU.cmu.Atlas.owls1_1.process.Process, com.hp.hpl.jena.ontology.OntModel)
     */
    public Individual writeProcess(Process process, OntModel ontModel) throws OWLSWriterException {
    	
    	Individual processInd = super.writeProcess(process, ontModel);
    	
        if (process instanceof AtomicProcessMultiparty) {
            // processing multiparty stuff
        	AtomicProcessMultiparty apm = (AtomicProcessMultiparty) process;

            MessageType inMessageType = apm.getInMessageType();
            if (inMessageType != null) {
                writeInMessageType(inMessageType, processInd, inMessageTypeProperty, ontModel);
            }
        }
 	    	
    	return processInd;
    }
    
    public Individual writeResult(Result result, OntModel ontModel) throws OWLSWriterException {

    	Individual resultInd = super.writeResult(result, ontModel);
    	
        if (result instanceof ResultMultiparty) {
            // processing multiparty stuff
        	ResultMultiparty rm = (ResultMultiparty) result;

            MessageType outMessageType = rm.getOutMessageType();
            if (outMessageType != null) {
                writeInMessageType(outMessageType, resultInd, outMessageTypeProperty,ontModel);
            }
        }
 	    	
        return resultInd;
    }
    
    
    public void writeInMessageType(MessageType messageType, Individual processResultInd, ObjectProperty msgProperty, OntModel ontModel) throws OWLSWriterException {
        
    	OntClass messageTypeClass  = messageType.getMessageTypeClass();
    	if (messageTypeClass==null) {
    		throw new OWLSWriterException("MessageType OWL class was not specified. Cannot creat an instance of the MessageType.");
    	}
    	
    	Individual messageTypeInd = OWLSWriterUtils.checkIfIndividualExist(messageType, ontModel);
        
        if (messageTypeInd == null) {
        	messageTypeInd = ontModel.createIndividual(messageType.getURI(), messageTypeClass);
        }
        if (messageTypeInd==null) {
        	throw new OWLSWriterException("Instance of MessageType cannot be created. Probably the relevat ontology was not added to th import of the service model.");
        }
    	
        processResultInd.addProperty(msgProperty, messageTypeInd);
    }


}
