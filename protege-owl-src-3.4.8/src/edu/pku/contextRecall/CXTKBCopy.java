package edu.pku.contextRecall;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import edu.pku.util.Global;
import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter.OWLModelWriter;

/**
 * concrete strategy1...
 * @author Jacob
 *
 */
public class CXTKBCopy implements RecallStrategy{

	private OWLModel owlModel;
	
	public CXTKBCopy(OWLModel owlModel) {
		
		this.owlModel = owlModel;
	}
	
	@Override
	public void updateAlgorithm() {
		// TODO Auto-generated method stub
		System.out.println("CXTKBCopy--update");
		System.out.println("update before: " + Global.sessionUpdateID);
		
		String filePath = Global.FILE_PRE + "CTXKB_" + String.valueOf(Global.sessionUpdateID) + ".owl";
		
		FileOutputStream outFile = null;
		try {
			outFile = new FileOutputStream(filePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    Writer out = null;
		try {
			out = new OutputStreamWriter(outFile,"UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		OWLModelWriter omw = new OWLModelWriter(owlModel, owlModel.getTripleStoreModel().getActiveTripleStore(), out);
	    try {
			omw.write();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    try {
			out.close();//Attention!!!
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    Global.sessionUpdateID = (Global.sessionUpdateID + 1) % Global.ROUND;
	    System.out.println("update after: " + Global.sessionUpdateID);
	}

	@Override
	public OWLModel recallAlgorithm(int sessionUpdateID) {
		// TODO Auto-generated method stub
		System.out.println("CXTKBCopy--recall");
		System.out.println("recall before: " + Global.sessionUpdateID);
		
		Global.sessionUpdateID = (Global.sessionUpdateID + Global.ROUND - 1) % Global.ROUND;
		String filePath = Global.FILE_PRE + "CTXKB_" + String.valueOf(Global.sessionUpdateID) + ".owl";
		
		FileInputStream inFile = null;
		try {
			System.out.println("filePath: " + filePath);
			inFile = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    	
        try {
			owlModel = ProtegeOWL.createJenaOWLModelFromInputStream(inFile);
		} catch (OntologyLoadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        try {
			inFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Attention!!!
        
        Global.sessionUpdateID = (Global.sessionUpdateID + 1) % Global.ROUND;
        System.out.println("recall after: " + Global.sessionUpdateID);
        
        return owlModel;
	}
}
