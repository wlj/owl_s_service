package cn.edu.pku.ss.matchmaker.process.impl;

import java.util.List;

import cn.edu.pku.ss.matchmaker.process.model.Edge;
import cn.edu.pku.ss.matchmaker.process.model.Node;
import cn.edu.pku.ss.matchmaker.process.model.ProcessGraphModel;

public abstract class ProcessGraphModelImpl implements ProcessGraphModel {
	private List<Node> nodeList;
	private List<Edge> edgeList;
	private static double weight;
	private static int atomicProcessNum;
	public ProcessGraphModelImpl() {
		// TODO Auto-generated constructor stub
	}

//	@Override
//	public boolean createModel(CompositeProcess process) {
//		// TODO Auto-generated method stub
//		
//		return false;
//	}
	
	public List<Edge> getEdge() {
		// TODO Auto-generated method stub
		return edgeList;
	}

	public List<Node> getNode() {
		// TODO Auto-generated method stub
		return nodeList;
	}
	
	public double increaseWeight() {
		return ++weight;
	}
	
	public int increaseAtomicProcessNum() {
		return ++atomicProcessNum;
	}
	
	public void setAtomicProcessNum(int atomicProcessNum) {
		this.atomicProcessNum = atomicProcessNum;
	}
	
	public int getAtomicProcessNum() {
		return atomicProcessNum;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public double getWeight() {
		return weight;
	}
}
