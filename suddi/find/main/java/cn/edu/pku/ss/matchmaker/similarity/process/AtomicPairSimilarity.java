package cn.edu.pku.ss.matchmaker.similarity.process;

import EDU.cmu.Atlas.owls1_1.process.Process;

public interface AtomicPairSimilarity {
	public double getSimilarity(Process srcProcess, Process destProcess);

}
