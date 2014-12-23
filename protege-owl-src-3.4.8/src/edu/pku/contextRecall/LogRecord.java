package edu.pku.contextRecall;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLModel;

/**
 * concrete strategy2...
 * @author Jacob
 *
 */
public class LogRecord implements RecallStrategy{

	@Override
	public void updateAlgorithm() {
		// TODO Auto-generated method stub
		System.out.println("LogRecord--update");
	}
	
	@Override
	public OWLModel recallAlgorithm(int sessionUpdateID) {
		// TODO Auto-generated method stub
		System.out.println("LogRecord--recall");
		OWLModel owlModel = null;
		try {
			owlModel = ProtegeOWL.createJenaOWLModel();
		} catch (OntologyLoadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return owlModel;
	}
}
