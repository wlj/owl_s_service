package cn.edu.pku.ss.matchmaker.similarity.process;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.process.Process;
import cn.edu.pku.ss.matchmaker.process.impl.ProcessGraphModelFactory;
import cn.edu.pku.ss.matchmaker.process.mcs.MCS;
import cn.edu.pku.ss.matchmaker.process.mcs.impl.MCSImpl;
import cn.edu.pku.ss.matchmaker.process.model.ProcessGraphModel;
import cn.edu.pku.ss.matchmaker.similarity.process.AtomicSimilarity;
import cn.edu.pku.ss.matchmaker.similarity.process.ProcessSimilarity;
import cn.edu.pku.ss.matchmaker.similarity.process.StructureSimilarity;

public class ProcessSimilarityImpl implements ProcessSimilarity {
	private MCS mcs;
	private AtomicSimilarity atomicSimilarity;
	private StructureSimilarity structureSimilarity;
	private final double ATOMIC_SIMILARITY_RATE = 0.6;
	private Logger logger;
	
	public ProcessSimilarityImpl() {
		// TODO Auto-generated constructor stub
		mcs = new MCSImpl();
		atomicSimilarity = new AtomicSimilarityImpl(mcs);
		structureSimilarity = new StructureSimilarityImpl(mcs);
		
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.similarity.process.impl.ProcessSimilarityImpl.class);
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

	
	public double getSimilarity(MCS mcs) {
		// TODO Auto-generated method stub
		if (mcs == null) {
			logger.error("parameter is null or illegal");
			return -1;
		}
		
		double atomicMatchDegree = atomicSimilarity.getSimilarity(mcs);
		double structureMatchDegree = structureSimilarity.getSimilarity(mcs);
		
		
		if (atomicMatchDegree < 0)
			return structureMatchDegree;
		
		if (structureMatchDegree < 0)
			return atomicMatchDegree;
		
		
		double result = ATOMIC_SIMILARITY_RATE * atomicMatchDegree
		                + (1 - ATOMIC_SIMILARITY_RATE) * structureMatchDegree;
		
		return result;
	}

	
	public double getSimilarity(ProcessGraphModel srcGraphModel,
			ProcessGraphModel destGraphModel) {
		// TODO Auto-generated method stub
		if (srcGraphModel == null || destGraphModel == null) {
			logger.error("failed to get src process graph model or failed to get dest process graph model");
			return -1;
		}
		
		if (mcs.createMCS(srcGraphModel, destGraphModel) == false) {
			logger.error("failed to create MCS");
			return -1;
		}
		
		return getSimilarity(mcs);
	}

}
