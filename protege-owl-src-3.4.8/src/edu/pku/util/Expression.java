package edu.pku.util;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLModel;

public class Expression {
	
	public boolean isBoolean(String exp) {
		
		try {
			Boolean.parseBoolean(exp);
			return true;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isInt(String exp) {
		
		try {
			Integer.parseInt(exp);
			return true;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isInteger(String exp) {
		
		try {
			Integer.parseInt(exp);
			return true;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isNonNegativeInteger(String exp) {
		
		try {
			Integer integer = Integer.parseInt(exp);
			return integer >= 0;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isFloat(String exp) {
		
		try {
			Float.parseFloat(exp);
			return true;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isDouble(String exp) {
		
		try {
			Double.parseDouble(exp);
			return true;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isDate(String exp) {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
		
		try {
			Date date = (Date)formatter.parse(exp);
	        return exp.equals(formatter.format(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isDateTime(String exp) {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		
		try {
			Date date = (Date)formatter.parse(exp);
	        return exp.equals(formatter.format(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args) {
		
		Expression exp = new Expression();
//		String str = "-112.323";
//		System.out.println(exp.isDouble(str));
		
		String str = "-112323";
		System.out.println(exp.isNonNegativeInteger(str));
		
//		OWLModel owlModel = null;
//		try {
//			owlModel = ProtegeOWL.createJenaOWLModel();
//		} catch (OntologyLoadException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		owlModel.getXSDNonNegativeInteger();
		
//		String str = "2009-10-10 28:10:11";
//		System.out.println(exp.isDateTime(str));
	}
}
