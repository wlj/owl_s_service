package edu.pku.func;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import edu.pku.context.model.OWL_SE;
import edu.pku.context.model.QoS;
import edu.pku.context.model.ReturnPoint;
import edu.pku.contextConditon.CXTCondition;
import edu.pku.contextConditon.CXTCondition2;
import edu.pku.contextEffect.CXTEffect;
import edu.pku.contextRecall.CXTKBCopy;
import edu.pku.contextRecall.CXTKBRecall;
import edu.pku.dos.DoS;
import edu.pku.util.Most;
import edu.pku.util.OWL_SE_FileParser;
import edu.pku.util.Reasoner;
import edu.pku.util.Test;
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

public class ServiceSelection {
	
	private OWLModel owlModel;
	private ProtegeReasoner reasoner;
	
	//Degree of Similarity
	private DoS dos;
	//Context
	private CXTCondition2 cxtCondition;/*yes!!!*/
	private CXTEffect cxtEffect;
	private CXTKBRecall cxtKBRecall;
	//Quality of Service
	
	private Map map;
	
	public ServiceSelection(OWLModel owlModel, ProtegeReasoner reasoner) {
		
		this.owlModel = owlModel;
		this.reasoner = reasoner;
	}
	
	public void init(List<OWL_SE> services) {
		
		for (int i = 0; i < services.size(); i++) {
			
			services.get(i).setGrade(0.0);
		}
		System.out.println("init the grade of all services be 0.0...");
	}
	
	public void init() {
		
		map = new HashMap();
		//Usability, Reliability, Security, Satisfaction, Price, Time
		map.put("Usability", new Most());
		map.put("Reliability", new Most());
		map.put("Security", new Most());
		map.put("Satisfaction", new Most());
		map.put("Price", new Most());
		map.put("Time", new Most());
		
	}
	
	public String getString(String str) {
		System.out.println("str: " + str);
		String Regex = "[#]";
		Pattern p = Pattern.compile(Regex);
		String[] result = p.split(str);
		System.out.println("length: " + result.length);
		return result[1];
	}

	//ProfileInfo-->OWL_SE:::wait for YangWei
	public String select(OWL_SE task, List<OWL_SE> services, List<OWL_SE> taskNext, List<List<OWL_SE>> servicesNext) {
		/**
		 * get the keySet...
		 */
//		String tmp = "service";
//		Test test = new Test();
//		if (tmp.equals("service"))return test.list(task);
//		
		System.out.println("");
		System.out.println("*****welcom to select of ServiceSelection***********");
		System.out.println("");
		
		init(services);
		
		//init map for the min, max;
		init();
		
		for (int i = 0; i < services.size(); i++) {
			
			services.get(i).feedBack();
			
			Map QoSMap = services.get(i).getMap();
			/**
			 * get the keySet...
			 */
//			String tmp = "service";
//			Test test = new Test();
//			if (tmp.equals("service"))return test.list(task);
			for (Iterator it = QoSMap.keySet().iterator(); it.hasNext(); ) {
				
				String key = (String) it.next();
				System.out.println("key-: " + key);
				QoS qos = (QoS) QoSMap.get(key);
				Most most = (Most) map.get(key);
				if (qos.judge(most.getMin(), qos.getPointAll()) > 0) {
					most.setMin(qos.getPointAll());
				}
				if (qos.judge(most.getMax(), qos.getPointAll()) < 0) {
					most.setMax(qos.getPointAll());
				}
				map.put(key, most);//modify--important!!!
				System.out.println("key*: " + key + "Most-min: " + most.getMin() + " max: " + most.getMax());
			}
		}
		
		dos = new DoS(owlModel, reasoner);
		System.out.println("    effect");
		//modify the context for user...
		cxtEffect = new CXTEffect(owlModel, reasoner);
		cxtEffect.execAll(task.getContext());
		
		//important!
		Reasoner reasoner = new Reasoner();
	    ProtegeReasoner pellet = reasoner.createPelletOWLAPIReasoner(owlModel);
	    
		System.out.println("   condition");
		cxtCondition = new CXTCondition2(owlModel, pellet);
		
		for (int i = 0; i < services.size(); i++) {
			/**
			 * dos must be improved...
			 */
			//Category
			System.out.println("Key: " + services.get(i).getKey());
			System.out.println("C: " + services.get(i).getCategory());
			/**
			 * Attention please!!! must transfer "http://www.owl-ontologies.com/Ontology1364019654.owl#Airplane" to Airplane
			 */
			
			dos.getDoSAll(task.getCategory(), services.get(i).getCategory());
//			dos.getDoSAll("Transport", "Airplane");
			System.out.println("****");
			services.get(i).addGrade(dos.getDos());
			
			//Input
			dos.getDoSAll(task.getInput(), services.get(i).getInput());
			services.get(i).addGrade(dos.getDos());
			
			//Output
			dos.getDoSAll(services.get(i).getOutput(), task.getOutput());
			services.get(i).addGrade(dos.getDos());
			
			//Precondition--check for the service..
//			if (cxtCondition.execAllBoolean(services.get(i).getPreconditon())) {//we can do it better!!!
//				services.get(i).addGrade(0.5);
//			}
//			else {
//				services.get(i).addGrade(0.0);
//			}
			services.get(i).addGrade(cxtCondition.execAllDouble(services.get(i).getPrecondition()));
			
			//ContextRule--check for the service
//			if (cxtCondition.execAllBoolean(services.get(i).getContextRule())) {//we can do it better!!!
//				services.get(i).addGrade(1.0);
//			}
//			else {
//				services.get(i).addGrade(0.0);
//			}
			services.get(i).addGrade(cxtCondition.execAllDouble(services.get(i).getContextRule()));
			
			//O--I, E--P
			int numOfTaskAndServicesNext = 0;
			double OIOfTaskAndServicesNext = 0.0;
			double EPOfTaskAndServicesNext = 0.0;
			
			//context knowledge base recall
			
			////cxtKBRecall = new CXTKBRecall(new CXTKBCopy(owlModel));
			//copy the cxt kb
			
			////cxtKBRecall.update();
			
			//exec the effect of the candidate service
			
			////cxtEffect.execAll(services.get(i).getEffect());
			
			numOfTaskAndServicesNext += taskNext.size();
			
			System.out.println("   begin O_I");
			
			for (int j = 0; j < taskNext.size(); j++) {
				
				System.out.println("taskNext.size:" + taskNext.size() + " j:" + j);
				System.out.println("  "  + taskNext.get(j).getInput() + " " + services.get(i).getOutput());
				
				dos.getDoSAll(taskNext.get(j).getInput(), services.get(i).getOutput());
				OIOfTaskAndServicesNext += dos.getDos();
				
				System.out.println("   after dosAll, begin execAllDouble");
				
				EPOfTaskAndServicesNext += cxtCondition.execAllDouble(taskNext.get(j).getPrecondition());
			}
			
			System.out.println("   begin E_P");
			
			for (int j = 0; j < servicesNext.size(); j++) {
				
				System.out.println("servicesNext.size:" + servicesNext.size() + " j:" + j);
				
				numOfTaskAndServicesNext += servicesNext.size();
				
				for (int k = 0; k < servicesNext.get(j).size(); k++) {
					
					System.out.println("servicesNext.get(j).size:" + servicesNext.get(j).size() + " k:" + k);
					System.out.println("  "  + servicesNext.get(j).get(k).getInput() + " " + services.get(i).getOutput());
					dos.getDoSAll(servicesNext.get(j).get(k).getInput(), services.get(i).getOutput());
					OIOfTaskAndServicesNext += dos.getDos();
					
					System.out.println("after dosAll, begin execAllDouble");
					
					EPOfTaskAndServicesNext += cxtCondition.execAllDouble(servicesNext.get(j).get(k).getPrecondition());
				}
			}
			
			System.out.println("   cxtKBRecall.recall");
			
			//recall to the prior cxt kb
			
			////owlModel = cxtKBRecall.recall();
			
			//add the grade of O--I
			services.get(i).addGrade(OIOfTaskAndServicesNext/numOfTaskAndServicesNext);
			//add the grade of E--P
			services.get(i).addGrade(EPOfTaskAndServicesNext/numOfTaskAndServicesNext);
			
			System.out.println("   QoS");
			
			//QoS
			double pointQoS = 0.0;
			for (Iterator it = map.keySet().iterator(); it.hasNext(); ) {
				
				String key = (String) it.next();
				Most most = (Most) map.get(key);
				services.get(i).standardize(key, most.getMin(), most.getMax());
				QoS qosTask = (QoS) task.getMap().get(key);
				QoS qosService = (QoS) services.get(i).getMap().get(key);
				pointQoS += qosTask.getWeight() * qosService.getPointAll();
			}
			//add the grade of QoS
			services.get(i).addGrade(pointQoS);
		}
		
		System.out.println("   sort!");
		
		System.out.println("Service1_1:" + services.get(0).getGrade());
		System.out.println("Service1_2:" + services.get(1).getGrade());
		System.out.println("Service1_3:" + services.get(2).getGrade());
		
		Collections.sort(services, new Comparator<OWL_SE>() {
			public int compare(OWL_SE arg0, OWL_SE arg1){
				/**
				 * attention!!!
				 */
				return String.valueOf(arg1.getGrade()).compareTo(String.valueOf(arg0.getGrade()));
			}
		});
		
		System.out.println("0:" + services.get(0).getGrade());
		System.out.println("1:" + services.get(1).getGrade());
		System.out.println("2:" + services.get(2).getGrade());
		
		return services.get(0).getKey();//identify...not address..
	}
	
	public OWL_SE getOWL_SE_task(String filePath) {
		
		OWL_SE_FileParser owl_se_FileParse = new OWL_SE_FileParser(filePath);
		owl_se_FileParse.parseAll();
		
		OWL_SE task = new OWL_SE();
		
		task.setKey(owl_se_FileParse.getKey());
		task.setCategory(owl_se_FileParse.getCategory());
		task.setInput(owl_se_FileParse.getInput());
		task.setOutput(owl_se_FileParse.getOutput());
		task.setPrecondition(owl_se_FileParse.getPrecondition());
		task.setEffect(owl_se_FileParse.getEffect());
		task.setContext(owl_se_FileParse.getContext());
		task.setGrade(0.0);
		
//		(Name:Usability, Weight:0.1) ^ (Name:Reliability, Weight:0.1) ^ (Name:Security, Weight:0.2) ^
//				(Name:Satisfaction, Weight:0.2) ^ (Name:Price, Weight:0) ^ (Name:Time, Weight:0.4)
		
		// 生成一个Pattern,同时编译一个正则表达式 ;用Pattern的split()方法把字符串分割 
		String Regex = "[\\^]";
		Pattern p = Pattern.compile(Regex);
		String[] result = p.split(owl_se_FileParse.getQoS());
		
		if (result.length != 6) {
			System.out.println("QoS format error!");
			return task;
		}
		
		Map map = new HashMap();
		
		for (int i = 0; i < result.length; i++) {
			String Regex_ = "[():,]";
			p = Pattern.compile(Regex_);
			String[] tmp = p.split(result[i].trim());
			if (tmp.length != 5) {
				System.out.println("QoS format error!!!");
				return task;
			}
//			System.out.println("2: " + tmp[2].trim());
//			System.out.println("4: " + tmp[4].trim());
			QoS qos = new QoS();
			qos.setName(tmp[2].trim());
			qos.setWeight(Double.parseDouble(tmp[4].trim()));
			map.put(tmp[2].trim(), qos);
		}
		
		task.setMap(map);
		
		return task;
	}
	
	public OWL_SE getOWL_SE_service(String filePath) {
		
		OWL_SE_FileParser owl_se_FileParse = new OWL_SE_FileParser(filePath);
		owl_se_FileParse.parseAll();
		
		OWL_SE service = new OWL_SE();
		
		service.setKey(owl_se_FileParse.getKey());
		service.setCategory(owl_se_FileParse.getCategory());
		service.setInput(owl_se_FileParse.getInput());
		service.setOutput(owl_se_FileParse.getOutput());
		service.setPrecondition(owl_se_FileParse.getPrecondition());
		service.setEffect(owl_se_FileParse.getEffect());
		service.setContextRule(owl_se_FileParse.getContextRule());
		
		service.setGrade(0.0);
		
//		(Name:Usability, Type:0, PointOfSupply:0.5,
//				PointOfValuation:0.5, PointOfReturn<hasTotalPoint:
//				0.5, hasTimes:1, hasCurrentPoint:0.5>)  ^

		
		// 生成一个Pattern,同时编译一个正则表达式 ;用Pattern的split()方法把字符串分割 
		String Regex = "[\\^]";
		Pattern p = Pattern.compile(Regex);
		String[] result = p.split(owl_se_FileParse.getQoS());
		
		if (result.length != 6) {
			System.out.println("QoS format error!");
			return service;
		}
		
		Map map = new HashMap();
		
		for (int i = 0; i < result.length; i++) {
			String Regex_ = "[():,{}]";
			p = Pattern.compile(Regex_);
			String[] tmp = p.split(result[i].trim());
			if (tmp.length != 16 & tmp.length != 17) {
				System.out.println(tmp.length);
				System.out.println("QoS format error!!!");
				return service;
			}
//			System.out.println("1: " + tmp[1].trim());
//			System.out.println("2: " + tmp[2].trim());
			QoS qos = new QoS();
			qos.setName(tmp[2].trim());
			qos.setType(Boolean.parseBoolean(tmp[4].trim()));
			qos.setPointOfSupply(Double.parseDouble(tmp[6].trim()));
			qos.setPointOfValuation(Double.parseDouble(tmp[8].trim()));
			
			ReturnPoint returnPoint = new ReturnPoint();
			returnPoint.setTotalPoint(Double.parseDouble(tmp[11].trim()));
			returnPoint.setTimes(Integer.parseInt(tmp[13].trim()));
			returnPoint.setCurrentPoint(Double.parseDouble(tmp[15].trim()));
			qos.setReturnPoint(returnPoint);
			
			map.put(tmp[2].trim(), qos);
		}
		
		service.setMap(map);
		
		return service;
	}
	
	public void print_task(OWL_SE task) {
		System.out.println("Key: " + task.getKey());
		System.out.println("C: " + task.getCategory());
		System.out.println("I: " + task.getInput());
		System.out.println("O: " + task.getOutput());
		System.out.println("P: " + task.getPrecondition());
		System.out.println("E: " + task.getEffect());
		System.out.println("Context: " + task.getContext());
		System.out.println("QoS: ");
		Map map = task.getMap();
		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			String str = (String) it.next();
			QoS qos = (QoS) map.get(str);
			System.out.println("Name: " + qos.getName() + " || Weight: " + qos.getWeight());
		}
	}
	
	public void print_service(OWL_SE service) {
		System.out.println("Key: " + service.getKey());
		System.out.println("C: " + service.getCategory());
		System.out.println("I: " + service.getInput());
		System.out.println("O: " + service.getOutput());
		System.out.println("P: " + service.getPrecondition());
		System.out.println("E: " + service.getEffect());
		System.out.println("ContextRule: " + service.getContextRule());
		System.out.println("QoS: ");
		Map map = service.getMap();
		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			String str = (String) it.next();
			QoS qos = (QoS) map.get(str);
			System.out.println("Name: " + qos.getName());
			System.out.println("Type: " + qos.getType());
			System.out.println("PointOfSupply: " + qos.getPointOfSupply());
			System.out.println("PointOfValuation: " + qos.getPointOfValuation());
			ReturnPoint returnPoint = qos.getReturnPoint();
			
			System.out.println("hasTotalPoint: " + returnPoint.getTotalPoint());
			System.out.println("hasTimes: " + returnPoint.getTimes());
			System.out.println("hasCurrentPoint: " + returnPoint.getCurrentPoint());
		}
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
		
//		OWLNamedClass Transport = owlModel.getOWLNamedClass("Transport");
//		System.out.println(Transport.getLocalName());
//		
//		OWLNamedClass Airplane = owlModel.getOWLNamedClass("Airplane");
//		System.out.println(Airplane.getLocalName());
		
//		System.out.println("move on");
//		
//		OWLNamedClass Person = owlModel.createOWLNamedClass("Person");
//		System.out.println("move on");
//		OWLNamedClass Man = owlModel.createOWLNamedClass("Man");
//		OWLNamedClass Female = owlModel.createOWLNamedClass("Female");
		
//		OWLNamedClass Course = owlModel.createOWLNamedClass("Course");
//		OWLNamedClass IDCard = owlModel.createOWLNamedClass("IDCard");
//		OWLNamedClass Ticket = owlModel.createOWLNamedClass("Ticket");
//		OWLNamedClass PTicket = owlModel.createOWLNamedClass("PTicket");
//		OWLNamedClass Weather = owlModel.createOWLNamedClass("Weather");
//		OWLNamedClass Flow = owlModel.createOWLNamedClass("Flow");
//		OWLNamedClass Goods = owlModel.createOWLNamedClass("Goods");
		
//		PTicket.addSuperclass(Ticket);
//		Man.addSuperclass(Person);
		
//		OWLIndividual Darwin = Person.createOWLIndividual("Darwin");
//		OWLIndividual Biology = Course.createOWLIndividual("Biology");
//		OWLIndividual LittleDarwin = Person.createOWLIndividual("LittleDarwin");
		
//		OWLObjectProperty hasHobby = owlModel.createOWLObjectProperty("hasHobby");
//		hasHobby.setDomain(Person);
//		hasHobby.setRange(Course);
		
//		Darwin.setPropertyValue(hasHobby, Biology);
		
//		OWLDatatypeProperty hasAge = owlModel.createOWLDatatypeProperty("hasAge");
//		hasAge.setDomain(Person);
//		hasAge.setRange(owlModel.getXSDinteger());
		
//		Darwin.setPropertyValue(hasAge, new Integer(18));
		
//		OWLObjectProperty hasChild = owlModel.createOWLObjectProperty("hasChild");
//		hasChild.setDomain(Person);
//		hasChild.setRange(Person);
		
//		Darwin.setPropertyValue(hasChild, LittleDarwin);
		
//		OWLObjectProperty hasID = owlModel.createOWLObjectProperty("hasID");
//		hasID.setDomain(Person);
//		hasID.setRange(IDCard);
		
//		OWLObjectProperty hasTicket = owlModel.createOWLObjectProperty("hasTicket");
//		OWLObjectProperty hasPTicket = owlModel.createOWLObjectProperty("hasPTicket");
//		hasTicket.setDomain(Person);
//		hasTicket.setRange(Ticket);
		
//		OWLObjectProperty hasLuggage = owlModel.createOWLObjectProperty("hasLuggage");
//		hasLuggage.setDomain(Person);
//		hasLuggage.setRange(Goods);
//		OWLDatatypeProperty hasWeight = owlModel.createAnnotationOWLDatatypeProperty("hasWeight");
//		hasWeight.setDomain(Goods);
//		hasWeight.setRange(owlModel.getXSDinteger());//may be more good!!!
		
		/**
	     * subsume the context is:
	     * Man(person) ^ hasID(person, interiumID) ^ hasTicket(person, p_ticket)
	     */
		
//		OWLIndividual person = Person.createOWLIndividual("person");
//		OWLIndividual idCard = IDCard.createOWLIndividual("idCard");//equivalent class...
//		OWLIndividual p_ticket = Ticket.createOWLIndividual("p_ticket");
//		person.setPropertyValue(hasID, idCard);//idCard -- interiumCard
//		person.setPropertyValue(hasTicket, p_ticket);
		
//		SWRLFactory factory = new SWRLFactory(owlModel);
//		try {
//			SWRLImp imp = factory.createImp("Person(?p) ^ hasTicket(?p, ?ticket) ^ sameAs(?ticket, p_ticket) -> hasPTicket(?p, ?ticket)");
//		} catch (SWRLParseException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		Reasoner reasoner = new Reasoner();
	    ProtegeReasoner pellet = reasoner.createPelletOWLAPIReasoner(owlModel);
	    
		ServiceSelection serviceSelection = new ServiceSelection(owlModel, pellet);
		
		/**
		 * OWL_SE task
		 */
		OWL_SE task = new OWL_SE();
		task = serviceSelection.getOWL_SE_task("G:/thesis/Test/Task1.owl");
		serviceSelection.print_task(task);
		
		/**
		 * List<OWL_SE> services
		 */
		List<OWL_SE> services = new ArrayList<OWL_SE>();
		OWL_SE service = new OWL_SE();
		service = serviceSelection.getOWL_SE_service("G:/thesis/Test/Service1-1.owl");
		
		serviceSelection.print_service(service);
		services.add(service);
		service = serviceSelection.getOWL_SE_service("G:/thesis/Test/Service1-2.owl");
		
		serviceSelection.print_service(service);
		services.add(service);
		service = serviceSelection.getOWL_SE_service("G:/thesis/Test/Service1-3.owl");
		
		serviceSelection.print_service(service);
		services.add(service);
		
		/**
		 * List<OWL_SE> taskNext
		 */
		List<OWL_SE> taskNext = new ArrayList<OWL_SE>();
		OWL_SE task_ = serviceSelection.getOWL_SE_task("G:/thesis/Test/Task2.owl");
		taskNext.add(task_);
		serviceSelection.print_task(task_);
		
		/**
		 * List<List<OWL_SE>> servicesNext
		 */
		List<List<OWL_SE>> servicesNext = new ArrayList<List<OWL_SE>>();
		List<OWL_SE> servicesNext_ = new ArrayList<OWL_SE>();
		OWL_SE service_ = serviceSelection.getOWL_SE_service("G:/thesis/Test/Service2-1.owl");
		servicesNext_.add(service_);
		
		serviceSelection.print_service(service_);
		
		service_ = serviceSelection.getOWL_SE_service("G:/thesis/Test/Service2-2.owl");
		servicesNext_.add(service_);
		
		serviceSelection.print_service(service_);
		
		service_ = serviceSelection.getOWL_SE_service("G:/thesis/Test/Service2-3.owl");
		servicesNext_.add(service_);
		servicesNext.add(servicesNext_);
		
		serviceSelection.print_service(service_);
		
		System.out.println(serviceSelection.select(task, services, taskNext, servicesNext));
	}
}
