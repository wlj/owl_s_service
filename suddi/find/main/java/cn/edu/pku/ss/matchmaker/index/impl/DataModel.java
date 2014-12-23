package cn.edu.pku.ss.matchmaker.index.impl;

import java.util.Vector;


public class DataModel {
	private Vector<Object> actor;
	
	private Vector<Object> classification;
	
	
	// store services which contain input or output parameter type
	private Vector<Object> input; 
	private Vector<Object> output;
	
	private Vector<Object> precondition;
	private Vector<Object> effect;
	
	
	
	private Vector<Object> serviceContext;
	private Vector<Object> qosContext;
	private Vector<Object> locationContext;
	private Vector<Object> serviceContextRule;
	private Vector<Object> serviceProduct;
	
	
	
	// contructor
	public DataModel() {
		actor = new Vector<Object> ();
		
		classification = new Vector<Object>();
		
		input = new Vector<Object>();
		output = new Vector<Object>();

		precondition = new Vector<Object>();
		effect = new Vector<Object>();
		
		serviceContext = new Vector<Object>();
		qosContext = new Vector<Object>();
		locationContext = new Vector<Object>();
		serviceContextRule = new Vector<Object>();
		serviceProduct = new Vector<Object>();
	}
	
	public Vector<Object> getInput() {
		return input;
	}
	public void setInput(Vector<Object> input) {
		this.input = input;
	}
	public Vector<Object> getOutput() {
		return output;
	}
	public void setOutput(Vector<Object> ouput) {
		this.output = ouput;
	}
	public Vector<Object> getPrecondition() {
		return precondition;
	}
	public void setPrecondition(Vector<Object> precondition) {
		this.precondition = precondition;
	}
	public Vector<Object> getEffect() {
		return effect;
	}
	public void setEffect(Vector<Object> effect) {
		this.effect = effect;
	}
	public Vector<Object> getClassification() {
		return classification;
	}
	public void setClassification(Vector<Object> classification) {
		this.classification = classification;
	}
	public Vector<Object> getServiceContext() {
		return serviceContext;
	}
	public void setServiceContext(Vector<Object> serviceContext) {
		this.serviceContext = serviceContext;
	}
	public Vector<Object> getQosContext() {
		return qosContext;
	}
	public void setQosContext(Vector<Object> qosContext) {
		this.qosContext = qosContext;
	}
	public Vector<Object> getLocationContext() {
		return locationContext;
	}
	public void setLocationContext(Vector<Object> locationContext) {
		this.locationContext = locationContext;
	}
	public Vector<Object> getServiceContextRule() {
		return serviceContextRule;
	}
	public void setServiceContextRule(Vector<Object> serviceContextRule) {
		this.serviceContextRule = serviceContextRule;
	}
	public Vector<Object> getServiceProduct() {
		return serviceProduct;
	}
	public void setServiceProduct(Vector<Object> serviceProduct) {
		this.serviceProduct = serviceProduct;
	}

	public void setActor(Vector<Object> actor) {
		this.actor = actor;
	}

	public Vector<Object> getActor() {
		return actor;
	}
}
