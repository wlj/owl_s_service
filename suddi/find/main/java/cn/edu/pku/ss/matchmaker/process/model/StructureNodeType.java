package cn.edu.pku.ss.matchmaker.process.model;

public class StructureNodeType {
	StructureType structureType;
	BeginEndType beginEndType;
	
	public StructureNodeType(StructureType structureType, BeginEndType beginEndType) {
		this.structureType = structureType;
		this.beginEndType = beginEndType;
	}

	public StructureType getStructureType() {
		return structureType;
	}

	public void setStructureType(StructureType structureType) {
		this.structureType = structureType;
	}

	public BeginEndType getBeginEndType() {
		return beginEndType;
	}

	public void setBeginEndType(BeginEndType beginEndType) {
		this.beginEndType = beginEndType;
	}
}
