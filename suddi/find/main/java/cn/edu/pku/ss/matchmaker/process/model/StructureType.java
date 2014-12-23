package cn.edu.pku.ss.matchmaker.process.model;

import EDU.cmu.Atlas.owls1_1.process.ControlConstruct;
import EDU.cmu.Atlas.owls1_1.process.implementation.AnyOrderImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ChoiceImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.IfThenElseImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.RepeatUntilImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.RepeatWhileImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.SequenceImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.SplitImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.SplitJoinImpl;

public enum StructureType {
	SEQUENCE,
	IFTHENELSE,
	CHOICE,
	SPLIT,
	SPLITJOIN,
	REPEATWHILE,
	REPEATUNTIL,
	ANYORDER,
	OTHERS;
	
	
	public static StructureType getStructureType(ControlConstruct construct) {
		if (construct == null)
			return null;
		
		if (construct instanceof IfThenElseImpl)
			return IFTHENELSE;
		
		if (construct instanceof SequenceImpl)
			return SEQUENCE;
		
		if (construct instanceof ChoiceImpl)
			return CHOICE;
		
		if (construct instanceof SplitImpl)
			return SPLIT;
		
		if (construct instanceof SplitJoinImpl)
			return SPLITJOIN;
		
		if (construct instanceof RepeatWhileImpl)
			return REPEATWHILE;
		
		if (construct instanceof RepeatUntilImpl)
			return REPEATUNTIL;
		
		if (construct instanceof AnyOrderImpl)
			return ANYORDER;
		
		return OTHERS;
	}
}
