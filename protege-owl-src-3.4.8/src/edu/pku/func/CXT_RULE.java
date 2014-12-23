package edu.pku.func;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import edu.pku.contextConditon.CXTCondition2;
import edu.pku.contextEffect.CXTEffect;
import edu.pku.util.Reasoner;
import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.model.OWLModel;

public class CXT_RULE {

	private OWLModel owlModel;
	private ProtegeReasoner reasoner;
	
	public CXT_RULE(OWLModel owlModel, ProtegeReasoner reasoner) {
		
		this.owlModel = owlModel;
		this.reasoner = reasoner;
	}
	
	public double exec(String context, String rule) {
		
		//modify the context to the KB
		CXTEffect cxtEffect = new CXTEffect(owlModel, reasoner);
		cxtEffect.execAll(context);
		
		//modify the pellet reasoner...
		Reasoner reasoner2 = new Reasoner();
	    ProtegeReasoner pellet = reasoner2.createPelletOWLAPIReasoner(owlModel);
	    reasoner = pellet;
	    
		//get the dos
		CXTCondition2 cxtCondition = new CXTCondition2(owlModel, reasoner);
		return cxtCondition.execAllDouble(rule);
	}
	
	public static void main(String[] args) {
		
		String ONTOLOGY_FILE = "G:/thesis/source/tour.owl";
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(ONTOLOGY_FILE);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		OWLModel owlModel = null;
		try {
//			owlModel = ProtegeOWL.createJenaOWLModel();
			owlModel = ProtegeOWL.createJenaOWLModelFromInputStream(fis);
		} catch (OntologyLoadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Reasoner reasoner = new Reasoner();
	    ProtegeReasoner pellet = reasoner.createPelletOWLAPIReasoner(owlModel);
	    
	    String context = "Female(jacob) ^ hasIDCard(jacob, interimIDCard) ^ hasTicket(jacob, firstClass_Ticket) ^ hasGoods(jacob, goods) ^ hasWeight(goods, w_high)";
	    String rule = " hasIDCard(Person, IDCard) ^ hasTicket(Person, E_Ticket)";
	    
	    CXT_RULE cxt_Rule = new CXT_RULE(owlModel, pellet);
	    System.out.println(cxt_Rule.exec(context, rule));
	}
}
