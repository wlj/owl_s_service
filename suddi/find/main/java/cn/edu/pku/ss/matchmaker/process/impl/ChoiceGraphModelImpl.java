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

public class ChoiceGraphModelImpl extends ProcessGraphModelImpl {
	private List<Node> nodeList;
	private List<Edge> edgeList;
	private Node beginNode;
	private Node endNode;
	
	Logger logger;
	public ChoiceGraphModelImpl() {
		// TODO Auto-generated constructor stub
		nodeList = new ArrayList<Node>();
		edgeList = new ArrayList<Edge>();
		
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.process.impl.ChoiceGraphModelImpl.class);
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
		
		if (StructureType.getStructureType(construct).equals(StructureType.CHOICE) == false) {
			logger.error("process: " + process.getURI());
			logger.error("structure type is not Choice");
			return false;
		}
		
		
		ControlConstructList components = construct.getComponents();
		// add beginNode to nodeList
		beginNode = new StructureNodeImpl(NodeType.STRUCTURE, StructureType.CHOICE, BeginEndType.BEGIN);
		endNode = new StructureNodeImpl(NodeType.STRUCTURE, StructureType.CHOICE, BeginEndType.END);
		nodeList.add(beginNode);
		

		while (components != null) {
			Process tmpProcess = ((Perform)components.getFirst()).getProcess();
			if (tmpProcess != null && tmpProcess.isAtomic()) {
				Node currentServiceNode = new ServiceNodeImpl(NodeType.SERVICE, tmpProcess);
				nodeList.add(currentServiceNode);
				
				// add edges
				Edge beginEdge = new EdgeImpl(beginNode, currentServiceNode, increaseWeight());
				Edge endEdge = new EdgeImpl(currentServiceNode, endNode, increaseWeight());
				
				edgeList.add(beginEdge);
				edgeList.add(endEdge);	
				increaseAtomicProcessNum();
			}
			
			if (tmpProcess != null && tmpProcess.isComposite()) {
				CompositeProcess compositeProcess = (CompositeProcess) tmpProcess;
				ProcessGraphModel model = ProcessGraphModelFactory.getProcessGraphModel(compositeProcess.getComposedOf());
				double tmpWeight = increaseWeight();
				model.createModel(compositeProcess);
				nodeList.addAll(model.getNode());
				// add edge
				Edge beginEdge = new EdgeImpl(beginNode, model.getBeginNode(), tmpWeight);
				Edge endEdge = new EdgeImpl(model.getEndNode(), endNode, increaseWeight());
				edgeList.add(beginEdge);
				edgeList.addAll(model.getEdge());
				edgeList.add(endEdge);
			}
			
			components = (ControlConstructList) components.getRest();
		}
		nodeList.add(endNode);
		
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