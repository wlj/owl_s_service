package cn.edu.pku.ss.matchmaker.similarity.process.impl;

import java.util.List;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.process.Process;
import cn.edu.pku.ss.matchmaker.process.impl.ProcessGraphModelFactory;
import cn.edu.pku.ss.matchmaker.process.mcs.MCS;
import cn.edu.pku.ss.matchmaker.process.mcs.impl.MCSImpl;
import cn.edu.pku.ss.matchmaker.process.mcs.impl.MCSNode;
import cn.edu.pku.ss.matchmaker.process.model.ProcessGraphModel;
import cn.edu.pku.ss.matchmaker.similarity.process.AtomicSimilarity;

public class AtomicSimilarityImpl implements AtomicSimilarity {
	private MCS mcs;
	private Logger logger;
	
	public AtomicSimilarityImpl() {
		// TODO Auto-generated constructor stub
		mcs = new MCSImpl();
		
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.similarity.process.impl.AtomicSimilarityImpl.class);
	}
	
	public AtomicSimilarityImpl(MCS mcs) {
		this.mcs = mcs;
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.similarity.process.impl.AtomicSimilarityImpl.class);
	}

	// -1 means failed
	
	public double getSimilarity(MCS mcs) {
		// TODO Auto-generated method stub
		if (mcs == null) {
			logger.error("parameter is null or illegal!");
			return -1;
		}
		
		List<MCSNode> atomicNodesList = mcs.getAtomicNodes();
		
		int srcAtomicNodeNum = mcs.getSrcAtomicNodeNum();
		int destAtomicNodeNum = mcs.getDestAtomicNodeNum();
		
		int maxAtomicNodeNum = srcAtomicNodeNum > destAtomicNodeNum ? srcAtomicNodeNum : destAtomicNodeNum;
		
		double atomicPairSimilarity = 0;
		for (int i = 0; i < atomicNodesList.size(); ++i)
			atomicPairSimilarity += atomicNodesList.get(i).getSimilarity();
		
		double avgAtomicSimilarity = 0;
		if (maxAtomicNodeNum > 0)
			avgAtomicSimilarity = atomicPairSimilarity / maxAtomicNodeNum;
		
		return avgAtomicSimilarity;
	}
	


	
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
