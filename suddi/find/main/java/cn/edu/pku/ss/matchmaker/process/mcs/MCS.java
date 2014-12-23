package cn.edu.pku.ss.matchmaker.process.mcs;

import java.util.List;

import cn.edu.pku.ss.matchmaker.process.mcs.impl.MCSEdge;
import cn.edu.pku.ss.matchmaker.process.mcs.impl.MCSNode;
import cn.edu.pku.ss.matchmaker.process.model.ProcessGraphModel;

public interface MCS {
	public boolean createMCS(ProcessGraphModel srcModel, ProcessGraphModel destModel);
	public List<MCSNode> getMcsNodes();
	public List<MCSEdge> getMcsEdges();
	
	public List<MCSNode> getAtomicNodes();
	
	public int getSrcModelNodeNum();
	public int getSrcModelEdgeNum();
	public int getDestModelNodeNum();
	public int getDestModelEdgeNum();
	
	public int getSrcAtomicNodeNum();
	public int getDestAtomicNodeNum();
	
}
