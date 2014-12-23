package cn.edu.pku.ss.matchmaker.reasoning.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mindswap.pellet.owlapi.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

import cn.edu.pku.ss.matchmaker.index.impl.SemanticDataModel;
import cn.edu.pku.ss.matchmaker.reasoning.MatchLevel;
import cn.edu.pku.ss.matchmaker.reasoning.SemanticReasoner;
import cn.edu.pku.ss.matchmaker.reasoning.impl.SemanticReasonerImpl;


public class ReasoningTest {
	public static void main(String[] args) {
		SemanticReasoner reasoner = new SemanticReasonerImpl();
		
		String uri = "http://localhost:8080/juddiv3/owl-s/1.1/Concepts.owl";
		//String uri1 = "http://localhost:8080/juddiv3/owl-s/1.1/Concepts.owl";
	//	String uri2 = "http://localhost:8080/juddiv3/owl-s/1.1/koala.owl";
	//	String uri3 = "http://localhost:8080/juddiv3/owl-s/1.1/koala2.owl";
		List<String> uriList = new ArrayList<String>();
		uriList.add(uri);
	//	uriList.add(uri1);
	//	uriList.add(uri2);
		//uriList.add(uri3);
		
		reasoner.loadOntologies(uriList);
		
		System.out.println(reasoner.getAllConcepts());
		
		String base = "http://localhost:8080/juddiv3/owl-s/1.1/Concepts.owl#";

		
		String concept1 = base +"Password";
		String concept2 = base +"AcctName";
		
		if (reasoner.getMatchLevel(concept1, concept2).equals(MatchLevel.PLUGIN))
			System.out.println("plugin");
		else if (reasoner.getMatchLevel(concept1, concept2).equals(MatchLevel.SUBSUME))
			System.out.println("subsume");
		
		else if (reasoner.getMatchLevel(concept1, concept2).equals(MatchLevel.EXACT))
			System.out.println("exact");
		else if (reasoner.getMatchLevel(concept1, concept2).equals(MatchLevel.NOMATCH))
			System.out.println("no match");
		
		System.out.println("distance: " + reasoner.getSemanticDistance(concept1, concept2));
		
		
		Set<SemanticDataModel> models = reasoner.getExactMatch(IRI.create(concept2));
		for (SemanticDataModel model : models)
			System.out.println("result: " + model.getDestConcept());
		
	}

}
