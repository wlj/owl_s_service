package edu.pku.sj.rscasd.reasoningmodule.exception;

public class InvalidReasonerPreparedException extends RuntimeException {

	private static final long serialVersionUID = -3866416035654679925L;

	private String message;

	public InvalidReasonerPreparedException() {
		this.message = "UNKNOWN REASON";
	}

	public InvalidReasonerPreparedException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

}
