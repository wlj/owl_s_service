package cn.edu.pku.ss.matchmaker.process.impl;

import EDU.cmu.Atlas.owls1_1.process.CompositeProcess;
import EDU.cmu.Atlas.owls1_1.process.ControlConstruct;
import EDU.cmu.Atlas.owls1_1.process.ControlConstructList;
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

public class SequenceGraphModelImpl extends ProcessGraphModelImpl {
	private List<Node> nodeList;
	private List<Edge> edgeList;
	private Node beginNode;
	private Node endNode;
	
	Logger logger;
	public SequenceGraphModelImpl() {
		// TODO Auto-generated constructor stub
		nodeList = new ArrayList<Node>();
		edgeList = new ArrayList<Edge>();
		
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.process.impl.SequenceGraphModelImpl.class);
	}


	public boolean createModel(CompositeProcess process) {
		// TODO Auto-generated method stub
		if (process == null || process.isAtomic() == true) {
			logger.error("process: " + process.getURI() + " is null or is an atomic process!");
			return false;
		}
		
		ControlConstruct construct = process.getComposedOf();
		if (construct == null) {
		    logger.error("failed to get construct!");
			return false;
		}
		
		if (StructureType.getStructureType(construct).equals(StructureType.SEQUENCE) == false) {
			logger.error("process: " + process.getURI());
			logger.error("structure type is not Sequence");
			return false;
		}
		
		
		ControlConstructList components = construct.getComponents();
		
		// add beginNode to nodeList
		beginNode = new StructureNodeImpl(NodeType.STRUCTURE, StructureType.SEQUENCE, BeginEndType.BEGIN);
		Node preNode = beginNode;
		nodeList.add(preNode);

		while (components != null) {
			//ControlConstruct tmpConstruct = (ControlConstruct) components.getFirst();
			Process tmpProcess = ((Perform)components.getFirst()).getProcess();
			if (tmpProcess != null && tmpProcess.isAtomic()) {
				Node currentServiceNode = new ServiceNodeImpl(NodeType.SERVICE, tmpProcess);
				
				//add preNode's successor node
				preNode.getSuccessor().add(currentServiceNode);
				
				//add current node's precursor
				currentServiceNode.getPrecursor().add(preNode);
				
				// add current node to nodeList
				nodeList.add(currentServiceNode);
				
				// add edge to edgeList
				Edge edge = new EdgeImpl(preNode, currentServiceNode, increaseWeight());
				edgeList.add(edge);
				
				// preNode points to current node
				preNode = currentServiceNode;
				
				increaseAtomicProcessNum();
			} else if (tmpProcess.isComposite()) {
				CompositeProcess compositeProcess = (CompositeProcess)tmpProcess;
				// don't forget to tmpWeight = increaseWeight();
				double tmpWeight = increaseWeight();
				
				ProcessGraphModel model = ProcessGraphModelFactory.getProcessGraphModel(compositeProcess.getComposedOf());
			//	model.setWeight(weight);
				
				model.createModel(compositeProcess);
				// TODO
				model.getBeginNode().getPrecursor().add(preNode);
				preNode.getSuccessor().add(model.getBeginNode());
				
				nodeList.addAll(model.getNode());
				// add edge
				Edge edge = new EdgeImpl(preNode, model.getBeginNode(), tmpWeight);
				edgeList.add(edge);
				edgeList.addAll(model.getEdge());
				
				// set preNode
				preNode = model.getEndNode();			
			}
			
			components = (ControlConstructList) components.getRest();
		}
		
		
		endNode = new StructureNodeImpl(NodeType.STRUCTURE, StructureType.SEQUENCE, BeginEndType.END);
		endNode.getPrecursor().add(preNode);
		preNode.getSuccessor().add(endNode);
		Edge edge = new EdgeImpl(preNode, endNode, increaseWeight());
		
		// add endNode and edge
		nodeList.add(endNode);
		edgeList.add(edge);
		
		return true;
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
