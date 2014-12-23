package edu.pku.dos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

import edu.pku.util.Reasoner;
import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLProperty;

public class DoS {

	//Degree of Match
	private final String EXACT = "exact";
	private final String PLUGIN = "plugIn";
	private final String SUBSUMES = "subsumes";
	private final String FAIL = "fail";
	
	private final int MIN = 0;
	private final int MAX = 10000;
	
	private OWLModel owlModel;
	private ProtegeReasoner reasoner;
	
	private String dom;//Degree of Match
	private int dod;//Degree of Distance
	private double dos;//Degree of Similarity
	
	public DoS(OWLModel owlModel, ProtegeReasoner reasoner) {
		
		this.owlModel = owlModel;
		this.reasoner = reasoner;
	}
	
	public String getDom() {
		
		return dom;
	}
	
	public void setDom(String dom) {
		
		this.dom = dom;
	}
	
	public int getDod() {
		
		return dod;
	}
	
	public void setDod(int dod) {
		
		this.dod = dod;
	}
	
	public double getDos() {
		
		return dos;
	}
	
	public void setDos(double dos) {
		
		this.dos = dos;
	}
	
	//resume x isSubsumedby Y
	public int DFS(OWLNamedClass X, OWLNamedClass Y) {
		
		try {
			
			Collection<OWLClass> Y_ = reasoner.getSubclasses(Y);
			for (Iterator it = Y_.iterator(); it.hasNext(); ) {
				
				OWLNamedClass Z = (OWLNamedClass) it.next();
				System.out.println("Z of Y_: " + Z.getName());
				if (Z.equals(X)) return 1;
			}
		} catch (ProtegeReasonerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			
			Collection<OWLClass> Y_ = reasoner.getSubclasses(Y);
			for (Iterator it = Y_.iterator(); it.hasNext(); ) {
				
				OWLNamedClass Z = (OWLNamedClass) it.next();
				System.out.println("Z of Y_: " + Z.getName());
				return 1 + DFS(X, Z);
			}
		} catch (ProtegeReasonerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
	}
	
	//resume x isSubsumedby Y
	public int DFS_Property(OWLProperty X, OWLProperty Y) {
			
			try {
				
//				Collection<OWLProperty> Y_ = reasoner.getSubProperties(Y);
				Collection<OWLProperty> Y_ = Y.getSubproperties(false);
				for (Iterator it = Y_.iterator(); it.hasNext(); ) {
					
					OWLProperty Z = (OWLProperty) it.next();
					System.out.println("Z of Y_: " + Z.getLocalName());
					if (Z.equals(X)) return 1;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				
//				Collection<OWLProperty> Y_ = reasoner.getSubProperties(Y);
				Collection<OWLProperty> Y_ = Y.getSubproperties(false);
				for (Iterator it = Y_.iterator(); it.hasNext(); ) {
					
					OWLProperty Z = (OWLProperty) it.next();
					System.out.println("Z of Y_: " + Z.getLocalName());
					return 1 + DFS_Property(X, Z);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return -1;
		}
	
	//dos of X --> Y; firstly get the dom, then get the dos including dod;
	public void getDoS(OWLNamedClass X, OWLNamedClass Y) {
		System.out.println("X:"+X.getName() + " Y:" + Y.getName());
		if (X.equals(Y) || X.hasEquivalentClass(Y)) {//X.equals(Y) can check the getDoS(X, X)
			
			dom = EXACT;
			dod = MIN;
			dos = 1;
			return ;
		}
		
		Reasoner reasoner2 = new Reasoner();
	    ProtegeReasoner reasoner = reasoner2.createPelletOWLAPIReasoner(owlModel);
	    this.reasoner = reasoner;
	    
		try {
			System.out.println("reasoner...");
			Collection<OWLClass> X_ = null;
			System.out.println("X_ is not error ???" + X.getLocalName());
			X_ = reasoner.getEquivalentClasses(X);
			System.out.println("X_ is not error !!!");
			if (X_ != null) {
				
				System.out.println("...reasoner...");
				for (Iterator it = X_.iterator(); it.hasNext(); ) {
				
					OWLNamedClass Z = (OWLNamedClass) it.next();
					System.out.println("Z of X_: " + Z.getName());
					if (Z.equals(Y)) {
					
						dom = EXACT;
						dod = MIN;
						dos = 1;
						return ;
					}
				}
			}
			
		} catch (ProtegeReasonerException e) {
			// TODO Auto-generated catch block
			System.out.println("ProtegeReasonerException");
			e.printStackTrace();
			return ;
		}
		
		try {
			
			Collection<OWLClass> Y_ = null;
			Y_ = reasoner.getSubclasses(Y);//han yi qian owlModel de ??
			if (Y_ != null) {//Attention!!!
				for (Iterator it = Y_.iterator(); it.hasNext(); ) {
				
					OWLNamedClass Z = (OWLNamedClass) it.next();
					System.out.println("Z of Y_: " + Z.getName());
					if (Z.equals(X)) {
					
						dom = EXACT;
						dod = 1;
						dos = 1;
						return ;
					}
				}
			}
		} catch (ProtegeReasonerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ;
		}
		
		try {
			System.out.println("X isSubsumed Y ?");
			
			if (reasoner.isSubsumedBy(X, Y)) {
				
				dom = PLUGIN;
				dod = DFS(X, Y);
				dos = 0.5 + 1.0/Math.pow(Math.E, dod - 1);
				return ;
			}
		} catch (ProtegeReasonerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ;
		}
		
		try {
			System.out.println("Y isSubsumed X ?");
			if (reasoner.isSubsumedBy(Y, X)) {
				System.out.println("Y isSubsumed X !");
				dom = SUBSUMES;
				dod = DFS(Y, X);
				dos = 1.0/(2 * Math.pow(Math.E, dod - 1));
				return ;
			}
		} catch (ProtegeReasonerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ;
		}
		
		dom = FAIL;
		dod = MAX;
		dos = 0;
		System.out.println("over!");
	}
	
	//dos of X --> Y; firstly get the dom, then get the dos including dod;
	public void getDoS_Property(OWLProperty X, OWLProperty Y) {
		
		System.out.println("X:"+X.getLocalName() + " Y:" + Y.getLocalName());
		
		System.out.println("begin!!!");
		if (X.equals(Y) || X.getEquivalentProperties().contains(Y)) {//X.equals(Y) can check the getDoS(X, X)
				
			dom = EXACT;
			dod = MIN;
			dos = 1;
			return ;
		}
			
//		Reasoner reasoner2 = new Reasoner();
//		ProtegeReasoner pellet = reasoner2.createPelletOWLAPIReasoner(owlModel);
//		reasoner = pellet;

		System.out.println("111111");
//			try {
//				System.out.println("reasoner...");
//				Collection<OWLClass> X_ = null;
//				System.out.println("X_ is not error ???" + X.getLocalName());
//				X_ = reasoner.getEquivalentClasses(X);
//				System.out.println("X_ is not error !!!");
//				if (X_ != null) {
//					
//					System.out.println("...reasoner...");
//					for (Iterator it = X_.iterator(); it.hasNext(); ) {
//					
//						OWLNamedClass Z = (OWLNamedClass) it.next();
//						System.out.println("Z of X_: " + Z.getName());
//						if (Z.equals(Y)) {
//						
//							dom = EXACT;
//							dod = MIN;
//							dos = 1;
//							return ;
//						}
//					}
//				}
//				
//			} catch (ProtegeReasonerException e) {
//				// TODO Auto-generated catch block
//				System.out.println("ProtegeReasonerException");
//				e.printStackTrace();
//				return ;
//			}
		System.out.println("222222");
		Collection<OWLProperty> Y_ = null;
		
		try {
				
			System.out.println("Y property:" + Y);
			
//			Y_ = reasoner.getSubProperties(Y);//han yi qian owlModel de ??
			Y_ = Y.getSubproperties(false);
			
			System.out.println("???");
			if (Y_ != null) {//Attention!!!
				for (Iterator it = Y_.iterator(); it.hasNext(); ) {
					
					OWLProperty Z = (OWLProperty) it.next();
					System.out.println("Z of Y_: " + Z.getLocalName());
					if (Z.equals(X)) {
						
						dom = EXACT;
						dod = 1;
						dos = 1;
						return ;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (Y_ != null)return ;
		}
		
		System.out.println("333333");	
		
		Collection<OWLProperty> X_ = null;
		
		try {
			System.out.println("X isSubsumed Y ?");
//			X_ = reasoner.getAncestorProperties(X);
			X_ = X.getSuperproperties(true);
			if (X_ != null) {//Attention!!!
				for (Iterator it = X_.iterator(); it.hasNext(); ) {
					
					OWLProperty Z = (OWLProperty) it.next();
					System.out.println("Z of X_: " + Z.getLocalName());
					if (Z.equals(Y)) {
						
						dom = PLUGIN;
						dod = DFS_Property(X, Y);
						dos = 0.5 + 1.0/Math.pow(Math.E, dod - 1);
						return ;
					}
				}
			}
//				if (reasoner.isSubsumedBy(X, Y)) {
//					
//					dom = PLUGIN;
//					dod = DFS(X, Y);
//					dos = 0.5 + 1.0/Math.pow(Math.E, dod - 1);
//					return ;
//				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (X_ != null)return ;
		}
		System.out.println("444444");
		
		Y_ = null;
		
		try {
			System.out.println("Y isSubsumed X ?");

//			Y_ = reasoner.getAncestorProperties(Y);
			Y_ = Y.getSuperproperties(true);
			if (Y_ != null) {//Attention!!!
				for (Iterator it = Y_.iterator(); it.hasNext(); ) {
					
					OWLProperty Z = (OWLProperty) it.next();
					System.out.println("Z of Y_: " + Z.getLocalName());
					if (Z.equals(X)) {
						System.out.println("Y isSubsumed X !");
						dom = SUBSUMES;
						dod = DFS_Property(Y, X);
						dos = 1.0/(2 * Math.pow(Math.E, dod - 1));
						return ;
					}
				}
			}
//				if (reasoner.isSubsumedBy(Y, X)) {
//					System.out.println("Y isSubsumed X !");
//					dom = SUBSUMES;
//					dod = DFS(Y, X);
//					dos = 1.0/(2 * Math.pow(Math.E, dod - 1));
//					return ;
//				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (Y_ != null)return ;
		}
		System.out.println("555555");	
		dom = FAIL;
		dod = MAX;
		dos = 0;
		System.out.println("over!");
	}
	
	//Maybe need to modify...
	public void getDoS(String x, String y) {
		
		OWLNamedClass X = owlModel.getOWLNamedClass(x);
		OWLNamedClass Y = owlModel.getOWLNamedClass(y);
		getDoS(X, Y);
	}
	
	//Maybe need to modify...
	public void getDoS_Property(String x, String y) {
			
		OWLProperty X = owlModel.getOWLProperty(x);
		OWLProperty Y = owlModel.getOWLProperty(y);
		getDoS_Property(X, Y);
	}	
	
	public void getDoSAll(String x, String y) {
		
		System.out.println("getDoSAll----of " + x + " & " + y);
		if (x == null || y == null) {
			System.out.println("X or Y is null");
			dom = FAIL;
			dod = MAX;
			dos = 0;
			return ;
		}
		// 生成一个Pattern,同时编译一个正则表达式 ;用Pattern的split()方法把字符串分割 
		String Regex = "[,]";
		Pattern p = Pattern.compile(Regex);
		
		String[] result_x = p.split(x);
		String[] result_y = p.split(y);
		
		String dom_ = FAIL;
		int dod_ = MAX;
	    double dos_;
	    
	    String domAll = FAIL;
	    int dodAll = MAX;
	    double dosAll = 0.0;
		
		for (int i = 0; i < result_x.length; i++) {
			OWLNamedClass X = owlModel.getOWLNamedClass(result_x[i].trim());
			dos_ = 0.0;
			for (int j = 0; j < result_y.length; j++) {
				System.out.println(result_x[i].trim() + " " + result_y[j].trim());
				OWLNamedClass Y = owlModel.getOWLNamedClass(result_y[j].trim());
				System.out.println("getDoS: X:" + X.getName() + " Y:" + Y.getName());
				getDoS(X, Y);
				System.out.println("-- " + X.getName() + ", " + Y.getName());
				print();
				if (dos_ < dos) {//all right???
					dom_ = dom;
					dod_ = dod;
					dos_ = dos;
					System.out.println("dom_:" + dom_ + "   dod_:" + dod_ + "   dos_:" + dos_);
				}
			}
			domAll = dom_;
			dodAll = dod_;
			dosAll += dos_;
		}
		dom = domAll;
		dod = dodAll;
		dos = dosAll/result_x.length;
		print();
	}
	
	public void print() {
		
		System.out.println("dom:" + dom + "   dod:" + dod + "   dos:" + dos);
	}
	
	public static void main(String [] args) {
		
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
//	    //创建Person类
//	    OWLNamedClass Person = owlModel.createOWLNamedClass("Person");
//	    OWLNamedClass Man = owlModel.createOWLNamedClass("Man");
//	    OWLNamedClass Woman = owlModel.createOWLNamedClass("Woman");
//	    OWLNamedClass Father = owlModel.createOWLNamedClass("Father");
//	    OWLNamedClass GrandFather = owlModel.createOWLNamedClass("GrandFather");
//	    
//	    
//	    Man.addSuperclass(Person);
//	    Woman.addSuperclass(Person);
//	    Father.addSuperclass(Man);
//	    GrandFather.addSuperclass(Father);
//	    
	    Reasoner reasoner = new Reasoner();
	    ProtegeReasoner pellet = reasoner.createPelletOWLAPIReasoner(owlModel);
	    
	    DoS dos = new DoS(owlModel, pellet);
//	    dos.getDoS(Person, Person);//dom:exact   dod:0   dos:1.0
//	    dos.getDoS(Man, Person);//dom:exact   dod:1   dos:1.0
//	    dos.getDoS(Father, Person);//dom:plugIn   dod:2   dos:0.8678794411714423
//	    dos.getDoS(Person, Man);//dom:subsumes   dod:1   dos:0.5
//	    dos.getDoS(Person, Father);//dom:subsumes   dod:2   dos:0.18393972058572117
//	    dos.getDoS(Woman, Father);//dom:fail   dod:10000   dos:0.0
//	    dos.print();
	    
//	    dos.getDoS(Woman, Father);//dom:fail   dod:10000   dos:0.0
//	    dos.print();
	    
//	    dos.getDoS(Person, Father);
//	    dos.print();
	    
	    dos.getDoSAll("Transport", "Aircraft");
	    dos.print();
	}
}
