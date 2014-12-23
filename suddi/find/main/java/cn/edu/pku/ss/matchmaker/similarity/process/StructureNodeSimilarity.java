package cn.edu.pku.ss.matchmaker.similarity.process;

import cn.edu.pku.ss.matchmaker.process.model.BeginEndType;
import cn.edu.pku.ss.matchmaker.process.model.StructureType;

public interface StructureNodeSimilarity {
	public double getSimilarity(StructureType srcType, StructureType destType);
	public double getSimilarity(StructureType srcType, BeginEndType srcBegEndType, StructureType destType, BeginEndType destBegEndType);

}
