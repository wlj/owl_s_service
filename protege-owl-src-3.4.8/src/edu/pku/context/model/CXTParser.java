package edu.pku.context.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import edu.pku.context.model.CXTObject.FirstType;
import edu.pku.context.model.CXTObject.SecondType;
import edu.pku.util.Reasoner;
import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;

public class CXTParser {

	private OWLModel owlModel;
	private ProtegeReasoner reasoner;
	
	private final String SAMEAS = "sameAs";
	private final String DIFFERENTFROM = "differentFrom";
	private final String SWRLB = "swrlb";
	
	private CXTObject cxtObject;
	
	public CXTParser(OWLModel owlModel, ProtegeReasoner reasoner) {
		
		this.owlModel = owlModel;
		this.reasoner = reasoner;
	}
	
	public CXTObject getCXTObject() {
		
		return cxtObject;
	}
	
	public void parserFirstType(String swrl) {
		
		System.out.println("welcome to parserFirstType...");
		
		//parse the operation...
		
		//FirstType
		if (swrl.contains(SAMEAS)) {
			
			cxtObject.setFirstType(FirstType.sameAs);
			System.out.println(swrl + "---FirstType: sameAs");
		}
		else if (swrl.contains(DIFFERENTFROM)) {
			
			cxtObject.setFirstType(FirstType.differentFrom);
			System.out.println(swrl + "---FirstType: differentFrom");
		}
		else if (swrl.contains(SWRLB)) {
			
			cxtObject.setFirstType(FirstType.swrlb);
			System.out.println(swrl + "---FirstType: swrlb");
		}
		else if (swrl.contains("[")) {
			
			cxtObject.setFirstType(FirstType.C);//[3, 4, 5](?x)
			System.out.println(swrl + "---FirstType: C");
		}
		else if (swrl.contains(",")) {
			
			cxtObject.setFirstType(FirstType.P);
			System.out.println(swrl + "---FirstType: P");
		}
		else {
			
			cxtObject.setFirstType(FirstType.C);
			System.out.println(swrl + "---FirstType: C");
		}
	}
	
	public void parser_C(String swrl) {
		
		if (swrl.contains("xsd")) {//must have xsd:int(?x)
			
			// 生成一个Pattern,同时编译一个正则表达式 ;用Pattern的split()方法把字符串分割 
			String Regex = "[:()]";
			Pattern p = Pattern.compile(Regex);
			String[] result = p.split(swrl);
			
			if (result.length != 3) {
				System.out.println("swrl format error!");
				return ;
			}
			
			//SecondType
			cxtObject.setSecondType(SecondType.DirectData);
			
			//TypeValue
			cxtObject.setTypeValue(result[1].trim());
					
			//arguments
			List<String> arguments = new ArrayList<String>();
			arguments.add(result[2].trim());
			cxtObject.setArguments(arguments);
		}
		else if (swrl.contains("[")) {//like [3, 4, 5](?x)
			
			// 生成一个Pattern,同时编译一个正则表达式 ;用Pattern的split()方法把字符串分割 
			String Regex = "[()]";
			Pattern p = Pattern.compile(Regex);
			String[] result = p.split(swrl);
						
			if (result.length != 2) {
				System.out.println("swrl format error!");
				return ;
			}
						
			//SecondType
			cxtObject.setSecondType(SecondType.IndirectData);
						
			//TypeValue
			cxtObject.setTypeValue(result[0].trim());
								
			//arguments
			List<String> arguments = new ArrayList<String>();
			arguments.add(result[1].trim());
			cxtObject.setArguments(arguments);
		}
		else if (swrl.contains(">") || swrl.contains("<") || swrl.contains("=")) {//like (hasChild >= 1)(Jacob)
			
			// 生成一个Pattern,同时编译一个正则表达式 ;用Pattern的split()方法把字符串分割 
			String Regex = "[()]";
			Pattern p = Pattern.compile(Regex);
			String[] result = p.split(swrl);
									
			if (result.length != 4) {
				System.out.println("swrl format error!");
				return ;
			}
									
			//SecondType
			cxtObject.setSecondType(SecondType.IndirectClass);
									
			//TypeValue
			cxtObject.setTypeValue(result[1].trim());
											
			//arguments
			List<String> arguments = new ArrayList<String>();
			arguments.add(result[3].trim());
			cxtObject.setArguments(arguments);
		}
		else {//Person(Jacob)
			// 生成一个Pattern,同时编译一个正则表达式 ;用Pattern的split()方法把字符串分割 
			String Regex = "[()]";
			Pattern p = Pattern.compile(Regex);
			String[] result = p.split(swrl);
									
			if (result.length != 2) {
				System.out.println("swrl format error!");
				return ;
			}
									
			//SecondType
			cxtObject.setSecondType(SecondType.DirectClass);
									
			//TypeValue
			cxtObject.setTypeValue(result[0].trim());
											
			//arguments
			List<String> arguments = new ArrayList<String>();
			arguments.add(result[1].trim());
			cxtObject.setArguments(arguments);
		}
		
	}

	public void parser_P(String swrl) {
	
		// 生成一个Pattern,同时编译一个正则表达式 ;用Pattern的split()方法把字符串分割 
		String Regex = "[(,)]";
		Pattern p = Pattern.compile(Regex); 
		String[] result = p.split(swrl);
		
		if (result.length != 3) {
			System.out.println("swrl format error!");
			return ;
		}
		
		//SecondType
		OWLProperty property = owlModel.getOWLProperty(result[0].trim());
		
		if (property.isObjectProperty()) {
			
			cxtObject.setSecondType(SecondType.ObjectProperty);
		}
		else {
			
			cxtObject.setSecondType(SecondType.DatatypeProperty);
		}
		
		//TypeValue
		cxtObject.setTypeValue(result[0].trim());
		
		//arguments
		List<String> arguments = new ArrayList<String>();
		arguments.add(result[1].trim());
		arguments.add(result[2].trim());
		cxtObject.setArguments(arguments);
	}
	
	public void parser_sameAs(String swrl) {
		
		// 生成一个Pattern,同时编译一个正则表达式 ;用Pattern的split()方法把字符串分割 
		String Regex = "[(,)]";
		Pattern p = Pattern.compile(Regex); 
		String[] result = p.split(swrl);
				
		if (result.length != 3) {
			System.out.println("swrl format error!");
			return ;
		}
				
		//SecondType
		cxtObject.setSecondType(SecondType.sameAs);
		
		//TypeValue
		cxtObject.setTypeValue(result[0].trim());
				
		//arguments
		List<String> arguments = new ArrayList<String>();
		arguments.add(result[1].trim());
		arguments.add(result[2].trim());
		cxtObject.setArguments(arguments);
	}
	
	public void parser_differentFrom(String swrl) {
		
		// 生成一个Pattern,同时编译一个正则表达式 ;用Pattern的split()方法把字符串分割 
		String Regex = "[(,)]";
		Pattern p = Pattern.compile(Regex); 
		String[] result = p.split(swrl);
						
		if (result.length != 3) {
			System.out.println("swrl format error!");
			return ;
		}
						
		//SecondType
		cxtObject.setSecondType(SecondType.differentFrom);
				
		//TypeValue
		cxtObject.setTypeValue(result[0].trim());
						
		//arguments
		List<String> arguments = new ArrayList<String>();
		arguments.add(result[1].trim());
		arguments.add(result[2].trim());
		cxtObject.setArguments(arguments);
	}
	
	public void parser_swrlb(String swrl) {
		
		// 生成一个Pattern,同时编译一个正则表达式 ;用Pattern的split()方法把字符串分割 
		String Regex = "[:(,)]";
		Pattern p = Pattern.compile(Regex); 
		String[] result = p.split(swrl);
								
		if (result.length != 4) {
			System.out.println("swrl format error!");
			return ;
		}
								
		//SecondType
		cxtObject.setSecondType(SecondType.swrlb);
						
		//TypeValue
		cxtObject.setTypeValue(result[1].trim());
								
		//arguments
		List<String> arguments = new ArrayList<String>();
		arguments.add(result[2].trim());
		arguments.add(result[3].trim());
		cxtObject.setArguments(arguments);
	}
	
	public void parser(String swrl) {
		
		System.out.println("welcome to parser...");
		
		cxtObject = new CXTObject();
		
		parserFirstType(swrl);
		
		if (cxtObject.getFirstType().equals(FirstType.C)) parser_C(swrl);
		else if (cxtObject.getFirstType().equals(FirstType.P)) parser_P(swrl);
		else if (cxtObject.getFirstType().equals(FirstType.sameAs)) parser_sameAs(swrl);
		else if (cxtObject.getFirstType().equals(FirstType.differentFrom)) parser_differentFrom(swrl);
		else if (cxtObject.getFirstType().equals(FirstType.swrlb)) parser_swrlb(swrl);
		else {
			System.out.println(swrl + ": Format Error!");
		}
	}
	
	public void print() {
		System.out.println("FirstType: " + cxtObject.getFirstType());
		System.out.println("SecondType: " + cxtObject.getSecondType());
		System.out.println("TypeValue: " + cxtObject.getTypeValue());
		System.out.println("arguments: " + cxtObject.getArguments());
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
		
		OWLObjectProperty hasHobby = owlModel.createOWLObjectProperty("hasHobby");
		hasHobby.setDomain(Person);
		hasHobby.setRange(Course);
		
		OWLDatatypeProperty hasAge = owlModel.createOWLDatatypeProperty("hasAge");
		hasAge.setDomain(Person);
		hasAge.setRange(owlModel.getXSDinteger());
		
		Reasoner reasoner = new Reasoner();
	    ProtegeReasoner pellet = reasoner.createPelletOWLAPIReasoner(owlModel);
	    
		CXTParser cxtParser = new CXTParser(owlModel, pellet);
		
//		String swrl = "Person(Darwin)";
		String swrl = "(hasChild >= 1)(Jacob)";
//		String swrl = "xsd:int(?x)";
//		String swrl = "[3, 4, 5](?x)";
		
//		String swrl = "hasHobby(Darwin, Biology)";
//		String swrl = "hasAge(Darwin, 18)";
		
//		String swrl = "sameAs(Darwin, Jacob)";
//		String swrl = "differentFrom(Darwin, Jacob)";
		
//		String swrl = "swrlb:greaterThan(?age, 17)";
		
		cxtParser.parser(swrl);
		cxtParser.print();
	}
}
