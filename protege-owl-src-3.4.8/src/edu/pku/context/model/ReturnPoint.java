package edu.pku.context.model;

public class ReturnPoint {

	private double totalPoint;
	private double currentPoint;
	private int times;
	
	public void print() {
		System.out.println("  ReturnPoint----");
		System.out.println("totalPoint: " + totalPoint);
		System.out.println("currentPoint: " + currentPoint);
		System.out.println("times: " + times);
	}
	
	public void feedBack() {
		
		totalPoint = (times * totalPoint + currentPoint) / (1 + times);
	}
	
	public double getTotalPoint() {
		
		return totalPoint;
	}
	
	public void setTotalPoint(double totalPoint) {
		
		this.totalPoint = totalPoint;
	}
	
	public double getCurrentPoint() {
		
		return currentPoint;
	}
	
	public void setCurrentPoint(double currentPoint) {
		
		this.currentPoint = currentPoint;
	}
	
	public double getTimes() {
		
		return times;
	}
	
	public void setTimes(int times) {
		
		this.times = times;
	}
}
