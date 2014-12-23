package cn.edu.pku.ss.matchmaker.process.impl;

import java.util.List;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.process.CompositeProcess;
import EDU.cmu.Atlas.owls1_1.process.ControlConstruct;
import EDU.cmu.Atlas.owls1_1.process.Process;
import cn.edu.pku.ss.matchmaker.process.model.Edge;
import cn.edu.pku.ss.matchmaker.process.model.Node;
import cn.edu.pku.ss.matchmaker.process.model.ProcessGraphModel;
import cn.edu.pku.ss.matchmaker.process.model.StructureType;

public class ProcessGraphModelFactory {
	private static Logger logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.process.impl.ProcessGraphModelFactory.class);
	public static ProcessGraphModel getProcessGraphModel(ControlConstruct construct) {
		if (StructureType.getStructureType(construct).equals(StructureType.SEQUENCE))
			return new SequenceGraphModelImpl();
		
		if (StructureType.getStructureType(construct).equals(StructureType.CHOICE))
			return new ChoiceGraphModelImpl();
		
		if (StructureType.getStructureType(construct).equals(StructureType.SPLIT))
			return new SplitGraphModelImpl();
		
		if (StructureType.getStructureType(construct).equals(StructureType.SPLITJOIN))
			return new SplitJoinGraphModelImpl();
		
		if (StructureType.getStructureType(construct).equals(StructureType.IFTHENELSE))
			return new IfThenElseGraphModelImpl();
		
		if (StructureType.getStructureType(construct).equals(StructureType.ANYORDER))
			return new AnyOrderGraphModelImpl();
		
		if (StructureType.getStructureType(construct).equals(StructureType.REPEATUNTIL))
			return new RepeatUntilGraphModelImpl();
		
		if (StructureType.getStructureType(construct).equals(StructureType.REPEATWHILE))
			return new RepeatWhileGraphModelImpl();
		
		return null;
	}
	
	public static boolean getProcessGraphModel(Process process, List<Node> nodeList, List<Edge> edgeList) {
		if (process == null || process.isAtomic()) {
			logger.error("process: " + process.getURI() + " is null or is an atomic process!");
			return false;
		}
		
		CompositeProcess compositeProcess = (CompositeProcess) process;
		ControlConstruct construct = compositeProcess.getComposedOf();
		if (construct == null) {
		    logger.error("failed to get construct!");
			return false;
		}
		
		ProcessGraphModel processGraphModel = getProcessGraphModel(construct);
		processGraphModel.setWeight(0);
		processGraphModel.setAtomicProcessNum(0);
		if (processGraphModel.createModel(compositeProcess) == false) {
			logger.error("faied to create process graph model");
			return false;
		}
		
		nodeList = processGraphModel.getNode();
		edgeList = processGraphModel.getEdge();
		
		return true;		
	}
	
	public static ProcessGraphModel getProcessGraphModel(Process process) {
		if (process == null || process.isAtomic()) {
			logger.error("process: " + process.getURI() + " is null or is an atomic process!");
			return null;
		}
		
		CompositeProcess compositeProcess = (CompositeProcess) process;
		ControlConstruct construct = compositeProcess.getComposedOf();
		if (construct == null) {
		    logger.error("failed to get construct!");
			return null;
		}
		
		ProcessGraphModel processGraphModel = getProcessGraphModel(construct);
		processGraphModel.setWeight(0);
		processGraphModel.setAtomicProcessNum(0);
		if (processGraphModel.createModel(compositeProcess) == false) {
			logger.error("faied to create process graph model");
			return null;
		}
		
		return processGraphModel;
	}
}
