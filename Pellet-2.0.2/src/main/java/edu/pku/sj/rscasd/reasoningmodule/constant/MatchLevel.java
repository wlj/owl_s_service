package edu.pku.sj.rscasd.reasoningmodule.constant;

public enum MatchLevel {

	EXACT("EXACT"), EXACT_ALMOST("EXACT_ALMOST"), PLUGIN("PLUGIN"), SUBSUME("SUBSUME"), NOMATCH("NOMATCH");

	private String level;

	MatchLevel(String level) {
		this.level = level;
	}

	public static MatchLevel fromString(String level) {
		if ("EXACT".equalsIgnoreCase(level)) {
			return EXACT;
		} else if ("EXACT_ALMOST".equalsIgnoreCase(level)) {
			return EXACT_ALMOST;
		} else if ("PLUGIN".equalsIgnoreCase(level)) {
			return PLUGIN;
		} else if ("SUBSUME".equalsIgnoreCase(level)) {
			return SUBSUME;
		} else if ("NOMATCH".equalsIgnoreCase(level)) {
			return NOMATCH;
		} else {
			return NOMATCH;
		}
	}

	public String getLevel() {
		return level;
	}

	public String toString() {
		return level;
	}
}
