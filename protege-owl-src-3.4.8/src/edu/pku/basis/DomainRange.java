package edu.pku.basis;

import java.util.Collection;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

public class DomainRange {

	public static void main(String[] args) {
		
	    OWLModel owlModel = null;
		try {
			owlModel = ProtegeOWL.createJenaOWLModel();
		} catch (OntologyLoadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
		OWLNamedClass Person = owlModel.createOWLNamedClass("Person");
		OWLNamedClass Course = owlModel.createOWLNamedClass("Course");
		
		OWLIndividual Darwin = Person.createOWLIndividual("Darwin");
		OWLIndividual Biology = Course.createOWLIndividual("Biology");
		OWLIndividual LittleDarwin = Person.createOWLIndividual("LittleDarwin");
		
		OWLObjectProperty hasHobby = owlModel.createOWLObjectProperty("hasHobby");
		hasHobby.setDomain(Person);
		hasHobby.setRange(Course);
	 
		Darwin.setPropertyValue(hasHobby, Biology);
		
		OWLDatatypeProperty hasAge = owlModel.createOWLDatatypeProperty("hasAge");
		hasAge.setDomain(Person);
		hasAge.setRange(owlModel.getXSDdouble());
		
//		Darwin.setPropertyValue(hasAge, new Integer(18));
		
		OWLObjectProperty hasChild = owlModel.createOWLObjectProperty("hasChild");
		hasChild.setDomain(Person);
		hasChild.setRange(Person);
		
		Darwin.setPropertyValue(hasChild, LittleDarwin);
	    
	    //ObjectProperty
	    RDFSClass domain = hasHobby.getDomain(false);
	    RDFResource range = hasHobby.getRange(false);
	    
	    System.out.println(domain.getClass() + " & " + domain.getBrowserText() + " & " + domain.getLocalName());
	    System.out.println(range.getClass() + " & " + range.getBrowserText() + " & " +range.getLocalName());
	    
	    //DatatypeProperty
	    domain = hasAge.getDomain(false);
	    range = hasAge.getRange(false);
	    
	    System.out.println(domain.getClass() + " & " + domain.getBrowserText() + " & " + domain.getLocalName());
	    System.out.println(range.getClass() + " & " + range.getBrowserText() + " & " +range.getLocalName());
	    
	}
}
