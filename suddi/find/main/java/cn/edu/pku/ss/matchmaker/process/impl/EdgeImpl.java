package cn.edu.pku.ss.matchmaker.process.impl;

import cn.edu.pku.ss.matchmaker.process.model.Edge;
import cn.edu.pku.ss.matchmaker.process.model.Node;

public class EdgeImpl implements Edge {
	private Node precursor;
	private Node successor;
	private double weight;

	public EdgeImpl(Node precursor, Node successor, double weight) {
		// TODO Auto-generated constructor stub
		this.precursor = precursor;
		this.successor = successor;
		this.weight = weight;
	}
	
	public Node getPrecursor() {
		// TODO Auto-generated method stub
		return precursor;
	}

	public Node getSuccessor() {
		// TODO Auto-generated method stub
		return successor;
	}

	public double getWeight() {
		// TODO Auto-generated method stub
		return weight;
	}

}
