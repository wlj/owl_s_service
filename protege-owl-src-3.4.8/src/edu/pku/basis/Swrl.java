package edu.pku.basis;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import edu.pku.util.Reasoner;
import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.inference.pellet.ProtegePelletOWLAPIReasoner;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ReasonerManager;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFObject;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLAtomList;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLBuiltin;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLBuiltinAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLClassAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLDatavaluedPropertyAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParseException;

public class Swrl {
	
	 //递归调用答应所有类的层次关系,indentation参数是使的打印的子类比其父类更缩进
	 private static void printClassTree(RDFSClass cls, String indentation)
	 {
	        System.out.println(indentation + cls.getName());
	        //遍历并打印子类
	        for (Iterator it = cls.getSubclasses(false).iterator(); it.hasNext();)
	        {
	            RDFSClass subclass = (RDFSClass) it.next();
	            printClassTree(subclass, indentation + "    ");
	        }
	        
	 }
	 
	 private static void printIndividualOfClass(RDFSClass cls) {
		 
		 Collection instance = cls.getInstances(false);
		 
		 for (Iterator it = instance.iterator(); it.hasNext(); ) {
			 
			 OWLIndividual individual = (OWLIndividual)it.next();
			 System.out.println(individual.getName());
		 }
		 
	 }
	
	public static void main(String[] args) throws OntologyLoadException, SWRLParseException, ProtegeReasonerException{
		
		//exception
		//获取OWLModel对象
	    OWLModel owlModel = ProtegeOWL.createJenaOWLModel();
	    //创建Person类
	    OWLNamedClass Person = owlModel.createOWLNamedClass("Person");
	    OWLNamedClass Adult = owlModel.createOWLNamedClass("Adult");
	     
	    //创建数据属性hasAge
	    OWLDatatypeProperty hasAge = owlModel.createOWLDatatypeProperty("hasAge");
	    //设置属性的值域是xsd:int
	    hasAge.setRange(owlModel.getXSDint());
	    //设置属性的定义域为Person
	    hasAge.setDomain(Person);
	    
	    //创建Person类的个体darwin
//	    RDFIndividual darwin = Person.createRDFIndividual("darwin");
	    OWLIndividual darwin = Person.createOWLIndividual("darwin");
	    //设置该个体的数据属性age的值为18
	    int i = 20;
	    darwin.setPropertyValue(hasAge, i);
//	    darwin.setPropertyValue(hasAge, new Integer(18));
	    
		SWRLFactory factory = new SWRLFactory(owlModel);
		
		//exception
//		SWRLImp imp = factory.createImp("IsAdult-Rule", "Person(?p) ^ hasAge(?p, ?age) ^ swrlb:greaterThan(?age, 17) -> Adult(?p)");
		try {
		
			SWRLImp imp = factory.createImp("IsAdult-Rule", "Person(?p) ^ Person(?p) ^ hasAge(?p, ?age) ^ swrlb:greaterThan(?age, 17) -> Adult(?p)");
//			SWRLImp imp = factory.createImp("IsAdult-Rule", "Person(darwin) -> True(darwin)");
//			System.out.println("1");
//			// head and body
//			SWRLAtomList body = factory.createAtomList();
//			SWRLAtomList head = factory.createAtomList();
//			
//			System.out.println("2");
//			//Person(?p)
//			SWRLVariable p = factory.createVariable("p");
//			SWRLClassAtom classAtom_Person = factory.createClassAtom(Person, p);
//			body.append(classAtom_Person);
//			
//			System.out.println("3");
//			//hasAge(?p, ?age)
//			SWRLVariable age = factory.createVariable("age");
//			SWRLDatavaluedPropertyAtom datavaluedPropertyAtom = factory.createDatavaluedPropertyAtom(hasAge, p, age);
//			body.append(datavaluedPropertyAtom);
//			
//			System.out.println("4");
//			//swrlb:greaterThan(?age, 17)
//			//RDFSLiteral joe = owlModel.createRDFSLiteral("joe");
//			RDFObject year = owlModel.createRDFSLiteral("17", owlModel.getXSDint());
//			System.out.println("44");
//			SWRLBuiltin greaterThanBuiltin = factory.createBuiltin("greaterThan");
//			System.out.println("444");
//			List arguments = new ArrayList();
//			arguments.add(age);
//			arguments.add(year);
//			SWRLBuiltinAtom builtinAtom = factory.createBuiltinAtom(greaterThanBuiltin, arguments.iterator()); 
//			body.append(builtinAtom);
//			
//			System.out.println("5");
//			//Adult(?p)
//			SWRLClassAtom classAtom_Adult = factory.createClassAtom(Adult, p);
//			head.append(classAtom_Adult);
//			
//			System.out.println("6");
//			SWRLImp imp = factory.createImp(head, body);//Attention, please!
//			
//	        System.out.println("Imp: " + imp.getBrowserText());
//	        System.out.println("Body: " + imp.getBody().getBrowserText());
//	        System.out.println("Head: " + imp.getHead().getBrowserText());
	        
		}
		catch (Exception e) {
			
			System.out.println("---------Exception----------");
		}
//		Swrl swrl = new Swrl();
//		System.out.println("Class.Person........");
//		swrl.printClassTree(Person, "");
//		System.out.println("Class.Adult........");
//		swrl.printClassTree(Adult, "");
//		
//		System.out.println("instance.Person........");
//		swrl.printIndividualOfClass(Person);
//		System.out.println("instance.Adult........");
//		swrl.printIndividualOfClass(Adult);
		
		System.out.println("After Reasoning........");

//		ReasonerManager reasonerManager = ReasonerManager.getInstance(); 
//		ProtegeReasoner pellet = reasonerManager.createProtegeReasoner(owlModel, ProtegePelletOWLAPIReasoner.class);
		
		Reasoner reasoner = new Reasoner();
		ProtegeReasoner pellet = reasoner.createPelletOWLAPIReasoner(owlModel);
		
        System.out.println("instance.Adult........");
        System.out.println(darwin.getPropertyValue(hasAge, false));
        
		Collection<OWLIndividual> instances = pellet.getIndividualsBelongingToClass(Adult);
		System.out.println(instances);
		for (Iterator<OWLIndividual> it = instances.iterator(); it.hasNext(); ) {
			
			OWLIndividual instance = (OWLIndividual)it.next();
			System.out.println(instance.getName());
		}
		System.out.println("exec over.");
	}
	
}
