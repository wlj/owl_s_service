package cn.edu.pku.ss.matchmaker.process.model;

public interface StructrueNode extends Node {
	public StructureType getStructureType();
	public void setStructureType(StructureType structureType);
	public BeginEndType getBeginEndType();
	public void setBeginEndType(BeginEndType beginEndType);
}
