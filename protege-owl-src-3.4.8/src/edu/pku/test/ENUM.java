package edu.pku.test;

public class ENUM {

	private enum Operation {
		Query,
		Add,
		Delete,
		Modify;
		
		public String operation;
		
		public void setOperation(String operation) {
			
			this.operation = operation;
		}
		
		public String getOperation() {
			
			return operation;
		}
	};
	
	public static void main(String[] args) {
		
		
	}
}
