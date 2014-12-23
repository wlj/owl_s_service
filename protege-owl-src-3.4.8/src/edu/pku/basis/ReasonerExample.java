package edu.pku.basis;

import edu.stanford.smi.protegex.owl.model.OWLModel; 
import edu.stanford.smi.protegex.owl.model.OWLNamedClass; 
import edu.stanford.smi.protegex.owl.ProtegeOWL; 
import edu.stanford.smi.protegex.owl.inference.pellet.ProtegePelletOWLAPIReasoner;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ReasonerManager; 
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.dig.reasoner.DIGReasonerIdentity; 
 
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Iterator; 
import java.util.Collection; 
 
/** 
 * The Univeristy Of Manchester
 * Medical Informatics Group
 * Date: Jan 7, 2005 
 */
public class ReasonerExample { 

    public static void main(String[] args) { 
        try { 
//            final String ONTOLOGY_URL = "http://www.co-ode.org/ontologies/pizza/pizza_20041007.owl"; 
//            final String REASONER_URL = "http://localhost:8080"; 
 
        	final String ONTOLOGY_FILE = "G:/thesis/source/tour.owl";
        	
//        	final String ONTOLOGY_FILE = "G:\\thesis\\source\\pizza_20041007.owl";
//        	final String ONTOLOGY_FILE = "src/pizza_20041007.owl";
//        	FileReader fr =  new FileReader (ONTOLOGY_FILE);
        	FileInputStream fis = new FileInputStream(ONTOLOGY_FILE);  
        	
            // Load the ontology from the specified URL 
//            OWLModel model = ProtegeOWL.createJenaOWLModelFromURI(ONTOLOGY_URL);
            OWLModel model = ProtegeOWL.createJenaOWLModelFromInputStream(fis);
            
            fis.close();//Attention!!!
 
            // Get the reasoner manager and obtain a reasoner for the OWL model. 
            ReasonerManager reasonerManager = ReasonerManager.getInstance(); 
//            ProtegeOWLReasoner reasoner = reasonerManager.getReasoner(model);
            ProtegeReasoner reasoner = reasonerManager.createProtegeReasoner(model, ProtegePelletOWLAPIReasoner.class); 
 
//            // Set the reasoner URL and test the connection 
//            reasoner.setURL(REASONER_URL); 
//            if(reasoner.isConnected()) { 
//                // Get the reasoner identity - this contains information
//                // about the reasoner, such as it's name and version,
//                // and the tell and ask operations that it supports.
//                DIGReasonerIdentity reasonerIdentity = reasoner.getIdentity(); 
//                System.out.println("Connected to " + reasonerIdentity.getName()); 
// 
                // Get the VegetarianPizza OWLNamedClass from the OWLModel 
//                OWLNamedClass vegetarianPizza = model.getOWLNamedClass("VegetarianPizza"); 
                OWLNamedClass vegetarianPizza = model.getOWLNamedClass("Person"); 
                if(vegetarianPizza != null) { 
                    // Get the number of asserted subclasses of VegetarianPizza 
                    Collection assertedSubclasses = vegetarianPizza.getNamedSubclasses(); 
                    System.out.println("Number of asserted VegetarianPizzas: " + assertedSubclasses.size()); 
 
                    // Now get the inferred subclasses of VegetarianPizza 
//                    Collection inferredSubclasses = reasoner.getSubclasses(vegetarianPizza, null); 
                    Collection inferredSubclasses = reasoner.getSubclasses(vegetarianPizza); 
                    System.out.println("Number of inferred VegetarianPizzas: " + inferredSubclasses.size()); 
                    System.out.println("VegetarianPizzas:"); 
                    for(Iterator it = inferredSubclasses.iterator(); it.hasNext();) { 
                        OWLNamedClass curClass = (OWLNamedClass) it.next(); 
                        System.out.println(curClass.getName()); 
                    } 
 
                    // We can classify the whole ontology, which will put the
                    // inferred class hierarchy information directly into the Protégé-OWL model. 
                    System.out.println("Classifying taxonomy..."); 
//                  reasoner.classifyTaxonomy(null); 
                    reasoner.classifyTaxonomy(); 
                    System.out.println("...Classified taxonomy!"); 

                    // We can then use the methods on OWLNamedClass for getting inferred
                    // information, without having to make separate queries to the external
                    // DIG reasoner, as this information is now in the OWLModel. 
                    System.out.println("Inferred subclasses of VegetarianPizza:"); 
                    inferredSubclasses = vegetarianPizza.getInferredSubclasses(); 
                    for(Iterator it = inferredSubclasses.iterator(); it.hasNext();) { 
                        OWLNamedClass curClass = (OWLNamedClass) it.next(); 
                        System.out.println(curClass.getName()); 
                    } 
                } 
                else { 
                    System.out.println("Could not find VegetarianPizza"); 
                } 
//            } 
//            else { 
//                System.out.println("Reasoner not connected!"); 
//            } 
        } 
        catch(Exception e) { 
            e.printStackTrace(); 
        } 
    } 
}

