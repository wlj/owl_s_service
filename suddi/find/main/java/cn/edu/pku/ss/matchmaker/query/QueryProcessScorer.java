package cn.edu.pku.ss.matchmaker.query;

import java.util.Vector;

import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.implementation.ProcessImpl;


public class QueryProcessScorer extends ProcessImpl{
	private double inputScore;
	private double outputScore;
	private double preconditionScore;
	private double effectScore;
	private Process process;
	
	private Vector<Object> parameterRecord;
	
	public QueryProcessScorer(Process process) {
		// TODO Auto-generated constructor stub
		super();
		inputScore = 0;
		outputScore = 0;
		preconditionScore = 0;		
		effectScore = 0;
		
		this.process = process;
		
		parameterRecord =  new Vector<Object> ();
		
	}
	
	public double getInputScore() {
		return inputScore;
	}

	public void setInputScore(double inputScore) {
		this.inputScore = inputScore;
	}

	public double getOutputScore() {
		return outputScore;
	}

	public void setOutputScore(double outputScore) {
		this.outputScore = outputScore;
	}

	public double getPreconditionScore() {
		return preconditionScore;
	}

	public void setPreconditionScore(double preconditionScore) {
		this.preconditionScore = preconditionScore;
	}

	public double getEffectScore() {
		return effectScore;
	}

	public void setEffectScore(double effectScore) {
		this.effectScore = effectScore;
	}

	public void clear() {
		parameterRecord.clear();
	}

	public Vector<Object> getParameterRecord() {
		return parameterRecord;
	}

	public void setParameterRecord(Vector<Object> parameterRecord) {
		this.parameterRecord = parameterRecord;
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}
}
