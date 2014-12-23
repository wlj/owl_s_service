package edu.pku.context.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * the owl-s extended for describing tasks and services
 * @author Jacob
 *
 */
public class OWL_SE {

	private String key;
	private String name;
	
	private double grade;
	
	private String Category;
	
	private String Input;
	private String Output;
	private String Precondition;
	private String Effect;
	
	private String Context;
	private String ContextRule;
	
	//Usability, Reliability, Security, Satisfaction, Price, Time
	private Map map;
	
	public void print() {
		System.out.println("   OWL_SE----");
		System.out.println("key: " + key);
		System.out.println("name: " + name);
		System.out.println("grade: " + grade);
		System.out.println("Category: " + Category);
		System.out.println("Input: " + Input);
		System.out.println("Output: " + Output);
		System.out.println("Precondition: " + Precondition);
		System.out.println("Effect: " + Effect);
		System.out.println("Context: " + Context);
		System.out.println("ContextRule: " + ContextRule);
		
		for (Iterator it = map.keySet().iterator(); it.hasNext(); ) {
			String mapKey = (String) it.next();
			QoS qos = (QoS) map.get(mapKey);
			qos.print();
		}
	}
	
	public void fileToObject(String file) {
		
		//read the file, example...
		
		map = new HashMap();
		map.put("Usability", new QoS());
		map.put("Reliability", new QoS());
		map.put("Security", new QoS());
		map.put("Satisfaction", new QoS());
		map.put("Price", new QoS());
		map.put("Time", new QoS());
	}
	
	/**
	 * modify user's feedback...
	 */
	public void feedBack() {
		
		for (Iterator it = map.keySet().iterator(); it.hasNext(); ) {
			
			String key = (String) it.next();
			System.out.println("key: " + key);
			QoS qos = (QoS) map.get(key);
			qos.feedBack();
		}
	}
	
	//(min, max) coming from the lots of services, means lots of OWL_SE...
	public void standardize(String key, double min, double max) {
		
		QoS qos = (QoS) map.get(key);
		qos.standardize(min, max);
	}
	
	//one for task_OWL_SE, another for service_OWL_SE...
//	public double qosWeight() {
//		
//		double total = 0.0;
//		
//		for (Iterator it = map.keySet().iterator(); it.hasNext(); ) {
//			
//			String key = (String) it.next();
//			System.out.println("key: " + key);
//			QoS qos = (QoS) map.get(key);
//			
//			total += qos.getWeight() * qos.getPointAll();
//		}
//		
//		return total;
//	}
	
	public String getKey() {
		
		return key;
	}
	
	public void setKey(String key) {
		
		this.key = key;
	}
	
	public String getName() {
		
		return name;
	}
	
	public void setName(String name) {
		
		this.name = name;
	}
	
	public double getGrade() {
		
		return grade;
	}
	
	public void setGrade(double grade) {
		
		this.grade = grade;
	}
	
	public void addGrade(double grade) {
		
		this.grade = this.grade + grade;
	}
	
	public String getCategory() {
		
		return Category;
	}
	
	public void setCategory(String Category) {
		
		this.Category = Category;
	}
	
	public String getInput() {
		
		return Input;
	}
	
	public void setInput(String Input) {
		
		this.Input = Input;
	}
	
	public String getOutput() {
		
		return Output;
	}
	
	public void setOutput(String Output) {
		
		this.Output = Output;
	}
	
	public String getPrecondition() {
		
		return Precondition;
	}
	
	public void setPrecondition(String Precondition) {
		
		this.Precondition = Precondition;
	}
	
	public String getEffect() {
		
		return Effect;
	}
	
	public void setEffect(String Effect) {
		
		this.Effect = Effect;
	}
	
	public String getContext() {
		
		return Context;
	}
	
	public void setContext(String Context) {
		
		this.Context = Context;
	}
	
	public String getContextRule() {
		
		return ContextRule;
	}
	
	public void setContextRule(String ContextRule) {
		
		this.ContextRule = ContextRule;
	}
	
	public Map getMap() {
		
		return map;
	}
	
	public void setMap(Map map) {
		
		this.map = map;
	}
}
