package edu.pku.contextEffect;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Pattern;

import edu.pku.context.model.CXTObject;
import edu.pku.context.model.CXTParser;
import edu.pku.context.model.CXTObject.FirstType;
import edu.pku.context.model.CXTObject.SecondType;
import edu.pku.contextConditon.CXTCondition;
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
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

public class CXTEffect {

	private OWLModel owlModel;
	private ProtegeReasoner reasoner;
	
	private CXTParser cxtParser;
	
	public CXTEffect(OWLModel owlModel, ProtegeReasoner reasoner) {
		
		this.owlModel = owlModel;
		this.reasoner = reasoner;
	}
	
	public void print_DirectClass(OWLNamedClass cls, OWLIndividual individual) {
		System.out.println("***After exec_C***");
		
		Reasoner reasoner2 = new Reasoner();
		ProtegeReasoner pellet = reasoner2.createPelletOWLAPIReasoner(owlModel);
		
		Collection<OWLIndividual> individualSet = null;
		try {
			individualSet = pellet.getIndividualsBelongingToClass(cls);
			
			for (Iterator it = individualSet.iterator(); it.hasNext(); ) {
				OWLIndividual ind = (OWLIndividual) it.next();
				if (ind.equals(individual)) {
					System.out.println("-----create the instance successed!-----");
					System.out.println(ind.getName() + " is **already** be instance of class " + cls.getName());
					return ;
				}
			}
		} catch (ProtegeReasonerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ;
		}
		
		System.out.println("***exec_C error!***");
		return ;
	}
	
	public void exec_C(CXTObject cxtObject) {
		
		if (cxtObject.getSecondType().equals(SecondType.DirectClass)) {//Just like: Person(Jacob)
			
			OWLNamedClass cls = null;
			cls = owlModel.getOWLNamedClass(cxtObject.getTypeValue());
			
			if (cls == null) {
				System.out.println(cxtObject.getTypeValue() + " isn't be a OWLNamedClass!");
				return ;
			}
			
			OWLIndividual individual = null;
			individual = owlModel.getOWLIndividual(cxtObject.getArguments().get(0));
			
			if (individual == null) {
				System.out.println(cxtObject.getArguments().get(0) + " isn't be a OWLIndividual!");
				System.out.println("-----now create the individual!-----");
				individual = cls.createOWLIndividual(cxtObject.getArguments().get(0));
				if (individual == null) {
					System.out.println("create individual error!");
					return ;
				}
				System.out.println("-----create the individual successed!-----");
			}
			
			Collection<OWLIndividual> individualSet = null;
			try {
				/**
				 * it's very important...
				 */
				Reasoner reasoner2 = new Reasoner();
				ProtegeReasoner pellet = reasoner2.createPelletOWLAPIReasoner(owlModel);
				reasoner = pellet;
				individualSet = reasoner.getIndividualsBelongingToClass(cls);
				
				for (Iterator it = individualSet.iterator(); it.hasNext(); ) {
					OWLIndividual ind = (OWLIndividual) it.next();
					if (ind.equals(individual)) {
						System.out.println(ind.getName() + " is a instance of class " + cls.getName());
						print_DirectClass(cls, individual);
						return ;
					}
				}
			} catch (ProtegeReasonerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return ;
			}
			
			System.out.println(individual.getName() + " isn't a instance of class " + cls.getName());
			return ;
		}
//		else if (cxtObject.getSecondType().equals(SecondType.IndirectClass)) {//Just like: (hasChild >= 1)(Jacob)
//			
//			//get ?x
//			OWLIndividual subjectIndividual = null;
//			subjectIndividual = owlModel.getOWLIndividual(cxtObject.getArguments().get(0));
//			
//			if (subjectIndividual == null) {
//				System.out.println(cxtObject.getArguments().get(0) + " cann't be a OWLIndividual!");
//				return ;
//			}
//			
//			String op = null;
//			
//			if (cxtObject.getTypeValue().contains(">=")) op = ">=";//must be the sort.
//			else if (cxtObject.getTypeValue().contains("<=")) op = "<=";
//			else if (cxtObject.getTypeValue().contains("=")) op = "=";
//			else if (cxtObject.getTypeValue().contains(">")) op = ">";
//			else if (cxtObject.getTypeValue().contains("<")) op = "<";
//			else {
//				System.out.println("exec_C: swrl format error!");
//				return ;
//			}
//			
//			// 生成一个Pattern,同时编译一个正则表达式 ;用Pattern的split()方法把字符串分割 
//			String Regex = op;
//			Pattern p = Pattern.compile(Regex);
//			String[] result = p.split(cxtObject.getTypeValue());
//			
//			if (result.length != 2) {
//				System.out.println("swrl format error!");
//				return ;
//			}
//			
//			//get property...
//			OWLProperty property = owlModel.getOWLProperty(result[0].trim());
//			int ExpectNum = Integer.parseInt(result[1].trim());//String to int...
//			
//			if (property.isObjectProperty()) {
//				
//				OWLObjectProperty objectProperty = null;
//				objectProperty = owlModel.getOWLObjectProperty(result[0].trim());
//				
//				if (objectProperty == null) {
//					System.out.println(result[0].trim() + " cann't be a OWLObjectProperty!");
//					return ;
//				}
//				
//				try {
//					Collection<OWLIndividual> individualSet = null;
//					individualSet = reasoner.getRelatedIndividuals(subjectIndividual, objectProperty);
//					System.out.println("individualSet: " + individualSet);
//					
//					int RealNum = individualSet.size();
//					return judge(RealNum, op, ExpectNum);
//					
//				} catch (ProtegeReasonerException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					return ;
//				}
//			}
//			else {
//				
//				OWLDatatypeProperty datatypeProperty = null;
//				datatypeProperty = owlModel.getOWLDatatypeProperty(result[0].trim());
//				
//				if (datatypeProperty == null) {
//					System.out.println(result[0].trim() + " cann't be a OWLDatatypeProperty!");
//					return ;
//				}
//				
//				try {
//					Collection dataSet = null;
//					dataSet = reasoner.getRelatedValues(subjectIndividual, datatypeProperty);
//					System.out.println("dataSet: " + dataSet);
//					
//					int RealNum = dataSet.size();
//					return judge(RealNum, op, ExpectNum);
//				} catch (ProtegeReasonerException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					return ;
//				}
//				
//			}
//		}
//		else if (cxtObject.getSecondType().equals(SecondType.DirectData)) {//Just like: xsd:int(?x)
//			
//			Expression exp = new Expression();
//			
//			if (cxtObject.getTypeValue().equals("boolean")) return exp.isBoolean(cxtObject.getArguments().get(0));
//			else if (cxtObject.getTypeValue().equals("int")) return exp.isInt(cxtObject.getArguments().get(0));
//			else if (cxtObject.getTypeValue().equals("integer")) return exp.isInteger(cxtObject.getArguments().get(0));
//			else if (cxtObject.getTypeValue().equals("nonNegativeInteger")) return exp.isNonNegativeInteger(cxtObject.getArguments().get(0));
//			else if (cxtObject.getTypeValue().equals("float")) return exp.isFloat(cxtObject.getArguments().get(0));
//			else if (cxtObject.getTypeValue().equals("double")) return exp.isDouble(cxtObject.getArguments().get(0));
//			else if (cxtObject.getTypeValue().equals("date")) return exp.isDate(cxtObject.getArguments().get(0));
//			else if (cxtObject.getTypeValue().equals("dateTime")) return exp.isDateTime(cxtObject.getArguments().get(0));
//			
//			return false;
//		}
//		else if (cxtObject.getSecondType().equals(SecondType.IndirectData)) {//Just like: [3, 4, 5](?x)
//			
//			// 生成一个Pattern,同时编译一个正则表达式 ;用Pattern的split()方法把字符串分割 
//			String Regex = "[,]";
//			Pattern p = Pattern.compile(Regex);
//			String[] result = p.split(cxtObject.getTypeValue().replace("[", "").replace("]", ""));//attention: must be!
//			
//			for (int i = 0; i < result.length; i++) {
//				System.out.println(result[i].trim());
//				if (result[i].trim().equals(cxtObject.getArguments().get(0))) {
//					
//					System.out.println(result[i].trim() + " equals: " + cxtObject.getArguments().get(0));
//					return true;
//				}
//			}
//			
//			System.out.println(cxtObject.getArguments().get(0) + " isn't in " + cxtObject.getTypeValue());
//			return false;
//		}
//		else {
//			System.out.println("exec_C: swrl format error!");
//			return false;
//		}
	}
	
	public void print_ObjectProperty(OWLIndividual subjectIndividual, OWLObjectProperty objectProperty, OWLIndividual objectIndividual) {
		System.out.println("--------After exec CXTEffect!---------");
		Reasoner reasoner2 = new Reasoner();
		ProtegeReasoner pellet = reasoner2.createPelletOWLAPIReasoner(owlModel);
		Collection<OWLIndividual> individualSet = null;
		try {
			individualSet = pellet.getRelatedIndividuals(subjectIndividual, objectProperty);
			for (Iterator it = individualSet.iterator(); it.hasNext(); ) {
				OWLIndividual individual = (OWLIndividual) it.next();
				if (individual.equals(objectIndividual)) {
					System.out.println("-----create the relationship successed!-----");
					System.out.println(objectIndividual.getName() + " is **already** be related to "+ subjectIndividual.getName() + " by " + objectProperty.getName());
					return ;
				}
			}
			
			System.out.println("CXTEffect not success!");
		} catch (ProtegeReasonerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void print_DatatypeProperty(OWLIndividual subjectIndividual, OWLDatatypeProperty datatypeProperty, Object exp) {
		System.out.println("--------After exec CXTEffect!---------");
		Reasoner reasoner2 = new Reasoner();
		ProtegeReasoner pellet = reasoner2.createPelletOWLAPIReasoner(owlModel);
		Collection dataSet = null;
		try {
			dataSet = pellet.getRelatedValues(subjectIndividual, datatypeProperty);
			for (Iterator it = dataSet.iterator(); it.hasNext(); ) {
				Object data = it.next();
				if (data.toString().equals(exp.toString())) {
					System.out.println("-----create the relationship successed!-----");
					System.out.println(exp.toString() + " is **already** be related to "+ subjectIndividual.getName() + " by " + datatypeProperty.getName());
					return ;
				}
			}
			
			System.out.println("CXTEffect not success!");
		} catch (ProtegeReasonerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Object createData(RDFResource range, String str){
		
		Object data = null;
		Expression exp = new Expression();
		
		if (range.getLocalName().equals("boolean")) {
			if (exp.isBoolean(str)) {
				System.out.println("-----now create the data!(Boolean)-----");
				data = Boolean.parseBoolean(str);
				System.out.println("-----create the data(Boolean) successed!-----");
			}
			else {
				System.out.println("exec_P: swrl format error!");
				return data;
			}
		}
		
		return data;
	}
	
	public void exec_P(CXTObject cxtObject) {
		
		if (cxtObject.getSecondType().equals(SecondType.ObjectProperty)) {
			
			OWLObjectProperty objectProperty = null;
			objectProperty = owlModel.getOWLObjectProperty(cxtObject.getTypeValue());
			
			if (objectProperty == null) {//we won't create!
				System.out.println(cxtObject.getTypeValue() + " isn't be a OWLObjectProperty!");
				return ;
			}
			
			RDFSClass domain = objectProperty.getDomain(false);
			RDFResource range = objectProperty.getRange(false);
			
			OWLIndividual subjectIndividual = null;
			subjectIndividual = owlModel.getOWLIndividual(cxtObject.getArguments().get(0));
			
			if (subjectIndividual == null) {
				System.out.println(cxtObject.getArguments().get(0) + " isn't be a OWLIndividual!");
				System.out.println("-----now create the subjectIndividual!-----");
				OWLNamedClass cls = owlModel.getOWLNamedClass(domain.getLocalName());
				subjectIndividual = cls.createOWLIndividual(cxtObject.getArguments().get(0));
				if (subjectIndividual == null) {
					System.out.println("create subjectIndividual error!");
					return ;
				}
				System.out.println("-----create the subjectIndividual successed!-----");
			}
			
			OWLIndividual objectIndividual = null;
			objectIndividual = owlModel.getOWLIndividual(cxtObject.getArguments().get(1));
			
			if (objectIndividual == null) {
				System.out.println(cxtObject.getArguments().get(1) + " isn't be a OWLIndividual!");
				System.out.println("-----now create the objectIndividual!-----");
				OWLNamedClass cls = owlModel.getOWLNamedClass(range.getLocalName());
				objectIndividual = cls.createOWLIndividual(cxtObject.getArguments().get(1));
				if (objectIndividual == null) {
					System.out.println("create objectIndividual error!");
					return ;
				}
				System.out.println("-----create the objectIndividual successed!-----");
			}
			
			try {
				Collection<OWLIndividual> individualSet = null;
				
				/**
				 * must like this...
				 */
				Reasoner reasoner2 = new Reasoner();
				ProtegeReasoner pellet = reasoner2.createPelletOWLAPIReasoner(owlModel);
				reasoner = pellet;
				
				individualSet = reasoner.getRelatedIndividuals(subjectIndividual, objectProperty);
				
				for (Iterator it = individualSet.iterator(); it.hasNext(); ) {
					
					OWLIndividual individual = (OWLIndividual) it.next();
					if (individual.equals(objectIndividual)) {
						System.out.println(individual.getName() + " equals " + objectIndividual.getName());
						System.out.println(objectIndividual.getName() + " is **already** be related to "+ subjectIndividual.getName() + " by " + objectProperty.getName());
						return ;
					}
				}
				
				System.out.println(objectIndividual.getName() + " isn't related to "+ subjectIndividual.getName() + " by " + objectProperty.getName());
				System.out.println("-----now create the relationship!-----");
				
				subjectIndividual.setPropertyValue(objectProperty, objectIndividual);
				
				print_ObjectProperty(subjectIndividual, objectProperty, objectIndividual);
				return ;
			} catch (ProtegeReasonerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return ;
			}
		}
		else if (cxtObject.getSecondType().equals(SecondType.DatatypeProperty)) {
			
			OWLDatatypeProperty datatypeProperty = null;
			datatypeProperty = owlModel.getOWLDatatypeProperty(cxtObject.getTypeValue());
			
			if (datatypeProperty == null) {
				System.out.println(cxtObject.getTypeValue() + " isn't be a OWLDatatypeProperty!");
				return ;
			}
			System.out.println("DatatypeProperty: " + datatypeProperty.getName());
			
			RDFSClass domain = datatypeProperty.getDomain(false);
			RDFResource range = datatypeProperty.getRange(false);
			
			OWLIndividual subjectIndividual = null;
			subjectIndividual = owlModel.getOWLIndividual(cxtObject.getArguments().get(0));
			
			if (subjectIndividual == null) {
				System.out.println(cxtObject.getArguments().get(0) + " isn't be a OWLIndividual!");
				System.out.println("-----now create the subjectIndividual!-----");
				OWLNamedClass cls = owlModel.getOWLNamedClass(domain.getLocalName());
				subjectIndividual = cls.createOWLIndividual(cxtObject.getArguments().get(0));
				if (subjectIndividual == null) {
					System.out.println("create subjectIndividual error!");
					return ;
				}
				System.out.println("-----create the subjectIndividual successed!-----");
			}
			System.out.println("subjectIndividual: " + subjectIndividual.getName());
			
			Object data = null;
			Expression exp = new Expression();
			
			if (range.getLocalName().equals("boolean")) {
				if (exp.isBoolean(cxtObject.getArguments().get(1))) {
					System.out.println("-----now create the data!(Boolean)-----");
					data = Boolean.parseBoolean(cxtObject.getArguments().get(1));
					System.out.println("-----create the data(Boolean) successed!-----");
				}
				else {
					System.out.println("exec_P: swrl format error!");
					return ;
				}
			}
			else if (range.getLocalName().equals("int")) {
				if (exp.isInt(cxtObject.getArguments().get(1))) {
					System.out.println("-----now create the data!(Int)-----");
					data = Integer.parseInt(cxtObject.getArguments().get(1));
					System.out.println("-----create the data(Int) successed!-----");
				}
				else {
					System.out.println("exec_P: swrl format error!");
					return ;
				}
			}
			else if (range.getLocalName().equals("integer")) {
				if (exp.isInteger(cxtObject.getArguments().get(1))) {
					System.out.println("-----now create the data!(Integer)-----");
					data = Integer.parseInt(cxtObject.getArguments().get(1));
					System.out.println("-----create the data(Integer) successed!-----");
				}
				else {
					System.out.println("exec_P: swrl format error!");
					return ;
				}
			}
			else if (range.getLocalName().equals("nonNegativeInteger")) {
				if (exp.isNonNegativeInteger(cxtObject.getArguments().get(1))) {
					System.out.println("-----now create the data!(NonNegativeInteger)-----");
					data = Integer.parseInt(cxtObject.getArguments().get(1));
					System.out.println("-----create the data(NonNegativeInteger) successed!-----");
				}
				else {
					System.out.println("exec_P: swrl format error!");
					return ;
				}
			}
			else if (range.getLocalName().equals("float")) {
				if (exp.isFloat(cxtObject.getArguments().get(1))) {
					System.out.println("-----now create the data!(Float)-----");
					data = Float.parseFloat(cxtObject.getArguments().get(1));
					System.out.println("-----create the data(Float) successed!-----");
				}
				else {
					System.out.println("exec_P: swrl format error!");
					return ;
				}
			}
			else if (range.getLocalName().equals("double")) {
				if (exp.isDouble(cxtObject.getArguments().get(1))) {
					System.out.println("-----now create the data!(Double)-----");
					data = Double.parseDouble(cxtObject.getArguments().get(1));
					System.out.println("-----create the data(Double) successed!-----");
				}
				else {
					System.out.println("exec_P: swrl format error!");
					return ;
				}
			}
			else if (range.getLocalName().equals("date")) {
				if (exp.isDate(cxtObject.getArguments().get(1))) {
					System.out.println("-----now create the data!(Date)-----");
					
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
					try {
						data = (Date)formatter.parse(cxtObject.getArguments().get(1));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					System.out.println("-----create the data(Date) successed!-----");
				}
				else {
					System.out.println("exec_P: swrl format error!");
					return ;
				}
			}
			else if (range.getLocalName().equals("dateTime")) {
				if (exp.isDateTime(cxtObject.getArguments().get(1))) {
					System.out.println("-----now create the data!(DateTime)-----");
					
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					try {
						data = (Date)formatter.parse(cxtObject.getArguments().get(1));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("-----create the data(DateTime) successed!-----");
				}
				else {
					System.out.println("exec_P: swrl format error!");
					return ;
				}
			}
			
			try {
				Collection dataSet = null;
				
				/**
				 * must like this...
				 */
				Reasoner reasoner2 = new Reasoner();
				ProtegeReasoner pellet = reasoner2.createPelletOWLAPIReasoner(owlModel);
				reasoner = pellet;
				
				dataSet = reasoner.getRelatedValues(subjectIndividual, datatypeProperty);
				System.out.println("dataSet: " + dataSet);
				
//				if (dataSet.contains(cxtObject.getArguments().get(1))) {
//					return true;
//				}
				
				for (Iterator it = dataSet.iterator(); it.hasNext(); ) {
					
					Object data_ = it.next();					
					System.out.println(data_);
					if (data_.toString().equals(cxtObject.getArguments().get(1))) {//attention: must be..toString()
						System.out.println(data_ + " equals " + cxtObject.getArguments().get(1));
						System.out.println(data_ + " is related to "+ subjectIndividual.getName() + " by " + datatypeProperty.getName());
						return ;
					}
				}
				
				System.out.println(cxtObject.getArguments().get(1) + " isn't related to "+ subjectIndividual.getName() + " by " + datatypeProperty.getName());
				System.out.println("-----now create the relationship!-----");
//				System.out.println("data--- " + data.toString());
				subjectIndividual.setPropertyValue(datatypeProperty, data);
				print_DatatypeProperty(subjectIndividual, datatypeProperty, data);
				return ;
			} catch (ProtegeReasonerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return ;
			}
		}
		
		System.out.println("exec_sameAs: swrl format error!");
		return ;
	}
	
	public void exec_sameAs(CXTObject cxtObject) {
		
		OWLIndividual individual_1 = null;
		individual_1 = owlModel.getOWLIndividual(cxtObject.getArguments().get(0));
		
		if (individual_1 == null) {
			System.out.println(cxtObject.getArguments().get(0) + " cann't be a OWLIndividual!");
			return ;
		}
		System.out.println("individual_1: " + individual_1.getName());
		
		OWLIndividual individual_2 = null;
		individual_2 = owlModel.getOWLIndividual(cxtObject.getArguments().get(1));
		
		if (individual_2 == null) {
			System.out.println(cxtObject.getArguments().get(1) + " cann't be a OWLIndividual!");
			return ;
		}
		System.out.println("individual_2: " + individual_2.getName());
	
		/**
		 * Whether judge the class of individual?
		 */
		individual_1.addSameAs(individual_2);
		System.out.println("---" + individual_1 + " is sameAs(now) " + individual_2);
		
		return ;
	}

	public void exec_differentFrom(CXTObject cxtObject) {
	
		OWLIndividual individual_1 = null;
		individual_1 = owlModel.getOWLIndividual(cxtObject.getArguments().get(0));
		
		if (individual_1 == null) {
			System.out.println(cxtObject.getArguments().get(0) + " cann't be a OWLIndividual!");
			return ;
		}
		System.out.println("individual_1: " + individual_1.getName());
		
		OWLIndividual individual_2 = null;
		individual_2 = owlModel.getOWLIndividual(cxtObject.getArguments().get(1));
		
		if (individual_2 == null) {
			System.out.println(cxtObject.getArguments().get(1) + " cann't be a OWLIndividual!");
			return ;
		}
		System.out.println("individual_2: " + individual_2.getName());
	
		/**
		 * Whether judge the class of individual?
		 */
		individual_1.addDifferentFrom(individual_2);
		
		System.out.println("---" + individual_1 + " is sameAs(now) " + individual_2);
		
		return ;
	}

	public void exec_swrlb(CXTObject cxtObject) {
		
		
	}
	
	public void exec(String swrl) {
		
		//parser
		cxtParser = new CXTParser(owlModel, reasoner);
		cxtParser.parser(swrl);
		cxtParser.print();
		
		//exec
		if (cxtParser.getCXTObject().getFirstType().equals(FirstType.C)) {
			exec_C(cxtParser.getCXTObject());
			return ;
		}
		else if (cxtParser.getCXTObject().getFirstType().equals(FirstType.P)) {
			exec_P(cxtParser.getCXTObject());
			return ;
		}
		else if (cxtParser.getCXTObject().getFirstType().equals(FirstType.sameAs)) {
			exec_sameAs(cxtParser.getCXTObject());
			return ;
		}
		else if (cxtParser.getCXTObject().getFirstType().equals(FirstType.differentFrom)) {
			exec_differentFrom(cxtParser.getCXTObject());
			return ;
		}
		else if (cxtParser.getCXTObject().getFirstType().equals(FirstType.swrlb)) {
			exec_swrlb(cxtParser.getCXTObject());
			return ;
		}
				
		System.out.println("exec: swrl format error!");
	}
	
	public void execAll(String swrl) {
		
		if (swrl.equals("")) {
			System.out.println("swrl is empty!");
			return ;
		}
		// 生成一个Pattern,同时编译一个正则表达式 ;用Pattern的split()方法把字符串分割 
		String Regex = "[\\^]";
		Pattern p = Pattern.compile(Regex);
		String[] result = p.split(swrl);

		for (int i = 0; i < result.length; i++) {
			
			System.out.println("---exec " + (i+1) + ": " + result[i].trim());
			exec(result[i].trim());
		}
		System.out.println("---execAll " + swrl.trim());
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
		
//		OWLIndividual Darwin = Person.createOWLIndividual("Darwin");
//		OWLIndividual Biology = Course.createOWLIndividual("Biology");
		OWLIndividual LittleDarwin = Person.createOWLIndividual("LittleDarwin");
		
		OWLObjectProperty hasHobby = owlModel.createOWLObjectProperty("hasHobby");
		hasHobby.setDomain(Person);
		hasHobby.setRange(Course);
		
//		Darwin.setPropertyValue(hasHobby, Biology);
		
		OWLDatatypeProperty hasAge = owlModel.createOWLDatatypeProperty("hasAge");
		hasAge.setDomain(Person);
		hasAge.setRange(owlModel.getXSDinteger());
		
//		Darwin.setPropertyValue(hasAge, new Integer(18));
		
		OWLObjectProperty hasChild = owlModel.createOWLObjectProperty("hasChild");
		hasChild.setDomain(Person);
		hasChild.setRange(Person);
		
//		Darwin.setPropertyValue(hasChild, LittleDarwin);
		
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
		
//		String swrl = "sameAs(Darwin, Jacob)";
//		String swrl = "differentFrom(Darwin, Jacob)";
		
//		String swrl = "swrlb:greaterThan(?age, 17)";
		
		String swrl = "Person(Darwin) ^ hasHobby(Darwin, Biology) ^ hasAge(Darwin, 18)";
		
		CXTEffect cxtEffect = new CXTEffect(owlModel, pellet);
		cxtEffect.execAll(swrl);
	}
}
