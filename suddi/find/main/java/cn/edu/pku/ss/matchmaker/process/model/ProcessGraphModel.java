package cn.edu.pku.ss.matchmaker.process.model;

import java.util.List;


import EDU.cmu.Atlas.owls1_1.process.CompositeProcess;


public interface ProcessGraphModel {
	public boolean createModel(CompositeProcess process);
	public List<Node> getNode();
	public List<Edge> getEdge();
	
	public Node getBeginNode();
	public Node getEndNode();
	
	public double getWeight();
	public void setWeight(double weight);
	
	public int getAtomicProcessNum();
	public void setAtomicProcessNum(int atomicProcessNum);
	
	public int getNodeNum();
	public int getEdgeNum();
	
}
