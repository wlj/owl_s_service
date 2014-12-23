package cn.edu.pku.ss.matchmaker.similarity.process;

import cn.edu.pku.ss.matchmaker.process.model.BeginEndType;
import cn.edu.pku.ss.matchmaker.process.model.StructureType;
import cn.edu.pku.ss.matchmaker.similarity.process.StructureNodeSimilarity;

public class StructureNodeSimilarityImpl implements StructureNodeSimilarity {

	
	public double getSimilarity(StructureType srcType, StructureType destType) {
		// TODO Auto-generated method stub
		if (srcType.equals(destType))
			return 1;
		
		if (srcType.equals(StructureType.SEQUENCE) && destType.equals(StructureType.ANYORDER))
			return 0.9;
		
		if (srcType.equals(StructureType.ANYORDER) && destType.equals(StructureType.SEQUENCE))
			return 0.5;
		
		if ((srcType.equals(StructureType.REPEATUNTIL) && destType.equals(StructureType.REPEATWHILE))
				|| (srcType.equals(StructureType.REPEATWHILE) && destType.equals(StructureType.REPEATUNTIL)))
			return 0.8;
		
		return 0;
	}

	
	public double getSimilarity(StructureType srcType,
			BeginEndType srcBegEndType, StructureType destType,
			BeginEndType destBegEndType) {
		// TODO Auto-generated method stub
		if (srcBegEndType.equals(destBegEndType) == true)
			return getSimilarity(srcType, destType);
		
		return 0;
	}
}
