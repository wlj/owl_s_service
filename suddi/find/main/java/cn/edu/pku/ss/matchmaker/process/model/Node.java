package cn.edu.pku.ss.matchmaker.process.model;

import java.util.List;

public interface Node {
	public NodeType getNodeType();
	public void setNodeType(NodeType nodeType);
	
	public List<Node> getPrecursor();
	public List<Node> getSuccessor();
	
	public boolean isTraversed();
	public void setIsTraversed(boolean isTraversed);
}
