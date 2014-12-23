package edu.pku.contextConditon;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import edu.pku.context.model.Bool;
import edu.pku.context.model.CXTObject;
import edu.pku.context.model.CXTParser;
import edu.pku.context.model.Bool.BoolType;
import edu.pku.context.model.CXTObject.FirstType;
import edu.pku.context.model.CXTObject.SecondType;
import edu.pku.contextEffect.CXTEffect;
import edu.pku.util.Reasoner;
import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParseException;

public class CXTCondition2 {

	private OWLModel owlModel;
	private ProtegeReasoner reasoner;
	
	private CXTParser cxtParser;
	
	private Map map;
	private String REASONTRUE = "ReasonTrue";
	private OWLNamedClass ReasonTrue;
	private int numOfReasoning;
	
	public CXTCondition2(OWLModel owlModel, ProtegeReasoner reasoner) {
		
		this.owlModel = owlModel;
		this.reasoner = reasoner;
	}
	
	public String getVariable(String cls) {
		
		if (map.containsKey(cls)) {
			return (String)map.get(cls);
		}
//		for (Iterator it = map.keySet().iterator(); it.hasNext(); ) {
//			String key = (String) it.next();
//			System.out.println("key:" + key);
//			if (map.containsKey(cls)) {
//				return (String)map.get(key);
//			}
//		}
		
		System.out.println("map.size:" + map.size());
		
		String var = "?x" + map.size();
		map.put(cls, var);
		
		System.out.println("  map.size:" + map.size());
		
		return var;
	}
	
	public void clearIndividuals() {
		
		System.out.println("   clear the individuals of Class:" + REASONTRUE);
		
		OWLNamedClass ReasonTrue_ = owlModel.getOWLNamedClass(REASONTRUE);
		
		System.out.println(" before delete:" + ReasonTrue_.getLocalName());
		System.out.println("delete!");
		
		ReasonTrue_.delete();
		
		System.out.println(" after delete:" + ReasonTrue_.getLocalName());
		
		/**
		 * Attention!!
		 */
		
		ReasonTrue = null;
		System.out.println(" before create:" + ReasonTrue);
		ReasonTrue = owlModel.createOWLNamedClass(REASONTRUE);
		
		if (ReasonTrue == null) {
			System.out.println("   ReasonTrue is null...clear");
		}
		
		System.out.println("create!");
		System.out.println(" after create:" + ReasonTrue.getLocalName());
		System.out.println("ReasonTrue:" + ReasonTrue.getLocalName());
		
		Reasoner reasoner2 = new Reasoner();
	    ProtegeReasoner pellet = reasoner2.createPelletOWLAPIReasoner(owlModel);
	    reasoner = pellet;
	    
	    System.out.println("modify reasoner!");
	    
	    Collection<OWLIndividual> instances = null;
		try {
			
			instances = reasoner.getIndividualsBelongingToClass(ReasonTrue);
			
			System.out.println("??");
			if (instances != null) {
				System.out.println("   error!");
			}
		} catch (ProtegeReasonerException e) {
			// TODO Auto-generated catch block
			System.out.println("!!");
			e.printStackTrace();
		}
		catch (Exception e) {
			System.out.println("wrong!_clear");
			e.printStackTrace();
		}
		System.out.println("reasoner over");
		System.out.println("instances:" + instances);
		
		System.out.println("   success clear the individuals of Class:" + REASONTRUE);
	}
	
	public Bool exec_P_Args(CXTObject cxtObject) {
		
		Bool bool = new Bool();
		
		REASONTRUE = REASONTRUE + numOfReasoning;
		numOfReasoning = numOfReasoning + 1;
		
		ReasonTrue = null;
		ReasonTrue = owlModel.createOWLNamedClass(REASONTRUE);
		if (ReasonTrue == null) {
			System.out.println("   create the class: " + REASONTRUE + " error!");
		}
		
		String swrlImp_p = "";
		String swrlImp_c = REASONTRUE;
		
		if (cxtObject.getSecondType().equals(SecondType.ObjectProperty)) {
			swrlImp_p = swrlImp_p + cxtObject.getArguments().get(0) + "(" + getVariable(cxtObject.getArguments().get(0)) + ") ^ ";
			swrlImp_p = swrlImp_p + cxtObject.getArguments().get(1) + "(" + getVariable(cxtObject.getArguments().get(1)) + ")";
			
			System.out.println("swrlImp_p:" + swrlImp_p);
			
			for (int i = 0; i < map.size(); i++) {
				String str = "?x" + i;
				if (swrlImp_p.contains(str)) {
					swrlImp_c = swrlImp_c + "(" + str + ")";
					System.out.println("swrlImp_c:" + swrlImp_c);
					break;
				}
			}
			
			SWRLFactory factory = new SWRLFactory(owlModel);
//			SWRLImp imp = factory.createImp("IsAdult-Rule", "Person(?p) ^ Person(?p) ^ hasAge(?p, ?age) ^ swrlb:greaterThan(?age, 17) -> Adult(?p)");
			try {
				System.out.println("swrl:" + swrlImp_p + " -> " + swrlImp_c);
				SWRLImp imp = factory.createImp(swrlImp_p + " -> " + swrlImp_c);
			} catch (SWRLParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ReasonTrue = null;
			ReasonTrue = owlModel.getOWLNamedClass(REASONTRUE);
			if (ReasonTrue == null) {
				System.out.println("   ReasonTrue is null...Args");
			}
			
			/**
			 * important!!!
			 */
			Reasoner reasoner2 = new Reasoner();
		    ProtegeReasoner pellet = reasoner2.createPelletOWLAPIReasoner(owlModel);
		    reasoner = pellet;
		    System.out.println("getIndividuals_Args...");
			
			Collection<OWLIndividual> instances = null;
			try {
				instances = reasoner.getIndividualsBelongingToClass(ReasonTrue);
			} catch (ProtegeReasonerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (Exception e) {
				System.out.println("wrong! Args");
			}
			
			System.out.println("instances:" + instances);
			
			if (instances == null || instances.isEmpty()) {
				bool.setBoolType(BoolType.Unknown);
				System.out.println(swrlImp_p + " -> " + swrlImp_c + ": Unknown(Args)");
				
				
				return bool;
			}
			else {
				bool.setBoolType(BoolType.True);
				System.out.println(swrlImp_p + " -> " + swrlImp_c + ": True(Args)");
				/**
				 * important!!!
				 */
				//clearIndividuals();
				return bool;
			}
		}
		else {
			
		}
		return bool;
	}
	
	public Bool exec_P_All(CXTObject cxtObject) {
		
		Bool bool_ = new Bool();
		Bool bool = new Bool();
		
		bool_ = exec_P_Args(cxtObject);
		if (bool_.getBoolType() == BoolType.Unknown) {
			bool.setBoolType(BoolType.Unknown);
			System.out.println(": Unknown(All)");
			return bool;
		}
//		else if (bool_.getBoolType() == BoolType.False) {
//			bool.setBoolType(BoolType.False);
//			System.out.println(": False(All)");
//			return bool;
//		}
		
		REASONTRUE = REASONTRUE + numOfReasoning;
		numOfReasoning = numOfReasoning + 1;
		
		ReasonTrue = null;
		ReasonTrue = owlModel.createOWLNamedClass(REASONTRUE);
		if (ReasonTrue == null) {
			System.out.println("   create the class: " + REASONTRUE + " error!");
		}
		
		String swrlImp_p = "";
		String swrlImp_c = REASONTRUE;
		
		if (cxtObject.getSecondType().equals(SecondType.ObjectProperty)) {
			swrlImp_p = swrlImp_p + cxtObject.getArguments().get(0) + "(" + getVariable(cxtObject.getArguments().get(0)) + ") ^ ";
			swrlImp_p = swrlImp_p + cxtObject.getArguments().get(1) + "(" + getVariable(cxtObject.getArguments().get(1)) + ") ^ ";
			
			swrlImp_p = swrlImp_p + cxtObject.getTypeValue() + "(" + getVariable(cxtObject.getArguments().get(0)) + ", " + getVariable(cxtObject.getArguments().get(1)) + ")";
			System.out.println("swrlImp_p:" + swrlImp_p);
			
			for (int i = 0; i < map.size(); i++) {
				String str = "?x" + i;
				if (swrlImp_p.contains(str)) {
					swrlImp_c = swrlImp_c + "(" + str + ")";
					System.out.println("swrlImp_c:" + swrlImp_c);
					break;
				}
			}
			
			SWRLFactory factory = new SWRLFactory(owlModel);
//			SWRLImp imp = factory.createImp("IsAdult-Rule", "Person(?p) ^ Person(?p) ^ hasAge(?p, ?age) ^ swrlb:greaterThan(?age, 17) -> Adult(?p)");
			try {
				System.out.println("swrl:" + swrlImp_p + " -> " + swrlImp_c);
				SWRLImp imp = factory.createImp(swrlImp_p + " -> " + swrlImp_c);
			} catch (SWRLParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			ReasonTrue = null;
			ReasonTrue = owlModel.getOWLNamedClass(REASONTRUE);
			if (ReasonTrue == null) {
				System.out.println("    ReasonTrue is null...All");
			}
			
			/**
			 * important!!!
			 */
			Reasoner reasoner2 = new Reasoner();
		    ProtegeReasoner pellet = reasoner2.createPelletOWLAPIReasoner(owlModel);
		    reasoner = pellet;
//			reasoner.setOWLModel(owlModel);
		    System.out.println("getIndividuals_All...");
			
			Collection<OWLIndividual> instances = null;
			try {
				instances = reasoner.getIndividualsBelongingToClass(ReasonTrue);
			} catch (ProtegeReasonerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (Exception e) {
				System.out.println("wrong! All");
			}
			
			System.out.println("instances:" + instances);
			
			if (instances == null || instances.isEmpty()) {
				bool.setBoolType(BoolType.False);
				System.out.println(swrlImp_p + " -> " + swrlImp_c + ": False(All)");
				return bool;
			}
			else {
				bool.setBoolType(BoolType.True);
				System.out.println(swrlImp_p + " -> " + swrlImp_c + ": True(All)");
				/**
				 * important!!!
				 */
				//clearIndividuals();
				return bool;
			}
		}
		else {//
			
		}
		return bool;
	}
	
	public Bool exec(String swrl) {
		
		//parser
		cxtParser = new CXTParser(owlModel, reasoner);
		cxtParser.parser(swrl);
		cxtParser.print();
		
		return exec_P_All(cxtParser.getCXTObject());
		
		//Exec
//		if (cxtParser.getCXTObject().getFirstType().equals(FirstType.C)) return exec_C(cxtParser.getCXTObject());
//		else if (cxtParser.getCXTObject().getFirstType().equals(FirstType.P)) return exec_P(cxtParser.getCXTObject());
//		else if (cxtParser.getCXTObject().getFirstType().equals(FirstType.sameAs)) return exec_sameAs(cxtParser.getCXTObject());
//		else if (cxtParser.getCXTObject().getFirstType().equals(FirstType.differentFrom)) return exec_differentFrom(cxtParser.getCXTObject());
//		else if (cxtParser.getCXTObject().getFirstType().equals(FirstType.swrlb)) return exec_swrlb(cxtParser.getCXTObject());
		
//		System.out.println("exec: swrl format unknonw!");
		//return false;
//		Bool bool = new Bool();
//		bool.setBoolType(BoolType.Unknown);
//		return bool;
	}
	
	public Bool execAllBoolean(String swrl) {
		
		map = new HashMap();
		numOfReasoning = 0;
		//ReasonTrue = owlModel.createOWLNamedClass(REASONTRUE);
		
		// 生成一个Pattern,同时编译一个正则表达式 ;用Pattern的split()方法把字符串分割 
		String Regex = "[\\^]";
		Pattern p = Pattern.compile(Regex);
		String[] result = p.split(swrl);
		
		boolean flag = true;

		for (int i = 0; i < result.length; i++) {
			System.out.println("Test " + (i+1) + ": " + result[i].trim());
			Bool bool_ = exec(result[i].trim());
			if (bool_.getBoolType() == BoolType.False) {
				System.out.println("---exec " + result[i].trim() + " false!");
				//return false;
				Bool bool = new Bool();
				bool.setBoolType(BoolType.False);
				return bool;
			}
			else if (bool_.getBoolType() == BoolType.Unknown) {
				System.out.println("---exec " + result[i].trim() + " unknown!");
				//return false;
//				Bool bool = new Bool();
//				bool.setBoolType(BoolType.Unknown);
//				return bool;
				flag = false;
			}
		}
		if (flag) {
		
			System.out.println("---execAll " + swrl.trim() + " true!");
			//return true;
			Bool bool = new Bool();
			bool.setBoolType(BoolType.True);
			return bool;
		}
		else {
			System.out.println("---execAll " + swrl.trim() + " unknown!");
			//return true;
			Bool bool = new Bool();
			bool.setBoolType(BoolType.Unknown);
			return bool;
		}
	}
	
	public double execAllDouble(String swrl) {
		
		map = new HashMap();
		numOfReasoning = 0;
		//ReasonTrue = owlModel.createOWLNamedClass(REASONTRUE);
		
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
	    
	    //Goods(goods)
	    String swrl_ = "Female(jacob) ^ hasIDCard(jacob, interimIDCard) ^ hasTicket(jacob, firstClass_Ticket) ^ hasGoods(jacob, goods) ^ hasWeight(goods, w_high)";
	    
	    CXTEffect cxtEffect = new CXTEffect(owlModel, pellet);
	    cxtEffect.execAll(swrl_);
	    
	    //important!!!
	    pellet = reasoner.createPelletOWLAPIReasoner(owlModel);
	    
	    String swrl = "hasIDCard(Person, IDCard) ^ hasGoods(Person, Goods)";
//	    String swrl = "hasIDCard(Person, IDCard) ^ hasGoods(Client, Goods)";
	    
	    CXTCondition2 cxtCondition = new CXTCondition2(owlModel, pellet);
	    System.out.println(cxtCondition.execAllDouble(swrl));
	}
}
