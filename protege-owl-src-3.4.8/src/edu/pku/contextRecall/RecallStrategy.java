package edu.pku.contextRecall;

import edu.stanford.smi.protegex.owl.model.OWLModel;

public interface RecallStrategy {

	public void updateAlgorithm();
	
	public OWLModel recallAlgorithm(int sessionUpdateID);
}
