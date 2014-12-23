package edu.pku.func;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import edu.pku.context.model.Bool.BoolType;
import edu.pku.contextConditon.CXTCondition;
import edu.pku.contextConditon.CXTCondition2;
import edu.pku.contextEffect.CXTEffect;
import edu.pku.util.Reasoner;
import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;

public class ConstraintPrune {

	private OWLModel owlModel;
	private ProtegeReasoner reasoner;
	
	private CXTCondition2 cxtCondition;/**/
	private CXTEffect cxtEffect;
	
	public ConstraintPrune(OWLModel owlModel, ProtegeReasoner reasoner) {
		
		this.owlModel = owlModel;
		this.reasoner = reasoner;
	}
	
	public void print(List<List<TaskBranch>> taskBranches) {
		for (int i = 0; i < taskBranches.size(); i++) {
			for (int j = 0; j < taskBranches.get(i).size(); j++) {
				System.out.println("Name: " + taskBranches.get(i).get(j).getTaskName() + " ---Value: " + taskBranches.get(i).get(j).getTaskContextValue());
			}
		}
	}
	
	public List<List<TaskBranch>> prune(String globalCXT, List<List<TaskBranch>> taskBranches) {
		
		//modify the global context information...
		cxtEffect = new CXTEffect(owlModel, reasoner);
		cxtEffect.execAll(globalCXT);
		
		/*important*/
		Reasoner reasoner2 = new Reasoner();
	    ProtegeReasoner pellet = reasoner2.createPelletOWLAPIReasoner(owlModel);
	    reasoner = pellet;
		
		cxtCondition = new CXTCondition2(owlModel, reasoner);
		
		for (int i = 0; i < taskBranches.size(); i++) {
			for (int j = 0; j < taskBranches.get(i).size(); j++) {
				if (cxtCondition.execAllBoolean(taskBranches.get(i).get(j).getTaskContextValue()).getBoolType() == BoolType.False) {
					System.out.println("The taskBranche:" + taskBranches.get(i).get(j).getTaskContextValue() + " is false!");
					taskBranches.get(i).get(j).setTaskContextValue(BoolType.False.toString());
				}
				else {
					System.out.println("The taskBranche:" + taskBranches.get(i).get(j).getTaskContextValue() + " is not fasle!");
					taskBranches.get(i).get(j).setTaskContextValue(taskBranches.get(i).get(j).getTaskContextValue());
				}
			}
		}
		print(taskBranches);
		return taskBranches;
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
		
//		OWLModel owlModel = null;
//		try {
//			owlModel = ProtegeOWL.createJenaOWLModel();
//		} catch (OntologyLoadException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		OWLNamedClass Person = owlModel.createOWLNamedClass("Person");
//		OWLNamedClass Course = owlModel.createOWLNamedClass("Course");
//		OWLNamedClass IDCard = owlModel.createOWLNamedClass("IDCard");
//		OWLNamedClass Ticket = owlModel.createOWLNamedClass("Ticket");
//		OWLNamedClass PTicket = owlModel.createOWLNamedClass("PTicket");
//		OWLNamedClass Weather = owlModel.createOWLNamedClass("Weather");
//		OWLNamedClass Flow = owlModel.createOWLNamedClass("Flow");
//		OWLNamedClass City = owlModel.createOWLNamedClass("City");
//			
//		OWLIndividual Wuhan = Person.createOWLIndividual("Wuhan");
//		OWLIndividual Shanghai = Person.createOWLIndividual("Shanghai");
//		
//		PTicket.addSuperclass(Ticket);
//		
//		OWLIndividual Darwin = Person.createOWLIndividual("Darwin");
//		OWLIndividual Biology = Course.createOWLIndividual("Biology");
//		OWLIndividual LittleDarwin = Person.createOWLIndividual("LittleDarwin");
//		
//		OWLObjectProperty hasHobby = owlModel.createOWLObjectProperty("hasHobby");
//		hasHobby.setDomain(Person);
//		hasHobby.setRange(Course);
//		
//		Darwin.setPropertyValue(hasHobby, Biology);
//		
//		OWLDatatypeProperty hasAge = owlModel.createOWLDatatypeProperty("hasAge");
//		hasAge.setDomain(Person);
//		hasAge.setRange(owlModel.getXSDinteger());
//		
//		Darwin.setPropertyValue(hasAge, new Integer(18));
//		
//		OWLObjectProperty hasChild = owlModel.createOWLObjectProperty("hasChild");
//		hasChild.setDomain(Person);
//		hasChild.setRange(Person);
//		
//		Darwin.setPropertyValue(hasChild, LittleDarwin);
//		
//		OWLObjectProperty hasID = owlModel.createOWLObjectProperty("hasID");
//		hasID.setDomain(Person);
//		hasID.setRange(IDCard);
//		
//		OWLObjectProperty hasNoShip = owlModel.createOWLObjectProperty("hasNoShip");
//		hasNoShip.setDomain(City);
//		hasNoShip.setRange(City);
//		
//		OWLObjectProperty hasTicket = owlModel.createOWLObjectProperty("hasTicket");
//		hasTicket.setDomain(Person);
//		hasTicket.setRange(Ticket);
		
		Reasoner reasoner = new Reasoner();
	    ProtegeReasoner pellet = reasoner.createPelletOWLAPIReasoner(owlModel);
	    
	    String globalCXT = "System(boardingSystem) ^ Service(self_service) ^ noSelfService(boardingSystem, self_service)";
	    List<List<TaskBranch>> taskBranches = new ArrayList<List<TaskBranch>>();
	   
	    List<TaskBranch> TB = new ArrayList<TaskBranch>();
	    TaskBranch tb = new TaskBranch("check-in yourself", "hasSelfService(System, Service) ^ hasETicket(Person,E_Ticket) ^ Adult(Person) ^ isDomestic(E_Ticket, true) ^ hasIDCard(Person, IDCard)");
	    TB.add(tb);
	    
	    List<TaskBranch> TB2 = new ArrayList<TaskBranch>();
	    TaskBranch tb2 = new TaskBranch("shipment", "hasLuggage(Person, Luggage) ^ shipmentAllowed(Luggage, true)  ^ heavyGoods(Luggage)");
	    TB2.add(tb2);
	    tb2 = new TaskBranch("check", "hasLuggage(Person, Luggage) ^ carryAllowed(Luggage, true)");
	    TB2.add(tb2);
	    
	    taskBranches.add(TB);
	    taskBranches.add(TB2);
	    
	    /**
	     * ConstraintPrune
	     */
	    ConstraintPrune constraintPrune = new ConstraintPrune(owlModel, pellet);
	    constraintPrune.print(taskBranches);
	    System.out.println("*******After*********");
	    constraintPrune.prune(globalCXT, taskBranches);
	}
}
