package cn.edu.pku.ss.matchmaker.process.model;

import EDU.cmu.Atlas.owls1_1.process.Process;

public interface ServiceNode extends Node {
	public boolean isAtomic();
	public boolean isComposite();
	public boolean isSimple();
	
	public Process getProcess();
}
