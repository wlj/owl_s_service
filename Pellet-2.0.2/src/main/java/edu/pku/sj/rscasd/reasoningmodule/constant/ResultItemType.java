package edu.pku.sj.rscasd.reasoningmodule.constant;

public enum ResultItemType {

	CLASS("CS"), INDIVIDUAL("IN"), DATAPROPERTY("DP"), OBJECTPROPERTY("OP"), UNKNOWN("UN");

	private String itemType;

	ResultItemType(String itemType) {
		this.itemType = itemType;
	}

	public static ResultItemType fromString(String itemType) {
		if ("CS".equalsIgnoreCase(itemType)) {
			return CLASS;
		} else if ("IN".equalsIgnoreCase(itemType)) {
			return INDIVIDUAL;
		} else if ("DP".equalsIgnoreCase(itemType)) {
			return DATAPROPERTY;
		} else if ("OP".equalsIgnoreCase(itemType)) {
			return OBJECTPROPERTY;
		} else {
			return UNKNOWN;
		}
	}

	public String getItemType() {
		return itemType;
	}

	public String toString() {
		return itemType;
	}
}
