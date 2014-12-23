package cn.edu.pku.ss.matchmaker.process.impl;

import cn.edu.pku.ss.matchmaker.process.model.NodeType;
import cn.edu.pku.ss.matchmaker.process.model.ServiceNode;
import EDU.cmu.Atlas.owls1_1.process.Process;
public class ServiceNodeImpl extends NodeImpl implements ServiceNode{
	private Process process;
	
	public ServiceNodeImpl(NodeType nodeType, Process process) {
		// TODO Auto-generated constructor stub
		super(nodeType);
		this.process = process;	
	}

	public Process getProcess() {
		// TODO Auto-generated method stub
		return process;
	}
	

	public boolean isAtomic() {
		// TODO Auto-generated method stub
		return process.isAtomic();
	}

	public boolean isComposite() {
		// TODO Auto-generated method stub
		return process.isComposite();
	}

	public boolean isSimple() {
		// TODO Auto-generated method stub
		return process.isSimple();
	}
}
