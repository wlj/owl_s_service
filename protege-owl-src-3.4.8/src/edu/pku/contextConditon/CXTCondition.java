package edu.pku.contextConditon;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;


import edu.pku.context.model.Bool;
import edu.pku.context.model.CXTObject;
import edu.pku.context.model.Bool.BoolType;
import edu.pku.context.model.CXTObject.FirstType;
import edu.pku.context.model.CXTObject.SecondType;
import edu.pku.context.model.CXTParser;
import edu.pku.util.Expression;
import edu.pku.util.Reasoner;
import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;

public class CXTCondition {

	private OWLModel owlModel;
	private ProtegeReasoner reasoner;
	
	private CXTParser cxtParser;
	
	public CXTCondition(OWLModel owlModel, ProtegeReasoner reasoner) {
		
		this.owlModel = owlModel;
		this.reasoner = reasoner;
	}
	
	public Bool judge(int RealNum, String op, int ExpectNum) {
		
		Bool bool = new Bool();
		
		System.out.println("???---RealNum:" + RealNum + " op:" + op + " ExpectNum:" + ExpectNum);
		if (op.equals(">=")) {
			if (RealNum >= ExpectNum) {
				bool.setBoolType(BoolType.True);
				return bool;
			}
			else {
				bool.setBoolType(BoolType.False);
				return bool;
			}
		}
		else if (op.equals("<=")) {
			if (RealNum <= ExpectNum) {
				bool.setBoolType(BoolType.True);
				return bool;
			}
			else {
				bool.setBoolType(BoolType.False);
				return bool;
			}
		}
		else if (op.equals("=")) {
			if (RealNum == ExpectNum) {
				bool.setBoolType(BoolType.True);
				return bool;
			}
			else {
				bool.setBoolType(BoolType.False);
				return bool;
			}
		}
		else if (op.equals(">")) {
			if (RealNum > ExpectNum) {
				bool.setBoolType(BoolType.True);
				return bool;
			}
			else {
				bool.setBoolType(BoolType.False);
				return bool;
			}
		}
		else if (op.equals("<")) {
			if (RealNum < ExpectNum) {
				bool.setBoolType(BoolType.True);
				return bool;
			}
			else {
				bool.setBoolType(BoolType.False);
				return bool;
			}
		}
		else {
			System.out.println("judeg: op error!");
			//return false;
			bool.setBoolType(BoolType.Unknown);
			return bool;
		}
	}
	
	public Bool exec_C(CXTObject cxtObject) {
		
		//return value ...
		Bool bool = new Bool();
		
		if (cxtObject.getSecondType().equals(SecondType.DirectClass)) {//Just like: Person(Jacob)
			
			OWLNamedClass cls = null;
			cls = owlModel.getOWLNamedClass(cxtObject.getTypeValue());
			
			if (cls == null) {
				System.out.println(cxtObject.getTypeValue() + " cann't be a OWLNamedClass!");
				//return false;
				bool.setBoolType(BoolType.Unknown);
				return bool;
			}
			
			OWLIndividual individual = null;
			individual = owlModel.getOWLIndividual(cxtObject.getArguments().get(0));
			
			if (individual == null) {
				System.out.println(cxtObject.getArguments().get(0) + " cann't be a OWLIndividual!");
				//return false;
				bool.setBoolType(BoolType.Unknown);
				return bool;
			}
			
			Collection<OWLIndividual> individualSet = null;
			try {
				individualSet = reasoner.getIndividualsBelongingToClass(cls);
				
				for (Iterator it = individualSet.iterator(); it.hasNext(); ) {
					OWLIndividual ind = (OWLIndividual) it.next();
					if (ind.equals(individual)) {
						System.out.println(ind.getName() + " is a instance of class " + cls.getName());
						//return true;
						bool.setBoolType(BoolType.True);
						return bool;
					}
				}
			} catch (ProtegeReasonerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//return false;
				bool.setBoolType(BoolType.False);
				return bool;
			}
			
			System.out.println(individual.getName() + " isn't a instance of class " + cls.getName());
			//return false;
			bool.setBoolType(BoolType.False);
			return bool;
		}
		else if (cxtObject.getSecondType().equals(SecondType.IndirectClass)) {//Just like: (hasChild >= 1)(Jacob)
			
			//get ?x
			OWLIndividual subjectIndividual = null;
			subjectIndividual = owlModel.getOWLIndividual(cxtObject.getArguments().get(0));
			
			if (subjectIndividual == null) {
				System.out.println(cxtObject.getArguments().get(0) + " cann't be a OWLIndividual!");
				//return false;
				bool.setBoolType(BoolType.Unknown);
				return bool;
			}
			
			String op = null;
			
			if (cxtObject.getTypeValue().contains(">=")) op = ">=";//must be the sort.
			else if (cxtObject.getTypeValue().contains("<=")) op = "<=";
			else if (cxtObject.getTypeValue().contains("=")) op = "=";
			else if (cxtObject.getTypeValue().contains(">")) op = ">";
			else if (cxtObject.getTypeValue().contains("<")) op = "<";
			else {
				System.out.println("exec_C: swrl format unknown!");
				//return false;
				bool.setBoolType(BoolType.Unknown);
				return bool;
			}
			
			// 生成一个Pattern,同时编译一个正则表达式 ;用Pattern的split()方法把字符串分割 
			String Regex = op;
			Pattern p = Pattern.compile(Regex);
			String[] result = p.split(cxtObject.getTypeValue());
			
			if (result.length != 2) {
				System.out.println("swrl format error!");
				//return false;
				bool.setBoolType(BoolType.False);
				return bool;
			}
			
			//get property...
			OWLProperty property = owlModel.getOWLProperty(result[0].trim());
			int ExpectNum = Integer.parseInt(result[1].trim());//String to int...
			
			if (property.isObjectProperty()) {
				
				OWLObjectProperty objectProperty = null;
				objectProperty = owlModel.getOWLObjectProperty(result[0].trim());
				
				if (objectProperty == null) {
					System.out.println(result[0].trim() + " cann't be a OWLObjectProperty!");
					//return false;
					bool.setBoolType(BoolType.Unknown);
					return bool;
				}
				
				try {
					Collection<OWLIndividual> individualSet = null;
					individualSet = reasoner.getRelatedIndividuals(subjectIndividual, objectProperty);
					System.out.println("individualSet: " + individualSet);
					
					int RealNum = individualSet.size();
					return judge(RealNum, op, ExpectNum);
					
				} catch (ProtegeReasonerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//return false;
					bool.setBoolType(BoolType.Unknown);
					return bool;
				}
			}
			else {
				
				OWLDatatypeProperty datatypeProperty = null;
				datatypeProperty = owlModel.getOWLDatatypeProperty(result[0].trim());
				
				if (datatypeProperty == null) {
					System.out.println(result[0].trim() + " cann't be a OWLDatatypeProperty!");
					//return false;
					bool.setBoolType(BoolType.Unknown);
					return bool;
				}
				
				try {
					Collection dataSet = null;
					dataSet = reasoner.getRelatedValues(subjectIndividual, datatypeProperty);
					System.out.println("dataSet: " + dataSet);
					
					int RealNum = dataSet.size();
					return judge(RealNum, op, ExpectNum);
				} catch (ProtegeReasonerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//return false;
					bool.setBoolType(BoolType.Unknown);
					return bool;
				}
				
			}
		}
		else if (cxtObject.getSecondType().equals(SecondType.DirectData)) {//Just like: xsd:int(?x)
			
			Expression exp = new Expression();
			
			if (cxtObject.getTypeValue().equals("boolean")) {
				if (exp.isBoolean(cxtObject.getArguments().get(0))) {
					bool.setBoolType(BoolType.True);
					return bool;
				}
				else {
					bool.setBoolType(BoolType.False);
					return bool;
				}
			}
			else if (cxtObject.getTypeValue().equals("int")) {
				if (exp.isInt(cxtObject.getArguments().get(0))) {
					bool.setBoolType(BoolType.True);
					return bool;
				}
				else {
					bool.setBoolType(BoolType.False);
					return bool;
				}
			}
			else if (cxtObject.getTypeValue().equals("integer")) {
				if (exp.isInteger(cxtObject.getArguments().get(0))) {
					bool.setBoolType(BoolType.True);
					return bool;
				}
				else {
					bool.setBoolType(BoolType.False);
					return bool;
				}
			}
			else if (cxtObject.getTypeValue().equals("nonNegativeInteger")) {
				if (exp.isNonNegativeInteger(cxtObject.getArguments().get(0))) {
					bool.setBoolType(BoolType.True);
					return bool;
				}
				else {
					bool.setBoolType(BoolType.False);
					return bool;
				}
			}
			else if (cxtObject.getTypeValue().equals("float")) {
				if (exp.isFloat(cxtObject.getArguments().get(0))) {
					bool.setBoolType(BoolType.True);
					return bool;
				}
				else {
					bool.setBoolType(BoolType.False);
					return bool;
				}
			}
			else if (cxtObject.getTypeValue().equals("double")) {
				if (exp.isDouble(cxtObject.getArguments().get(0))) {
					bool.setBoolType(BoolType.True);
					return bool;
				}
				else {
					bool.setBoolType(BoolType.False);
					return bool;
				}
			}
			else if (cxtObject.getTypeValue().equals("date")) {
				if (exp.isDate(cxtObject.getArguments().get(0))) {
					bool.setBoolType(BoolType.True);
					return bool;
				}
				else {
					bool.setBoolType(BoolType.False);
					return bool;
				}
			}
			else if (cxtObject.getTypeValue().equals("dateTime")) {
				if (exp.isDateTime(cxtObject.getArguments().get(0))) {
					bool.setBoolType(BoolType.True);
					return bool;
				}
				else {
					bool.setBoolType(BoolType.False);
					return bool;
				}
			}
			
			//return false;
			bool.setBoolType(BoolType.Unknown);
			return bool;
		}
		else if (cxtObject.getSecondType().equals(SecondType.IndirectData)) {//Just like: [3, 4, 5](?x)
			
			// 生成一个Pattern,同时编译一个正则表达式 ;用Pattern的split()方法把字符串分割 
			String Regex = "[,]";
			Pattern p = Pattern.compile(Regex);
			String[] result = p.split(cxtObject.getTypeValue().replace("[", "").replace("]", ""));//attention: must be!
			
			for (int i = 0; i < result.length; i++) {
				System.out.println(result[i].trim());
				if (result[i].trim().equals(cxtObject.getArguments().get(0))) {
					
					System.out.println(result[i].trim() + " equals: " + cxtObject.getArguments().get(0));
					//return true;
					bool.setBoolType(BoolType.True);
					return bool;
				}
			}
			
			System.out.println(cxtObject.getArguments().get(0) + " isn't in " + cxtObject.getTypeValue());
			//return false;
			bool.setBoolType(BoolType.False);
			return bool;
		}
		else {
			System.out.println("exec_C: swrl format unknown!");
			//return false;
			bool.setBoolType(BoolType.Unknown);
			return bool;
		}
	}
	
	public Bool exec_P(CXTObject cxtObject) {
		
		Bool bool = new Bool();
	
		if (cxtObject.getSecondType().equals(SecondType.ObjectProperty)) {
			
			OWLObjectProperty objectProperty = null;
			objectProperty = owlModel.getOWLObjectProperty(cxtObject.getTypeValue());
			
			if (objectProperty == null) {
				System.out.println(cxtObject.getTypeValue() + " cann't be a OWLObjectProperty!");
				//return false;
				bool.setBoolType(BoolType.Unknown);
				return bool;
			}
			
			OWLIndividual subjectIndividual = null;
			subjectIndividual = owlModel.getOWLIndividual(cxtObject.getArguments().get(0));
			
			if (subjectIndividual == null) {
				System.out.println(cxtObject.getArguments().get(0) + " cann't be a OWLIndividual!");
				//return false;
				bool.setBoolType(BoolType.Unknown);
				return bool;
			}
			
			OWLIndividual objectIndividual = null;
			objectIndividual = owlModel.getOWLIndividual(cxtObject.getArguments().get(1));
			
			if (objectIndividual == null) {
				System.out.println(cxtObject.getArguments().get(1) + " cann't be a OWLIndividual!");
				//return false;
				bool.setBoolType(BoolType.Unknown);
				return bool;
			}
			
			try {
				Collection<OWLIndividual> individualSet = null;
				individualSet = reasoner.getRelatedIndividuals(subjectIndividual, objectProperty);
				
				for (Iterator it = individualSet.iterator(); it.hasNext(); ) {
					
					OWLIndividual individual = (OWLIndividual) it.next();
					if (individual.equals(objectIndividual)) {
						System.out.println(individual.getName() + " equals " + objectIndividual.getName());
						System.out.println(objectIndividual.getName() + " is related to "+ subjectIndividual.getName() + " by " + objectProperty.getName());
						//return true;
						bool.setBoolType(BoolType.True);
						return bool;
					}
				}
				
				System.out.println(objectIndividual.getName() + " isn't related to "+ subjectIndividual.getName() + " by " + objectProperty.getName());
				//return false;
				bool.setBoolType(BoolType.False);
				return bool;
			} catch (ProtegeReasonerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//return false;
				bool.setBoolType(BoolType.Unknown);
				return bool;
			}
		}
		else if (cxtObject.getSecondType().equals(SecondType.DatatypeProperty)) {
			
			OWLDatatypeProperty datatypeProperty = null;
			datatypeProperty = owlModel.getOWLDatatypeProperty(cxtObject.getTypeValue());
			
			if (datatypeProperty == null) {
				System.out.println(cxtObject.getTypeValue() + " cann't be a OWLDatatypeProperty!");
				//return false;
				bool.setBoolType(BoolType.Unknown);
				return bool;
			}
			System.out.println("DatatypeProperty: " + datatypeProperty.getName());
			
			OWLIndividual subjectIndividual = null;
			subjectIndividual = owlModel.getOWLIndividual(cxtObject.getArguments().get(0));
			
			if (subjectIndividual == null) {
				System.out.println(cxtObject.getArguments().get(0) + " cann't be a OWLIndividual!");
				//return false;
				bool.setBoolType(BoolType.Unknown);
				return bool;
			}
			System.out.println("subjectIndividual: " + subjectIndividual.getName());
			
			try {
				Collection dataSet = null;
				dataSet = reasoner.getRelatedValues(subjectIndividual, datatypeProperty);
				System.out.println("dataSet: " + dataSet);
				
//				if (dataSet.contains(cxtObject.getArguments().get(1))) {
//					return true;
//				}
				
				for (Iterator it = dataSet.iterator(); it.hasNext(); ) {
					
					Object data = it.next();					
					System.out.println(data);
					if (data.toString().equals(cxtObject.getArguments().get(1))) {//attention: must be..toString()
						System.out.println(data + " equals " + cxtObject.getArguments().get(1));
						System.out.println(data + " is related to "+ subjectIndividual.getName() + " by " + datatypeProperty.getName());
						//return true;
						bool.setBoolType(BoolType.True);
						return bool;
					}
				}
				
				System.out.println(cxtObject.getArguments().get(1) + " isn't related to "+ subjectIndividual.getName() + " by " + datatypeProperty.getName());
				//return false;
				bool.setBoolType(BoolType.False);
				return bool;
			} catch (ProtegeReasonerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//return false;
				bool.setBoolType(BoolType.Unknown);
				return bool;
			}
		}
		
		System.out.println("exec_sameAs: swrl format unknown!");
		//return false;
		bool.setBoolType(BoolType.Unknown);
		return bool;
	}
	
	public Bool exec_sameAs(CXTObject cxtObject) {
		
		Bool bool = new Bool();
		
		OWLIndividual individual_1 = null;
		individual_1 = owlModel.getOWLIndividual(cxtObject.getArguments().get(0));
		
		if (individual_1 == null) {
			System.out.println(cxtObject.getArguments().get(0) + " cann't be a OWLIndividual!");
			//return false;
			bool.setBoolType(BoolType.Unknown);
			return bool;
		}
		System.out.println("individual_1: " + individual_1.getName());
		
		
		OWLIndividual individual_2 = null;
		individual_2 = owlModel.getOWLIndividual(cxtObject.getArguments().get(1));
		
		if (individual_2 == null) {
			System.out.println(cxtObject.getArguments().get(1) + " cann't be a OWLIndividual!");
			//return false;
			bool.setBoolType(BoolType.Unknown);
			return bool;
		}
		System.out.println("individual_2: " + individual_2.getName());
	
		if (individual_1.equals(individual_2)) {
			System.out.println(individual_1 + " sameAs " + individual_2);
			//return true;
			bool.setBoolType(BoolType.True);
			return bool;
		}
		
		System.out.println(individual_1 + " isn't sameAs " + individual_2);
		//return false;
		bool.setBoolType(BoolType.False);
		return bool;
	}
	
	public Bool exec_differentFrom(CXTObject cxtObject) {
	
		Bool bool = new Bool();
		
		OWLIndividual individual_1 = null;
		individual_1 = owlModel.getOWLIndividual(cxtObject.getArguments().get(0));
		
		if (individual_1 == null) {
			System.out.println(cxtObject.getArguments().get(0) + " cann't be a OWLIndividual!");
			//return false;
			bool.setBoolType(BoolType.Unknown);
			return bool;
		}
		System.out.println("individual_1: " + individual_1.getName());
		
		
		OWLIndividual individual_2 = null;
		individual_2 = owlModel.getOWLIndividual(cxtObject.getArguments().get(1));
		
		if (individual_2 == null) {
			System.out.println(cxtObject.getArguments().get(1) + " cann't be a OWLIndividual!");
			//return false;
			bool.setBoolType(BoolType.Unknown);
			return bool;
		}
		System.out.println("individual_2: " + individual_2.getName());
	
		if (individual_1.equals(individual_2)) {
			System.out.println(individual_1 + " isn't differentFrom " + individual_2);
			//return false;
			bool.setBoolType(BoolType.False);
			return bool;
		}
		
		System.out.println(individual_1 + " differentFrom " + individual_2);
		//return true;
		bool.setBoolType(BoolType.True);
		return bool;
	}

	public Bool exec_swrlb(CXTObject cxtObject) {
	
		Bool bool = new Bool();
		
		if (cxtObject.getTypeValue().equals("greaterThan")) {
			if (cxtObject.getArguments().get(0).compareTo(cxtObject.getArguments().get(1)) > 0) {
				//return true;
				bool.setBoolType(BoolType.True);
				return bool;
			}
			else {
				//return false;
				bool.setBoolType(BoolType.False);
				return bool;
			}
		}
		if (cxtObject.getTypeValue().equals("lessThan")) {
			if (cxtObject.getArguments().get(0).compareTo(cxtObject.getArguments().get(1)) < 0) {
				//return true;
				bool.setBoolType(BoolType.True);
				return bool;
			}
			else {
				//return false;
				bool.setBoolType(BoolType.False);
				return bool;
			}
		}
		//return false;
		bool.setBoolType(BoolType.Unknown);
		return bool;
	}
	
	public Bool exec(String swrl) {
		
		//parser
		cxtParser = new CXTParser(owlModel, reasoner);
		cxtParser.parser(swrl);
		cxtParser.print();
		
		//Exec
		if (cxtParser.getCXTObject().getFirstType().equals(FirstType.C)) return exec_C(cxtParser.getCXTObject());
		else if (cxtParser.getCXTObject().getFirstType().equals(FirstType.P)) return exec_P(cxtParser.getCXTObject());
		else if (cxtParser.getCXTObject().getFirstType().equals(FirstType.sameAs)) return exec_sameAs(cxtParser.getCXTObject());
		else if (cxtParser.getCXTObject().getFirstType().equals(FirstType.differentFrom)) return exec_differentFrom(cxtParser.getCXTObject());
		else if (cxtParser.getCXTObject().getFirstType().equals(FirstType.swrlb)) return exec_swrlb(cxtParser.getCXTObject());
		
		System.out.println("exec: swrl format unknonw!");
		//return false;
		Bool bool = new Bool();
		bool.setBoolType(BoolType.Unknown);
		return bool;
	}
	
	public Bool execAllBoolean(String swrl) {
		
		// 生成一个Pattern,同时编译一个正则表达式 ;用Pattern的split()方法把字符串分割 
		String Regex = "[\\^]";
		Pattern p = Pattern.compile(Regex);
		String[] result = p.split(swrl);

		for (int i = 0; i < result.length; i++) {
			System.out.println("Test " + (i+1) + ": " + result[i].trim());
			if (exec(result[i].trim()).getBoolType() == BoolType.False) {
				System.out.println("---exec " + result[i].trim() + " false!");
				//return false;
				Bool bool = new Bool();
				bool.setBoolType(BoolType.False);
				return bool;
			}
			else if (exec(result[i].trim()).getBoolType() == BoolType.Unknown) {
				System.out.println("---exec " + result[i].trim() + " unknown!");
				//return false;
				Bool bool = new Bool();
				bool.setBoolType(BoolType.Unknown);
				return bool;
			}
		}
		System.out.println("---execAll " + swrl.trim() + " true!");
		//return true;
		Bool bool = new Bool();
		bool.setBoolType(BoolType.True);
		return bool;
	}
	
	public double execAllDouble(String swrl) {
		
		// 生成一个Pattern,同时编译一个正则表达式 ;用Pattern的split()方法把字符串分割 
		String Regex = "[\\^]";
		Pattern p = Pattern.compile(Regex);
		String[] result = p.split(swrl);

		int numOfTrue = 0;
		
		for (int i = 0; i < result.length; i++) {
			System.out.println("Test " + (i+1) + ": " + result[i].trim());
			if (exec(result[i].trim()).getBoolType() == BoolType.True) {
				System.out.println("---exec " + result[i].trim() + " true!");
				numOfTrue++;
			}
			else {
				System.out.println("---exec " + result[i].trim() + " unknown or false!");
			}
		}
		
		System.out.println("---execAll " + numOfTrue + "/" + result.length + " is true!");
		return 1.0 * numOfTrue / result.length;
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
		hasAge.setRange(owlModel.getXSDinteger());
		
		Darwin.setPropertyValue(hasAge, new Integer(18));
		
		OWLObjectProperty hasChild = owlModel.createOWLObjectProperty("hasChild");
		hasChild.setDomain(Person);
		hasChild.setRange(Person);
		
		Darwin.setPropertyValue(hasChild, LittleDarwin);
		
		Reasoner reasoner = new Reasoner();
	    ProtegeReasoner pellet = reasoner.createPelletOWLAPIReasoner(owlModel);
	    
		CXTParser cxtParser = new CXTParser(owlModel, pellet);
		
//		String swrl = "Person(Darwin)";
//		String swrl = "(hasChild >= 1)(Darwin)";
//		String swrl = "(hasAge > 1)(Darwin)";
//		String swrl = "xsd:boolean(false)";//must hava xsd...
//		String swrl = "[3, 4, 5](5)";
		
//		String swrl = "hasHobby(Darwin, Biology)";
//		String swrl = "hasAge(Darwin, 18)";
		
//		String swrl = "sameAs(Darwin, LittleDarwin)";
//		String swrl = "differentFrom(Darwin, LittleDarwin)";
		
//		String swrl = "swrlb:greaterThan(19, 17)";
		
		String swrl = "Person(Darwin) ^ (hasChild >= 1)(Darwin) ^ hasHobby(Darwin, Biology)";
		
		CXTCondition cxtCondition = new CXTCondition(owlModel, pellet);
		
		cxtCondition.execAllBoolean(swrl);
		
		System.out.println(cxtCondition.execAllDouble(swrl));
	}
}
