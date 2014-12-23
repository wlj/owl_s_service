package cn.edu.pku.ss.matchmaker.process.impl;

import EDU.cmu.Atlas.owls1_1.process.CompositeProcess;
import EDU.cmu.Atlas.owls1_1.process.ControlConstruct;
import EDU.cmu.Atlas.owls1_1.process.IfThenElse;
import EDU.cmu.Atlas.owls1_1.process.Perform;
import EDU.cmu.Atlas.owls1_1.process.Process;
import cn.edu.pku.ss.matchmaker.process.impl.ProcessGraphModelImpl;
import cn.edu.pku.ss.matchmaker.process.model.BeginEndType;
import cn.edu.pku.ss.matchmaker.process.model.Edge;
import cn.edu.pku.ss.matchmaker.process.model.Node;
import cn.edu.pku.ss.matchmaker.process.model.NodeType;
import cn.edu.pku.ss.matchmaker.process.model.ProcessGraphModel;
import cn.edu.pku.ss.matchmaker.process.model.StructureType;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class IfThenElseGraphModelImpl extends ProcessGraphModelImpl {
	private List<Node> nodeList;
	private List<Edge> edgeList;
	private Node beginNode;
	private Node endNode;
	
	
	Logger logger;
	public IfThenElseGraphModelImpl() {
		// TODO Auto-generated constructor stub
		nodeList = new ArrayList<Node>();
		edgeList = new ArrayList<Edge>();
		
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.process.impl.IfThenElseGraphModelImpl.class);
	}

	public boolean createModel(CompositeProcess process) {
		// TODO Auto-generated method stub
		if (process == null || process.isAtomic() == true) {
			logger.error("process is null or is an atomic process!");
			return false;
		}
		
		ControlConstruct construct = process.getComposedOf();
		if (construct == null) {
		    logger.error("failed to get construct!");
			return false;
		}
		
		if (StructureType.getStructureType(construct).equals(StructureType.IFTHENELSE) == false) {
			logger.error("process: " + process.getURI());
			logger.error("structure type is not If-Then-Else");
			return false;
		}
		
	    ControlConstruct thenConstruct = ((IfThenElse)construct).getThen();
	    ControlConstruct elseConstruct = ((IfThenElse)construct).getElse();
	    beginNode = new StructureNodeImpl(NodeType.STRUCTURE, StructureType.IFTHENELSE, BeginEndType.BEGIN);
	    endNode = new StructureNodeImpl(NodeType.STRUCTURE, StructureType.IFTHENELSE, BeginEndType.END);
	    nodeList.add(beginNode);
	    
	    
	    // then
	    if (thenConstruct != null) { 
	    	Process tmpProcess = ((Perform)thenConstruct).getProcess();
	    	processNodeAndEdge(tmpProcess);
	    }
	    
	    // else
	    if (elseConstruct != null) {
	    	Process tmpProcess = ((Perform) elseConstruct).getProcess();
	    	processNodeAndEdge(tmpProcess);
	    }
	    
	    nodeList.add(endNode);
		return true;
	}

	
	private void processNodeAndEdge(Process process) {
		if (process != null && process.isAtomic()) {
    		Node serviceNode = new ServiceNodeImpl(NodeType.SERVICE, process);
    		
    		// TODO
    		beginNode.getSuccessor().add(serviceNode);
    		serviceNode.getPrecursor().add(beginNode);
    		serviceNode.getSuccessor().add(endNode);
    		endNode.getPrecursor().add(serviceNode);
    		
    		nodeList.add(serviceNode);
    		
    		// add edge
    		Edge beginEdge = new EdgeImpl(beginNode, serviceNode, increaseWeight());
    		Edge endEdge = new EdgeImpl(serviceNode, endNode, increaseWeight());
    		
    		edgeList.add(beginEdge);
    		edgeList.add(endEdge);
    		increaseAtomicProcessNum();
    	}
    	
    	if (process != null && process.isComposite()) {
    		CompositeProcess compositeProcess = (CompositeProcess)process;
    		ProcessGraphModel model = ProcessGraphModelFactory.getProcessGraphModel(compositeProcess.getComposedOf());
    		// don't forget to tmpWeight = increaseWeight();
    		double tmpWeight = increaseWeight();
    		//model.setWeight(weight);
			model.createModel(compositeProcess);
			// TODO
			model.getBeginNode().getPrecursor().add(beginNode);
			beginNode.getSuccessor().add(model.getBeginNode());
			model.getEndNode().getSuccessor().add(endNode);
			endNode.getPrecursor().add(model.getEndNode());
			
			nodeList.addAll(model.getNode());
			// add edge
			Edge beginEdge = new EdgeImpl(beginNode, model.getBeginNode(), tmpWeight);
			Edge endEdge = new EdgeImpl(model.getEndNode(), endNode, increaseWeight());
			edgeList.add(beginEdge);
			edgeList.addAll(model.getEdge());
			edgeList.add(endEdge);
    	}
	}
	
	@Override
	public List<Edge> getEdge() {
		// TODO Auto-generated method stub
		return edgeList;
	}

	@Override
	public List<Node> getNode() {
		// TODO Auto-generated method stub
		return nodeList;
	}

	public Node getBeginNode() {
		// TODO Auto-generated method stub
		return beginNode;
	}

	public Node getEndNode() {
		// TODO Auto-generated method stub
		return endNode;
	}

	public int getEdgeNum() {
		// TODO Auto-generated method stub
		return edgeList.size();
	}

	public int getNodeNum() {
		// TODO Auto-generated method stub
		return nodeList.size();
	}
}
