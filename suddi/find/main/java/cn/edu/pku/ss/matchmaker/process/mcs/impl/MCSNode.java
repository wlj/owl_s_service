package cn.edu.pku.ss.matchmaker.process.mcs.impl;

import cn.edu.pku.ss.matchmaker.process.model.Node;

public class MCSNode {
	private Node srcNode;
	private Node destNode;
	private double similarity;
	
	public MCSNode(Node srcNode, Node destNode, double similarity) {
		// TODO Auto-generated constructor stub
		this.srcNode = srcNode;
		this.destNode = destNode;
		this.similarity = similarity;
	}
	
	
	public MCSNode() {
		// TODO Auto-generated constructor stub
	}
	
	public Node getSrcNode() {
		return srcNode;
	}
	
	public void setSrcNode(Node srcNode) {
		this.srcNode = srcNode;
	}
	
	public Node getDestNode() {
		return destNode;
	}
	
	public void setDestNode(Node destNode) {
		this.destNode = destNode;
	}
	
	public double getSimilarity() {
		return similarity;
	}
	
	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
}
