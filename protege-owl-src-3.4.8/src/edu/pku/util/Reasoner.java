package edu.pku.util;

import edu.stanford.smi.protegex.owl.inference.dig.DefaultProtegeDIGReasoner;
import edu.stanford.smi.protegex.owl.inference.pellet.ProtegePelletJenaReasoner;
import edu.stanford.smi.protegex.owl.inference.pellet.ProtegePelletOWLAPIReasoner;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ReasonerManager;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.model.OWLModel;

public class Reasoner {

	public DefaultProtegeDIGReasoner createDIGReasoner(OWLModel owlModel) {
	 	final String REASONER_URL = "http://localhost:8081";

		// Get the reasoner manager instance
		ReasonerManager reasonerManager = ReasonerManager.getInstance();

		DefaultProtegeDIGReasoner reasoner = (DefaultProtegeDIGReasoner) reasonerManager.createProtegeReasoner(owlModel, reasonerManager.getDefaultDIGReasonerClass());			 

		// Set the reasoner URL and test the connection
		reasoner.setURL(REASONER_URL);

		if (!reasoner.isConnected()) {
			System.out.println("Reasoner not connected!");		
		}

		return reasoner;
	}
	
	public ProtegeReasoner createPelletJenaReasoner(OWLModel owlModel) {

		// Get the reasoner manager instance
		ReasonerManager reasonerManager = ReasonerManager.getInstance();

		//Get an instance of the Protege Pellet reasoner
		ProtegeReasoner reasoner = reasonerManager.createProtegeReasoner(owlModel, ProtegePelletJenaReasoner.class);
		
		return reasoner;
	}
	
	public ProtegeReasoner createPelletOWLAPIReasoner(OWLModel owlModel) {

		// Get the reasoner manager instance
		ReasonerManager reasonerManager = ReasonerManager.getInstance();

		//Get an instance of the Protege Pellet reasoner
		ProtegeReasoner reasoner = reasonerManager.createProtegeReasoner(owlModel, ProtegePelletOWLAPIReasoner.class);

		return reasoner;
	}
	
	
}
