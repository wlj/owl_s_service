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
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParseException;

public class TaskSelection {
	
	private OWLModel owlModel;
	private ProtegeReasoner reasoner;
	
	private CXTCondition2 cxtCondition;
	
	public TaskSelection(OWLModel owlModel, ProtegeReasoner reasoner) {
		
		this.owlModel = owlModel;
		this.reasoner = reasoner;
	}
	
	public void print(List<TaskBranch> taskBranches) {
		
		System.out.println("welcome to print list...");
		for (int i = 0; i < taskBranches.size(); i++) {
			System.out.println("Name: " + taskBranches.get(i).getTaskName() + " -----Value: " + taskBranches.get(i).getTaskContextValue());
		}
	}

	//ProfileInfo --> OWL_SE
	public String select(List<TaskBranch> taskBranches) {
		
		cxtCondition = new CXTCondition2(owlModel, reasoner);
		
		//lots of return the prior...nothing return the last...
		int i;
		for (i = 0; i < taskBranches.size(); i++) {
			
			if (cxtCondition.execAllBoolean(taskBranches.get(i).getTaskContextValue()).getBoolType() == BoolType.True) {
				
				System.out.println("The taskBranche:" + taskBranches.get(i).getTaskContextValue() + " is true!");
				return taskBranches.get(i).getTaskName();
			}
		}
		
//		print(taskBranches);
		//nothing return the last...
		return taskBranches.get(i - 1).getTaskName();
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
		
//		OWLModel owlModel = null;
//		try {
//			owlModel = ProtegeOWL.createJenaOWLModel();
//		} catch (OntologyLoadException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		OWLNamedClass Person = owlModel.createOWLNamedClass("Person");
//		OWLNamedClass Man = owlModel.createOWLNamedClass("Man");
//		
//		OWLNamedClass Course = owlModel.createOWLNamedClass("Course");
//		OWLNamedClass IDCard = owlModel.createOWLNamedClass("IDCard");
//		OWLNamedClass Ticket = owlModel.createOWLNamedClass("Ticket");
//		OWLNamedClass PTicket = owlModel.createOWLNamedClass("PTicket");
//		OWLNamedClass Weather = owlModel.createOWLNamedClass("Weather");
//		OWLNamedClass Flow = owlModel.createOWLNamedClass("Flow");
//		
//		PTicket.addSuperclass(Ticket);
//		Man.addSuperclass(Person);
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
//		OWLObjectProperty hasTicket = owlModel.createOWLObjectProperty("hasTicket");
//		OWLObjectProperty hasPTicket = owlModel.createOWLObjectProperty("hasPTicket");
//		hasTicket.setDomain(Person);
//		hasTicket.setRange(Ticket);
//		
//		/**
//	     * subsume the context is:
//	     * Man(person) ^ hasID(person, interiumID) ^ hasTicket(person, p_ticket)
//	     */
//		
//		OWLIndividual person = Person.createOWLIndividual("person");
//		OWLIndividual idCard = IDCard.createOWLIndividual("idCard");//equivalent class...
//		OWLIndividual p_ticket = Ticket.createOWLIndividual("p_ticket");
//		person.setPropertyValue(hasID, idCard);//idCard -- interiumCard
//		person.setPropertyValue(hasTicket, p_ticket);
//		
//		SWRLFactory factory = new SWRLFactory(owlModel);
//		try {
//			SWRLImp imp = factory.createImp("Person(?p) ^ hasTicket(?p, ?ticket) ^ sameAs(?ticket, p_ticket) -> hasPTicket(?p, ?ticket)");
//		} catch (SWRLParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		Reasoner reasoner = new Reasoner();
//	    ProtegeReasoner pellet = reasoner.createPelletOWLAPIReasoner(owlModel);
		
	    String cxt = "Female(jacob) ^ hasIDCard(jacob, interimIDCard) ^ hasTicket(jacob, p_ticket)";
	    CXTEffect cxtEffect = new CXTEffect(owlModel, pellet);
	    cxtEffect.execAll(cxt);
	    
	    pellet.setOWLModel(owlModel);
		/**
		 * check-in
		 */
	    
		List<TaskBranch> TB = new ArrayList<TaskBranch>();
		TaskBranch tb = new TaskBranch("check-in counter", "hasTicket(Person, Ticket) ^ hasIDCard(Person, IDCard)");//I: Person(person), IDCard(idCard), Ticket(ticket)
	    TB.add(tb);
	    tb = new TaskBranch("check-in online", "hasETicket(Person, E_Ticket) ^ Adult(Person) ^ hasIDCard(Person, IDCard)");
	    TB.add(tb);
	    tb = new TaskBranch("check-in yourself", "hasSelfService(System, Service) ^ hasETicket(Person,E_Ticket) ^ Adult(Person) ^ isDomestic(E_Ticket, true) ^ hasIDCard(Person, IDCard)");
	    TB.add(tb);
	    
	    TaskSelection taskSelection = new TaskSelection(owlModel, pellet);
	    System.out.println(taskSelection.select(TB));
	}
}
