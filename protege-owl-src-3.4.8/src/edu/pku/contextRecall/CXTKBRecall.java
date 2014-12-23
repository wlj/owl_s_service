package edu.pku.contextRecall;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import edu.pku.util.Global;
import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

public class CXTKBRecall {
	
	private RecallStrategy strategy;
	
	private int sessionUpdateID;
	private List<String> session = null;
	
	public CXTKBRecall(RecallStrategy strategy) {
		
		this.strategy = strategy;
	}
	
	public void update() {
		
		strategy.updateAlgorithm();
	}

	public OWLModel recall() {
		
		return strategy.recallAlgorithm(sessionUpdateID);
	}
	
	public void printIndividual(OWLModel owlModel, String clsName) {
		
		OWLNamedClass cls = owlModel.getOWLNamedClass(clsName);
		
		Collection<OWLIndividual> instances = cls.getInstances(false);
		
		for (Iterator it = instances.iterator(); it.hasNext(); ) {
			
			OWLIndividual instance = (OWLIndividual) it.next();
			System.out.println(instance.getName());
		}
	}
	
	public static void main(String[] args) {
		
		OWLModel owlModel = null;
		try {
			owlModel = ProtegeOWL.createJenaOWLModel();
		} catch (OntologyLoadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		OWLNamedClass Person = owlModel.createOWLNamedClass("Person");
		OWLNamedClass Course = owlModel.createOWLNamedClass("Course");//CTXKB_0.owl
		
		OWLIndividual Darwin = Person.createOWLIndividual("Darwin");
		
		CXTKBRecall cxtKBRecall = new CXTKBRecall(new CXTKBCopy(owlModel));
//		CXTKBRecall cxtKBRecall = new CXTKBRecall(new LogRecord());
		cxtKBRecall.update();
		
		cxtKBRecall.printIndividual(owlModel, "Person");
		
		OWLIndividual LittleDarwin = Person.createOWLIndividual("LittleDarwin");
		OWLIndividual Biology = Course.createOWLIndividual("Biology");
		OWLNamedClass Man = owlModel.createOWLNamedClass("Man");//CTXKB_1.owl
		
		cxtKBRecall.update();
		
		cxtKBRecall.printIndividual(owlModel, "Person");
		
		OWLIndividual Jacob = Person.createOWLIndividual("Jacob");
		OWLNamedClass Woman = owlModel.createOWLNamedClass("Woman");//CTXKB_2.owl
		
		System.out.println("------before recall....");
		cxtKBRecall.printIndividual(owlModel, "Person");
		
		owlModel = cxtKBRecall.recall();
		
		System.out.println("------after recall....");
		cxtKBRecall.printIndividual(owlModel, "Person");
//		System.out.println(Woman.getName());//Attention!!! Yes
//		System.out.println(owlModel.getOWLNamedClass("Woman").getName());//!!! No
		
	}
}
