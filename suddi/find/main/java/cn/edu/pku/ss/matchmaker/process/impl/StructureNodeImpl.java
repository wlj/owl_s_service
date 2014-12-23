package cn.edu.pku.ss.matchmaker.process.impl;

import cn.edu.pku.ss.matchmaker.process.model.BeginEndType;
import cn.edu.pku.ss.matchmaker.process.model.NodeType;
import cn.edu.pku.ss.matchmaker.process.model.StructrueNode;
import cn.edu.pku.ss.matchmaker.process.model.StructureType;

public class StructureNodeImpl extends NodeImpl implements StructrueNode{
	private StructureType structureType;
	private BeginEndType beginEndType;

	public StructureNodeImpl(NodeType nodeType, StructureType structureType, BeginEndType beginEndType) {
		// TODO Auto-generated constructor stub
		super(nodeType);
		this.structureType = structureType;
		this.beginEndType = beginEndType;
	}
	
	public StructureType getStructureType() {
		// TODO Auto-generated method stub
		return structureType;
	}

	public void setStructureType(StructureType structureType) {
		// TODO Auto-generated method stub
		this.structureType = structureType;
		
	}

	public BeginEndType getBeginEndType() {
		// TODO Auto-generated method stub
		return beginEndType;
	}

	public void setBeginEndType(BeginEndType beginEndType) {
		// TODO Auto-generated method stub
		this.beginEndType = beginEndType;
		
	}

}
