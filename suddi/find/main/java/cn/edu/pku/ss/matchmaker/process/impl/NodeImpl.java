package cn.edu.pku.ss.matchmaker.process.impl;

import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.ss.matchmaker.process.model.Node;
import cn.edu.pku.ss.matchmaker.process.model.NodeType;

public class NodeImpl implements Node{
    private NodeType nodeType;
    private List<Node> precursorList;
    private List<Node> successorList;
    private boolean isTraversed;
	
	public NodeImpl(NodeType nodeType) {
		this.nodeType =nodeType;
		precursorList = new ArrayList<Node>();
		successorList = new ArrayList<Node>();
	}

	// get and set
	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	public NodeType getNodeType() {
		// TODO Auto-generated method stub
		return nodeType;
	}

	public List<Node> getPrecursor() {
		// TODO Auto-generated method stub
		return precursorList;
	}
	
	public void setPrecursor(List<Node> precursorList) {
		this.precursorList = precursorList;
	}

	public List<Node> getSuccessor() {
		// TODO Auto-generated method stub
		return successorList;
	}
	
	public void setSuccessor(List<Node> successorList) {
		this.successorList = successorList;
	}

	public boolean isTraversed() {
		// TODO Auto-generated method stub
		return isTraversed;
	}

	public void setIsTraversed(boolean isTraversed) {
		// TODO Auto-generated method stub
		
		this.isTraversed = isTraversed;
	}
	
}
