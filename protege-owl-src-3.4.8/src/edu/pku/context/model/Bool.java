package edu.pku.context.model;

public class Bool {

	public enum BoolType {
		True,
		False,
		Unknown;
	};
	
	private BoolType boolType;
	
	public BoolType getBoolType() {
		
		return boolType;
	}
	
	public void setBoolType(BoolType boolType) {
		
		this.boolType = boolType;
	}
}
