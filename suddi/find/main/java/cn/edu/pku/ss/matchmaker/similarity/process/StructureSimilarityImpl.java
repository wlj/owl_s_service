package cn.edu.pku.ss.matchmaker.similarity.process;

import java.util.List;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.process.Process;

import cn.edu.pku.ss.matchmaker.process.impl.ProcessGraphModelFactory;
import cn.edu.pku.ss.matchmaker.process.mcs.MCS;
import cn.edu.pku.ss.matchmaker.process.mcs.impl.MCSEdge;
import cn.edu.pku.ss.matchmaker.process.mcs.impl.MCSImpl;
import cn.edu.pku.ss.matchmaker.process.mcs.impl.MCSNode;
import cn.edu.pku.ss.matchmaker.process.model.ProcessGraphModel;
import cn.edu.pku.ss.matchmaker.similarity.process.StructureSimilarity;

public class StructureSimilarityImpl implements StructureSimilarity {
	private MCS mcs;
	private final double NODE_SIMILARITY_RATE = 0.6;
	private Logger logger;
	
	public StructureSimilarityImpl() {
		// TODO Auto-generated constructor stub
		mcs = new MCSImpl();
		
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.similarity.process.impl.StructureSimilarityImpl.class);
	}
	
	
	public StructureSimilarityImpl(MCS mcs) {
		// TODO Auto-generated constructor stub
		this.mcs = mcs;
		
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.similarity.process.impl.StructureSimilarityImpl.class);
	}
	
	
	
	public double getSimilarity(MCS mcs) {
		// TODO Auto-generated method stub
		List<MCSNode> mcsNodeList = mcs.getMcsNodes();
		List<MCSEdge> mcsEdgeList = mcs.getMcsEdges();
		
		int maxNodeNum = 0;
		if (mcs.getDestModelNodeNum() > mcs.getSrcModelNodeNum())
			maxNodeNum = mcs.getDestModelNodeNum();
		else 
			maxNodeNum = mcs.getSrcModelNodeNum();
		
		int maxEdgeNum = 0;
		if (mcs.getDestModelEdgeNum() > mcs.getSrcModelEdgeNum())
			maxEdgeNum = mcs.getDestModelEdgeNum();
		else 
			maxEdgeNum = mcs.getSrcModelEdgeNum();
		
		double nodesSimilarity = 0;
		for (int i = 0; i < mcsNodeList.size(); ++i)
			nodesSimilarity += mcsNodeList.get(i).getSimilarity();
		
		double edgesSimilarity = 0;
		for (int i = 0; i < mcsEdgeList.size(); ++i)
			edgesSimilarity += mcsEdgeList.get(i).getWeightRate();
		
		double avgNodesSimilarity = 0;
		if (maxNodeNum != 0)
			avgNodesSimilarity = nodesSimilarity / maxNodeNum;
		
		double avgEdgesSimilarity = 0;
		if (maxEdgeNum != 0)
			avgEdgesSimilarity = edgesSimilarity / maxEdgeNum;
		
		double similarity = NODE_SIMILARITY_RATE * avgNodesSimilarity
		                    + (1 - NODE_SIMILARITY_RATE) * avgEdgesSimilarity;
			
		return similarity;
	}
	
	// return -1 means failed
	
	public double getSimilarity(ProcessGraphModel srcGraphModel,
			ProcessGraphModel destGraphModel) {
		// TODO Auto-generated method stub
		if (srcGraphModel == null || destGraphModel == null) {
			logger.error("parameter is null or illegal");
			return -1;
		}
		
		if (mcs.createMCS(srcGraphModel, destGraphModel) == false) {
			logger.error("failed to create MCS");
			return -1;
		}
		
		return getSimilarity(mcs);
	}
	
	
	// return -1 means failed
	
	public double getSimilarity(Process srcProcess, Process destProcess) {
		// TODO Auto-generated method stub
		if (srcProcess == null || destProcess == null) {
			logger.error("parameter is null or illegal");
			return -1;
		}
		
		ProcessGraphModel srcGraphModel = ProcessGraphModelFactory.getProcessGraphModel(srcProcess);
		ProcessGraphModel destGraphModel = ProcessGraphModelFactory.getProcessGraphModel(destProcess);
		
		if (srcGraphModel == null || destGraphModel == null) {
			logger.error("failed to get src process graph model or failed to get dest process graph model");
			return -1;
		}
		
		
		return getSimilarity(srcGraphModel, destGraphModel);
	}
}
