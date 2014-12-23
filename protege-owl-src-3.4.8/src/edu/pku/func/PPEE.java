package edu.pku.func;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.regex.Pattern;

import edu.pku.context.model.CXTParser;
import edu.pku.dos.DoS;
import edu.pku.util.Reasoner;
import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLProperty;

public class PPEE {

	private OWLModel owlModel;
	private ProtegeReasoner reasoner;
	
	private CXTParser cxtParser_1;
	private CXTParser cxtParser_2;
	
	public PPEE(OWLModel owlModel, ProtegeReasoner reasoner) {
		
		this.owlModel = owlModel;
		this.reasoner = reasoner;
	}
	
	public double exec(String swrl_1, String swrl_2) {
		
		cxtParser_1 = new CXTParser(owlModel, reasoner);
		cxtParser_1.parser(swrl_1);
		
		cxtParser_2 = new CXTParser(owlModel, reasoner);
		cxtParser_2.parser(swrl_2);
		
		// 生成一个Pattern,同时编译一个正则表达式 ;用Pattern的split()方法把字符串分割 
//		String Regex = "[(,)]";
//		Pattern p = Pattern.compile(Regex);
//		
//		String[] result_1 = p.split(swrl_1);
//		String[] result_2 = p.split(swrl_2);
//		
//		if (result_1.length != 3 || result_2.length != 3) {
//			System.out.println("swrl format error!");
//			return 0.0;
//		}
		
		double dosAll = 0.0;
		DoS dos = new DoS(owlModel, reasoner);
		
		//Property
		dos.getDoS_Property(cxtParser_1.getCXTObject().getTypeValue(), cxtParser_2.getCXTObject().getTypeValue());
		dos.print();
		dosAll = dosAll + dos.getDos();
		
		//Argument1
		dos.getDoS(cxtParser_1.getCXTObject().getArguments().get(0), cxtParser_2.getCXTObject().getArguments().get(0));
		dos.print();
		dosAll = dosAll + dos.getDos();
		
		//Argument2
		dos.getDoS(cxtParser_1.getCXTObject().getArguments().get(1), cxtParser_2.getCXTObject().getArguments().get(1));
		dos.print();
		dosAll = dosAll + dos.getDos();
		
		return dosAll/3.0;
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
	    
	    PPEE ppee = new PPEE(owlModel, pellet);
	    System.out.println(ppee.exec("hasCard(Client, InterimIDCard)", "hasIDCard(Passenger, IDCard)"));
	    
//	    OWLProperty hasCard = owlModel.getOWLProperty("hasCard");
//	    OWLProperty hasIDCard = owlModel.getOWLProperty("hasIDCard");
//	    
//	    Collection<OWLProperty> cards = null;
//	    try {
////			cards = pellet.getSuperProperties(hasIDCard);
//	    	cards = hasCard.getSubproperties(false);
//			System.out.println(cards);
//			for (java.util.Iterator<OWLProperty> it = cards.iterator(); it.hasNext();) {
//				OWLProperty z = (OWLProperty)it.next();
//				System.out.println("z:" + z.getLocalName());
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	    
	}
}
