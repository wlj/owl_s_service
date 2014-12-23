package edu.pku.context.model;

public class QoS {

	//Usability, Reliability, Security, Satisfaction, Price, Time
	private String name;
	private boolean type;
	
	private double weight;//the value may be 1.0/6 
	
	private double pointOfSupply;
	private double pointOfValuation;
	private double pointOfReturn;//caculate
	private ReturnPoint returnPoint;
	
	//the grade of QoS--after feedBack
	private double pointAll;
	
	public void print() {
		System.out.println("  QoS----");
		System.out.println("name: " + name);
		System.out.println("type: " + type);
		System.out.println("weight: " + weight);
		System.out.println("pointOfSupply: " + pointOfSupply);
		System.out.println("pointOfValuation: " + pointOfValuation);
		System.out.println("pointOfReturn: " + pointOfReturn);
		System.out.println("pointAll: " + pointAll);
		
		returnPoint.print();
	}
	
	public void feedBack() {
		
		returnPoint.feedBack();
		pointOfReturn = returnPoint.getTotalPoint();
		
		if (name.equals("Usability") || name.equals("Reliability") || name.equals("Security") || name.equals("Time")) {
			
			pointAll = (pointOfSupply + pointOfValuation + pointOfReturn) / 3;
		}
		else if (name.equals("Satisfaction")) {
			
			pointAll = (pointOfValuation + pointOfReturn) / 2;
		}
		else if (name.equals("Price")) {
			
			pointAll = pointOfSupply;
		}
		else {
			
			System.out.println("name: " + name + " error!");
		}
	}
	
	public int judge(double a, double b) {
		
		if (Math.abs(a - b) < 1e-6) return 0;
		else if (a < b) return -1;
		else return 1;
	}
	
	public void standardize(double min, double max) {
		
		if (judge(min, max) == 0) return ;
		
		//zheng zhi biao
		if (type == true) {
			
			pointAll = (pointAll - min) / (max - min);
		}
		else {//fu zhi biao
			
			pointAll = 1 - (pointAll - min) / (max - min);
		}
		
		if (judge(pointAll, 0.0) < 0 || judge(pointAll, 1.0) > 0) {
			
			System.out.println("standardize error!");
		}
	}
	
	public String getName() {
		
		return name;
	}
	
	public void setName(String name) {
		
		this.name = name;
	}
	public boolean getType() {
		
		return type;
	}
	
	public void setType(boolean type) {
		
		this.type = type;
	}
	
	public double getWeight() {
		
		return weight;
	}
	
	public void setWeight(double weight) {
		
		this.weight = weight;
	}
	
	public double getPointOfSupply() {
		
		return pointOfSupply;
	}
	
	public void setPointOfSupply(double pointOfSupply) {
		
		this.pointOfSupply = pointOfSupply;
	}
	
	public double getPointOfValuation() {
		
		return pointOfValuation;
	}
	
	public void setPointOfValuation(double pointOfValuation) {
		
		this.pointOfValuation = pointOfValuation;
	}
	
	public double getPointOfReturn() {
		
		return pointOfReturn;
	}
	
	public void setPointOfReturn(double pointOfReturn) {
		
		this.pointOfReturn = pointOfReturn;
	}
	
	public ReturnPoint getReturnPoint() {
		
		return returnPoint;
	}
	
	public void setReturnPoint(ReturnPoint returnPoint) {
		
		this.returnPoint = returnPoint;
	}
	
	public double getPointAll() {
		
		return pointAll;
	}
	
	public void setPointAll(double pointAll) {
		
		this.pointAll = pointAll;
	}
}
