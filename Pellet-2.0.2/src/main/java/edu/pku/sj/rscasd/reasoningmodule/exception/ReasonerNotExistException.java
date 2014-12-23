package edu.pku.sj.rscasd.reasoningmodule.exception;

public class ReasonerNotExistException extends RuntimeException {

	private static final long serialVersionUID = -3866416035654679925L;

	private String message;

	public ReasonerNotExistException() {
		this.message = "UNKNOWN REASON";
	}

	public ReasonerNotExistException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

}
