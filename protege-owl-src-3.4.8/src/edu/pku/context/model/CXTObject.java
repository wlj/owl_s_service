package edu.pku.context.model;

import java.util.List;

public class CXTObject {

	public enum Operation {
		Query,
		Add,
		Delete,
		Modify;
	};
	
	public enum FirstType {
		C,//C(x)
		P,//P(x, y)
		sameAs,//sameAs(?x, ?y)
		differentFrom,//differentFrom(?x, ?y)
		swrlb;//builtIn(r, x,....)
	};
	
	public enum SecondType {
		//C(x)
		DirectClass,//Person(Jacob)
		IndirectClass,//(hasChild >= 1)(Jacob)
		DirectData,//xsd:int(?x)
		IndirectData,//[3, 4, 5](?x)
		
		//P(x, y)
		ObjectProperty,//hasHobby(Darwin, biology)
		DatatypeProperty,//hasAge(Darwin, 73)
		
		//sameAs(?x, ?y)
		sameAs,
		
		//differentFrom(?x, ?y)
		differentFrom,
		
		//BuiltIn
		swrlb;//swrlb:greaterThan(?age, 17)
	};
	
	private Operation operation;
	private FirstType firstType;
	private SecondType secondType;
	private String typeValue;
	private List<String> arguments = null;
	
	//set.........
	public void setOperation(Operation operation) {
		
		this.operation = operation;
	}
	
	public void setFirstType(FirstType firstType) {
		
		this.firstType = firstType;
	}
	
	public void setSecondType(SecondType secondType) {
		
		this.secondType = secondType;
	}
	
	public void setTypeValue(String typeValue) {
		
		this.typeValue = typeValue;
	}
	
	public void setArguments(List<String> arguments) {
		
		this.arguments = arguments;
	}
	
	//get......
	public Operation getOperation() {
		
		return operation;
	}
	
	public FirstType getFirstType() {
		
		return firstType;
	}
	
	public SecondType getSecondType() {
		
		return secondType;
	}
	
	public String getTypeValue() {
		
		return typeValue;
	}
	
	public List<String> getArguments() {
		
		return arguments;
	}
}
