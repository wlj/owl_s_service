package cn.edu.pku.ss.matchmaker.process.mcs.impl;

import cn.edu.pku.ss.matchmaker.process.model.Edge;

public class MCSEdge {
	private Edge srcEdge;
	private Edge destEdge;
	
	public MCSEdge() {
		// TODO Auto-generated constructor stub
	}
	
	public MCSEdge(Edge srcEdge, Edge destEdge) {
		this.srcEdge = srcEdge;
		this.destEdge = destEdge;
	}
	
	public Edge getSrcEdge() {
		return srcEdge;
	}
	
	public void setSrcEdge(Edge srcEdge) {
		this.srcEdge = srcEdge;
	}
	
	public Edge getDestEdge() {
		return destEdge;
	}
	
	public void setDestEdge(Edge destEdge) {
		this.destEdge = destEdge;
	}
	
	// weightRate =  min(srcWeight, destWeight) / max(srcWeight, destWeight);
	public double getWeightRate() {
		double srcWeight = srcEdge.getWeight();
		double destWeight = destEdge.getWeight();
		
		if (srcWeight >= destWeight && srcWeight != 0)
			return destWeight / srcWeight;
		
		if (srcWeight <= destWeight && destWeight != 0)
			return srcWeight / destWeight;
		
		return 0;
	}
}
