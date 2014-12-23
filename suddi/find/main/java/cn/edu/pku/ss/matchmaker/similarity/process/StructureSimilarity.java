package cn.edu.pku.ss.matchmaker.similarity.process;

import EDU.cmu.Atlas.owls1_1.process.Process;
import cn.edu.pku.ss.matchmaker.process.mcs.MCS;
import cn.edu.pku.ss.matchmaker.process.model.ProcessGraphModel;

public interface StructureSimilarity {
	public double getSimilarity(MCS mcs);
	public double getSimilarity(ProcessGraphModel srcGraphModel, ProcessGraphModel destGraphModel);
	public double getSimilarity(Process srcProcess, Process destProcess);

}
