package cn.edu.pku.ss.matchmaker.process.model;

public interface Edge {
	public Node getPrecursor();
	public Node getSuccessor();
	public double getWeight();
}
