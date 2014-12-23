package cn.edu.pku.ss.matchmaker.similarity.process;

import java.util.Hashtable;

import EDU.cmu.Atlas.owls1_1.process.Process;
import cn.edu.pku.ss.matchmaker.query.impl.Scorer;
import cn.edu.pku.ss.matchmaker.similarity.process.AtomicPairSimilarity;

public class AtomicPairSimilarityImpl implements AtomicPairSimilarity{
	public AtomicPairSimilarityImpl() {
		// TODO Auto-generated constructor stub
	}

	public double getSimilarity(Process srcProcess, Process destProcess) {
		// TODO Auto-generated method stub
		Hashtable<Process, Hashtable<Process, Double>> atomicProcessPairSimilarity = Scorer.getAtomicProcessPairSimilarity();
		if (atomicProcessPairSimilarity.containsKey(srcProcess) == false)
			return 0;
		
		Hashtable<Process, Double> processSimilarity = atomicProcessPairSimilarity.get(srcProcess);
		if (processSimilarity == null)
			return 0;
		
		if (processSimilarity.containsKey(destProcess))
			return processSimilarity.get(destProcess);
		
		
		return 0;
	}
}
