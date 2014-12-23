package EDU.cmu.Atlas.owls1_1.process;

import EDU.cmu.Atlas.owls1_1.expression.Condition;

public interface RepeatWhile extends ControlConstruct {
	
	public Condition getWhileCondition();
	
	public void setWhileCondition(Condition condition);
	
	public ControlConstruct getWhileProcess();
	
	public void setWhileProcess(ControlConstruct controlConstruct);
}
